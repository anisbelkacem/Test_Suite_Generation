package de.uni_passau.fim.se2.sbse.suite_generation.mutation;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.Chromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;

import java.util.List;
import java.util.Random;

public class MyMutation<C extends Chromosome<C>> implements Mutation<C> {

    private final Random random = new Random();

    @Override
    public C apply(final C parent) {
        /*if (parent == null) {
            throw new NullPointerException("Parent chromosome cannot be null");
        }
        C offspring = parent.copy();
        List<Statement> statements = offspring.getStatements();

        if (statements.isEmpty()) {
            return offspring;
        }
        int indexToMutate = random.nextInt(statements.size());
        Statement newStatement = generateRandomStatement();
        statements.set(indexToMutate, newStatement);*/

        return parent;
    }

    @Override
    public String toString() {
        return "Random mutation";
    }
    private Statement generateRandomStatement() {
        return null;
    }
}
