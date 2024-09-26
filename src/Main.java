import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main{

    private static ArrayList<Integer> Solucion;
    private static double coste;
    private static Random random;
    private static LectorParametros lectorParametros;

    public static void main(String[] args) {

        Solucion = new ArrayList<>();
        lectorParametros = new LectorParametros("parametros.txt");

        for (int i = 0; i < lectorParametros.getnFicheros(); i++) {
            LectorDatos lector = new LectorDatos(lectorParametros.getRutas()[i]);

            for (int j = 0; j < lectorParametros.getnSemillas(); j++) {
                random = new Random(lectorParametros.getSemillas()[j]);
                double Tiempo_Incial = System.currentTimeMillis();


                coste = GreedyAleatorio(Solucion, lector.getMatriz_Distancias().length, lector.getMatriz_Distancias());
                System.out.println("Procesando archivo: "+ lectorParametros.getRutas()[i]+", ejecucion numero: "+ (j+1) +", semilla: "+lectorParametros.getSemillas()[j]);
                System.out.println("Con un coste total de: " + coste);
                double Tiempo_Final = System.currentTimeMillis();
                double Tiempo_Ejecucion = (Tiempo_Final - Tiempo_Incial) / 1000;
                System.out.println("Duracion del tiempo de ejecucion: " + Tiempo_Ejecucion + " segundos. "+"\n");
            }
            Solucion.clear();
        }
    }

    /**
     * Greedy Aleatorio
     * @param Solucion Vector con el camino que hace el viajante
     * @param Tam Numero de ciudades
     * @param Matriz_Distancias Matriz donde se guardan las distancias entre ciudades
     * @return
     */
    static double GreedyAleatorio(ArrayList<Integer> Solucion, int Tam, final double[][] Matriz_Distancias) {

        ArrayList<Pair> Posibles_Ciudades = new ArrayList<>();
        ArrayList<Boolean> Ciudades_Visitadas = new ArrayList<>();
        for (int i = 0; i < Tam; i++) {
            Ciudades_Visitadas.add(false);
        }

        for (int i = 0; i < Tam; i++) {
            double Suma_Total = 0.0;
            for (int j = 0; j < Tam; j++) {
                if (i != j) {
                    Suma_Total += Matriz_Distancias[i][j];
                }
            }
            Posibles_Ciudades.add(new Pair(i, Suma_Total));
        }
        //Ordenamos todos los pairs
        Collections.sort(Posibles_Ciudades);

        // Comenzar a construir la solución aleatoriamente desde las primeras K ciudades
        for (int i = 0; i < Tam; i++) {
            //Hacemos el minimo por si hay menos que k y entrar a una posicion correcta
            int K_Correcta = Math.min(lectorParametros.getNumeroK(), Posibles_Ciudades.size());
            int Pos = random.nextInt(K_Correcta);

            Pair Pair_Pos = Posibles_Ciudades.get(Pos);
            Solucion.add(Pair_Pos.ciudad + 1);
            Ciudades_Visitadas.set(Pair_Pos.ciudad, true);

            Posibles_Ciudades.remove(Pos);
        }

        return Calculo_Coste(Solucion, Matriz_Distancias, Tam);
    }

    /**
     * Método para calcular el coste del camino
     * @param Solucion
     * @param Matriz_Distancias
     * @param Tam
     * @return coste el camino
     */
    static double Calculo_Coste(ArrayList<Integer> Solucion, final double[][] Matriz_Distancias, int Tam) {
        double Distancia_Total = 0.0;

        //Unimos las ciudades de la solucion sumandoles la distancia
        for (int i = 0; i < Tam - 1; i++) {
            Distancia_Total += Matriz_Distancias[Solucion.get(i) - 1][Solucion.get(i + 1) -1];
        }

        //Sumamos el coste de ir de la ultima a la primera ciudad para cerrar el ciclo
        Distancia_Total += Matriz_Distancias[Solucion.get(0) - 1][Solucion.get(Tam - 1) - 1];

        return Distancia_Total;
    }

    /**
     * Método para seleccionar la ciudad inicial
     * @param Solucion
     * @param Tam
     * @param Ciudades_Visitadas
     * @return ciudad por la que empieza el algoritmo
     */
    static int ciudadInicial(ArrayList<Integer> Solucion, int Tam, ArrayList<Boolean> Ciudades_Visitadas) {
        int Ciudad_Inicial = random.nextInt(Tam);
        Solucion.add(Ciudad_Inicial + 1);
        Ciudades_Visitadas.set(Ciudad_Inicial, true);
        return Ciudad_Inicial;
    }

    /**
     * Clase Pair con la que guardaremos la suma del camino hasta ella y la propia ciudad
     */
    static class Pair implements Comparable<Pair> {
        private Integer ciudad;
        private Double suma;

        Pair(Integer ciudad, Double suma) {
            this.suma = suma;
            this.ciudad = ciudad;
        }

        @Override
        public int compareTo(Pair p){
            Double dif= this.suma - p.suma;
            if (dif<=0)
                return -1;
            return 1;
        }
    }
}