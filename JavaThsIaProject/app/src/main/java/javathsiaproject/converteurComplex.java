package javathsiaproject;
import FFT.ComplexeCartesien;
import FFT.Complexe;

public class converteurComplex {
    public static ComplexeCartesien[] convertirEnComplexe(float[] tableauFlottants) {
        ComplexeCartesien[] tableauComplexes = new ComplexeCartesien[tableauFlottants.length];

        // Remplir le tableau de Complexe avec des nombres complexes
        for (int i = 0; i < tableauFlottants.length; i++) {
            tableauComplexes[i] = new ComplexeCartesien(tableauFlottants[i],0); // Partie imaginaire mise à 0
        }
        return tableauComplexes;
    }
}
