package de.uni_passau.fim.se2.sbse.suite_generation.crossover;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinglePointCrossover<C extends Chromosome<C>> implements Crossover<C> {

    private final Random random = new Random();

    @Override
    public Pair<C> apply(C parent1, C parent2) {
        if (parent1 == null || parent2 == null) {
            throw new NullPointerException("Parents cannot be null");
        }
        List<Statement> parent1Statements = parent1.getStatements();
        List<Statement> parent2Statements = parent2.getStatements();
        int crossoverPoint = random.nextInt(Math.min(parent1Statements.size(), parent2Statements.size()));

        List<Statement> offspring1Statements = new ArrayList<>();
        List<Statement> offspring2Statements = new ArrayList<>();
        offspring1Statements.addAll(parent1Statements.subList(0, crossoverPoint));
        offspring1Statements.addAll(parent2Statements.subList(crossoverPoint, parent2Statements.size()));

        offspring2Statements.addAll(parent2Statements.subList(0, crossoverPoint));
        offspring2Statements.addAll(parent1Statements.subList(crossoverPoint, parent1Statements.size()));

        C offspring1 = parent1.copy();
        C offspring2 = parent2.copy();
        offspring1.getStatements().clear();
        offspring1.getStatements().addAll(offspring1Statements);

        offspring2.getStatements().clear();
        offspring2.getStatements().addAll(offspring2Statements);
        return Pair.of(offspring1, offspring2);
    }

    @Override
    public String toString() {
        return "Single-point crossover";
    }
}
