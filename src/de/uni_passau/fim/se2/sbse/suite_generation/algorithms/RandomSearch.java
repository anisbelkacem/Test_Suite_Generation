package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.ChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.SinglePointCrossover;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.BranchCovFF;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.MyMutation;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;

import java.util.*;
@SuppressWarnings({ "unchecked", "rawtypes" })
public class RandomSearch<C extends Chromosome<C>> implements  GeneticAlgorithm<C> {
    private final Random random;
    private final int populationSize ;
    private final StoppingCondition stoppingCondition;
    private final ChromosomeGenerator<C> chromosomeGenerator;
    private final BranchCovFF<C> fitnessFunction;
    private final IBranchTracer branchTracer;
    private final Set<IBranch> branchesToCover;
    private final MyMutation mutation = new MyMutation();
    private final SinglePointCrossover crossover =new SinglePointCrossover();

    
    public RandomSearch(
        Random random,StoppingCondition stoppingCondition,
        int populationSize,Class<?> testGenerationTarget,
        IBranchTracer branchTracer,Set<IBranch> branchesToCover) {
        this.random=random;
        this.stoppingCondition=stoppingCondition;
        this.populationSize = populationSize;
        this.chromosomeGenerator = (ChromosomeGenerator<C>) new MyChromosomeGenerator(testGenerationTarget, mutation, crossover);
        this.fitnessFunction = new BranchCovFF(branchTracer , branchesToCover);
        this.branchTracer = branchTracer;
        this.branchesToCover = branchesToCover;
    }

    @Override
    public List<C> findSolution(){
        stoppingCondition.notifySearchStarted();
        List<C> population = new ArrayList<>();
        while (stoppingCondition.searchMustStop()) {
            C candidate =  chromosomeGenerator.get();
            double fitness = fitnessFunction.applyAsDouble((C)candidate);
            stoppingCondition.notifyFitnessEvaluation();
            ((MyChromosome)candidate).setFitness(fitness);
            if (isBetter(candidate,population)) {
                population.add((C)candidate);
            }
            if (population.size() >= populationSize) {
                break;
            }
        }
        return population;
    }

    private boolean isBetter(C candidate, List<C> population) {
        for (C stored : population) {
            if (((MyChromosome)stored).getFitness()>= ((MyChromosome)candidate).getFitness()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }
}
