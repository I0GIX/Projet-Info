package info;

import org.apache.commons.math3.random.MersenneTwister;

class Individu {
    private char statut;
    private int dE;
    private int dI;
    private int dR;
    private int positionX;
    private int positionY;

    /* -------------------------------------------------------------------- */
    /* Constructeur */
    /* -------------------------------------------------------------------- */

    public Individu(int dE, int dI, int dR) {
        this.statut = 'S';
        this.dE = dE;
        this.dI = dI;
        this.dR = dR;
    }

    /* -------------------------------------------------------------------- */
    /* Accesseurs */
    /* -------------------------------------------------------------------- */

    public char getStatut() {
        return statut;
    }

    public void setStatut(char statut) {
        this.statut = statut;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    /* -------------------------------------------------------------------- */
    /*
     * miseAJourStatut Mets a jour le statut d'un individu en fonction de ses durées
     * de/dr/di
     */

    /*
     * En entrée: random Une instance de MersenneTwister
     */

    /* En sortie: Rien */
    /* -------------------------------------------------------------------- */

    public void miseAJourStatut(MersenneTwister random) {
        switch (statut) {
            case 'E':
                // Vérifier si la durée d'exposition est dépassée
                if (dE <= 0) {
                    statut = 'I'; // Passer à l'état infecté
                    dI = negExp(7, random); // Réinitialiser la durée d'infection
                } else {
                    dE--; // Décrémenter la durée d'exposition
                }
                break;
            case 'I':
                // Vérifier si la durée d'infection est dépassée
                if (dI <= 0) {
                    statut = 'R'; // Passer à l'état récupéré
                    dR = negExp(365, random); // Réinitialiser la durée de récupération
                } else {
                    dI--; // Décrémenter la durée d'infection
                }
                break;
            case 'R':
                // Vérifier si la durée de récupération est dépassée
                if (dR <= 0) {
                    statut = 'S'; // Passer à l'état susceptible
                } else {
                    dR--; // Décrémenter la durée de récupération
                }
                break;
            default:
                // Pour l'état S, aucune action n'est nécessaire
                break;
        }
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

    private int negExp(double inMean, MersenneTwister random) {
        return (int) (-inMean * Math.log(1 - random.nextDouble()));
    }

}
