package javathsiaproject;
import neurone.*;

import java.util.Scanner;

public class testIABruit {

    /**
     * Fonction secondaire de la partie IA qui permet de lancer l'pprentissage d'un neurone pour faire une porte (OR ou AND)
     * L'utilisateur choisit la porte et la fonction d'activation
     * La différence avec testIA est que ici, on rajoute du bruit qui varie entre -0.1 et 0.1 sur les entrées
     */
    public static void main(String[] args) {

        int menu1, menu2; // Variables qui contiendront le choix de l'utilisateur
        Scanner sc = new Scanner(System.in); // Permet de récupérer la valeur saisie sur le clavier d'un utilisateur
        iNeurone n = null; // Initialisation du neurone à null pour le moment
        float nbTests = 0;
        float compteurEchecs = 0;
        float nbErreurs = 0;
        final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};

        // On met les menus ici car on ne veut pas devoir rechoisir la porte et la fonction d'activation
        // à chaque itération du bruit
        System.out.println("Quelle fonction d'activation voulez-vous utiliser ?");
        System.out.println("1) Heaviside");
        System.out.println("2) ReLU");
        System.out.println("3) Sigmoide");

        // On récupère le choix de l'utilisateur (entre 1 et 3)
        menu1 = sc.nextInt();

        System.out.println("Quelle porte voulez-vous tester ? AND (1) ou OR (2)");
        menu2 = sc.nextInt();

        // Tableau des entrées des fonctions ET et OU (0 = faux, 1 = vrai)
        for(float b = -0.1f ; b <= 0.1f ; b+=0.01) {
            final float[][] entreesbruitee = {{0+b, 0+b}, {0+b, 1+b}, {1+b, 0+b}, {1+b, 1+b}};

            // Tableau des sorties initalisé à 0 pour le moment
            float[] resultats = new float[0];

            do {
                if (menu1 == 1) {
                    n = new NeuroneHeaviside(entrees[0].length); // On initialise notre neurone avec la fonction d'activation Heaviside
                } else if (menu1 == 2) {
                    n = new NeuroneReLU(entrees[0].length); // On initialise notre neurone avec la fonction d'activation ReLU
                } else if (menu1 == 3) {
                    n = new NeuroneSigmoide(entrees[0].length); // On initialise notre neurone avec la fonction d'activation Sigmoide
                } else {
                    System.out.println("Choix non valide. Entrez une valeur entre 1 et 3.");
                }
            } while (menu1 != 1 && menu1 != 2 && menu1 != 3);


            do {
                if (menu2 == 1) {
                    // Tableau des sorties de la fonction ET
                    resultats = new float[]{0, 0, 0, 1};
                } else if (menu2 == 2) {
                    // Tableau des sorties de la fonction OU
                    resultats = new float[]{0, 1, 1, 1};
                } else {
                    System.out.println("Choix non valide. Entrez une valeur entre 1 et 2.");
                }
            } while (menu2 != 1 && menu2 != 2);

            // Conversion dynamique d'une référence iNeurone vers une référence neurone,
            // pour pouvoir accéder aux synapses et au biais.
            final Neurone oldNeurone = (Neurone)n;

            // Apprentissage de la fonction choisie par l'utilisateur sur le neurone
            compteurEchecs = n.apprentissage(entrees, resultats);


            for (int i = 0; i < entrees.length; ++i)
            {
                // Pour une entrée donné
                final float[] entree = entrees[i];
                // On met à jour la sortie du neurone
                n.metAJour(entreesbruitee[i]);

                // Comparaison de la sortie obtenue avec la sortie voulue afin de détecter et de compter les potentielles erreurs
                // Le compteur va servir pour calculer le pourcentage de réussite
                if(menu1 == 1){
                    if(n.sortie() != resultats[i]){ nbErreurs += 1; }
                }
                if(menu1 == 2 || menu1 == 3){
                    if(n.sortie() <= resultats[i]*0.5f){ nbErreurs += 1; }
                }
            }
            nbTests += 1;
        }
        System.out.println("========================================");
        System.out.println("Pourcentage de réussite : "+(1.f-(nbErreurs/nbTests))*100+"%");
        System.out.println("========================================");
    }

}
