package edu.pjwstk.s35267.workaholic.domain.contract;

public interface IWykonawca {
    String getSpecjalizacja();

    default boolean czyDostepny() {
        return true;
    }
}
