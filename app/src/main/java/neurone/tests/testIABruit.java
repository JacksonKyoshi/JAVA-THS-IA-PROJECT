package neurone.tests;
import neurone.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Fichier de test de la partie IA avec entrées bruitées.
 * L'utilisateur doit choisir entre 3 options :
 *      1) Effectuer un simple test soit de AND soit de OR avec la fonction d'activation de son choix
 *      2) Effectuer plusieurs tests (il choisit combien) soit de AND soit de OR avec la fonction Heaviside
 *      (permet de calculer la moyenne et l'écart-type des synapses et du biais afin d'analyser les performances de Heaviside)
 *      3) IDEM, mais avec Sigmoide
 */

public class testIABruit {

    public static void main(String[] args) {

        int menu;
        Scanner sc = new Scanner(System.in);

        do{
            System.out.println("Que voulez-vous faire ? ==============================================================================================================");
            System.out.println("1) testIAbruit           (test simple de AND ou OR avec n'importe quelle fonction d'activation MAIS avec des entrées bruitées)       =");
            System.out.println("                                                                                                                                     =");
            System.out.println("2) testHeavisideBruit    (plusieurs tests de AND ou OR avec Heaviside et calculs de moyenne/écart-type, avec entrées bruitées)       =");
            System.out.println("                                                                                                                                     =");
            System.out.println("3) testSigmoideBruit     (plusieurs tests de AND ou OR avec Sigmoide et calculs de moyenne/écart-type, avec entrées bruitées)        =");
            System.out.println("======================================================================================================================================");
            menu = sc.nextInt();

            if (menu == 1) {
                testIAbruit();
            } else if (menu == 2) {
                testHeavisideBruit();
            } else if (menu == 3) {
                testSigmoideBruit();
            } else {
                System.out.println("Choix non valide. Entrez une valeur entre 1 et 3.");
            }
        } while(menu != 1 && menu != 2 && menu != 3);

    }

    /**
     * Fonction principale de la partie IA Bruit qui permet de lancer l'apprentissage d'un neurone pour faire une porte (OR ou AND)
     * Avec comme fonction d'activation soit Heaviside, soit ReLU, soit Sigmoide
     * L'utilisateur choisit la porte et la fonction d'activation
     * La différence avec testIA est que ici, on rajoute du bruit qui varie entre -0.1 et 0.1 sur les entrées
     */
    public static void testIAbruit() {

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

    /**
     * Fonction de test de la partie IA Bruit qui permet de lancer l'apprentissage d'un neurone pour faire une porte (OR ou AND) en boucle
     * Avec comme fonction d'activation Heaviside
     * Afin d'analyser les poids et le biais du perceptron (calculs de moyenne et d'écart-type)
     * Les entrées sont toujours bruitées
     */
    public static void testHeavisideBruit() {

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

            System.out.println("Test " + nbTests + " / " + menu2 + " : ");
            Neurone vueNeurone = null;
            for (float bruit = -0.1f; bruit <= 0.1f; bruit += 0.01) {
                // On initialise notre neurone avec la fonction d'activation Heaviside
                // A chaque itération on créé un nouveau neurone pour que ce dernier puisse re apprendre de 0
                iNeurone n = new NeuroneHeaviside(entrees[0].length);

                final float[][] entreesbruitee = {{0 + bruit, 0 + bruit}, {0 + bruit, 1 + bruit}, {1 + bruit, 0 + bruit}, {1 + bruit, 1 + bruit}};

                // Apprentissage de la fonction choisie par l'utilisateur sur le neurone
                System.out.println("Nombre de tours : " + n.apprentissage(entrees, resultats));

                vueNeurone = (Neurone) n;

                for (int i = 0; i < entrees.length; ++i) {
                    // Pour une entrée donné
                    final float[] entree = entrees[i];
                    // On met à jour la sortie du neurone
                    n.metAJour(entreesbruitee[i]);
                }
            }
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
            nbTests += 1;
        }
    }

    /**
     * Fonction de test de la partie IA qui permet de lancer l'apprentissage d'un neurone pour faire une porte (OR ou AND) en boucle
     * Avec comme fonction d'activation Sigmoide
     * Afin d'analyser les poids et le biais du perceptron (calculs de moyenne et d'écart-type)
     * Les entrées sont toujours bruitées
     */
    public static void testSigmoideBruit() {

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

            System.out.println("Test " + nbTests + " / " + menu2 + " : ");
            Neurone vueNeurone = null;
            for (float bruit = -0.1f; bruit <= 0.1f; bruit += 0.01) {
                // On initialise notre neurone avec la fonction d'activation Heaviside
                // A chaque itération on créé un nouveau neurone pour que ce dernier puisse re apprendre de 0
                iNeurone n = new NeuroneSigmoide(entrees[0].length);

                final float[][] entreesbruitee = {{0 + bruit, 0 + bruit}, {0 + bruit, 1 + bruit}, {1 + bruit, 0 + bruit}, {1 + bruit, 1 + bruit}};

                // Apprentissage de la fonction choisie par l'utilisateur sur le neurone
                System.out.println("Nombre de tours : " + n.apprentissage(entrees, resultats));

                vueNeurone = (Neurone) n;

                for (int i = 0; i < entrees.length; ++i) {
                    // Pour une entrée donné
                    final float[] entree = entrees[i];
                    // On met à jour la sortie du neurone
                    n.metAJour(entreesbruitee[i]);
                }
            }
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
            nbTests += 1;
        }
    }

}
