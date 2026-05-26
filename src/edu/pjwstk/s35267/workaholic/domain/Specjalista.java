package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.domain.contract.IWykonawca;
import edu.pjwstk.s35267.workaholic.domain.contract.Pracownik;

import java.time.LocalDate;

public class Specjalista extends Pracownik implements IWykonawca {
    private String specjalizacja;
    private boolean available;

    public Specjalista(String name, String surname, LocalDate birth, DzialPracownikow department, String specjalizacja) {
        this(name, surname, birth, department, specjalizacja, true);
    }

    public Specjalista(
        String name,
        String surname,
        LocalDate birth,
        DzialPracownikow department,
        String specjalizacja,
        boolean available
    ) {
        super(name, surname, birth, department);
        this.specjalizacja = specjalizacja;
        this.available = available;
    }

    @Override
    public String getSpecjalizacja() {
        return this.specjalizacja;
    }

    @Override
    public boolean czyDostepny() {
        return this.available;
    }
}
