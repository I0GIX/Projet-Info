package info;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.random.MersenneTwister;
import com.opencsv.CSVWriter;

public class Main {

    public static void main(String[] args) {

        MersenneTwister random = new MersenneTwister(2);

        List<List<Integer>> resultats = new ArrayList<>();

        for (int i = 0; i < SimulationMaladie.NB_SIMULATIONS; i++) {

            List<Integer> resultatSimulation = new ArrayList<>();
            List<Individu> population = SimulationMaladie.initialiserPopulation(random);

            for (int j = 0; j < SimulationMaladie.NB_ITERATIONS; j++) {
                SimulationMaladie.deplacerIndividus(population, random);
                initmaladie(population);
                resultatSimulation.add(compterStatut(population, 'S'));
                resultatSimulation.add(compterStatut(population, 'E'));
                resultatSimulation.add(compterStatut(population, 'I'));
                resultatSimulation.add(compterStatut(population, 'R'));
                miseAJourStatuts(population, random);
                infection(population, random);
            }
            resultats.add(resultatSimulation);
            ecrireResultats(resultatSimulation, i);
        }
        afficherMoyennes(resultats);
    }

    /* -------------------------------------------------------------------- */

    /*
     * compterVoisinsMalades Compte le nombre de voisin autour de la case (voisinage
     * de Moore)
     */

    /*
     * En entrée: Individu un élément de la classe Individu, population une liste
     * d'individu de la classe individu
     */

    /* En sortie: le nombre d'individu contaminés dans le voisinage de Moore */

    /* -------------------------------------------------------------------- */

    private static int compterVoisinsMalades(Individu individu, List<Individu> population) {
        int nbVoisinsMalades = 0;
        int x = individu.getPositionX();
        int y = individu.getPositionY();
        for (Individu autre : population) {
            if (autre.getStatut() == 'I'
                    && Math.abs(autre.getPositionX() - x) <= 1 && Math.abs(autre.getPositionY() - y) <= 1) {
                nbVoisinsMalades++;
            }
        }
        return nbVoisinsMalades;
    }

    /* -------------------------------------------------------------------- */

    /* infection Determine la probabilité qu'un individu sain se fasse infecter */

    /*
     * En entrée: population une liste d'individu de la classe individu , random le
     * générateur de valeur aléatoire MersenneTwister passé en entrée de fonction
     * pour simplifier le code
     */

    /* En sortie: Rien */

    /* -------------------------------------------------------------------- */

    private static void infection(List<Individu> population, MersenneTwister random) {
        for (Individu individu : population) {
            if (individu.getStatut() == 'S') {
                int nbVoisinsMalades = compterVoisinsMalades(individu, population);
                double p = 1 - Math.exp(-0.5 * nbVoisinsMalades);
                if (random.nextDouble() < p) {
                    individu.setStatut('E');
                }
            }
        }
    }

    /* -------------------------------------------------------------------- */

    /* initmaladie Infecte 20 individu dans la population */

    /* En entrée: population une liste d'individu de la classe individu */

    /* En sortie: Rien */

    /* -------------------------------------------------------------------- */

    private static void initmaladie(List<Individu> population) {
        int cpt = 0;
        for (Individu individu : population) {
            if (cpt < 20) {
                individu.setStatut('E');
                cpt++;
            } else {
                break;
            }
        }
    }

    /* -------------------------------------------------------------------- */

    /*
     * miseAJourStatuts invoque la fonction du même nom dans la classe Individu pour
     * tout les membres de la population
     */

    /*
     * En entrée: population une liste d'individu de la classe individu, random le
     * générateur de valeur aléatoire MersenneTwister passé en entrée de fonction
     * pour simplifier le code
     */

    /* En sortie: Rien */

    /* -------------------------------------------------------------------- */

    private static void miseAJourStatuts(List<Individu> population, MersenneTwister random) {
        for (Individu individu : population) {
            individu.miseAJourStatut(random);
        }
    }

    /* -------------------------------------------------------------------- */

    /* compterStatut Compte le nombre d'individu pour chaque statut */

    /*
     * En entrée: population une liste d'individu de la classe individu, random le
     * générateur de valeur aléatoire MersenneTwister passé en entrée de fonction
     * pour simplifier le code
     */

    /* En sortie: Rien */

    /* -------------------------------------------------------------------- */

    private static int compterStatut(List<Individu> population, char statut) {
        int compteur = 0;
        for (Individu individu : population) {
            if (individu.getStatut() == statut) {
                compteur++;
            }
        }
        return compteur;
    }

    /* -------------------------------------------------------------------- */

    /*
     * ecrireResultats Créer et ouvre un fichier .csv pour inscire les résulats
     * obtenus
     */

    /* En entrée: resultat une liste d'entier, index un entier */

    /* En sortie: Rien */

    /* -------------------------------------------------------------------- */

    private static void ecrireResultats(List<Integer> resultat, int index) {
        String csvFile = "resultats_simulation_" + index + ".csv";
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            String[] ligne = new String[resultat.size()];
            for (int i = 0; i < resultat.size(); i++) {
                ligne[i] = String.valueOf(resultat.get(i));
            }
            writer.writeNext(ligne);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* -------------------------------------------------------------------- */

    /*
     * afficherMoyennes Afficher les moyennes calculer a la fin des simulations
     */

    /* En entrée: resultats une liste de listes d'entier */

    /* En sortie: Rien , les sorties se font avec des println */

    /* -------------------------------------------------------------------- */

    private static void afficherMoyennes(List<List<Integer>> resultats) {
        int nbSimulations = resultats.size();
        int nbIterations = resultats.get(0).size();
        int[] moyennes = new int[4];

        for (List<Integer> resultat : resultats) {
            for (int i = 0; i < resultat.size(); i++) {
                moyennes[i % 4] += resultat.get(i);
            }
        }

        // Afficher les moyennes
        System.out.println("Moyennes des résultats sur " + nbSimulations + " simulations :");
        System.out.println("Moyenne de S : " + (double) moyennes[0] / (nbSimulations * nbIterations));
        System.out.println("Moyenne de E : " + (double) moyennes[1] / (nbSimulations * nbIterations));
        System.out.println("Moyenne de I : " + (double) moyennes[2] / (nbSimulations * nbIterations));
        System.out.println("Moyenne de R : " + (double) moyennes[3] / (nbSimulations * nbIterations));
    }
}