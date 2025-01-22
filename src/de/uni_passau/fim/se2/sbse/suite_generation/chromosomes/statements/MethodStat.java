package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access to method: " + method.getName() + " on " + targetObject.getClass().getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Exception thrown by method: " + method.getName() + " on " + targetObject.getClass().getName(), e.getTargetException());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid arguments for method: " + method.getName() + " on " + targetObject.getClass().getName() + " with parameters: " + Arrays.toString(parameters), e);
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
            sb.append(parameters[i]);
            if (i < parameters.length - 1) sb.append(", ");
        }
        sb.append(");\n");
        return sb.toString();
    }
    
    public Object[] getParams() {
        return parameters;
    }

    public Method getMethod() {
        return method;
    }
}
