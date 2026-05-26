package edu.pjwstk.s35267.workaholic.application;

public enum RodzajPracy {
    OGOLNA(1),
    MONTAZ(3),
    DEMONTAZ(2),
    WYMIANA(4)
    ;

    public final int weight;

    RodzajPracy(int weight) {
        this.weight = weight;
    }
}
