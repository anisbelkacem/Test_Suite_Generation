package de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MaxFitnessEvaluationsTest {

    private MaxFitnessEvaluations stoppingCondition;

    @BeforeEach
    void setUp() {
        stoppingCondition = new MaxFitnessEvaluations(10);
    }

    @Test
    void testInitialization() {
        assertEquals(10, stoppingCondition.getMaximumFitnessEvaluations());
    }

    @Test
    void testNotifySearchStarted() {
        stoppingCondition.notifySearchStarted();
        assertEquals(0, stoppingCondition.getProgress(), "Progress should be 0 at start");
    }

    @Test
    void testNotifyFitnessEvaluation() {
        stoppingCondition.notifySearchStarted();
        stoppingCondition.notifyFitnessEvaluation();
        assertTrue(stoppingCondition.getProgress() > 0, "Progress should increase after evaluation");
    }

    @Test
    void testNotifyFitnessEvaluations() {
        stoppingCondition.notifySearchStarted();
        stoppingCondition.notifyFitnessEvaluations(5);
        assertEquals(0.5, stoppingCondition.getProgress(), 0.01, "Progress should be 50% after 5 evaluations");
    }

    @Test
    void testSearchMustStop() {
        stoppingCondition.notifySearchStarted();
        for (int i = 0; i < 10; i++) {
            stoppingCondition.notifyFitnessEvaluation();
        }
        assertTrue(stoppingCondition.searchMustStop(), "Search should stop after reaching max evaluations");
    }

    @Test
    void testToString() {
        assertEquals("MaxFitnessEvaluations(10)", stoppingCondition.toString());
    }
    @Test
    void testNotifyFitnessEvaluationsThrowsException() {
        stoppingCondition.notifySearchStarted();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            stoppingCondition.notifyFitnessEvaluations(-1);
        });
        assertEquals("Negative number of evaluations: -1", thrown.getMessage());
    }
}