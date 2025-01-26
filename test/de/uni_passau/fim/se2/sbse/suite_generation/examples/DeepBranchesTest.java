package de.uni_passau.fim.se2.sbse.suite_generation.examples;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeepBranchesTest {

    // Test for the 'deepIn' method
    @Test
    public void testDeepIn_case1() {
        DeepBranches deepBranches = new DeepBranches();
        // Test case where 'done' should be true
        deepBranches.deepIn(10, 60, 5, 6, 0);
        
    }

    @Test
    public void testDeepIn_case2() {
        DeepBranches deepBranches = new DeepBranches();
        // Test case where 'done' should be false due to condition 'c < a' failing
        deepBranches.deepIn(10, 60, 30, 6, 0);
    }

    @Test
    public void testDeepIn_case3() {
        DeepBranches deepBranches = new DeepBranches();
        // Test case where 'done' should be false due to condition 'a < 20' failing
        deepBranches.deepIn(30, 40, 20, 10, 0);
    }

    @Test
    public void testDeepIn_case4() {
        DeepBranches deepBranches = new DeepBranches();
        // Test case where 'done' should be false due to condition 'd < a && d > e' failing
        deepBranches.deepIn(10, 40, 5, 2, 1);
    }

    // Test for the 'hard' method
    @Test
    public void testHard_case1() {
        DeepBranches deepBranches = new DeepBranches();
        // Test case where 'done' should be true
        deepBranches.hard(1, 2, 4, 5);
    }

    @Test
    public void testHard_case2() {
        DeepBranches deepBranches = new DeepBranches();
        // Test case where 'done' should be false due to condition 'b == 2' failing
        deepBranches.hard(1, 3, 4, 5);
    }

    @Test
    public void testHard_case3() {
        DeepBranches deepBranches = new DeepBranches();
        // Test case where 'done' should be false due to condition 'c == 4' failing
        deepBranches.hard(1, 2, 3, 5);
    }

    @Test
    public void testHard_case4() {
        DeepBranches deepBranches = new DeepBranches();
        // Test case where 'done' should be false due to condition 'd == 5' failing
        deepBranches.hard(1, 2, 4, 4);
    }
}
