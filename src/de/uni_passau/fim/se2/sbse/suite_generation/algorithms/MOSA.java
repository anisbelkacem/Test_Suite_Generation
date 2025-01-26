

package de.uni_passau.fim.se2.sbse.suite_generation.algorithms;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.ChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosomeGenerator;
import de.uni_passau.fim.se2.sbse.suite_generation.crossover.SinglePointCrossover;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.BranchCovFF;
import de.uni_passau.fim.se2.sbse.suite_generation.fitness_functions.FitnessFunction;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranch;
import de.uni_passau.fim.se2.sbse.suite_generation.instrumentation.IBranchTracer;
import de.uni_passau.fim.se2.sbse.suite_generation.mutation.MyMutation;
import de.uni_passau.fim.se2.sbse.suite_generation.stopping_conditions.StoppingCondition;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Pair;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Randomness;

import java.util.*;

public class MOSA<C extends Chromosome<C>> implements GeneticAlgorithm<C> {
    private final Randomness random;
    private final int populationSize;
    private final StoppingCondition stoppingCondition;
    private final ChromosomeGenerator<C> chromosomeGenerator;
    private final IBranchTracer branchTracer;
    private final Set<IBranch> branchesToCover;
    private final MyMutation mutation = new MyMutation();
    private final SinglePointCrossover crossover = new SinglePointCrossover();

    public MOSA(
            Random random, StoppingCondition stoppingCondition,
            int populationSize, Class<?> testGenerationTarget,
            IBranchTracer branchTracer, Set<IBranch> branchesToCover) {
        this.random = new Randomness();
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
        List<C> population = initializePopulation();
        Map<C, Integer> coverageMap = new HashMap<>();
        Map<C, Double> distanceMap = new HashMap<>();
        List<C> archive = new ArrayList<>();

        while (!stoppingCondition.searchMustStop() && !uncoveredBranches.isEmpty()) {
            for (C candidate : population) {
                evaluateFitness(candidate, coverageMap, distanceMap);
            }
            archive = updateArchive(population, coverageMap, archive);
            List<C> selectedParents = rankSelection(population, coverageMap);
            population = generateNewPopulation(selectedParents);

            stoppingCondition.notifyFitnessEvaluations(population.size());
        }

        return archive; 
    }

    private List<C> initializePopulation() {
        List<C> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(chromosomeGenerator.get());
        }
        return population;
    }

    private void evaluateFitness(C candidate, Map<C, Integer> coverageMap, Map<C, Double> distanceMap) {
        int coveredBranches = 0;
        double totalDistance = 0.0;

        for (IBranch branch : branchesToCover) {
            BranchCovFF<C> fitnessFunction = new BranchCovFF(branch.getId());
            Double distance = fitnessFunction.applyAsDouble(candidate);
            if (distance == 0.0) {
                coveredBranches++;
            }
            totalDistance += distance;
            distanceMap.put(candidate, Math.min(distanceMap.getOrDefault(candidate, Double.MAX_VALUE), distance));
        }

        coverageMap.put(candidate, coveredBranches);
    }

    private List<C> updateArchive(List<C> population, Map<C, Integer> coverageMap, List<C> archive) {
        // Add individuals with new or better coverage to archive
        for (C candidate : population) {
            if (coverageMap.get(candidate) > 0 && !archive.contains(candidate)) {
                archive.add(candidate);
            }
        }
        return archive;
    }

    private List<C> rankSelection(List<C> population, Map<C, Integer> coverageMap) {
        population.sort(Comparator.comparingInt(c -> coverageMap.getOrDefault(c, 0)).reversed());
        return population.subList(0, population.size() / 2); // Top 50% for selection
    }

    private List<C> generateNewPopulation(List<C> selectedParents) {
        List<C> newPopulation = new ArrayList<>();
        while (newPopulation.size() < populationSize) {
            C parent1 = selectedParents.get(random.random().nextInt(selectedParents.size()));
            C parent2 = selectedParents.get(random.random().nextInt(selectedParents.size()));
            Pair<C> offspring = crossover.apply(parent1, parent2);
            C offspring1 = (C) mutation.apply(offspring.getFst());
            C offspring2 = (C) mutation.apply(offspring.getSnd());
            newPopulation.add(offspring1);
            newPopulation.add(offspring2);
        }
        return newPopulation;
    }

    @Override
    public StoppingCondition getStoppingCondition() {
        return stoppingCondition;
    }
}

