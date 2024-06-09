package neurone.tests;
import neurone.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Fichier de test de la partie IA.
 * L'utilisateur doit choisir entre 3 options :
 *      1) Effectuer un simple test soit de AND soit de OR avec la fonction d'activation de son choix
 *      2) Effectuer plusieurs tests (il choisit combien) soit de AND soit de OR avec la fonction Heaviside
 *      (permet de calculer la moyenne et l'écart-type des synapses et du biais afin d'analyser les performances de Heaviside)
 *      3) IDEM, mais avec Sigmoide
 */

public class testIA {

    public static void main(String[] args) {

        int menu;
        Scanner sc = new Scanner(System.in);

        do{
            System.out.println("Que voulez-vous faire ? ================================================================================");
            System.out.println("1) testIA           (test simple de AND ou OR avec n'importe quelle fonction d'activation)             =");
            System.out.println("                                                                                                       =");
            System.out.println("2) testHeaviside    (plusieurs tests de AND ou OR avec Heaviside et calculs de moyenne/écart-type)     =");
            System.out.println("                                                                                                       =");
            System.out.println("3) testSigmoide     (plusieurs tests de AND ou OR avec Sigmoide et calculs de moyenne/écart-type)      =");
            System.out.println("========================================================================================================");
            menu = sc.nextInt();

            if (menu == 1) {
                testIA();
            } else if (menu == 2) {
                testHeaviside();
            } else if (menu == 3) {
                testSigmoide();
            } else {
                System.out.println("Choix non valide. Entrez une valeur entre 1 et 3.");
            }
        } while(menu != 1 && menu != 2 && menu != 3);

    }

    /**
     * Fonction principale de la partie IA qui permet de lancer l'apprentissage d'un neurone pour faire une porte (OR ou AND)
     * Avec comme fonction d'activation soit Heaviside, soit ReLU, soit Sigmoide
     * L'utilisateur choisit la porte et la fonction d'activation
     */
    public static void testIA() {

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

    /**
     * Fonction de test de la partie IA qui permet de lancer l'apprentissage d'un neurone pour faire une porte (OR ou AND) en boucle
     * Avec comme fonction d'activation Heaviside
     * Afin d'analyser les poids et le biais du perceptron (calculs de moyenne et d'écart-type)
     */
    public static void testHeaviside() {

        int menu1, menu2; // Variables qui contiendront le choix de l'utilisateur
        int nbTests = 0;

        List<Float> poids1List = new ArrayList<>();
        List<Float> poids2List = new ArrayList<>();
        List<Float> biaisList = new ArrayList<>();

        Scanner sc = new Scanner(System.in); // Permet de récupérer la valeur saisie sur le clavier d'un utilisateur

        // Tableau des entrées des fonctions ET et OU (0 = faux, 1 = vrai)
        final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};

        // Tableau des sorties initalisé à 0 pour le moment
        float[] resultats = new float[0];

        do {
            System.out.println("Quelle porte voulez-vous tester ? AND (1) ou OR (2)");
            menu1 = sc.nextInt();

            if (menu1 == 1) {
                // Tableau des sorties de la fonction ET
                resultats = new float[]{0, 0, 0, 1};
            } else if (menu1 == 2) {
                // Tableau des sorties de la fonction OU
                resultats = new float[]{0, 1, 1, 1};
            } else {
                System.out.println("Choix non valide. Entrez une valeur entre 1 et 2.");
            }
        } while (menu1 != 1 && menu1 != 2);

        System.out.println("Combien de tests voulez-vous faire ?");
        menu2 = sc.nextInt();

        while (nbTests <= menu2) {

            // On initialise notre neurone avec la fonction d'activation Heaviside
            // A chaque itération on créé un nouveau neurone pour que ce dernier puisse re apprendre de 0
            iNeurone n = new NeuroneHeaviside(entrees[0].length);

            // Apprentissage de la fonction choisie par l'utilisateur sur le neurone
            System.out.println("Test " + nbTests + " / " + menu2 + " : ");
            System.out.println("Nombre de tours : " + n.apprentissage(entrees, resultats));

            final Neurone vueNeurone = (Neurone) n;

            float poids1 = vueNeurone.synapses()[0];
            float poids2 = vueNeurone.synapses()[1];
            float biais = vueNeurone.biais();

            poids1List.add(poids1);
            poids2List.add(poids2);
            biaisList.add(biais);

            float sommePoids1 = 0;
            float sommePoids2 = 0;
            float sommeBiais = 0;
            for (float p : poids1List) sommePoids1 += p;
            for (float p : poids2List) sommePoids2 += p;
            for (float b : biaisList) sommeBiais += b;

            float moyPoids1 = sommePoids1 / (nbTests + 1);
            float moyPoids2 = sommePoids2 / (nbTests + 1);
            float moyBiais = sommeBiais / (nbTests + 1);

            // Calcul des écarts-types (et des variances)
            float sommeCarrePoids1 = 0;
            float sommeCarrePoids2 = 0;
            float sommeCarreBiais = 0;

            for (float p : poids1List) sommeCarrePoids1 += Math.pow(p - moyPoids1, 2);
            for (float p : poids2List) sommeCarrePoids2 += Math.pow(p - moyPoids2, 2);
            for (float b : biaisList) sommeCarreBiais += Math.pow(b - moyBiais, 2);

            float variancePoids1 = sommeCarrePoids1 / (nbTests + 1);
            float variancePoids2 = sommeCarrePoids2 / (nbTests + 1);
            float varianceBiais = sommeCarreBiais / (nbTests + 1);

            float ecartTypePoids1 = (float) Math.sqrt(variancePoids1);
            float ecartTypePoids2 = (float) Math.sqrt(variancePoids2);
            float ecartTypeBiais = (float) Math.sqrt(varianceBiais);

            System.out.println("Moyennes : ----------");
            System.out.println("Poids : " + moyPoids1 + " / " + moyPoids2);
            System.out.println("Biais : " + moyBiais);
            System.out.println("Ecart-Type : ----------");
            System.out.println("Poids : " + ecartTypePoids1 + " / " + ecartTypePoids2);
            System.out.println("Biais : " + ecartTypeBiais);
            System.out.println("==================================================");

            for (int i = 0; i < entrees.length; ++i) {
                // Pour une entrée donné
                final float[] entree = entrees[i];
                // On met à jour la sortie du neurone
                n.metAJour(entree);
            }

            nbTests += 1;

        }
    }

