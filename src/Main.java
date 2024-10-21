import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;


public class Main{

    private static ArrayList<Integer> Solucion;
    private static double coste;
    static Random random;
    static LectorParametros lectorParametros;
    static FileWriter Archivo_Greddy;
    static FileWriter Archivo_BL;
    static FileWriter Archivo_Tabu;

    static StringBuilder Log_Tabu;
    static StringBuilder Log_BL;
    static StringBuilder Log_Greddy;

    public static void main(String[] args) throws IOException {

        Solucion = new ArrayList<>();
        lectorParametros = new LectorParametros("parametros.txt");
        Archivo_Tabu = new FileWriter("Log_Tabu.txt");
        Archivo_BL = new FileWriter("Log_BL.txt");
        Archivo_Greddy = new FileWriter("Log_Greddy.txt");

        for (int i = 0; i < lectorParametros.getnFicheros(); i++) {
            LectorDatos lector = new LectorDatos(lectorParametros.getRutas()[i]);
            Log_Tabu = new StringBuilder();
            Log_BL = new StringBuilder();
            Log_Greddy = new StringBuilder();

            for (int j = 0; j < lectorParametros.getnSemillas(); j++) {
                random = new Random(lectorParametros.getSemillas()[j]);
                double Tiempo_Incial = System.nanoTime();
                Log_Tabu.append("Archivo: "+lectorParametros.getRutas()[i]+", semilla: "+lectorParametros.getSemillas()[j]+"\n");
                Log_BL.append("Archivo: "+lectorParametros.getRutas()[i]+", semilla: "+lectorParametros.getSemillas()[j]+"\n");

            //   Greedy_Aleatorio.GreedyAleatorio(Solucion, lector.getMatriz_Distancias().length, lector.getMatriz_Distancias(), Archivo_Greddy, Log_Greddy);
               Busqueda_Tabu.Busqueda_Tabu(Solucion, lector.getMatriz_Distancias().length, lector.getMatriz_Distancias(), lectorParametros.getIteraciones(), lectorParametros.getPorcentaje_Interacciones(), lectorParametros.getEntorno(), lectorParametros.getReduccion(), lectorParametros.getEstancamiento(), lectorParametros.getTenencia(), lectorParametros.getOscilacion(), Archivo_Tabu, Log_Tabu);
            //    Busqueda_Local.Busqueda_Local(Solucion, lector.getMatriz_Distancias().length, lector.getMatriz_Distancias(), lectorParametros.getIteraciones(), lectorParametros.getPorcentaje_Interacciones(), lectorParametros.getEntorno(), lectorParametros.getReduccion(), Archivo_BL, Log_BL);

                coste = Utils.Calculo_Coste(Solucion, lector.getMatriz_Distancias(), lector.getMatriz_Distancias().length);
                System.out.println("Procesando archivo: "+ lectorParametros.getRutas()[i]+", ejecucion numero: " + (j+1) +", semilla: "+lectorParametros.getSemillas()[j]);
                System.out.println("Con un coste total de: " + coste);
                double Tiempo_Final = System.nanoTime();
                double Tiempo_Ejecucion = (Tiempo_Final - Tiempo_Incial) / 1000000000;
                System.out.println("Duracion del tiempo de ejecucion: " + Tiempo_Ejecucion + " segundos. "+"\n");

            }
            Archivo_BL.write(Log_BL.toString());
            Archivo_Greddy.write(Log_Greddy.toString());
            Archivo_Tabu.write(Log_Tabu.toString());
        }
        Archivo_BL.close();
        Archivo_Greddy.close();
        Archivo_Tabu.close();
    }
}