package edu.pjwstk.s35267.workaholic.infrastructure.contract;

public abstract class IdentifiableThread extends Thread {
    protected Identifiable identifiable;

    protected IdentifiableThread() {
        this.identifiable = new Identifiable(this) {};
    }

    public int getUnique() {
        return this.identifiable.getUnique();
    }

    public static <T> T getById(int id, Class clazz) {
        return Identifiable.getById(id, clazz);
    }
    public static <T> T getById(int id, String clazz) {
        return Identifiable.getById(id, clazz);
    }

    @Override
    public String toString() {
        return this.identifiable.toString();
    }
}
