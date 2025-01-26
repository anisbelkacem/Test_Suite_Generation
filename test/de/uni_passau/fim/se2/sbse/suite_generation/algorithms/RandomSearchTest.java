package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.examples.DeepBranches;
import de.uni_passau.fim.se2.sbse.suite_generation.examples.Feature;
import de.uni_passau.fim.se2.sbse.suite_generation.examples.SimpleExample;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.BranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.util.*;

class RandomSearchTest {
    
    private RandomSearch randomSearch1;
    private BranchTracer mockTracer;
    private Random random;
    private StoppingCondition stoppingCondition;

    @BeforeEach
    void setUp() {
        mockTracer = mock(BranchTracer.class);
        random = new Random();
        stoppingCondition = mock(StoppingCondition.class);

        try (MockedStatic<BranchTracer> mockedStatic = Mockito.mockStatic(BranchTracer.class)) {
            mockedStatic.when(BranchTracer::getInstance).thenReturn(mockTracer);

            Set<IBranch> branches = new HashSet<>();
            IBranch branch1 = mock(IBranch.class);
            IBranch branch2 = mock(IBranch.class);
            when(branch1.getId()).thenReturn(1);
            when(branch2.getId()).thenReturn(2);
            branches.add(branch1);
            branches.add(branch2);

            when(mockTracer.getBranches()).thenReturn(branches);
            when(mockTracer.getBranchById(1)).thenReturn(branch1);
            when(mockTracer.getBranchById(2)).thenReturn(branch2);
            when(mockTracer.getDistances()).thenReturn(Map.of(1, 0.5, 2, 0.2));
            when(stoppingCondition.searchMustStop()).thenReturn(false, false, false, true); // Stop after a few iterations

            randomSearch1 = new RandomSearch(random, stoppingCondition, 10, SimpleExample.class, mockTracer, branches);
            
        }
    }

    @Test
    void testRandomSearchSelectsBestTestCases() {
        List<MyChromosome> bestTests1 = randomSearch1.findSolution();
        

        assertNotNull(bestTests1);
        
        assertFalse(bestTests1.isEmpty());
        
    }
}
