import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class Main{

    private static ArrayList<Integer> Solucion;
    private static double coste;
    static Random random;
    static LectorParametros lectorParametros;

    public static void main(String[] args){

        Solucion = new ArrayList<>();
        lectorParametros = new LectorParametros("parametros.txt");

        for (int i = 0; i < lectorParametros.getnFicheros(); i++) {
            LectorDatos lector = new LectorDatos(lectorParametros.getRutas()[i]);

            for (int j = 0; j < lectorParametros.getnSemillas(); j++) {
                random = new Random(lectorParametros.getSemillas()[j]);
                double Tiempo_Incial = System.nanoTime();

                Algoritmos.GreedyAleatorio(Solucion, lector.getMatriz_Distancias().length, lector.getMatriz_Distancias());
                Algoritmos.Busqueda_Local(Solucion, lector.getMatriz_Distancias().length, lector.getMatriz_Distancias(), lectorParametros.getIteraciones(), lectorParametros.getAplica(), lectorParametros.getEntorno(), lectorParametros.getDescenso());
                coste = Algoritmos.Calculo_Coste(Solucion, lector.getMatriz_Distancias(), lector.getMatriz_Distancias().length);
                System.out.println("Procesando archivo: "+ lectorParametros.getRutas()[i]+", ejecucion numero: "+ (j+1) +", semilla: "+lectorParametros.getSemillas()[j]);
                System.out.println("Con un coste total de: " + coste);
                double Tiempo_Final = System.nanoTime();
                double Tiempo_Ejecucion = (Tiempo_Final - Tiempo_Incial) / 1000000000;
                System.out.println("Duracion del tiempo de ejecucion: " + Tiempo_Ejecucion + " segundos. "+"\n");
            }
        }
    }

}