package edu.pjwstk.s35267.workaholic.infrastructure.contract;

import java.util.HashMap;

public abstract class Identifiable {
    private static HashMap<String, Integer> classCounter = new HashMap<>();
    private static HashMap<String, Object> objectMap = new HashMap<>();
    protected final int unique;

    public static <T> T getById(int id, Class<T> clazz) {
        return (T) Identifiable.objectMap.get(Identifiable.generateObjectHas(id, clazz));
    }

    private static String generateObjectHas(int id, Class clazz) {
        return clazz.getName() + id;
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
        this.unique = setup(this.getClass());
    }

    protected Identifiable(Class clazz) {
        this.unique = setup(clazz);
    }

    protected synchronized int setup(Class clazz) {
        String className = clazz.getName();
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