    /**
     * Fonction de test de la partie IA qui permet de lancer l'apprentissage d'un neurone pour faire une porte (OR ou AND) en boucle
     * Avec comme fonction d'activation Sigmoide
     * Afin d'analyser les poids et le biais du perceptron (calculs de moyenne et d'écart-type)
     */
    public static void testSigmoide() {

        int menu1, menu2; // Variables qui contiendront le choix de l'utilisateur
        int nbTests = 0;

        List<Float> poids1List = new ArrayList<>();
        List<Float> poids2List = new ArrayList<>();
        List<Float> biaisList = new ArrayList<>();

        Scanner sc = new Scanner(System.in); // Permet de récupérer la valeur saisie sur le clavier d'un utilisateur

        // Tableau des entrées des fonctions ET et OU (0 = faux, 1 = vrai)
        final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};

        // Tableau des sorties initalisé à 0 pour le moment
        float[] resultats = new float[0];

        do {
            System.out.println("Quelle porte voulez-vous tester ? AND (1) ou OR (2)");
            menu1 = sc.nextInt();

            if (menu1 == 1) {
                // Tableau des sorties de la fonction ET
                resultats = new float[]{0, 0, 0, 1};
            } else if (menu1 == 2) {
                // Tableau des sorties de la fonction OU
                resultats = new float[]{0, 1, 1, 1};
            } else {
                System.out.println("Choix non valide. Entrez une valeur entre 1 et 2.");
            }
        } while (menu1 != 1 && menu1 != 2);

        System.out.println("Combien de tests voulez-vous faire ?");
        menu2 = sc.nextInt();

        while (nbTests <= menu2) {

            // On initialise notre neurone avec la fonction d'activation Heaviside
            // A chaque itération on créé un nouveau neurone pour que ce dernier puisse re apprendre de 0
            iNeurone n = new NeuroneSigmoide(entrees[0].length);

            // Apprentissage de la fonction choisie par l'utilisateur sur le neurone
            System.out.println("Test " + nbTests + " / " + menu2 + " : ");
            System.out.println("Nombre de tours : " + n.apprentissage(entrees, resultats));

            final Neurone vueNeurone = (Neurone) n;

            float poids1 = vueNeurone.synapses()[0];
            float poids2 = vueNeurone.synapses()[1];
            float biais = vueNeurone.biais();

            poids1List.add(poids1);
            poids2List.add(poids2);
            biaisList.add(biais);

            float sommePoids1 = 0;
            float sommePoids2 = 0;
            float sommeBiais = 0;
            for (float p : poids1List) sommePoids1 += p;
            for (float p : poids2List) sommePoids2 += p;
            for (float b : biaisList) sommeBiais += b;

            float moyPoids1 = sommePoids1 / (nbTests + 1);
            float moyPoids2 = sommePoids2 / (nbTests + 1);
            float moyBiais = sommeBiais / (nbTests + 1);

            // Calcul des écarts-types (et des variances)
            float sommeCarrePoids1 = 0;
            float sommeCarrePoids2 = 0;
            float sommeCarreBiais = 0;

            for (float p : poids1List) sommeCarrePoids1 += Math.pow(p - moyPoids1, 2);
            for (float p : poids2List) sommeCarrePoids2 += Math.pow(p - moyPoids2, 2);
            for (float b : biaisList) sommeCarreBiais += Math.pow(b - moyBiais, 2);

            float variancePoids1 = sommeCarrePoids1 / (nbTests + 1);
            float variancePoids2 = sommeCarrePoids2 / (nbTests + 1);
            float varianceBiais = sommeCarreBiais / (nbTests + 1);

            float ecartTypePoids1 = (float) Math.sqrt(variancePoids1);
            float ecartTypePoids2 = (float) Math.sqrt(variancePoids2);
            float ecartTypeBiais = (float) Math.sqrt(varianceBiais);

            System.out.println("Moyennes : ----------");
            System.out.println("Poids : " + moyPoids1 + " / " + moyPoids2);
            System.out.println("Biais : " + moyBiais);
            System.out.println("Ecart-Type : ----------");
            System.out.println("Poids : " + ecartTypePoids1 + " / " + ecartTypePoids2);
            System.out.println("Biais : " + ecartTypeBiais);
            System.out.println("==================================================");

            for (int i = 0; i < entrees.length; ++i) {
                // Pour une entrée donné
                final float[] entree = entrees[i];
                // On met à jour la sortie du neurone
                n.metAJour(entree);
            }

            nbTests += 1;

        }
    }
}
