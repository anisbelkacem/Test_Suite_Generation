package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.BranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyChromosomeTest {

    private List<Statement> statements;
    private MyChromosome chromosome;
    private BranchTracer mockTracer;

    @BeforeEach
    void setUp() {
        // Mocking the BranchTracer singleton
        mockTracer = mock(BranchTracer.class);
        try (MockedStatic<BranchTracer> mockedStatic = Mockito.mockStatic(BranchTracer.class)) {
            mockedStatic.when(BranchTracer::getInstance).thenReturn(mockTracer);

            // Creating mock statements
            Statement statement1 = mock(Statement.class);
            Statement statement2 = mock(Statement.class);
            statements = Arrays.asList(statement1, statement2);

            chromosome = new MyChromosome(statements);
        }
    }

    @Test
    void testCopyCreatesIdenticalChromosome() {
        MyChromosome copy = chromosome.copy();
        assertEquals(chromosome, copy);
        assertNotSame(chromosome, copy);
    }

    @Test
    void testCallExecutesStatementsAndReturnsBranchDistances() {
        try (MockedStatic<BranchTracer> mockedStatic = Mockito.mockStatic(BranchTracer.class)) {
            mockedStatic.when(BranchTracer::getInstance).thenReturn(mockTracer);

            Map<Integer, Double> mockDistances = Map.of(1, 0.5, 2, 0.2);
            when(mockTracer.getDistances()).thenReturn(mockDistances);

            Map<Integer, Double> result = chromosome.call();

            verify(statements.get(0)).run();
            verify(statements.get(1)).run();
            assertEquals(mockDistances, result);
        }
    }
}
