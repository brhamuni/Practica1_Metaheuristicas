import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main{

    private static ArrayList<Integer> Solucion;
    private static double coste;

    public static void main(String[] args) {

        Solucion = new ArrayList<>();
        double Tiempo_Incial = System.currentTimeMillis();

        LectorDatos lector_ch130 = new LectorDatos("ch130.tsp");
        LectorDatos lector_pr144 = new LectorDatos("pr144.tsp");
        LectorDatos lector_a280 = new LectorDatos("a280.tsp");
        LectorDatos lector_u1060 = new LectorDatos("u1060.tsp");
        LectorDatos lector_d18512 = new LectorDatos("d18512.tsp");

        System.out.println("Una posible solucion para el archivo ch130.tsp es: ");
        coste = GreedyAleatorio(Solucion,lector_ch130.getMatriz_Distancias().length,lector_ch130.getMatriz_Distancias(),5);
        //System.out.println("Solucion: " + Solucion.toString());
        System.out.println("Con un coste total de: " + coste+"\n\n\n");

        System.out.println("Una posible solucion para el archivo pr144.tsp es: ");
        coste= GreedyAleatorio(Solucion,lector_pr144.getMatriz_Distancias().length,lector_pr144.getMatriz_Distancias(),5);
        //System.out.println("Solucion: " + Solucion.toString());
        System.out.println("Con un coste total de: " + coste+"\n\n\n");

        System.out.println("Una posible solucion para el archivo a280.tsp es: ");
        coste = GreedyAleatorio(Solucion,lector_a280.getMatriz_Distancias().length,lector_a280.getMatriz_Distancias(),5);
       // System.out.println("Solucion: " + Solucion.toString());
        System.out.println("Con un coste total de: " + coste+"\n\n\n");

        System.out.println("Una posible solucion para el archivo u1060.tsp es: ");
        coste = GreedyAleatorio(Solucion,lector_u1060.getMatriz_Distancias().length,lector_u1060.getMatriz_Distancias(),5);
        //System.out.println("Solucion: " + Solucion.toString());
        System.out.println("Con un coste total de: " + coste+"\n\n\n");

        System.out.println("Una posible solucion para el archivo d18512.tsp es: ");
        coste = GreedyAleatorio(Solucion,lector_d18512.getMatriz_Distancias().length,lector_d18512.getMatriz_Distancias(),5);
        //System.out.println("Solucion: " + Solucion.toString());
        System.out.println("Con un coste total de: " + coste+"\n");

        double Tiempo_Final = System.currentTimeMillis();
        double Tiempo_Ejecucion = (Tiempo_Final - Tiempo_Incial) / 1000;
        System.out.println("Duracion del tiempo de ejecucion: " + Tiempo_Ejecucion + " segundos. ");
    }

    static double GreedyAleatorio(ArrayList<Integer> Solucion, int tam, final double[][] Matriz_Distancias, int k) {
        Solucion.clear();  // Limpiar la solución previa

        ArrayList<Pair> todos = new ArrayList<>();
        ArrayList<Boolean> Ciudades_Visitadas = new ArrayList<>();
        for (int i = 0; i < tam; i++) {
            Ciudades_Visitadas.add(false);
        }

        // Sumar todas las distancias para cada ciudad y crear pares (suma, ciudad)
        for (int i = 0; i < tam; i++) {
            double Suma_Total = 0.0;
            for (int j = 0; j < tam; j++) {
                if (i != j) {
                    Suma_Total += Matriz_Distancias[i][j];
                }
            }
            todos.add(new Pair(Suma_Total, i));  // i representa la ciudad, en formato 0-indexado
        }
        Collections.sort(todos);
        // Inicializar la solución
        Random random = new Random();

        // Comenzar a construir la solución aleatoriamente desde las primeras K ciudades
        for (int i = 0; i < tam; i++) {
            // Selección aleatoria de una ciudad dentro del rango de K
            int limite = Math.min(k, todos.size());
            int pos = random.nextInt(limite);  // Seleccionar aleatoriamente entre las primeras `K` ciudades

            Pair pairSeleccionada = todos.get(pos);
            Solucion.add(pairSeleccionada.ciudad + 1);  // Agregar la ciudad a la solución (sumar 1 para numeración correcta)

            // Marcar la ciudad como visitada
            Ciudades_Visitadas.set(pairSeleccionada.ciudad, true);

            // Eliminar la ciudad seleccionada de la lista "todos"
            todos.remove(pos);  // Eliminar por posición
        }
        // Retornar el coste de la solución  generada
        return Calculo_Coste(Solucion, Matriz_Distancias, tam);
    }

    // Método para calcular el coste de la solución
    static double Calculo_Coste(ArrayList<Integer> Solucion, final double[][] Matriz_Distancias, int tam) {
        double coste = 0.0;

        for (int i = 0; i < tam - 1; i++) {
            coste += Matriz_Distancias[Solucion.get(i) - 1][Solucion.get(i + 1) -1];  // Restamos 1 para acceder al índice correcto
        }

        // Sumar la distancia entre la última ciudad y la primera para completar el ciclo
        coste += Matriz_Distancias[Solucion.get(0) - 1][Solucion.get(tam - 1) - 1];  // Restamos 1 para ajustar el índice

        return coste;
    }

    // Método para seleccionar la ciudad inicial
    static int ciudadInicial(ArrayList<Integer> Solucion, int tam, ArrayList<Boolean> Ciudades_Visitadas) {
        Random random = new Random();
        int ciudadInicial = random.nextInt(tam); // Selección de ciudad inicial aleatoria (índice)

        // Asignar la ciudad inicial en la solución (sumar 1 para ajustar la numeración)
        Solucion.add(ciudadInicial + 1);

        // Marcar la ciudad inicial como visitada
        Ciudades_Visitadas.set(ciudadInicial, true);

        return ciudadInicial;
    }

    static class Pair implements Comparable<Pair> {
        private Double suma;
        private Integer ciudad;

        Pair(Double suma, Integer ciudad) {
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