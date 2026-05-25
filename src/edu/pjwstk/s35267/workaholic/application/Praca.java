package edu.pjwstk.s35267.workaholic.application;

import edu.pjwstk.s35267.workaholic.infrastructure.contract.IdentifiableThread;

import java.util.ArrayList;

public class Praca extends IdentifiableThread {
    public static final int unitOfTime = 1000;
    private RodzajPracy rodzajPracy;
    private int czasPracy; // @TODO what purpose this have??
    private boolean czyZrealizowane = false;
    private String opis; // @TODO what purpose this have??
    private ArrayList<Praca> dependencies;

    public static Praca getById(int id) {
        return IdentifiableThread.getById(id, Praca.class);
    }

    public Praca(RodzajPracy rodzajPracy, int czasPracy, String opis, ArrayList<Praca> dependencies) {
        this.rodzajPracy = rodzajPracy;
        this.czasPracy = czasPracy;
        this.opis = opis;
        this.dependencies = dependencies;
    }

    public Praca(RodzajPracy rodzajPracy, int czasPracy, String opis) {
        this(rodzajPracy, czasPracy, opis, new ArrayList<>());
    }

    @Override
    public void run() {
        try {
            while (
                this.dependencies.size() > 0
                && this.dependencies.stream().anyMatch(n -> n.czyZrealizowane == false)
            ) {
                Thread.sleep(this.unitOfTime);
            }

            System.out.println("Praca #" + this.getUnique() + " zaczyna swoje wykonywanie!");
            Thread.sleep(this.unitOfTime * this.rodzajPracy.weight);
            System.out.println("Praca #" + this.getUnique() + " zakończyła swoje wykonywanie!");
            this.czyZrealizowane = true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean czyZrealizowane() {
        return czyZrealizowane;
    }
}
