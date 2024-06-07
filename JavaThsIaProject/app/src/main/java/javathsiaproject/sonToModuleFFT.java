package javathsiaproject;

import FFT.Complexe;
import FFT.ComplexeCartesien;
import Son.Son;

import static FFT.FFTCplx.appliqueSur;

public class sonToModuleFFT {


    public static double[] convertSonModule(String cheminFichier){//methode pour récupérer le tableau de module en fonction de l'audio
        //Création d'une variable "son"
        Son son = new Son(cheminFichier);
        //Conversion de l'échantillonage en complex cartésien (valeur imaginaire = 0)
        ComplexeCartesien[] tablo = converteurComplex.convertirEnComplexe(son.bloc_deTaille(1,512));
        //application de la FFT sur les valeurs complex
        Complexe[] resultat = appliqueSur(tablo);
        //pour chacunes des valeurs complex de la FFT, on calcule le module
        double[] module = new double[resultat.length];
        for (int i = 0; i < resultat.length; ++i){
            //System.out.println("resultat : "+resultat[i].reel()+"+"+resultat[i].imag());
            module[i] = resultat[i].mod();
            System.out.println("mod : "+module[i]);
        }
        return module;
    }
}
