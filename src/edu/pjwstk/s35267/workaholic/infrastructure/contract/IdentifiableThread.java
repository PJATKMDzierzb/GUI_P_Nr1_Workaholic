package edu.pjwstk.s35267.workaholic.infrastructure.contract;

public abstract class IdentifiableThread extends Thread {
    protected Identifiable identifiable;

    protected IdentifiableThread() {
        this.identifiable = new Identifiable(this.getClass()) {};
    }

    public int getUnique() {
        return this.identifiable.getUnique();
    }

    public static <T> T getById(int id, Class<T> clazz) {
        return Identifiable.getById(id, clazz);
    }

    @Override
    public String toString() {
        return this.identifiable.toString();
    }
}
