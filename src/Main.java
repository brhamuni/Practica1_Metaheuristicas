//Pareja 10

import java.util.ArrayList;
import java.util.Random;

public class Main {

    private static ArrayList<Integer> Solucion;

    public static void main(String[] args) {

        Solucion = new ArrayList<>();

        LectorDatos lector_ch130 = new LectorDatos("ch130.tsp");
        LectorDatos lector_pr144 = new LectorDatos("pr144.tsp");
        LectorDatos lector_a280 = new LectorDatos("a280.tsp");
        LectorDatos lector_u1060 = new LectorDatos("u1060.tsp");
        LectorDatos lector_d18512 = new LectorDatos("d18512.tsp");

        System.out.println("Una posible solucion para el archivo ch130.tsp es: "+ GreedyAleatorio(lector_ch130.getMatriz_Distancias(),lector_ch130.getMatriz_Distancias().length,Solucion));
        Solucion.clear();
        System.out.println("Una posible solucion para el archivo pr144.tsp es: "+ GreedyAleatorio(lector_pr144.getMatriz_Distancias(),lector_pr144.getMatriz_Distancias().length,Solucion));
        Solucion.clear();
        System.out.println("Una posible solucion para el archivo a280.tsp es: "+ GreedyAleatorio(lector_a280.getMatriz_Distancias(),lector_a280.getMatriz_Distancias().length,Solucion));
        Solucion.clear();
        System.out.println("Una posible solucion para el archivo u1060.tsp es: "+ GreedyAleatorio(lector_u1060.getMatriz_Distancias(),lector_u1060.getMatriz_Distancias().length,Solucion));
        Solucion.clear();
        System.out.println("Una posible solucion para el archivo d18512.tsp es: "+ GreedyAleatorio(lector_d18512.getMatriz_Distancias(),lector_d18512.getMatriz_Distancias().length,Solucion));
        Solucion.clear();

    }

    static double GreedyAleatorio(final double[][] mat, int n, ArrayList<Integer> sol) {
        // Inicializar marcadores de ciudades visitadas y la solución
        ArrayList<Boolean> marcada = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            marcada.add(false); // Todas las ciudades no marcadas al inicio
        }

        sol.clear();  // Asegurarse de que la solución está vacía al comenzar
        for (int i = 0; i < n; i++) {
            sol.add(0);  // Rellenar la solución con ceros (se llenará después)
        }

        // Cargar la ciudad inicial
        ciudadInicial(sol, n, marcada);

        // Bucle para encontrar las siguientes ciudades de manera greedy
        for (int i = 0; i < n - 1; i++) {
            double menorDist = Double.MAX_VALUE;
            int posmenor = 0;

            // Buscar la ciudad más cercana que no ha sido visitada
            for (int j = 0; j < n; j++) {
                if (!marcada.get(j) && mat[sol.get(i)][j] < menorDist) {
                    menorDist = mat[sol.get(i)][j];
                    posmenor = j;
                }
            }

            // Actualizar la solución con la ciudad más cercana
            sol.set(i + 1, posmenor);
            marcada.set(posmenor, true); // Marcar la ciudad como visitada
        }

        // Retornar el coste total de la solución obtenida
        return Calculo_Coste(sol, mat, n);
    }


    static double Calculo_Coste(ArrayList<Integer> sol, final double[][] dist, int tam) {
        double cost = 0.0;

        for (int i = 0; i < tam - 1; i++) {
            cost += dist[sol.get(i)][sol.get(i + 1)];
        }

        cost += dist[sol.get(0)][sol.get(tam - 1)];

        return cost;
    }

    static void ciudadInicial(ArrayList<Integer> sol, int n, ArrayList<Boolean> marcada) {
        // Seleccionar la primera ciudad como ciudad inicial (puedes cambiar esto si es necesario)
        int ciudadInicial = new Random().nextInt(0,n); // Comenzamos por la ciudad 0 (puede ser otra ciudad según la lógica)

        // Asignar la ciudad inicial en la solución
        sol.set(0, ciudadInicial);

        // Marcar la ciudad inicial como visitada
        marcada.set(ciudadInicial, true);
    }




}


