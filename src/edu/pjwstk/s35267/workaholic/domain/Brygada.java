package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.domain.contract.Pracownik;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.Identifiable;
import edu.pjwstk.s35267.workaholic.presentation.Zlecenie;

import java.security.InvalidParameterException;
import java.util.ArrayList;

public class Brygada extends Identifiable {
    private String nazwa;
    private Brygadzista brygadzista;
    private ArrayList<Pracownik> pracownicy;
    private ArrayList<Zlecenie> tasks;

    public Brygada(String nazwa, Brygadzista brygadzista) {
        this(nazwa, brygadzista, new ArrayList<>());
    }

    public Brygada(String nazwa, Brygadzista brygadzista, ArrayList<Pracownik> pracownicy) {
        this.nazwa = nazwa;
        this.brygadzista = brygadzista;
        this.pracownicy = pracownicy;
        this.brygadzista.addBrigade(this);
        this.tasks = new ArrayList<>();
    }

    public void addWorker(Pracownik worker) {
        if (worker.getClass() == Uzytkownik.class) {
            throw new InvalidParameterException("Nie możesz dodać obiektu Uzytkownik do pracowników");
        }

        this.pracownicy.add(worker);
    }

    public void addWorker(ArrayList<Pracownik> workers) {
        this.pracownicy.addAll(workers);
    }

    public ArrayList<Pracownik> getWorkers() {
        return pracownicy;
    }

    public void addTask(Zlecenie task) {
        this.tasks.add(task);
    }

    public ArrayList<Zlecenie> getTasks() {
        return tasks;
    }
}
