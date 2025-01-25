package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.ChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.SinglePointCrossover;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.BranchCovFF;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.MyMutation;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;

import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class RandomSearch<C extends Chromosome<C>> implements GeneticAlgorithm<C> {
    private final Random random;
    private final int populationSize;
    private final StoppingCondition stoppingCondition;
    private final ChromosomeGenerator<C> chromosomeGenerator;
    private final IBranchTracer branchTracer;
    private final Set<IBranch> branchesToCover;
    private final MyMutation mutation = new MyMutation();
    private final SinglePointCrossover crossover = new SinglePointCrossover();

    public RandomSearch(
            Random random, StoppingCondition stoppingCondition,
            int populationSize, Class<?> testGenerationTarget,
            IBranchTracer branchTracer, Set<IBranch> branchesToCover) {
        this.random = random;
        this.stoppingCondition = stoppingCondition;
        this.populationSize = populationSize;
        this.chromosomeGenerator = (ChromosomeGenerator<C>) new MyChromosomeGenerator(testGenerationTarget, mutation, crossover);
        this.branchTracer = branchTracer;
        this.branchesToCover = branchesToCover;
    }

    @Override
    public List<C> findSolution() {
        stoppingCondition.notifySearchStarted();
        Map<IBranch, C> bestSolutions = new HashMap<>();
        Set<IBranch> uncoveredBranches = new HashSet<>(branchesToCover);

        while (!stoppingCondition.searchMustStop() && !uncoveredBranches.isEmpty()) {
            C candidate = chromosomeGenerator.get();
            Set<IBranch> branchesToRemove = new HashSet<>();
            for (IBranch branch : uncoveredBranches) {
                BranchCovFF<C> fitnessFunction = new BranchCovFF<>(branch.getId());
                double distance = fitnessFunction.applyAsDouble(candidate);
                

                if (distance == 0.0) { 
                    branchesToRemove.add(branch);
                    bestSolutions.put(branch, candidate);
                    stoppingCondition.notifyFitnessEvaluation();
                } else {
                    C bestCandidate = bestSolutions.get(branch);
                    BranchCovFF<C> bestFitnessFunction = new BranchCovFF<>(branch.getId());
                    double bestDistance = (bestCandidate == null) 
                        ? Double.MAX_VALUE 
                        : bestFitnessFunction.applyAsDouble(bestCandidate);

                    if (bestCandidate == null || distance < bestDistance) {
                        bestSolutions.put(branch, candidate);
                        stoppingCondition.notifyFitnessEvaluation();
                        
                    }
                }
            }
            uncoveredBranches.removeAll(branchesToRemove);
            if(bestSolutions.size() % populationSize == 0){
                stoppingCondition.notifyIteration();
            }
            
        }
        return new ArrayList<>(bestSolutions.values());
    }


    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }
}
