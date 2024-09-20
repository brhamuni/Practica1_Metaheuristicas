//Pareja 10

import java.util.ArrayList;
import java.util.Random;

public class Main implements Runnable{

    private static ArrayList<Integer> Solucion;

    public static void main(String[] args) {

        Solucion = new ArrayList<>();

        LectorDatos lector_ch130 = new LectorDatos("ch130.tsp");
        LectorDatos lector_pr144 = new LectorDatos("pr144.tsp");
        LectorDatos lector_a280 = new LectorDatos("a280.tsp");
        LectorDatos lector_u1060 = new LectorDatos("u1060.tsp");
        LectorDatos lector_d18512 = new LectorDatos("d18512.tsp");

        System.out.println("Una posible solucion para el archivo ch130.tsp es: "+ GreedyAleatorio(lector_ch130.getMatriz_Distancias(),lector_ch130.getMatriz_Distancias().length,Solucion));

        System.out.println("Una posible solucion para el archivo pr144.tsp es: "+ GreedyAleatorio(lector_pr144.getMatriz_Distancias(),lector_pr144.getMatriz_Distancias().length,Solucion));

        System.out.println("Una posible solucion para el archivo a280.tsp es: "+ GreedyAleatorio(lector_a280.getMatriz_Distancias(),lector_a280.getMatriz_Distancias().length,Solucion));

        System.out.println("Una posible solucion para el archivo u1060.tsp es: "+ GreedyAleatorio(lector_u1060.getMatriz_Distancias(),lector_u1060.getMatriz_Distancias().length,Solucion));

        System.out.println("Una posible solucion para el archivo d18512.tsp es: "+ GreedyAleatorio(lector_d18512.getMatriz_Distancias(),lector_d18512.getMatriz_Distancias().length,Solucion));


    }

    static double GreedyAleatorio(final double[][] Distancias, int tam, ArrayList<Integer> Solucion) {
        // Inicializar marcadores de ciudades visitadas y la solución
        ArrayList<Boolean> Ciudades_Visitadas = new ArrayList<>(tam);
        for (int i = 0; i < tam; i++) {
            Ciudades_Visitadas.add(false); // Todas las ciudades no marcadas al inicio
        }

        Solucion.clear();  // Asegurarse de que la solución está vacía al comenzar
        for (int i = 0; i < tam; i++) {
            Solucion.add(0);  // Rellenar la solución con ceros (se llenará después)
        }

        // Cargar la ciudad inicial
        ciudadInicial(Solucion, tam, Ciudades_Visitadas);

        // Bucle para encontrar las siguientes ciudades de manera greedy
        for (int i = 0; i < tam - 1; i++) {
            double menorDist = Double.MAX_VALUE;
            int posmenor = 0;

            // Buscar la ciudad más cercana que no ha sido visitada
            for (int j = 0; j < tam; j++) {
                if (!Ciudades_Visitadas.get(j) && Distancias[Solucion.get(i)][j] < menorDist) {
                    menorDist = Distancias[Solucion.get(i)][j];
                    posmenor = j;
                }
            }

            // Actualizar la solución con la ciudad más cercana
            Solucion.set(i + 1, posmenor);
            Ciudades_Visitadas.set(posmenor, true); // Marcar la ciudad como visitada
        }

        // Retornar el coste total de la solución obtenida
        return Calculo_Coste(Solucion, Distancias, tam);
    }


    static double Calculo_Coste(ArrayList<Integer> Solucion, final double[][] Distancias, int tam) {
        double cost = 0.0;

        for (int i = 0; i < tam - 1; i++) {
            cost += Distancias[Solucion.get(i)][Solucion.get(i + 1)];
        }

        cost += Distancias[Solucion.get(0)][Solucion.get(tam - 1)];

        return cost;
    }

    static void ciudadInicial(ArrayList<Integer> Solucion, int tam, ArrayList<Boolean> Ciudades_Visitadas) {
        // Seleccionar la primera ciudad como ciudad inicial (puedes cambiar esto si es necesario)
        int ciudadInicial = new Random().nextInt(0,tam); // Comenzamos por la ciudad 0 (puede ser otra ciudad según la lógica)

        // Asignar la ciudad inicial en la solución
        Solucion.set(0, ciudadInicial);

        // Marcar la ciudad inicial como visitada
        Ciudades_Visitadas.set(ciudadInicial, true);
    }

    @Override
    public void run() {
        long Tiempo_Incial = System.currentTimeMillis();
        long Tiempo_Final = System.currentTimeMillis();

        long Tiempo_Ejecucion = (Tiempo_Final-Tiempo_Incial)/1000;
        System.out.println("El costo es" + costo + "\n Duracion: " + Tiempo_Ejecucion + " segundos. ");
    }
}


