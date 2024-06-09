package javathsiaproject;/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import neurone.NeuroneSigmoide;
import neurone.iNeurone;

import java.util.Scanner;

import static javathsiaproject.sonToModuleFFT.convertSonModule;

public class main {
    public static void main(String[] args) {

        // Tableau contenant les noms des différents fichiers fournis
        String [] signal = {"Bruit","Carre","Sinusoide","Sinusoide2","Sinusoide3Harmoniques"};

        final float[][] entrees = new float[signal.length][1024];

        for(int i =0;i<signal.length;++i){
            entrees[i] = convertSonModule("app/src/main/resources/audio/"+signal[i]+".wav");
        }

        // Tableaux des sorties Sinusoide et Carré
        final float[] resultatsSinusoide = {0, 0, 1, 1, 1};
        final float[] resultatsCarre = {0, 1, 0, 0, 0};

        // Initialisation des 2 neurones
        // Neurone qui sera entrainé à détecter les signaux sinusoidaux
        final iNeurone neuroneSinusoide = entrainerNeurone(entrees, resultatsSinusoide);
        // Neurone qui sera entrainé à détecter les signaux carrés
        final iNeurone neuroneCarre = entrainerNeurone(entrees, resultatsCarre);


        int menu;
        Scanner sc = new Scanner(System.in);

        do{
            System.out.println("Que voulez-vous faire ? ================================================================================");
            System.out.println("1) testNeurone      (test des neurones pour voir leur taux de réussite)                                =");
            System.out.println("                                                                                                       =");
            System.out.println("2) SoundwAIve       (Détectecteur de type de signal (Carre, Sinusoidale, Bruit))                       =");
            System.out.println("========================================================================================================");
            menu = sc.nextInt();

            if (menu == 1) {

                System.out.println("Quel neurone ? =========================================================================================");
                System.out.println("1) Sinusoide                                                                                           =");
                System.out.println("                                                                                                       =");
                System.out.println("2) Carre                                                                                               =");
                System.out.println("========================================================================================================");
                int menu2 = sc.nextInt();
                if(menu2 == 1) {
                    //lancement de la fonction de test
                    testNeurone(neuroneSinusoide,entrees,resultatsSinusoide);
                } else if (menu2==2) {
                    //lancement de la fonction de test
                    testNeurone(neuroneCarre,entrees,resultatsCarre);
                }
            } else if (menu == 2) {
                System.out.println("Quel son ? ================================================================================");
                System.out.println("0) Sinusoide      (Le neurone à été entraîné avec ce signal)                              =");
                System.out.println("                                                                                          =");
                System.out.println("1) Sinusoide2       (Le neurone à été entraîné avec ce signal)                            =");
                System.out.println("                                                                                          =");
                System.out.println("2) Sinusoide3      (Sinusoide avec plus grande fréquence et plus grande amplitude)        =");
                System.out.println("                                                                                          =");
                System.out.println("3) SinusoideHarmonique       (Le neurone à été entraîné avec ce signal)                   =");
                System.out.println("                                                                                          =");
                System.out.println("4) Carre      (Le neurone à été entraîné avec ce signal)                                  =");
                System.out.println("                                                                                          =");
                System.out.println("5) Carre2      (Le signal carre provenant de la combinaison)                              =");
                System.out.println("                                                                                          =");
                System.out.println("6) Carre3      (Le carre 2 avec une plus petite fréquence)                                =");
                System.out.println("                                                                                          =");
                System.out.println("7) Carre4      (Le carre 2 avec une beacoup plus grande fréquence)                        =");
                System.out.println("                                                                                          =");
                System.out.println("8) Carre5      (le carre 4 très amplifié)                                                 =");
                System.out.println("                                                                                          =");
                System.out.println("9) bruit                                                                                  =");
                System.out.println("===========================================================================================");
                int menu2 = sc.nextInt();
                String[] son = {"Sinusoide","Sinusoide2","Sinusoide3","Sinusoide3Harmoniques","Carre","Carre2","Carre3","Carre4","Carre5","Bruit"};

                System.out.println("vous avez choisis : "+son[menu2]);
                float [] entree = convertSonModule("app/src/main/resources/audio/"+son[menu2]+".wav");

                //lancement des test du son dans les deux neurones entrainés
                float sortieSinusoide = testSon(neuroneSinusoide, entree, resultatsSinusoide);
                float sortieCarre = testSon(neuroneCarre, entree, resultatsCarre);

                if(sortieSinusoide < sortieCarre){
                    System.out.println("Le réseau de neurone suppose que c'est un signal carré");
                }
                else if(sortieSinusoide > sortieCarre){
                    System.out.println("le réseau de neurone suppose que c'est un signal sinusoidale");
                }
                else{
                    System.out.println("Le réseau de neurone ne connait pas ce signal, c'est surement du bruit");
                }

            } else {
                System.out.println("Choix non valide. Entrez une valeur entre 1 et 2.");
            }
        } while(menu != 1 && menu != 2);
    }


    public static iNeurone entrainerNeurone(float[][] entrees, float[] resultatVoulu){//methode pour lancer l'entrainement du neurone
        final iNeurone neurone = new NeuroneSigmoide(1024);
        //on donne 1024 entrées au neurone pour les 1024 valeurs de la FFT
        int nbErreur = neurone.apprentissage(entrees, resultatVoulu);
        //lancement de la methode d'apprentissage
        System.out.println("fin de l'apprentissage avec " + nbErreur + " erreur.");
        return neurone;

    }

    public static void testNeurone(iNeurone neuroneEntraine, float[][] entrees, float[] resultatVoulu){//fonction pour tester le taux d'apprentissage du neurone (toujours 100% avec sigmoid)
        float erreur = 0;
        float test = 0;

        for(int i=0 ; i<resultatVoulu.length ; i++){
            //on boucle sur les valeurs du resultat
            test +=1;
            float [] entree = entrees[i];
            neuroneEntraine.metAJour(entree);
            //on met à jour le neurone
            if (resultatVoulu[i] == 1 ? neuroneEntraine.sortie() < 0.5f : neuroneEntraine.sortie() >= 0.5f) { erreur += 1; }
            //si le resultat obtenue n'est pas le même que le resultat attendu avec une marge de 0.5 alors on incrémente une variable erreur

        }

        System.out.println("========================================");
        //petit calcul de pourcentage d'erreur
        System.out.println("Pourcentage de réussite : " + (1.f - (erreur / test)) * 100 + "%");
        System.out.println("========================================");

    }

    public static float testSon(iNeurone neuroneEntraine, float[] entree, float[] resultatVoulu){
        float resultat =0;
        //on initialise la variable resultat qui sera la somme des sorties du neurone
        for(int i=0 ; i<resultatVoulu.length ; i++){
            neuroneEntraine.metAJour(entree);
            //on met à jour avec le nouveau tableau de valeur
            resultat+=neuroneEntraine.sortie();
        }
        return resultat;
    }
}

