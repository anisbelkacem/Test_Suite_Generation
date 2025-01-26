package de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosome;

import java.util.HashMap;
import java.util.Map;

class BranchCovFFTest {
    private BranchCovFF<MyChromosome> fitnessFunction;
    private MyChromosome mockChromosome;
    private final int testBranchId = 1;

    @BeforeEach
    void setUp() {
        fitnessFunction = new BranchCovFF<>(testBranchId);
        mockChromosome = Mockito.mock(MyChromosome.class);
    }

    @Test
    void testApplyAsDouble_NullChromosome_ThrowsException() {
        assertThrows(NullPointerException.class, () -> fitnessFunction.applyAsDouble(null));
    }

    @Test
    void testApplyAsDouble_ChromosomeReturnsNull_ThrowsException() {
        Mockito.when(mockChromosome.call()).thenReturn(null);
        assertThrows(NullPointerException.class, () -> fitnessFunction.applyAsDouble(mockChromosome));
    }

    @Test
    void testApplyAsDouble_BranchNotCovered_ReturnsZero() {
        Map<Integer, Double> distances = new HashMap<>();
        Mockito.when(mockChromosome.call()).thenReturn(distances);

        double result = fitnessFunction.applyAsDouble(mockChromosome);
        assertEquals(0.0, result);
    }

    @Test
    void testApplyAsDouble_BranchCovered_CorrectFitnessValue() {
        Map<Integer, Double> distances = new HashMap<>();
        distances.put(testBranchId, 5.0);
        Mockito.when(mockChromosome.call()).thenReturn(distances);

        double expectedFitness = 5.0 / (1.0 + 5.0); // 5 / 6 = 0.8333
        double result = fitnessFunction.applyAsDouble(mockChromosome);
        assertEquals(expectedFitness, result, 1e-6);
    }

    @Test
    void testIsMinimizing_ReturnsFalse() {
        assertFalse(fitnessFunction.isMinimizing());
    }
}
