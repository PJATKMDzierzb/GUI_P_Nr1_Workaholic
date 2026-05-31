package edu.pjwstk.s35267.workaholic.presentation;

import edu.pjwstk.s35267.workaholic.application.Praca;
import edu.pjwstk.s35267.workaholic.domain.ActionLogger;
import edu.pjwstk.s35267.workaholic.domain.Brygada;
import edu.pjwstk.s35267.workaholic.domain.contract.IWykonawca;
import edu.pjwstk.s35267.workaholic.infrastructure.StateRepository;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.Identifiable;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Zlecenie extends Identifiable implements Runnable, Serializable {
    private Collection<Praca> work;
    private Brygada brigade;
    private final boolean czyPlanowane;
    private StanZlecenia state = StanZlecenia.UTWORZONE;
    private final LocalDateTime created;
    private LocalDateTime started;
    private LocalDateTime finished;
    private String code;

    public static Zlecenie getById(int id) {
        return Identifiable.getById(id, Zlecenie.class.getName());
    }

    public Zlecenie(boolean czyPlanowane, String code) {
        this(czyPlanowane, new ArrayList<>(), null, code);
    }

    public Zlecenie(boolean czyPlanowane, Brygada brigade, String code) {
        this(czyPlanowane, new ArrayList<>(), brigade, code);
    }

    public Zlecenie(boolean czyPlanowane, Collection<Praca> work, String code) {
        this(czyPlanowane, work, null, code);
    }

    public Zlecenie(boolean czyPlanowane, Collection<Praca> work, Brygada brigade, String code) {
        this.czyPlanowane = czyPlanowane;
        this.work = work;
        this.brigade = brigade;
        this.created = LocalDateTime.now();
        this.code = code;
        StateRepository.persistAndFlush(this, this.code);
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
        StateRepository.persistAndFlush(this, this.code);

        return true;
    }

    public ArrayList<Praca> getWork() {
        return new ArrayList<>(this.work);
    }

    public boolean setBrigade(Brygada newBrigade) {
        if (this.brigade != null) {
            return false;
        }

        ActionLogger.saveAction("Set new brigade for task #" + this.getUnique(), new Object[]{ newBrigade });
        this.brigade = newBrigade;
        StateRepository.persistAndFlush(this, this.code);

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

        long startTime = System.currentTimeMillis();
        synchronized (Praca.classLock) {
            while (this.work.stream().anyMatch(n -> n.czyZrealizowane() == false)) {
                StateRepository.persistAndFlush(this, this.code);
                try {
                    Praca.classLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        long totalDuration = (System.currentTimeMillis() - startTime);
        System.out.println("Zadanie #" + this.getUnique() + " zakończyło swoje działanie, zajęło mu to "
                + (totalDuration/1000.0) + " sekund");
        ActionLogger.saveAction(
            "Zadanie #" + this.getUnique() + " zakończyło swoje działanie, zajęło mu to "
            + (totalDuration/1000.0) + " sekund"
        );
        this.finished = this.changeState(StanZlecenia.ZAKONCZONE);
        StateRepository.persistAndFlush(this, this.code);
    }

    private LocalDateTime changeState(StanZlecenia state) {
        this.state = state;
        System.out.println(state.komunikat.get(this.getUnique() + ""));
        ActionLogger.saveAction("Task " + this.getUnique() + " changed the status to " + state.etykieta, new Object[]{ state });
        StateRepository.persistAndFlush(this, this.code);

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
