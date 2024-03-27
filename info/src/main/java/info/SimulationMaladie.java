package info;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.MersenneTwister;

public class SimulationMaladie {
    protected static final int TAILLE_GRILLE = 300;
    protected static final int NB_INDIVIDUS = 20000;
    protected static final int NB_ITERATIONS = 730;
    protected static final int NB_SIMULATIONS = 100;

    /* -------------------------------------------------------------------- */

    /*
     * initialiserPopulation Créer un population d'individu de 20000 personnes
     * différentes
     */

    /*
     * En entrée: random Une instance de MersenneTwister
     */

    /* En sortie: une liste d'individu */

    protected static List<Individu> initialiserPopulation(MersenneTwister random) {
        List<Individu> population = new ArrayList<>();
        for (int i = 0; i < NB_INDIVIDUS; i++) {
            int dE = (int) negExp(3, random);
            int dI = (int) negExp(7, random);
            int dR = (int) negExp(365, random);
            population.add(new Individu(dE, dI, dR));
        }
        return population;
    }

    /* -------------------------------------------------------------------- */

    /*
     * negExp Fonction issue du sujet
     */

    /*
     * En entrée: random Une instance de MersenneTwister, inMean un double
     */

    /* En sortie: Un entier */

    /* -------------------------------------------------------------------- */

    private static double negExp(double mean, MersenneTwister random) {
        return -mean * Math.log(1 - random.nextDouble());
    }

    /* -------------------------------------------------------------------- */

    /*
     * deplacerIndividus Déplace un individu au hasard dans la grille
     */

    /*
     * En entrée: random Une instance de MersenneTwister, population une liste
     * d'individu
     */

    /* En sortie: Rien */

    /* -------------------------------------------------------------------- */

    protected static void deplacerIndividus(List<Individu> population, MersenneTwister random) {

        for (Individu individu : population) {
            int x = random.nextInt(TAILLE_GRILLE);
            int y = random.nextInt(TAILLE_GRILLE);
            individu.setPosition(x, y);
        }
    }
}
