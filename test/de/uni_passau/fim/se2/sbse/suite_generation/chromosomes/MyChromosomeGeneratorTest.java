package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.ConstructorStat;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.MethodStat;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.AssignmentStat;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;
import net.bytebuddy.agent.ByteBuddyAgent.AttachmentProvider.Accessor.Simple;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_generation.examples.Feature;
import de.uni_passau.fim.se2.sbse.suite_generation.examples.SimpleExample;
import de.uni_passau.fim.se2.sbse.suite_generation.examples.Stack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyChromosomeGeneratorTest {

    private Mutation<MyChromosome> mutation;
    private Crossover<MyChromosome> crossover;
    private MyChromosomeGenerator generator;
    private Class<Stack> CUT = Stack.class;

    @BeforeEach
    void setUp() {
        mutation = Mockito.mock(Mutation.class);
        crossover = Mockito.mock(Crossover.class);
        generator = new MyChromosomeGenerator(CUT, mutation, crossover);
    }


    
    @Test
    void testGenerateChromosome() {
        MyChromosome chromosome = generator.get();
        assertNotNull(chromosome, "Generated chromosome should not be null");
        List<Statement> statements = chromosome.getStatements();
        System.out.println("the list of statement of generated chromosome is :"+ statements.toString());
        System.out.println("\n");
        System.out.println("the call methodin Mychromosome  is :"+ chromosome.call());
        assertFalse(statements.isEmpty(), "Chromosome statements should not be empty");
    }

    @Test
    void testGenerateRandomValue() {
        assertNotNull(generator.generateRandomValue(int.class), "Generated int value should not be null");
        assertTrue(generator.generateRandomValue(int.class) instanceof Integer, "Generated value should be of type Integer");
        
        assertNotNull(generator.generateRandomValue(double.class), "Generated double value should not be null");
        assertTrue(generator.generateRandomValue(double.class) instanceof Double, "Generated value should be of type Double");
        
        assertNotNull(generator.generateRandomValue(boolean.class), "Generated boolean value should not be null");
        assertTrue(generator.generateRandomValue(boolean.class) instanceof Boolean, "Generated value should be of type Boolean");
        
        assertNotNull(generator.generateRandomValue(String.class), "Generated String value should not be null");
        assertTrue(generator.generateRandomValue(String.class) instanceof String, "Generated value should be of type String");
        
        assertNotNull(generator.generateRandomValue(long.class), "Generated long value should not be null");
        assertTrue(generator.generateRandomValue(long.class) instanceof Long, "Generated value should be of type Long");
        
        assertNotNull(generator.generateRandomValue(float.class), "Generated float value should not be null");
        assertTrue(generator.generateRandomValue(float.class) instanceof Float, "Generated value should be of type Float");
        
        assertNotNull(generator.generateRandomValue(char.class), "Generated char value should not be null");
        assertTrue(generator.generateRandomValue(char.class) instanceof Character, "Generated value should be of type Character");
        
        
    }
        
        
}

