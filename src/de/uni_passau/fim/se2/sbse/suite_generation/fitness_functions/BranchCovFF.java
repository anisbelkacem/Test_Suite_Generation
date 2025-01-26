package de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.*;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.BranchTracer;

import java.util.Map;

public class BranchCovFF<C> implements FitnessFunction<C> {

    private final int branchId;

    
    public BranchCovFF(int branchId) {
        this.branchId = branchId;
    }

    @Override
    public double applyAsDouble(C chromosome) {
        BranchTracer.getInstance().clear();
        if (chromosome == null) {
            throw new NullPointerException("Chromosome cannot be null.");
        }

        Map<Integer, Double> branchDist = ((MyChromosome) chromosome).call(); 
        //System.out.println("the call methodin Mychromosome  is :"+ branchDist+"\n");

        if (branchDist == null) {
            throw new NullPointerException("Branch distances cannot be null.");
        }
        double Dist = branchDist.getOrDefault(branchId, Double.MAX_VALUE);
        //System.out.println("the Dist  is :"+ Dist +"\n");
        if(Dist==Double.MAX_VALUE) return 0.0;
        return (double) Dist/ (1.0 + Dist); 
    }


    @Override
    public boolean isMinimizing() {
        return false;
    }
    
    public int getBranchId() {
        return branchId;
    }

}
