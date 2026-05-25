package edu.pjwstk.s35267.workaholic.presentation;

public enum StanZlecenia {
    UTWORZONE("Utworzone", true, nr -> String.format("Zlecenie #%s zostało utworzone", nr)),
    ROZPOCZETE("Rozpoczęte", false, nr -> String.format("Zlecenie #%s zostało rozpoczęte", nr)),
    ZAKONCZONE("Zakończone", false, nr -> String.format("Zlecenie #%s zostało zakończone", nr))
    ;
    public final String etykieta;
    public final boolean moznaModyfikowac;

    public interface LogFn {
        String get(String orderNr);
    }
    public final LogFn komunikat;

    StanZlecenia(String etykieta, boolean moznaModyfikowac, LogFn komunikat) {
        this.etykieta = etykieta;
        this.moznaModyfikowac = moznaModyfikowac;
        this.komunikat = komunikat;
    }
}
