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
        int numStatements = random.nextInt(10) + 5; 
        List<Statement> statements = new ArrayList<>();

        ConstructorStat constructorStat = generateConstructorStatement();
        if (constructorStat != null) {
            statements.add(constructorStat);
            Object instance = executeConstructor(constructorStat);

            for (int i = 1; i < numStatements - 1; i++) {
                Statement statement = generateRandomStatement(instance);
                if (statement != null) {
                    statements.add(statement);
                }
            }
        }

        return new MyChromosome(mutation, crossover, statements);
    }

    public Statement generateRandomStatement(Object instance) {
        double probability = random.nextDouble(); 
        if (probability < 0.7) { 
            return generateMethodStatement(instance); 
        } else {
            return generateAssignmentStatement(instance);
        }
    }
    
    public ConstructorStat generateConstructorStatement() {
        Constructor<?>[] constructors = CUT.getConstructors();
        if (constructors.length == 0) return null;

        Constructor<?> constructor = constructors[random.nextInt(constructors.length)];
        Object[] parameters = generateRandomParameters(constructor.getParameterTypes());
        return new ConstructorStat(CUT, parameters);
    }

    public MethodStat generateMethodStatement(Object targetObject) {
        Method[] methods = CUT.getDeclaredMethods();
        if (methods.length == 0) return null;

        Method method = methods[random.nextInt(methods.length)];
        Object[] parameters = generateRandomParameters(method.getParameterTypes());
        return new MethodStat(targetObject, method, parameters);
    }

    public AssignmentStat generateAssignmentStatement(Object targetObject) {
        Field[] fields = CUT.getDeclaredFields();
        if (fields.length == 0) return null;

        Field field = fields[random.nextInt(fields.length)];
        Object value = generateRandomValue(field.getType());
        return new AssignmentStat(targetObject, field.getName(), value);
    }

    public Object executeConstructor(ConstructorStat constructorStat) {
        try {
            Constructor<?> constructor = CUT.getDeclaredConstructor(constructorStat.getParameterTypes());
            constructor.setAccessible(true); // Ensure accessibility
            return constructor.newInstance(constructorStat.getParameters());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Constructor not found in " + CUT.getName() 
                + " with parameters: " + java.util.Arrays.toString(constructorStat.getParameterTypes()), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute constructor for " + CUT.getName() 
                + " with parameters: " + java.util.Arrays.toString(constructorStat.getParameters()), e);
        }
    }
    
    
    public Object[] generateRandomParameters(Class<?>[] parameterTypes) {
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = generateRandomValue(parameterTypes[i]);
        }
        return params;
    }

    public Object generateRandomValue(Class<?> type) {
        if (type == int.class || type == Integer.class) return random.nextInt(100);
        if (type == double.class || type == Double.class) return random.nextDouble();
        if (type == boolean.class || type == Boolean.class) return random.nextBoolean();
        if (type == String.class) return generateRandomString(type);
        return null;
    }
    public String generateRandomString(Class<?> type) {
        Random random = new Random();
        
        if (type == String.class) {
            int length =random.nextInt(15) ;  
            StringBuilder sb = new StringBuilder(length);
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characters.length());
                sb.append(characters.charAt(index));
            }
            
            return sb.toString(); 
        }
        
        return null;
    }
}
