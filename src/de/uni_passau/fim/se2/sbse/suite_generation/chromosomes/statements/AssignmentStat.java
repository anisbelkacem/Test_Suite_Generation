package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Field;

public class AssignmentStat implements Statement {
    private final String variableName;
    private final Object value;
    private final Object targetObject;  // The object containing the field

    /**
     * Creates a new assignment statement.
     *
     * @param targetObject the object where the field is located (null for local variables)
     * @param variableName the name of the field (variable) to assign a value to
     * @param value the value to assign to the field
     */
    public AssignmentStat(Object targetObject, String variableName, Object value) {
        this.targetObject = targetObject;
        this.variableName = variableName;
        this.value = value;
    }

    /**
     * Executes the assignment statement by setting the value to the field of the target object.
     */
    @Override
    public void run() {
        try {
            Field field = targetObject.getClass().getDeclaredField(variableName);
            field.setAccessible(true);  
            field.set(targetObject, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("exception during execution of assignment statement :", e);
        }
    }

    /**
     * Returns a string representation of the assignment statement.
     *
     * @return the Java code for this assignment statement
     */
    @Override
    public String toString() {
        return variableName + " = " + value + ";";
    }
}
