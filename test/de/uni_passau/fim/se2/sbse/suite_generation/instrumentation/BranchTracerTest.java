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
        // Test the passedBranch method with different opcodes and values of i
        branchTracer.passedBranch(5, 153, 1, 2); // ifeq (i == 0)
        branchTracer.passedBranch(5, 154, 1, 2); // ifne (i != 0)
        branchTracer.passedBranch(5, 155, 1, 2); // iflt (i < 0)
        branchTracer.passedBranch(5, 156, 1, 2); // ifge (i >= 0)
        branchTracer.passedBranch(5, 157, 1, 2); // ifgt (i > 0)
        branchTracer.passedBranch(5, 158, 1, 2); // ifle (i <= 0)
    
        Map<Integer, Double> distances = branchTracer.getDistances();
    
        assertEquals(2, distances.size()); // Expecting two branches
        assertEquals(0.0, distances.get(1)); // Distance to true branch (for opcode 153, ifeq)
        assertEquals(0.0, distances.get(2)); // Distance to false branch (for opcode 153, ifeq)
    
        assertEquals(0.0, distances.get(1)); // for ifne, true branch distance (i != 0)
        assertEquals(0.0, distances.get(2)); // for ifne, false branch distance (i == 0)
    
    }
    

    @Test
void testPassedBranchWithBinaryInt() {
    // Test all supported opcodes
    branchTracer.passedBranch(4, 6, 159, 1, 2); // if_icmpeq (i == j)
    branchTracer.passedBranch(4, 4, 159, 3, 4); // if_icmpeq (i == j)
    
    branchTracer.passedBranch(4, 6, 160, 5, 6); // if_icmpne (i != j)
    branchTracer.passedBranch(4, 4, 160, 7, 8); // if_icmpne (i != j)
    
    branchTracer.passedBranch(4, 6, 161, 9, 10); // if_icmplt (i < j)
    branchTracer.passedBranch(6, 4, 161, 11, 12); // if_icmplt (i < j)
    
    branchTracer.passedBranch(4, 6, 162, 13, 14); // if_icmpge (i >= j)
    branchTracer.passedBranch(6, 4, 162, 15, 16); // if_icmpge (i >= j)
    
    branchTracer.passedBranch(4, 6, 163, 17, 18); // if_icmpgt (i > j)
    branchTracer.passedBranch(6, 4, 163, 19, 20); // if_icmpgt (i > j)
    
    branchTracer.passedBranch(4, 6, 164, 21, 22); // if_icmple (i <= j)
    branchTracer.passedBranch(6, 4, 164, 23, 24); // if_icmple (i <= j)
    
    // Verify computed distances
    Map<Integer, Double> distances = branchTracer.getDistances();
    
    assertEquals(2.0, distances.get(1)); // i == j, true branch
    assertEquals(0.0, distances.get(2)); // i == j, false branch
    
    assertEquals(0.0, distances.get(5)); // i != j, true branch
    assertEquals(2.0, distances.get(6)); // i != j, false branch
    
    assertEquals(0.0, distances.get(9)); // i < j, true branch
    assertEquals(2.0, distances.get(10)); // i < j, false branch
    
    assertEquals(2.0, distances.get(13)); // i >= j, true branch
    assertEquals(0.0, distances.get(14)); // i >= j, false branch
    
    assertEquals(3.0, distances.get(17)); // i > j, true branch
    assertEquals(0.0, distances.get(18)); // i > j, false branch
    
    assertEquals(0.0, distances.get(21)); // i <= j, true branch
    assertEquals(3.0, distances.get(22)); // i <= j, false branch
}


    @Test
void testPassedBranchWithNullCheck() {
    branchTracer.passedBranch(null, 198, 1, 2);
    
    Object obj = new Object();
    branchTracer.passedBranch(obj, 199, 3, 4);

    Map<Integer, Double> distances = branchTracer.getDistances();

    assertEquals(4, distances.size()); // Deux branches par test (true et false)

    assertEquals(0.0, distances.get(1)); // True branch
    assertEquals(1.0, distances.get(2)); // False branch

    assertEquals(0.0, distances.get(3)); // True branch
    assertEquals(1.0, distances.get(4)); // False branch
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

    @Test
    void testTraceBranchDistanceWithNegativeDistances() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> branchTracer.traceBranchDistance(1, -1.0, 2, 0.5)
        );
        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> branchTracer.traceBranchDistance(1, 1.0, 2, -0.5)
        );
        assertEquals("Distances must be non-negative", exception.getMessage());
        assertEquals("Distances must be non-negative", exception2.getMessage());
    }

    @Test
    void testTraceBranchDistanceWithNaNDistances() {
        branchTracer = BranchTracer.getInstance();
        branchTracer.clear();
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> branchTracer.traceBranchDistance(1, Double.NaN, 2, 0.5)
        );
        assertEquals("Distances must not be NaN", exception.getMessage());
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> branchTracer.traceBranchDistance(1, 1, 2, Double.NaN)
        );
        assertEquals("Distances must not be NaN", exception1.getMessage());
    }

    @Test
    void testTraceBranchDistanceValidValues() {
        branchTracer = BranchTracer.getInstance();
        branchTracer.clear();
        assertDoesNotThrow(() -> branchTracer.traceBranchDistance(1, 0.0, 2, 0.5));
        
        Map<Integer, Double> distances = branchTracer.getDistances();
        assertEquals(2, distances.size());
        assertEquals(0.0, distances.get(1));
        assertEquals(0.5, distances.get(2));
    }
}

