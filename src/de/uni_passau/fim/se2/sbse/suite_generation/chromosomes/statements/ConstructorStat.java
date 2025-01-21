package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Constructor;

/**
 * Represents a constructor statement in the test case.
 */
public class ConstructorStat implements Statement {
    private final Class<?> clazz;
    private final Object[] parameters;

    /**
     * Creates a new constructor statement.
     *
     * @param clazz the class to instantiate
     * @param parameters the parameters to pass to the constructor
     */
    public ConstructorStat(Class<?> clazz, Object... parameters) {
        this.clazz = clazz;
        this.parameters = parameters;
    }

    /**
     * Executes the constructor statement by invoking the class constructor using reflection.
     */
    @Override
    public void run() {
        try {
            Constructor<?> constructor = clazz.getConstructor(getParameterTypes(parameters));
            constructor.newInstance(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a string representation of the constructor statement.
     *
     * @return the Java code for this constructor statement
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(clazz.getSimpleName()).append("(");
        for (int i = 0; i < parameters.length; i++) {
            sb.append(parameters[i].getClass().getSimpleName());
            if (i < parameters.length - 1) sb.append(", ");
        }
        sb.append(");");
        return sb.toString();
    }

    private Class<?>[] getParameterTypes(Object[] parameters) {
        Class<?>[] types = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            types[i] = parameters[i].getClass();
        }
        return types;
    }

    public Object[] getparameters() {
        return parameters;
    }
}
