package edu.pjwstk.s35267.workaholic.domain.contract;

import edu.pjwstk.s35267.workaholic.domain.DzialPracownikow;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.Identifiable;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Pracownik extends Identifiable implements Comparable<Pracownik> {
    static ArrayList<Pracownik> workers = new ArrayList<>();
    protected String name;
    protected String surname;
    protected LocalDate birth;
    protected DzialPracownikow department;

    public Pracownik(String name, String surname, LocalDate birth, DzialPracownikow department) {
        this.name = name;
        this.surname = surname;
        this.birth = birth;
        this.department = department;
        this.department.addWorker(this);
        Pracownik.workers.add(this);
    }

    @Override
    public int compareTo(Pracownik o) {
        Comparable[][] toCompare = { { this.name, o.name }, { this.surname, o.surname }, { this.birth, o.birth } };
        for (Comparable[] comparable : toCompare ) {
            int result = comparable[0].compareTo(comparable[1]);
            if (result != 0){
                return result;
            }
        }

        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
