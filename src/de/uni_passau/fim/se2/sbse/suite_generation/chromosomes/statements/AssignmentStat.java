package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Field;

public class AssignmentStat implements Statement {
    private final Object value;
    private final Object targetObject;
    private final Field field;
      // The object containing the field

    /**
     * Creates a new assignment statement.
     *
     * @param targetObject the object where the field is located (null for local variables)
     * @param variableName the name of the field (variable) to assign a value to
     * @param value the value to assign to the field
     */
    public AssignmentStat(Object targetObject,Field field, Object value) {
        this.targetObject = targetObject;
        this.field = field;
        this.value = value;
    }

    /**
     * Executes the assignment statement by setting the value to the field of the target object.
     */
    @Override
    public void run() {
        try {
            field.setAccessible(true);
            field.set(targetObject, value);
        } catch (Exception e) {
            throw new RuntimeException("Error while running Assignement "+ e);
        }
    }


    /**
     * Returns a string representation of the assignment statement.
     *
     * @return the Java code for this assignment statement
     */
    @Override
    public String toString() {
        return field.getName() + " = " + value + ";\n";
    }
}
