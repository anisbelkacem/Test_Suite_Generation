package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Constructor;
import java.util.Arrays;

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
            Constructor<?> constructor = clazz.getConstructor(getParameterTypes());
            constructor.setAccessible(true);  // In case the constructor is not public
            constructor.newInstance(parameters);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No such constructor for class: " + clazz.getName() + " with parameters: " + Arrays.toString(parameters), e);
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
        sb.append(clazz.getSimpleName()).append("(");
        for (int i = 0; i < parameters.length; i++) {
            sb.append(parameters[i] != null ? parameters[i] : "null");
            if (i < parameters.length - 1) sb.append(", ");
        }
        sb.append(");\n");
        return sb.toString();
    }

    /**
     * Retrieves the parameter types of the constructor.
     *
     * @return an array of parameter types
     */
    public Class<?>[] getParameterTypes() {
        return Arrays.stream(parameters)
                     .map(param -> param != null ? param.getClass() : Object.class)
                     .toArray(Class<?>[]::new);
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
    public Class<?> getClazz() {
        return clazz;
    }
}
