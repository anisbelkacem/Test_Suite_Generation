package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import de.uni_passau.fim.se2.sbse.suite_generation.utils.Randomness;

/**
 * Represents a constructor statement in the test case.
 */
public class ConstructorStat implements Statement {
    private final Constructor<?> clazz;
    private final Object[] parameters;

    /**
     * Creates a new constructor statement.
     *
     * @param clazz the class to instantiate
     * @param parameters the parameters to pass to the constructor
     */
    public ConstructorStat(Constructor<?> clazz, Object... parameters) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        this.clazz = clazz;
        this.parameters = parameters != null ? parameters : new Object[0];
    }

    /**
     * Executes the constructor statement by invoking the class constructor using reflection.
     */
    @Override
    public void run() {
        try {
            clazz.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException("Error executing constructor for class: " + clazz.getName() + " with parameters: " + Arrays.toString(parameters), e);
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
        sb.append(clazz.getName()).append("(");
        for (int i = 0; i < parameters.length; i++) {
            sb.append(parameters[i] != null ? parameters[i].toString() : "null");
            if (i < parameters.length - 1) sb.append(", ");
        }
        sb.append(");\n");
        return sb.toString();
    }

    /**
     * Retrieves the parameters for the constructor.
     *
     * @return an array of parameters
     */
    public Object[] getParameters() {
        return this.parameters;
    }

    /**
     * Retrieves the class of the constructor.
     *
     * @return the class being instantiated
     */
    public Constructor<?> getClazz() {
        return clazz;
    }
}
