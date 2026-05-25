package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.domain.contract.IWykonawca;
import edu.pjwstk.s35267.workaholic.domain.contract.Pracownik;

import java.util.Date;

public class Specjalista extends Pracownik implements IWykonawca {
    private String specjalizacja;

    public Specjalista(String name, String surname, Date birth, DzialPracownikow department, String specjalizacja) {
        super(name, surname, birth, department);
        this.specjalizacja = specjalizacja;
    }

    @Override
    public String getSpecjalizacja() {
        return this.specjalizacja;
    }
}
