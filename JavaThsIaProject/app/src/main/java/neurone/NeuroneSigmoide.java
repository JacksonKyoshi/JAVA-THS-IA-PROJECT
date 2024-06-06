package neurone;
public class NeuroneSigmoide extends Neurone
{
    // Fonction d'activation d'un neurone (peut facilement être modifiée par héritage)
    protected float activation(final float valeur) {return (float) (1/(1+Math.exp(-valeur)));}

    // Constructeur
    public NeuroneSigmoide(final int nbEntrees) {super(nbEntrees);}
}
