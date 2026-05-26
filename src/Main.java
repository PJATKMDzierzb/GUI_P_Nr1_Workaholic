import edu.pjwstk.s35267.workaholic.application.Praca;
import edu.pjwstk.s35267.workaholic.application.RodzajPracy;
import edu.pjwstk.s35267.workaholic.domain.*;
import edu.pjwstk.s35267.workaholic.domain.contract.Pracownik;
import edu.pjwstk.s35267.workaholic.infrastructure.FileLogger;
import edu.pjwstk.s35267.workaholic.presentation.StanZlecenia;
import edu.pjwstk.s35267.workaholic.presentation.Zlecenie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    static String outputFile = "./app.log";

    public static void main(String[] args) {
        Main.preformFunctionalTests();

        System.out.println("#### Starting Workaholic™ application ####");
        try (FileLogger logger = new FileLogger(Main.outputFile)) {
            ActionLogger.register(logger);

            Main.runApp();

            ActionLogger.unregister();
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        System.out.println("#### Workaholic™ application has finished ####");
    }

    private static void runApp() {
        DzialPracownikow productionDept = DzialPracownikow.create("Production & Assembly");
        Brygadzista foreman = Main.generateForman(productionDept);
        Specjalista assembler = Main.generateSpecialist(productionDept);

        ArrayList<Pracownik> staff = new ArrayList<>();
        staff.add(assembler);
        Brygada buildCrew = new Brygada("Superforce Crew #1", foreman, staff);

        System.out.println("Brigade '" + buildCrew.getNazwa() + "' is ready for deployment.");

        Praca setupInfra = new Praca(RodzajPracy.OGOLNA, 2, "Preparing power supplies and server racks");

        ArrayList<Praca> phase2Deps = new ArrayList<>();
        phase2Deps.add(setupInfra);
        Praca installHardware = new Praca(RodzajPracy.MONTAZ, 3, "Mounting servers and switches", phase2Deps);

        ArrayList<Praca> phase3Deps = new ArrayList<>();
        phase3Deps.add(installHardware);
        Praca configNetwork = new Praca(RodzajPracy.MONTAZ, 1, "Configuring core routers and firewalls", phase3Deps);

        ArrayList<Praca> projectPlan = new ArrayList<>();
        projectPlan.add(setupInfra);
        projectPlan.add(installHardware);
        projectPlan.add(configNetwork);

        Zlecenie serverRoomDeployment = new Zlecenie(true, projectPlan, buildCrew);
        System.out.println("Created project order: " + serverRoomDeployment);
        System.out.println("Initial Order Status: " + serverRoomDeployment.getState());

        System.out.println(">>> Dispatching Server Room Deployment Thread...");
        Thread deploymentEngine = new Thread(serverRoomDeployment);
        deploymentEngine.start();

        try {
            deploymentEngine.join();
        } catch (InterruptedException e) {
            System.err.println("Main showcase execution monitoring was interrupted.");
            Thread.currentThread().interrupt();
        }

        System.out.println("Final Project Order Status: " + serverRoomDeployment.getState());
    }

    private static void preformFunctionalTests() {
        System.out.println("### Testing the application ###");
        Main.testIdentifiable();
        Main.testStringification();
        Main.testDepartmentNaming();
        Main.testInitialsBeingSetOnUser();
        Main.testLogger(Main.outputFile);
        Main.testWorkers();
        Main.testTasks();
        System.out.println("### Tests finished successfully ###" + "\n".repeat(10));
    }

    private static void testIdentifiable() {
        DzialPracownikow department = DzialPracownikow.create("Anon Workaholics");
        assert department.getUnique() == 1 : "Department #1 has incorrect ID " + department.getUnique();
        DzialPracownikow department2 = DzialPracownikow.create("Anon Workaholics");
        assert department2.getUnique() == 2 : "Department #2 has incorrect ID " + department2.getUnique();
        Specjalista professional1 = Main.generateSpecialist(department);
        assert professional1.getUnique() == 1 : "Specialist #1 has incorrect ID " + professional1.getUnique();
        Specjalista professional2 = Main.generateSpecialist(department);
        assert professional2.getUnique() == 2 : "Specialist #2 has incorrect ID " + professional2.getUnique();

        Praca work1 = new Praca(RodzajPracy.DEMONTAZ, 0, "Test IdThread");
        assert work1.getUnique() == 1 : "Praca #1 has incorrect ID " + work1.getUnique();
        Praca work2 = new Praca(RodzajPracy.DEMONTAZ, 0, "Test IdThread");
        assert work2.getUnique() == 2 : "Praca #2 has incorrect ID " + work2.getUnique();

        assert Praca.getById(1).equals(work1) : "Retrieving work #1 by ID doesn't work";
        assert Praca.getById(2).equals(work2) : "Retrieving work #2 by ID doesn't work";

        Zlecenie task1 = new Zlecenie(true);
        assert task1.getUnique() == 1 : "Zlecenie #1 has incorrect ID " + task1.getUnique();
        Zlecenie task2 = new Zlecenie(true);
        assert task2.getUnique() == 2 : "Zlecenie #2 has incorrect ID " + task2.getUnique();

        assert Zlecenie.getById(1).equals(task1) : "Retrieving task #1 by ID doesn't work";
        assert Zlecenie.getById(2).equals(task2) : "Retrieving task #2 by ID doesn't work";

        System.out.println("### Identifiable are passing! ###\n");
    }

    public static void testStringification() {
        DzialPracownikow department = DzialPracownikow.create("Anon Workaholics");
        System.out.println(department);
        assert department.toString().contains("DzialPracownikow") : "Stringification doesn't contain name of the class";
        assert department.toString().contains("=" + department.getUnique()) : "Stringification doesn't contain ID of the class";

        System.out.println("### Stringification is passing! ###\n");
    }

    private static void testDepartmentNaming() {
        String name = "Name testing " + (int) (100 * Math.random()) + "_";
        DzialPracownikow department = DzialPracownikow.create(name);
        assert department.getNazwa().equals(name) : "Department has incorrect name: " + department.getNazwa();
        DzialPracownikow department2 = DzialPracownikow.create(name);
        assert department2.getNazwa().equals(name + "2") : "Department #2 has incorrect name: " + department2.getNazwa() + "/" + name + "2";
        DzialPracownikow department3 = DzialPracownikow.create(name + "2");
        assert department3.getNazwa().equals(name + "22") : "Department #3 has incorrect name: " + department3.getNazwa() + "/" + name + "22";

        System.out.println("### Unique department name is passing! ###\n");
    }

    private static void testInitialsBeingSetOnUser() {
        Uzytkownik user = new Uzytkownik(
            "1Name",
            "2Surname",
            LocalDate.of(2000, 1, 1),
            DzialPracownikow.create("Anon Workaholics"),
            "login",
            "hasło"
        );

        assert user.getInicjaly().equals("12") : "Initials are incorrect: " + user.getInicjaly();
        user.setName("AName");
        assert user.getInicjaly().equals("A2") : "Initials #2 are incorrect: " + user.getInicjaly();
        user.setSurname("BSurname");
        assert user.getInicjaly().equals("AB") : "Initials #3 are incorrect: " + user.getInicjaly();

        System.out.println("### Initials are passing! ###\n");
    }

    private static void testLogger(String filePath) {
        try {
            Path path = Path.of(filePath);
            Files.deleteIfExists(path);

            try (FileLogger logger = new FileLogger(filePath)) {
                ActionLogger.register(logger);

                ActionLogger.saveAction("Test log #1...");
                ActionLogger.saveAction("Test log #2...");

                ActionLogger.unregister();
            } catch (IOException e) {
                System.err.println("An error occurred: " + e.getMessage());
                assert false : e.getMessage();
            }

            assert Files.exists(path) : "File does not exist!";
            assert Files.size(path) > 0 : "File is empty!";

            Files.deleteIfExists(path);

            System.out.println("### Logger is passing! ###\n");
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            assert false : e.getMessage();
        }
    }

    private static void testWorkers() {
        Praca work = new Praca(RodzajPracy.OGOLNA, 0, "Opis pracy #1");
        ArrayList<Praca> dependencies2 = new ArrayList<>();
        dependencies2.add(work);
        Praca work2 = new Praca(RodzajPracy.OGOLNA, 0, "Opis pracy #2", dependencies2);
        ArrayList<Praca> dependencies3 = new ArrayList<>();
        dependencies3.add(work2);
        Praca work3 = new Praca(RodzajPracy.OGOLNA, 0, "Opis pracy #3", dependencies3);

        // Set unit of time to smaller number to make tests pass faster
        int previousUnit = Praca.unitOfTime;
        Praca.unitOfTime = 1;

        try {
            work2.start();
            Thread.sleep(10);
            assert !work3.czyZrealizowane() : "Second worker didn't wait for his dependencies";
            work3.start();
            Thread.sleep(10);
            assert !work2.czyZrealizowane() : "Third worker didn't wait for his dependencies";
            work.start();
            Thread.sleep(10);
        } catch (InterruptedException e) {
            assert false : "Test interrupted";
        }

        assert work.czyZrealizowane() : "First worker didn't finish in time";
        assert work2.czyZrealizowane() : "Second worker didn't finish in time";
        assert work3.czyZrealizowane() : "Third worker didn't finish in time";

        // Reassign unit of time to original
        Praca.unitOfTime = previousUnit;

        System.out.println("### Workers are passing! ###\n");
    }

    private static void testTasks() {
        try {
            Main.testHappyPath();
            Main.testTaskCannotStartWithoutForemanOrWork();
            Main.testTaskCannotStartWithUnavailableWorkers();
        } catch (InterruptedException e) {
            assert false : "Test interrupted";
        }
        System.out.println("### Tasks are passing! ###\n");
    }

    private static void testTaskCannotStartWithUnavailableWorkers() {
        try {
            ArrayList<Praca> jobs = new ArrayList<>();
            jobs.add(new Praca(RodzajPracy.MONTAZ, 0, "Test"));

            DzialPracownikow department = DzialPracownikow.create("Anon Workaholics");
            Brygadzista foreman = Main.generateForman(department, false);
            Brygada brigade = new Brygada("Happy Team #1", foreman);
            Zlecenie task = new Zlecenie(true, jobs, brigade);
            task.run();
            assert false : "Task started with not available stuff";
        } catch (InvalidParameterException e) {
            assert e.getMessage().equals("Nie możesz zacząć nowego zlecenia jeżeli nie wszyscy pracownicy są dostępni");
        }
    }

    private static void testTaskCannotStartWithoutForemanOrWork() {
        try {
            Zlecenie task = new Zlecenie(true);
            task.run();
            assert false : "Task started even without foreman or work";
        } catch (InvalidParameterException e) {
            // do nothing
            assert e.getMessage().equals("Nie możesz zacząć nowego zlecenia jak nie ustawiłeś brygady albo nie ma pracy do wykonania");
        }

        try {
            DzialPracownikow department = DzialPracownikow.create("Anon Workaholics");
            Brygadzista foreman = Main.generateForman(department);
            Brygada brigade = new Brygada("Happy Team #1", foreman);
            Zlecenie task = new Zlecenie(true, brigade);
            task.run();
            assert false : "Task started even without work";
        } catch (InvalidParameterException e) {
            assert e.getMessage().equals("Nie możesz zacząć nowego zlecenia jak nie ustawiłeś brygady albo nie ma pracy do wykonania");
        }

        try {
            Zlecenie task = new Zlecenie(true, new ArrayList<>());
            task.run();
            assert false : "Task started even without foreman";
        } catch (InvalidParameterException e) {
            assert e.getMessage().equals("Nie możesz zacząć nowego zlecenia jak nie ustawiłeś brygady albo nie ma pracy do wykonania");
        }
    }

    private static void testHappyPath() throws InterruptedException {
        DzialPracownikow department = DzialPracownikow.create("Anon Workaholics");
        Specjalista professional = Main.generateSpecialist(department);
        Brygadzista foreman = Main.generateForman(department);

        ArrayList<Pracownik> workers = new ArrayList<>();
        workers.add(professional);
        Brygada brigade = new Brygada("Happy Team #1", foreman, workers);

        Praca general = new Praca(RodzajPracy.OGOLNA, 0, "Cleaning, setting up the space");
        ArrayList<Praca> disassemblyDependencies = new ArrayList<>();
        disassemblyDependencies.add(general);
        Praca disassembly1 = new Praca(RodzajPracy.DEMONTAZ, 0, "Removal of old electronics", disassemblyDependencies);
        Praca disassembly2 = new Praca(RodzajPracy.DEMONTAZ, 0, "Disassembling old furniture", disassemblyDependencies);
        ArrayList<Praca> assemblyDependencies = new ArrayList<>();
        assemblyDependencies.add(disassembly1);
        assemblyDependencies.add(disassembly2);
        Praca assembly = new Praca(RodzajPracy.MONTAZ, 0, "Installing new AGD and furniture", assemblyDependencies);

        ArrayList<Praca> jobs = new ArrayList<>();
        jobs.add(disassembly1);
        jobs.add(assembly);
        jobs.add(general);
        jobs.add(disassembly2);
        Zlecenie replaceKitchen = new Zlecenie(true, jobs, brigade);

        int previousUnit = Praca.unitOfTime;
        Praca.unitOfTime = 1;

        assert replaceKitchen.getState().equals(StanZlecenia.UTWORZONE) : "Task has incorrect initial state";

        Thread kitchenThread = new Thread(replaceKitchen);
        kitchenThread.start();
        Thread.sleep(Praca.unitOfTime * 5);

        assert replaceKitchen.getState().equals(StanZlecenia.ROZPOCZETE) : "Task has incorrect running state";

        Thread.sleep(Praca.unitOfTime * 30);

        assert replaceKitchen.getState().equals(StanZlecenia.ZAKONCZONE) : "Task has incorrect end state";

        Praca.unitOfTime = previousUnit;
    }

    private static Specjalista generateSpecialist(DzialPracownikow department) {
        return new Specjalista("Léon", "Professional", LocalDate.of(1994, 2, 12), department, "Loud work");
    }


    private static Brygadzista generateForman(DzialPracownikow department) {
        return new Brygadzista("Eric", "Foreman", LocalDate.of(1973, 6, 20), department, "house_2", "House1234");
    }

    private static Brygadzista generateForman(DzialPracownikow department, boolean available) {
        return new Brygadzista("Eric", "Foreman", LocalDate.of(1973, 6, 20), department, "house_2", "House1234", available);
    }
}
