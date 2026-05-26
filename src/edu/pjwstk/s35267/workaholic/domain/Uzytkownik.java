package edu.pjwstk.s35267.workaholic.domain;

import edu.pjwstk.s35267.workaholic.domain.contract.Pracownik;

import java.time.LocalDate;

public class Uzytkownik extends Pracownik {
    private String login;
    private String password;
    private String inicjaly;

    public Uzytkownik(String name, String surname, LocalDate birth, DzialPracownikow department, String login, String haslo) {
        super(name, surname, birth, department);
        this.login = login;
        this.password = haslo;
        this.detectInitials();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.detectInitials();
    }

    @Override
    public void setSurname(String surname) {
        super.setSurname(surname);
        this.detectInitials();
    }

    public String getInicjaly() {
        return inicjaly;
    }

    private void detectInitials() {
        this.inicjaly = this.name.charAt(0) + "" + this.surname.charAt(0);
    }
}
