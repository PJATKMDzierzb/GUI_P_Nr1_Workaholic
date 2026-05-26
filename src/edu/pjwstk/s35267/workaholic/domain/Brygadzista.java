package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.domain.contract.IWykonawca;
import edu.pjwstk.s35267.workaholic.presentation.Zlecenie;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Brygadzista extends Uzytkownik implements IWykonawca {
    private ArrayList<Brygada> brigades;
    private boolean available;

    public Brygadzista(
        String name,
        String surname,
        LocalDate birth,
        DzialPracownikow department,
        String login,
        String haslo
    ) {
        this(name, surname, birth, department, login, haslo, true);
    }
    public Brygadzista(
        String name,
        String surname,
        LocalDate birth,
        DzialPracownikow department,
        String login,
        String haslo,
        boolean available
    ) {
        super(name, surname, birth, department, login, haslo);
        this.brigades = new ArrayList<>();
        this.available = available;
    }

    @Override
    public String getSpecjalizacja() {
        return "Brygadzista";
    }

    @Override
    public boolean czyDostepny() {
        return this.available;
    }

    public void addBrigade(Brygada brigade) {
        ActionLogger.saveAction("Add new brigade to Brygadzista " + this.name + " " + this.surname, new Object[]{ brigade });
        this.brigades.add(brigade);
    }

    public ArrayList<Brygada> getBrigades() {
        return new ArrayList<>(this.brigades);
    }

    public ArrayList<Zlecenie> getAllTasks() {
        return this.getBrigades().stream()
            .flatMap(brigade -> brigade.getTasks().stream())
            .collect(Collectors.toCollection(ArrayList::new))
        ;
    }

    public ArrayList<Zlecenie> getUnfinishedTasks() {
        return this.getAllTasks().stream()
            .filter(task -> !task.isFinished())
            .collect(Collectors.toCollection(ArrayList::new))
        ;
    }
}
