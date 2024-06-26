package neurone.tests;

import neurone.Neurone;
import neurone.NeuroneHeaviside;
import neurone.iNeurone;

/**
 * Ancien fichier de test de la partie IA.
 * Permet de tester l'apprentissage d'un neurone sur la porte OR
 * La fonction d'activation doit être changées à la main, c'est pourquoi les fichiers testIA et testIABruit ont été créés.
 * Ils reprennent les mêmes fonctionnalités mais facilitent les tests.
 */
public class testNeuroneOR
{
    public static void main(String[] args)
    {
        // Tableau des entrées de la fonction OU (0 = faux, 1 = vrai)
        final float[][] entrees = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};

        // Tableau des sorties de la fonction OU
        final float[] resultats = {0, 1, 1, 1};

        // On crée un neurone taillé pour apprendre la fonction OU
        final iNeurone n = new NeuroneHeaviside(entrees[0].length);
        //final iNeurone n = new NeuroneSigmoide(entrees[0].length);
        //final iNeurone n = new NeuroneReLU(entrees[0].length);

        System.out.println("Apprentissage…");
        System.out.println("Avant apprentissage : ");
        // Conversion dynamique d'une référence iNeurone vers une référence neurone.
        // Le but est de pouvoir accéder aux poids et au biais AVANT apprentissage,
        // afin de pouvoir faire des comparaisons avec les poids et le biais APRES apprentissage.
        final Neurone oldNeurone = (Neurone)n;
        System.out.print("Anciennes synapses : ");
        for (final float f : oldNeurone.synapses())
            System.out.print(f+" ");
        System.out.print("\nAncien biais : ");
        System.out.println(oldNeurone.biais());
        System.out.println("========================================");
        // On lance l'apprentissage de la fonction ET sur ce neurone
        System.out.println("Nombre de tours : "+n.apprentissage(entrees, resultats));

        // On affiche les valeurs des synapses et du biais

        // Conversion dynamique d'une référence iNeurone vers une référence neurone.
        // Sans cette conversion on ne peut pas accéder à synapses() et biais()
        // à partir de la référence de type iNeurone
        // Cette conversion peut échouer si l'objet derrière la référence iNeurone
        // n'est pas de type neurone, ce qui n'est cependant pas le cas ici
        final Neurone vueNeurone = (Neurone)n;
        System.out.print("Nouvelles synapses : ");
        for (final float f : vueNeurone.synapses())
            System.out.print(f+" ");
        System.out.print("\nNouveau biais : ");
        System.out.println(vueNeurone.biais());

        // On affiche chaque cas appris
        System.out.println("========================================");
        System.out.println("Cas appris : ");
        for (int i = 0; i < entrees.length; ++i)
        {
            // Pour une entrée donnée
            final float[] entree = entrees[i];
            // On met à jour la sortie du neurone
            n.metAJour(entree);
            // On affiche cette sortie
            System.out.println("Entree "+i+" : "+n.sortie());
        }
    }
}
