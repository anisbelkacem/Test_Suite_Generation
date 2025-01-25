package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RandomSearchTest {

    private RandomSearch<MyChromosome> randomSearch;
    private Random random;
    private StoppingCondition stoppingCondition;
    private IBranchTracer branchTracer;
    private Set<IBranch> branchesToCover;

    @BeforeEach
    void setUp() {
        random = mock(Random.class);
        stoppingCondition = mock(StoppingCondition.class);
        branchTracer = mock(IBranchTracer.class);
        branchesToCover = new HashSet<>();

        IBranch branch1 = mock(IBranch.class);
        when(branch1.getId()).thenReturn(1);
        IBranch branch2 = mock(IBranch.class);
        when(branch2.getId()).thenReturn(2);

        branchesToCover.add(branch1);
        branchesToCover.add(branch2);

        randomSearch = new RandomSearch<>(
                random, stoppingCondition, 10,
                MyChromosome.class, branchTracer, branchesToCover
        );
    }

    @Test
    void testFindSolutionReturnsNonEmptyListWhenBranchesAreCovered() {
        when(stoppingCondition.searchMustStop()).thenReturn(false, false, true);

        MyChromosome candidate = mock(MyChromosome.class);
        when(candidate.copy()).thenReturn(candidate);

        // Simulate a chromosome covering all branches
        for (IBranch branch : branchesToCover) {
            when(candidate.evaluateBranch(branch)).thenReturn(0.0); // Branch is covered
        }

        List<MyChromosome> solutions = randomSearch.findSolution();

        assertNotNull(solutions, "Solutions list should not be null.");
        assertEquals(branchesToCover.size(), solutions.size(), "Solutions should match the number of branches.");
    }

    @Test
    void testFindSolutionStopsWhenStoppingConditionIsMet() {
        when(stoppingCondition.searchMustStop()).thenReturn(true);

        List<MyChromosome> solutions = randomSearch.findSolution();

        assertNotNull(solutions, "Solutions list should not be null.");
        assertTrue(solutions.isEmpty(), "Solutions should be empty when stopping condition is met immediately.");
        verify(stoppingCondition, times(1)).searchMustStop();
    }

    @Test
    void testFindSolutionHandlesUncoverableBranchesGracefully() {
        when(stoppingCondition.searchMustStop()).thenReturn(false, false, true);

        MyChromosome candidate = mock(MyChromosome.class);
        when(candidate.copy()).thenReturn(candidate);

        // Simulate chromosomes that can't cover any branch
        for (IBranch branch : branchesToCover) {
            when(candidate.evaluateBranch(branch)).thenReturn(Double.MAX_VALUE); // Uncoverable branch
        }
        List<MyChromosome> solutions = randomSearch.findSolution();
        assertNotNull(solutions, "Solutions list should not be null.");;
    }

    @Test
    void testFindSolutionTracksFitnessEvaluation() {
        when(stoppingCondition.searchMustStop()).thenReturn(false, false, true);

        MyChromosome candidate = mock(MyChromosome.class);
        when(candidate.copy()).thenReturn(candidate);

        // Simulate a chromosome that partially covers branches
        for (IBranch branch : branchesToCover) {
            when(candidate.evaluateBranch(branch)).thenReturn(1.0); // Partially covered
        }

        randomSearch.findSolution();

        verify(stoppingCondition, atLeastOnce()).notifyFitnessEvaluation();
    }
}
