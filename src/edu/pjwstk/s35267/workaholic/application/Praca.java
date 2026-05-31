package edu.pjwstk.s35267.workaholic.application;

import edu.pjwstk.s35267.workaholic.domain.ActionLogger;
import edu.pjwstk.s35267.workaholic.infrastructure.contract.IdentifiableThread;

import java.util.ArrayList;

public class Praca extends IdentifiableThread {
    public static final Object classLock = new Object();
    public static int unitOfTime = 1000;
    private RodzajPracy rodzajPracy;
    private int czasPracy;
    private boolean czyZrealizowane = false;
    private String opis;
    private ArrayList<Praca> dependencies;

    public static Praca getById(int id) {
        return IdentifiableThread.getById(id, Praca.class.getName());
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
            if (this.czyZrealizowane()) {
                ActionLogger.saveAction("Work already finished! " + this.opis + " #" + this.getUnique());
                System.out.println("Praca #" + this.getUnique() + " (" + this.getOpis() + ") już jest ukończona!");
                return;
            }

            ActionLogger.saveAction("Start work " + this.opis + " #" + this.getUnique());
            System.out.println("Praca #" + this.getUnique() + " (" + this.getOpis() + ") została zakolejkowana!");
            synchronized (classLock) {
                while (
                    !this.dependencies.isEmpty()
                    && this.dependencies.stream().anyMatch(n -> !n.czyZrealizowane)
                ) {
                    classLock.wait();
                }
            }

            ActionLogger.saveAction("Work " + this.opis + " #" + this.getUnique() + " can proceed!");
            System.out.println("Praca #" + this.getUnique() + " (" + this.getOpis() + ") zaczyna swoje wykonywanie!");
            Thread.sleep(this.czasPracy + (this.unitOfTime * this.rodzajPracy.weight));
            System.out.println("Praca #" + this.getUnique() + " zakończyła swoje wykonywanie!");
            synchronized (classLock) {
                this.czyZrealizowane = true;
                classLock.notifyAll();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean czyZrealizowane() {
        return czyZrealizowane;
    }

    public String getOpis() {
        return opis;
    }
}
