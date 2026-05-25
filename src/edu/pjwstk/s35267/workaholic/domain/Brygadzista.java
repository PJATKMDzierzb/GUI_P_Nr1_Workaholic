package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.domain.contract.IWykonawca;
import edu.pjwstk.s35267.workaholic.presentation.Zlecenie;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class Brygadzista extends Uzytkownik implements IWykonawca {
    private ArrayList<Brygada> brigades;

    public Brygadzista(
        String name,
        String surname,
        Date birth,
        DzialPracownikow department,
        String login,
        String haslo
    ) {
        super(name, surname, birth, department, login, haslo);
        this.brigades = new ArrayList<>();
    }

    // @TODO
    @Override
    public String getSpecjalizacja() {
        return "Brygadzista";
    }

    public void addBrigade(Brygada brigade) {
        this.brigades.add(brigade);
    }

    public ArrayList<Brygada> getBrigades() {
        return this.brigades;
    }

    public ArrayList<Zlecenie> getAllTasks() {
        return this.brigades.stream()
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
