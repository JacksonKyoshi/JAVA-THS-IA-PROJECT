package javathsiaproject;

import FFT.Complexe;
import FFT.ComplexeCartesien;
import Son.Son;

import static FFT.FFTCplx.appliqueSur;

public class sonToModuleFFT {


    public static float[] convertSonModule(String cheminFichier){//methode pour récupérer le tableau de module en fonction de l'audio
        //Création d'une variable "son"
        Son son = new Son(cheminFichier);
        //Conversion de l'échantillonage en complex cartésien (valeur imaginaire = 0)
        float[] valeurs = son.bloc_deTaille(1,1024);
        float max =0;
        //on normalise les valeurs pour un resultat plus simple
        for(int i = 0; i<valeurs.length; ++i){
            if(max<valeurs[i]){
                max = valeurs[i];
            }
        }
        for(int i = 0;i<valeurs.length;++i){
            valeurs[i]=valeurs[i]/max;
        }
        ComplexeCartesien[] tablo = converteurComplex.convertirEnComplexe(valeurs);
        //application de la FFT sur les valeurs complex
        Complexe[] resultat = appliqueSur(tablo);
        //pour chacunes des valeurs complex de la FFT, on calcule le module
        float[] module = new float[resultat.length];
        for (int i = 0; i < resultat.length; ++i){
            //System.out.println("resultat : "+resultat[i].reel()+"+"+resultat[i].imag());
            module[i] = (float) Math.sqrt(Math.pow(resultat[i].reel(),2)+Math.pow(resultat[i].imag(),2));
        }
        return module;
    }
}
