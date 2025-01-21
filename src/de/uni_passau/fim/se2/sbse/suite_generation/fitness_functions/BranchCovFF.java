package de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.*;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranchTracer;

import java.util.Map;
import java.util.Set;

public class BranchCovFF<C> implements FitnessFunction<C> {

    private final IBranchTracer branchTracer;
   

    private final Set<IBranch> branchesToCover;

    public BranchCovFF(IBranchTracer branchTracer, Set<IBranch> branchesToCover) {
        this.branchTracer = branchTracer;
        this.branchesToCover = branchesToCover;
    }

    @Override
    public double applyAsDouble(C chromosome) {
        if (chromosome == null) {
            throw new NullPointerException("Chromosome cannot be null.");
        }

        Map<Integer, Double> branchDistances = ((MyChromosome) chromosome).call(); 

        if (branchDistances == null) {
            throw new NullPointerException("Branch distances cannot be null.");
        }

        double totalFitness = 0.0;
        int branchCount = 0;

        for (IBranch branch : branchesToCover) {
            int branchId = branch.getId();
            double distance = branchDistances.getOrDefault(branchId, Double.MAX_VALUE);
            double branchFitness = 1.0 / (1.0 + distance);
            totalFitness += branchFitness;
            branchCount++;
        }

        return branchCount > 0 ? totalFitness / branchCount : 0; 
    }


    @Override
    public boolean isMinimizing() {
        return false;
    }

    public IBranchTracer getBranchTracer() {
        return branchTracer;
    }

    public Set<IBranch> getBranchesToCover() {
        return branchesToCover;
    }
}
