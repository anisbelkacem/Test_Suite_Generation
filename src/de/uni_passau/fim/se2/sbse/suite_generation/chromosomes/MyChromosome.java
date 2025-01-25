package de.uni_passau.fim.se2.sbse.suite_generation.chromosomes;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.Mutation;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.Crossover;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.BranchCovFF;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.BranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;

import java.util.List;
import java.util.Map;

public class MyChromosome extends Chromosome<MyChromosome> {

    private final List<Statement> statements;

    public MyChromosome( List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public MyChromosome self() {
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        MyChromosome otherChromosome = (MyChromosome) other;
        return getStatements().equals(otherChromosome.getStatements());
    }

    @Override
    public int hashCode() {
        return getStatements().hashCode();
    }

    @Override
    public MyChromosome copy() {
        return new MyChromosome( getStatements());
    }

    @Override
    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public Map<Integer, Double> call() {
        try {
            for (Statement statement : statements) {
                //System.out.println("Running statement: " + statement.toString()+ "\n");
                statement.run(); 
                
            }
        } catch (Exception e) {
            //System.out.println("Error while executing the chromosome: " + e+ "\n");
            throw new RuntimeException("Error while executing the chromosome: " + e);
        }

        return BranchTracer.getInstance().getDistances();
    }
    
}
