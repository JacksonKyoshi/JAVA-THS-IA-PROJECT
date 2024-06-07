package javathsiaproject;
import neurone.*;

import java.util.Scanner;

public class testIA {

    /**
     * Fonction principale de la partie IA qui permet de lancer l'pprentissage d'un neurone pour faire une porte (OR ou AND)
     * Avec comme fonction d'activation soit Heaviside, soit ReLU, soit Sigmoide
     * L'utilisateur choisit la porte et la fonction d'activation
     */
    public static void main(String[] args) {

        int menu1, menu2; // Variables qui contiendront le choix de l'utilisateur
        Scanner sc = new Scanner(System.in); // Permet de récupérer la valeur saisie sur le clavier d'un utilisateur
        iNeurone n = null; // Initialisation du neurone à null pour le moment

        // Tableau des entrées des fonctions ET et OU (0 = faux, 1 = vrai)
        final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};

        // Tableau des sorties initalisé à 0 pour le moment
        float[] resultats = new float[0];

        do {
            System.out.println("Quelle fonction d'activation voulez-vous utiliser ?");
            System.out.println("1) Heaviside");
            System.out.println("2) ReLU");
            System.out.println("3) Sigmoide");

            // On récupère le choix de l'utilisateur (entre 1 et 3)
            menu1 = sc.nextInt();

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


        do{
            System.out.println("Quelle porte voulez-vous tester ? AND (1) ou OR (2)");
            menu2 = sc.nextInt();

            if (menu2 == 1) {
                // Tableau des sorties de la fonction ET
                resultats = new float[]{0, 0, 0, 1};
            } else if (menu2 == 2) {
                // Tableau des sorties de la fonction OU
                resultats = new float[]{0, 1, 1, 1};
            } else {
                System.out.println("Choix non valide. Entrez une valeur entre 1 et 2.");
            }
        } while(menu2 != 1 && menu2 != 2);

        // Conversion dynamique d'une référence iNeurone vers une référence neurone,
        // pour pouvoir accéder aux synapses et au biais.
        final Neurone oldNeurone = (Neurone)n;

        // Affichage des synapses et du biais de la neurone AVANT apprentissage
        System.out.print("Anciennes synapses : ");
        for (final float f : oldNeurone.synapses())
            System.out.print(f+" ");
        System.out.print("\nAncien biais : ");
        System.out.println(oldNeurone.biais());
        System.out.println("========================================");

        // Apprentissage de la fonction choisie par l'utilisateur sur le neurone
        System.out.println("Nombre de tours : "+n.apprentissage(entrees, resultats));

        // Conversion dynamique d'une référence iNeurone vers une référence neurone,
        // pour pouvoir accéder aux synapses et au biais.
        final Neurone vueNeurone = (Neurone)n;

        // Affichage des synapses et du biais de la neurone APRES apprentissage
        System.out.print("Nouvelles synapses : ");
        for (final float f : vueNeurone.synapses())
            System.out.print(f+" ");
        System.out.print("\nNouveau biais : ");
        System.out.println(vueNeurone.biais());

        System.out.println("========================================");
        System.out.println("Cas appris : ");
        for (int i = 0; i < entrees.length; ++i)
        {
            // Pour une entrée donné
            final float[] entree = entrees[i];
            // On met à jour la sortie du neurone
            n.metAJour(entree);
            // On affiche cette sortie
            System.out.println("Entree "+i+" : "+n.sortie());
        }

    }

}
