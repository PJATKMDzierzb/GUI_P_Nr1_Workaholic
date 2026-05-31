package edu.pjwstk.s35267.workaholic.infrastructure;

import java.io.*;

public class StateRepository {
    public static String path = "./";

    public StateRepository(String path) {
        this.path = path;
    }

    public static void persistAndFlush(Object data, String name) {
        if (name == null) return;

        try (ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(StateRepository.getFileName(data.getClass(), name))
        )) {
            oos.writeObject(data);
        } catch (IOException e) {
            // do nothing
        }
    }

    public static Object find(Class<?> clazz, String name) {
        if (name == null) {
            System.out.println("Empty name for class " + clazz.getSimpleName());
            return null;
        }

        String filename = StateRepository.getFileName(clazz, name);
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File doesn't exist " + filename);
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("There was an error " + e.getMessage());
            return null;
        }
    }

    public static void remove(Serializable data, String name) {
        StateRepository.remove(data.getClass(), name);
    }

    public static void remove(Class<?> clazz, String name) {
        if (name == null) return;

        File file = new File(StateRepository.getFileName(clazz, name));
        if (!file.exists()) {
            return;
        }

        file.delete();
    }

    private static String getFileName(Class<?> clazz, String name) {
        return StateRepository.path + '/' + clazz.getName() + "_" + name + ".bk";
    }
}
