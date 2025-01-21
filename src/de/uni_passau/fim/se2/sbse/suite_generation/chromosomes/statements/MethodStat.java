package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.Method;

/**
 * Represents a method call statement in the test case.
 */
public class MethodStat implements Statement {
    private final Object targetObject;
    private final Method method;
    private final Object[] parameters;

    /**
     * Creates a new method statement.
     *
     * @param targetObject the object on which the method will be called
     * @param method the method to invoke
     * @param parameters the parameters to pass to the method
     */
    public MethodStat(Object targetObject, Method method, Object... parameters) {
        this.targetObject = targetObject;
        this.method = method;
        this.parameters = parameters;
    }

    /**
     * Executes the method call statement by invoking the method using reflection.
     */
    @Override
    public void run() {
        try {
            method.setAccessible(true); 
            method.invoke(targetObject, parameters);
        } catch (Exception e) {
            throw new RuntimeException("exception of running method :", e);
        }
    }

    /**
     * Returns a string representation of the method call statement.
     *
     * @return the Java code for this method call statement
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(targetObject.getClass().getSimpleName()).append(".")
          .append(method.getName()).append("(");
        for (int i = 0; i < parameters.length; i++) {
            sb.append(parameters[i].getClass().getSimpleName());
            if (i < parameters.length - 1) sb.append(", ");
        }
        sb.append(");");
        return sb.toString();
    }
    
    public Object[] getParams() {
        return parameters;
    }

    public Method getMethod() {
        return method;
    }
}
