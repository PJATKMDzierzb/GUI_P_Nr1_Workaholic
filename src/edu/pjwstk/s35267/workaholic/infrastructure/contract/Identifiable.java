package edu.pjwstk.s35267.workaholic.infrastructure.contract;

import java.util.HashMap;

public abstract class Identifiable {
    private static HashMap<String, Integer> classCounter = new HashMap<>();
    private static HashMap<String, Object> objectMap = new HashMap<>();
    protected final int unique;

    public static <T> T getById(int id, Class clazz) {
        return (T) Identifiable.objectMap.get(Identifiable.generateObjectHas(id, clazz.getName()));
    }

    public static <T> T getById(int id, String clazz) {
        return (T) Identifiable.objectMap.get(Identifiable.generateObjectHas(id, clazz));
    }

    private static String generateObjectHas(int id, String clazz) {
        return clazz + id;
    }

    public int getUnique() {
        return this.unique;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
            "unique=" + this.getUnique() +
        '}';
    }

    protected Identifiable() {
        this.unique = setup(this);
    }

    protected Identifiable(Object clazz) {
        this.unique = setup(clazz);
    }

    protected synchronized int setup(Object clazz) {
        String className = clazz.getClass().getName();
        int counter = 0;
        if (Identifiable.classCounter.containsKey(className)) {
            counter = Identifiable.classCounter.get(className);
        }

        // Automatically generates a unique ID when ANY child class is instantiated
        counter++;
        Identifiable.objectMap.put(className + counter, clazz);
        Identifiable.classCounter.put(className, counter);

        return counter;
    }
}
