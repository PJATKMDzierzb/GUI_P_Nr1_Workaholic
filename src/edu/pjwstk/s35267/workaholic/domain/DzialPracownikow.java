package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.domain.contract.Pracownik;
import edu.pjwstk.s35267.workaholic.domain.exception.NotUniqueNameException;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.Identifiable;

import java.util.ArrayList;
import java.util.HashMap;

public class DzialPracownikow extends Identifiable {
    static private HashMap<String, Integer> namesMap = new HashMap<>();
    private ArrayList<Pracownik> workers;
    private String nazwa;

    /**
     * @description
     * Generates new DzialPracownikow with always unique name.
     *
     * When not unique name is passed, it will return a new object with increased counter added to the name.
     * So when passing a name "Dział Płac" and there is already an object with "Dział Płac" name, and
     * (additionally) "Dział Płac2" already exists, it will return "Dział Płac3".
     *
     * Be aware that if you pass "Dział Płac2", script won't return "Dział Płac3" but instead it will return
     * "Dział Płac22" as it will treat passed name as new identifier, and will not try to find already existing counter.
     */
    static public DzialPracownikow create(String nazwa) {
        try {
            ActionLogger.saveAction("New department " + nazwa + "!");

            return new DzialPracownikow(nazwa);
        } catch (NotUniqueNameException e) {
            System.out.println(e.getMessage());
            DzialPracownikow newDzial = new DzialPracownikow(nazwa, DzialPracownikow.namesMap.get(nazwa));
            System.out.println("Nowa nazwa działu: " + newDzial.nazwa);
            ActionLogger.saveAction(
                    "Department tried to take existing name " + nazwa + ", new name was generated: " + newDzial.nazwa,
                    new Object[]{ newDzial }
            );

            return newDzial;
        }
    }

    public ArrayList<Pracownik> getWorkers() {
        return this.workers;
    }

    public void addWorker(Pracownik worker) {
        ActionLogger.saveAction("Add new worker to department " + this.nazwa, new Object[]{ worker });
        this.workers.add(worker);
    }

    private DzialPracownikow(String name) throws NotUniqueNameException {
        if (DzialPracownikow.namesMap.containsKey(name)) {
            throw new NotUniqueNameException(
                String.format("Nazwa Działu Pracowników musi byc unikalna, podana nazwa %s już istnieje", name)
            );
        }

        DzialPracownikow.namesMap.put(name, 1);
        this.nazwa = name;
        this.workers = new ArrayList<>();
    }

    private DzialPracownikow(String name, int counter) {
        DzialPracownikow.namesMap.put(name, ++counter);
        String newName = name + counter;
        DzialPracownikow.namesMap.put(newName, 1);
        this.nazwa = newName;
    }
}
