package de.uni_passau.fim.se2.sbse.suite_generation.crossover;

import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.MyChromosome;
import de.uni_passau.fim.se2.sbse.suite_generation.chromosomes.statements.Statement;
import de.uni_passau.fim.se2.sbse.suite_generation.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SinglePointCrossoverTest {

    private SinglePointCrossover<MyChromosome> crossover;

    @BeforeEach
    void setUp() {
        crossover = new SinglePointCrossover<>();
    }

    @Test
    void testCrossoverProducesValidOffspring() {
        List<Statement> parent1Statements = new ArrayList<>();
        List<Statement> parent2Statements = new ArrayList<>();

        // Ajouter des instructions factices aux parents (mocking peut être utilisé ici)
        parent1Statements.add(() -> {});
        parent1Statements.add(() -> {});
        parent2Statements.add(() -> {});
        parent2Statements.add(() -> {});

        MyChromosome parent1 = new MyChromosome(parent1Statements);
        MyChromosome parent2 = new MyChromosome(parent2Statements);

        Pair<MyChromosome> offspring = crossover.apply(parent1, parent2);

        assertNotNull(offspring);
        assertNotNull(offspring.getFst());
        assertNotNull(offspring.getSnd());
        
        assertFalse(offspring.getFst().getStatements().isEmpty());
        assertFalse(offspring.getSnd().getStatements().isEmpty());
    }
}
