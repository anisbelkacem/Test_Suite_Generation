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
        Set<IBranch> uncoveredBranches = new HashSet<>(branchesToCover);
        List<C> population = new ArrayList<>();
        Map<C, Integer> coverageMap = new HashMap<>();
        Map<C, Double> distanceMap = new HashMap<>();

        while (!stoppingCondition.searchMustStop() && !uncoveredBranches.isEmpty()) {
            C candidate = chromosomeGenerator.get();
            int coveredBranches = 0;
            double totalDistance = 0.0;
            
            for (IBranch branch : branchesToCover) {
                BranchCovFF<C> fitnessFunction = new BranchCovFF(branch.getId());
                Double distance = fitnessFunction.applyAsDouble(candidate);
                stoppingCondition.notifyFitnessEvaluation();
                if (distance == 0.0) {
                    coveredBranches++;
                } else {
                    distanceMap.put(candidate, distanceMap.getOrDefault(candidate, Double.MAX_VALUE));
                    distanceMap.put(candidate, Math.min(distanceMap.get(candidate), distance));
                }
                totalDistance += distance;
            }
            
            coverageMap.put(candidate, coveredBranches);
            population.add(candidate);
            //stoppingCondition.notifyFitnessEvaluation();
        }

        // Step 1: Sort by covered branches (descending)
        population.sort((c1, c2) -> Integer.compare(coverageMap.get(c2), coverageMap.get(c1)));
        
        // Step 2: Group by covered branches
        Map<Integer, List<C>> groupedPopulation = new TreeMap<>(Collections.reverseOrder());
        for (C chromosome : population) {
            int covered = coverageMap.get(chromosome);
            groupedPopulation.computeIfAbsent(covered, k -> new ArrayList<>()).add(chromosome);
        }
        
        // Step 3: Sort each group by branch distance (ascending)
        List<C> sortedPopulation = new ArrayList<>();
        for (List<C> group : groupedPopulation.values()) {
            group.sort(Comparator.comparingDouble(c -> distanceMap.getOrDefault(c, Double.MAX_VALUE)));
            sortedPopulation.addAll(group);
        }
        return sortedPopulation.subList(0, Math.min(populationSize, sortedPopulation.size()));
    }

    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }
}
