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
    private final Random random;

    public MyChromosomeGenerator(Class<?> CUT, Mutation<MyChromosome> mutation, Crossover<MyChromosome> crossover) {
        this.CUT = CUT;
        this.mutation = mutation;
        this.crossover = crossover;
        this.random = new Random();
    }

    @Override
    public MyChromosome get() {
        //int numberOfStat = random.nextInt(50) + 1; 
        int numberOfStat =50; 
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
        for (Statement st: statements) {
            System.out.println(st.toString()+ "\n");
        }
        
        return new MyChromosome(statements);
    }

    public Statement generateRandomStatement(Object instance) {
        //return generateMethodStatement(instance); 
        double probability = random.nextDouble(); 
        double bias=random.nextDouble();
        if (probability < bias) { 
            return generateMethodStatement(instance); 
        } else {
            return generateAssignmentStatement(instance);
        }
    }

    public MethodStat generateMethodStatement(Object targetObject) {
        Method[] methods = CUT.getDeclaredMethods();
        if (methods.length == 0) return null;

        Method method = methods[random.nextInt(methods.length)];
        Object[] parameters = generateRandomParameters(method.getParameterTypes());
        if (Modifier.isPublic(method.getModifiers())) {
            //System.out.println("Field is public");
            return new MethodStat(targetObject, method, parameters);
        }
        return null;
        
    }

    public AssignmentStat generateAssignmentStatement(Object targetObject) {
        Field[] fields = CUT.getDeclaredFields();
        if (fields.length == 0) return null;

        Field field = fields[random.nextInt(fields.length)];
        Object value = generateRandomValue(field.getType());
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
                Constructor<?> construct = constructs[0];
                Object[] parameters = generateRandomParameters(construct.getParameterTypes());
                statements.add(new ConstructorStat(construct , parameters));
                return construct.newInstance(parameters);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
    
    
    public Object[] generateRandomParameters(Class<?>[] parameterTypes) {
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            params[i] = generateRandomValue(parameterTypes[i]);
        }
        return params;
    }

    public Object generateRandomValue(Class<?> type) {
    if (type == int.class || type == Integer.class) return random.nextInt(-1024, 1023);
    if (type == double.class || type == Double.class) return random.nextDouble();
    if (type == boolean.class || type == Boolean.class) return random.nextBoolean();
    if (type == String.class) return generateRandomString(type);
    if (type == long.class || type == Long.class) return random.nextLong();
    if (type == float.class || type == Float.class) return random.nextFloat();
    if (type == char.class || type == Character.class) return (char) random.nextInt(32, 127);
    if (type == byte.class || type == Byte.class) return (byte) random.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
    if (type == short.class || type == Short.class) return (short) random.nextInt(Short.MIN_VALUE, Short.MAX_VALUE);
    if (type == BigDecimal.class) return new BigDecimal(random.nextDouble()).setScale(2, RoundingMode.HALF_UP);
    if (type == BigInteger.class) return new BigInteger(130, random); 
    if (type == Date.class) return new Date(random.nextLong()); 
    if (type == LocalDate.class) return LocalDate.ofEpochDay(random.nextLong()); 
    if (type == LocalDateTime.class) return LocalDateTime.ofEpochSecond(random.nextLong(), 0, ZoneOffset.UTC);
    if (type == Instant.class) return Instant.ofEpochSecond(random.nextLong()); 
    if (type == UUID.class) return UUID.randomUUID(); 
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
