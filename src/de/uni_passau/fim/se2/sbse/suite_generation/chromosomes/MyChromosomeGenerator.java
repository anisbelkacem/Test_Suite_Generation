package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStat;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.MethodStat;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.AssignmentStat;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyChromosomeGenerator implements ChromosomeGenerator<MyChromosome> {

    private final Mutation<MyChromosome> mutation;
    private final Crossover<MyChromosome> crossover;
    private final Class<?> CUT;
    private final Random random;

    public MyChromosomeGenerator(Class<?> CUT, Mutation<MyChromosome> mutation, Crossover<MyChromosome> crossover) {
        this.CUT = CUT;
        this.mutation = mutation;
        this.crossover = crossover;
        this.random = new Random();
    }

    @Override
    public MyChromosome get() {
        int numStatements = random.nextInt(20) + 10; 
        List<Statement> statements = new ArrayList<>();

        ConstructorStat constructorStat = generateConstructorStatement();
        if (constructorStat != null) {
            statements.add(constructorStat);
            Object instance = executeConstructor(constructorStat);

            for (int i = 1; i < numStatements-1; i++) {
                statements.add(generateRandomStatement(instance));
            }
        }

        return new MyChromosome(mutation, crossover, statements);
    }

    private Statement generateRandomStatement(Object instance) {
        double probability = random.nextDouble(); 
        if (probability < 0.7) { 
            return generateMethodStatement(instance); 
        } else {
            return generateAssignmentStatement(instance);
        }
    }
    
    private ConstructorStat generateConstructorStatement() {
        Constructor<?>[] constructors = CUT.getConstructors();
        if (constructors.length == 0) return null;

        Constructor<?> constructor = constructors[random.nextInt(constructors.length)];
        Object[] parameters = generateRandomParameters(constructor.getParameterTypes());
        return new ConstructorStat(CUT, parameters);
    }

    
    private MethodStat generateMethodStatement(Object targetObject) {
        Method[] methods = CUT.getDeclaredMethods();
        if (methods.length == 0) return null;

        Method method = methods[random.nextInt(methods.length)];
        Object[] parameters = generateRandomParameters(method.getParameterTypes());
        return new MethodStat(targetObject, method, parameters);
    }

    private AssignmentStat generateAssignmentStatement(Object targetObject) {
        Field[] fields = CUT.getDeclaredFields();
        if (fields.length == 0) return null;

        Field field = fields[random.nextInt(fields.length)];
        Object value = generateRandomValue(field.getType());
        return new AssignmentStat(targetObject, field.getName(), value);
    }

    private Object executeConstructor(ConstructorStat constructorStat) {
        try {
            Constructor<?> constructor = CUT.getConstructor(constructorStat.getClass().getConstructors()[0].getParameterTypes());
            return constructor.newInstance(constructorStat.getparameters());
        } catch (Exception e) {
             throw new RuntimeException("can't execute constructor " + e);
        }
    }

    private Object[] generateRandomParameters(Class<?>[] parameterTypes) {
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = generateRandomValue(parameterTypes[i]);
        }
        return params;
    }

    private Object generateRandomValue(Class<?> type) {
        if (type == int.class || type == Integer.class) return random.nextInt(100);
        if (type == double.class || type == Double.class) return random.nextDouble();
        if (type == boolean.class || type == Boolean.class) return random.nextBoolean();
        if (type == String.class) return "randomString" + random.nextInt(100);
        return null;
    }
}
