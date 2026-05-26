package edu.pjwstk.s35267.workaholic.presentation;

import edu.pjwstk.s35267.workaholic.application.Praca;
import edu.pjwstk.s35267.workaholic.domain.ActionLogger;
import edu.pjwstk.s35267.workaholic.domain.Brygada;
import edu.pjwstk.s35267.workaholic.domain.contract.IWykonawca;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.Identifiable;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Zlecenie extends Identifiable implements Runnable {
    private Collection<Praca> work;
    private Brygada brigade;
    private final boolean czyPlanowane; // @TODO what purpose this serves?
    private StanZlecenia state = StanZlecenia.UTWORZONE;
    private final LocalDateTime created;
    private LocalDateTime started;
    private LocalDateTime finished;

    public static Zlecenie getById(int id) {
        return Identifiable.getById(id, Zlecenie.class.getName());
    }

    public Zlecenie(boolean czyPlanowane) {
        this(czyPlanowane, new ArrayList<>(), null);
    }

    public Zlecenie(boolean czyPlanowane, Brygada brigade) {
        this(czyPlanowane, new ArrayList<>(), brigade);
    }

    public Zlecenie(boolean czyPlanowane, Collection<Praca> work) {
        this(czyPlanowane, work, null);
    }

    public Zlecenie(boolean czyPlanowane, Collection<Praca> work, Brygada brigade) {
        this.czyPlanowane = czyPlanowane;
        this.work = work;
        this.brigade = brigade;
        this.created = LocalDateTime.now();
    }

    public boolean isFinished() {
        return this.state.equals(StanZlecenia.ZAKONCZONE);
    }

    public boolean addWork(Praca newWork) {
        ActionLogger.saveAction("Try to add new work to task #" + this.getUnique(), new Object[]{ newWork });
        if (!this.state.moznaModyfikowac) {
            ActionLogger.saveAction("\tWork not added!");
            return false;
        }
        ActionLogger.saveAction("\tWork added successfully!");
        this.work.add(newWork);

        return true;
    }

    public boolean setBrigade(Brygada newBrigade) {
        if (this.brigade != null) {
            return false;
        }

        ActionLogger.saveAction("Set new brigade for task #" + this.getUnique(), new Object[]{ newBrigade });
        this.brigade = newBrigade;

        return true;
    }

    public StanZlecenia getState() {
        return state;
    }

    @Override
    public void run() {
        if (this.brigade == null || this.work.isEmpty()) {
            throw new InvalidParameterException(
                "Nie możesz zacząć nowego zlecenia jak nie ustawiłeś brygady albo nie ma pracy do wykonania"
            );
        }

        if (!this.isEveryoneAvailable()) {
            throw new InvalidParameterException(
                "Nie możesz zacząć nowego zlecenia jeżeli nie wszyscy pracownicy są dostępni"
            );
        }

        this.work.forEach(job -> job.start());

        this.started = this.changeState(StanZlecenia.ROZPOCZETE);

        int iterationLimit = 100;
        int counter = 0;
        while (this.work.stream().anyMatch(n -> n.czyZrealizowane() == false)) {

            // Failsafe
            if (counter >= iterationLimit) {
                System.out.println(
                        "Limit czasu oczekiwania na ukończenie pracy osiągnięty! Prace za długo się wykonywały! "
                        + "Całkowity czas wykonania jednego zlecenia nie może przekraczać " + iterationLimit
                        + " jednostek czasu."
                );
                break;
            }
            counter++;

            try {
                Thread.sleep(Praca.unitOfTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        ActionLogger.saveAction(
            "Zadanie #" + this.getUnique() + " zakończyło swoje działanie, zajęło mu to "
            + counter + " jednostek czasu"
        );
        this.finished = this.changeState(StanZlecenia.ZAKONCZONE);
    }

    private LocalDateTime changeState(StanZlecenia state) {
        this.state = state;
        System.out.println(state.komunikat.get(this.getUnique() + ""));
        ActionLogger.saveAction("Task " + this.getUnique() + " changed the status to " + state.etykieta, new Object[]{ state });

        return LocalDateTime.now();
    }

    private boolean isEveryoneAvailable() {
        return this.brigade.getWorkers().stream().allMatch(worker -> {
            if (worker instanceof IWykonawca) {
                return ((IWykonawca) worker).czyDostepny();
            }

            return true;
        });
    }
}
