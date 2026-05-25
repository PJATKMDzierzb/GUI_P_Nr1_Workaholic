package edu.pjwstk.s35267.workaholic.application;

import java.util.ArrayList;
import java.util.Arrays;

public enum RodzajPracy {
    OGOLNA(1, "Praca ogólna"),
    MONTAZ(3, "Montaż", OGOLNA),
    DEMONTAZ(2, "Demontaż", OGOLNA),
    WYMIANA(4, "Wymiana", OGOLNA, DEMONTAZ)
    ;

    public final int weight;
    public final String description;
    public final ArrayList<RodzajPracy> dependencies;

    RodzajPracy(int weight, String description, RodzajPracy... dependencies) {
        this.weight = weight;
        this.description = description;
        this.dependencies = new ArrayList<>(Arrays.asList(dependencies));
    }

    RodzajPracy(int weight, String description) {
        this.weight = weight;
        this.description = description;
        this.dependencies = new ArrayList<>();
    }
}
