import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main{

    private static ArrayList<Integer> Solucion;
    private static double coste;
    private static Random random;

    public static void main(String[] args) {

        Solucion = new ArrayList<>();
        random = new Random(53919521);
        double Tiempo_Incial = System.currentTimeMillis();

        //LectorDatos lector_ch130 = new LectorDatos("ch130.tsp");
        //LectorDatos lector_pr144 = new LectorDatos("pr144.tsp");
        //LectorDatos lector_a280 = new LectorDatos("a280.tsp");
        LectorDatos lector_u1060 = new LectorDatos("u1060.tsp");
        //LectorDatos lector_d18512 = new LectorDatos("d18512.tsp");

        //region Mostrar Soluciones
        System.out.println("Una posible solucion para el archivo a280.tsp es: ");
        coste = GreedyAleatorio(Solucion,lector_u1060.getMatriz_Distancias().length,lector_u1060.getMatriz_Distancias(),5);
       // System.out.println("Solucion: " + Solucion.toString());
        System.out.println("Con un coste total de: " + coste+"\n");

    //endregion

        double Tiempo_Final = System.currentTimeMillis();
        double Tiempo_Ejecucion = (Tiempo_Final - Tiempo_Incial) / 1000;
        System.out.println("Duracion del tiempo de ejecucion: " + Tiempo_Ejecucion + " segundos. ");
    }

    static double GreedyAleatorio(ArrayList<Integer> Solucion, int Tam, final double[][] Matriz_Distancias, int k) {
        Solucion.clear();

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
        //Ordenamos todos
        Collections.sort(Posibles_Ciudades);

        // Comenzar a construir la solución aleatoriamente desde las primeras K ciudades
        for (int i = 0; i < Tam; i++) {
            //Hacemos el minimo por si hay menos que k y entrar a una posicion correcta
            int K_Correcta = Math.min(k, Posibles_Ciudades.size());
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
        int ciudadInicial = random.nextInt(Tam);
        Solucion.add(ciudadInicial + 1);
        Ciudades_Visitadas.set(ciudadInicial, true);
        return ciudadInicial;
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