package de.uni_passau.fim.se2.sbse.suite_generation.instrumentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BranchTracerTest {

    private BranchTracer branchTracer;

    @BeforeEach
    void setUp() {
        branchTracer = BranchTracer.getInstance();
        branchTracer.clear();
    }

    @Test
    void testPassedBranchWithUnaryInt() {
        branchTracer.passedBranch(5, 155, 1, 2); // iflt (i < 0)

        Map<Integer, Double> distances = branchTracer.getDistances();
        assertEquals(2, distances.size());
        assertEquals(6.0, distances.get(1)); // Distance to true branch
        assertEquals(0.0, distances.get(2)); // Distance to false branch
    }

    @Test
    void testPassedBranchWithBinaryInt() {
        branchTracer.passedBranch(4, 6, 161, 1, 2); // if_icmplt (i < j)

        Map<Integer, Double> distances = branchTracer.getDistances();
        assertEquals(2, distances.size());
        assertEquals(0.0, distances.get(1)); // Distance to true branch
        assertEquals(2.0, distances.get(2)); // Distance to false branch
    }

    @Test
    void testPassedBranchWithUnaryObject() {
        Object obj = new Object();
        branchTracer.passedBranch(obj, 198, 1, 2); // ifnull (o == null)

        Map<Integer, Double> distances = branchTracer.getDistances();
        assertEquals(2, distances.size());
        assertEquals(1.0, distances.get(1)); // Distance to true branch
        assertEquals(0.0, distances.get(2)); // Distance to false branch
    }

    @Test
    void testPassedBranchWithBinaryObject() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        branchTracer.passedBranch(obj1, obj2, 165, 1, 2); // if_acmp_eq (o == p)

        Map<Integer, Double> distances = branchTracer.getDistances();
        assertEquals(2, distances.size());
        assertEquals(1.0, distances.get(1)); // Distance to true branch
        assertEquals(0.0, distances.get(2)); // Distance to false branch
    }

    @Test
    void testPassedBranchSingleBranch() {
        branchTracer.passedBranch(1);

        Map<Integer, Double> distances = branchTracer.getDistances();
        assertEquals(1, distances.size());
        assertEquals(0.0, distances.get(1)); // Root branch distance is 0
    }

    

    
}

