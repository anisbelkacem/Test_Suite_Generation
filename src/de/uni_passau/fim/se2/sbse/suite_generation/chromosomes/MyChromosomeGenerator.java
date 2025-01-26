package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.MethodStat;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.AssignmentStat;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStat;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Randomness;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class MyChromosomeGenerator implements ChromosomeGenerator<MyChromosome> {

    private final Mutation<MyChromosome> mutation;
    private final Crossover<MyChromosome> crossover;
    private final Class<?> CUT;
    private final Randomness random;

    public MyChromosomeGenerator(Class<?> CUT, Mutation<MyChromosome> mutation, Crossover<MyChromosome> crossover) {
        this.CUT = CUT;
        this.mutation = mutation;
        this.crossover = crossover;
        this.random = new Randomness();
    }

    @Override
    public MyChromosome get() {
        //int numberOfStat = random.random().nextInt(30) + 30; 
        int numberOfStat =60; 
        List<Statement> statements = new ArrayList<>();
        Object instance = Instance(statements);
        if (instance != null) { 
            for (int i = 0; i < numberOfStat; i++) {
                Statement statement = generateRandomStatement(instance);
                if (statement != null) {
                    statements.add(statement);
                }
            }
        }
        
        return new MyChromosome(statements);
    }

    public Statement generateRandomStatement(Object instance) {
        //return generateMethodStatement(instance); 
        int probability = random.random().nextInt(100); 
        int bias=random.random().nextInt(100);
        if (probability < bias) { 
            return generateMethodStatement(instance); 
        } else {
            return generateAssignmentStatement(instance);
        }
    }

    public MethodStat generateMethodStatement(Object targetObject) {
        Method[] methods = CUT.getDeclaredMethods();
        if (methods.length == 0) return null;
        Method method = methods[random.random().nextInt(methods.length)];
        Object[] parameters = generateRandomParameters(method.getParameterTypes());
        //method.setAccessible(true);
        //return new MethodStat(targetObject, method, parameters);
        /*if (Modifier.isStatic(method.getModifiers())) {
            return null; 
        }*/
        if (Modifier.isPublic(method.getModifiers())) {
            //System.out.println("Field is public");
            return new MethodStat(targetObject, method, parameters);
        }
    
        return null;
        
    }

    public AssignmentStat generateAssignmentStatement(Object targetObject) {
        Field[] fields = CUT.getDeclaredFields();
        if (fields.length == 0) return null;
        Field field = fields[random.random().nextInt(fields.length)];

        Object value = generateRandomValue(field.getType());
        //field.setAccessible(true);
        //return new AssignmentStat(targetObject,field, value);
        /*if (Modifier.isStatic(field.getModifiers())) {
            return null; 
        }*/
        if (Modifier.isPublic(field.getModifiers())) {
            //System.out.println("Field is public");
            return new AssignmentStat(targetObject,field, value);
        }
        return null;
        
    }

    public Object Instance(List<Statement> statements) {
        try {
            Constructor<?>[] constructs = CUT.getConstructors();
            if (constructs.length > 0) {
                Constructor<?> construct=constructs[Randomness.random().nextInt(constructs.length) ];
                while(!Modifier.isPublic(construct.getModifiers())){
                    construct=constructs[Randomness.random().nextInt(constructs.length)];
                }
                
                Object[] parameters = generateRandomParameters(construct.getParameterTypes());
                statements.add(new ConstructorStat(construct , parameters));
                return construct.newInstance(parameters);
            }
            else {
                return null;    
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    
    public Object[] generateRandomParameters(Class<?>[] parameterTypes) {
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = generateRandomValue(parameterTypes[i]);
        }
        return params;
    }

    @SuppressWarnings("static-access")
    public Object generateRandomValue(Class<?> type) {
        
        double nullProbability = 0.1;
        if (random.random().nextDouble() < nullProbability && (type == Integer.class || type == Float.class || type == Double.class || type == Long.class || type == Short.class || type == Byte.class || type == Boolean.class)) {
            return null;
        }
        //Randomness random = new Randomness();
        if (type == int.class || type == Integer.class) return random.random().nextInt(-1024, 1023);
        if (type == double.class || type == Double.class) return random.random().nextDouble();
        if (type == boolean.class || type == Boolean.class) return random.random().nextBoolean();
        if (type == String.class) return generateRandomString(type);
        if (type == long.class || type == Long.class) return random.random().nextLong();
        if (type == float.class || type == Float.class) return random.random().nextFloat();
        if (type == char.class || type == Character.class) return (char) random.random().nextInt(32, 127);
        if (type == byte.class || type == Byte.class) return (byte) random.random().nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
        if (type == short.class || type == Short.class) return (short) random.random().nextInt(Short.MIN_VALUE, Short.MAX_VALUE);
        if (type == BigDecimal.class) return new BigDecimal(random.random().nextDouble()).setScale(2, RoundingMode.HALF_UP);
        if (type == BigInteger.class) return new BigInteger(130, random.random()); 
        if (type == Date.class) return new Date(random.random().nextLong()); 
        if (type == LocalDate.class) return LocalDate.ofEpochDay(random.random().nextLong()); 
        if (type == LocalDateTime.class) return LocalDateTime.ofEpochSecond(random.random().nextLong(), 0, ZoneOffset.UTC);
        if (type == Instant.class) return Instant.ofEpochSecond(random.random().nextLong()); 
        if (type == UUID.class) return UUID.randomUUID(); 
        return null;
    }
    

    public String generateRandomString(Class<?> type) {
        //Randomness random = new Randomness();
        int length =random.random().nextInt(100) ;  
        StringBuilder sb = new StringBuilder(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789/*-+.-_.:,;?«»()[]&$#!|";
        for (int i = 0; i < length; i++) {
            int index = random.random().nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString(); 
    }
}
