import java.util.*;
import static java.util.Collections.swap;

public class Algoritmos {

    /**
     * Método que genera una solución al problema utilizando un enfoque Greedy Aleatorio.
     * @param Solucion Lista de enteros donde se almacenará la solución generada.
     * @param Tam Número entero que representa el número total de ciudades
     * @param Matriz_Distancias Una matriz que representa la distancia entre la ciudad i y la ciudad j.
     * @return El coste total de la solución generada utilizando el algoritmo Greedy Aleatorio.
     */
    static double GreedyAleatorio(ArrayList<Integer> Solucion, int Tam, final double[][] Matriz_Distancias) {
        Solucion.clear();
        ArrayList<Pair> Posibles_Ciudades = new ArrayList<>();
        ArrayList<Boolean> Ciudades_Visitadas = new ArrayList<>();

        for (int i = 0; i < Tam; ++i) { Ciudades_Visitadas.add(false); }
        for (int i = 0; i < Tam; ++i) {
            double Suma_Total = 0.0;
            for (int j = 0; j < Tam; ++j) {
                if (i != j) {
                    Suma_Total = Suma_Total + Matriz_Distancias[i][j];
                }
            }
            Posibles_Ciudades.add( new Pair(i, Suma_Total) );
        }
        Collections.sort(Posibles_Ciudades);

        // Comenzar a construir la solución aleatoriamente desde las primeras K ciudades
        for (int i = 0; i < Tam; ++i) {
            //Hacemos el minimo por si hay menos que k y entrar a una posicion correcta
            int K_Correcta = Math.min(Main.lectorParametros.getNumeroK(), Posibles_Ciudades.size());
            int Pos = Main.random.nextInt(K_Correcta);

            Pair Pair_Pos = Posibles_Ciudades.get(Pos);
            Solucion.add(Pair_Pos.ciudad + 1);
            Ciudades_Visitadas.set(Pair_Pos.ciudad, true);

            Posibles_Ciudades.remove(Pos);
        }
        return Calculo_Coste(Solucion, Matriz_Distancias, Tam);
    }

    public static void Busqueda_Local( ArrayList<Integer> Solucion, int Tam, final double[][] Matriz_Distancias, long Iteraciones, float Porcentaje_Interacciones, float Entorno, float Reduccion ) {
        Solucion.clear();
        GreedyAleatorio(Solucion,Tam,Matriz_Distancias);
        ArrayList<Integer> Mejor_Vecino = new ArrayList<>(Solucion);
        ArrayList<Integer> Array_Aux = new ArrayList<>(Solucion);
        double Mejor_Coste = Calculo_Coste(Solucion, Matriz_Distancias, Tam);
        double Coste_Mejor;
        int Iteraccion = 0;
        boolean Mejora = true;
        int Reducir = (int) (Iteraciones * Porcentaje_Interacciones);
        int Tam_Vecindario = (int) (Iteraciones * Entorno);
        // Comienzan las vueltas del bucle principal (no iteraciones)
        while (Iteraccion < Iteraciones && Mejora) {
            Coste_Mejor = Double.MAX_VALUE;
            Mejora = false;

            // Evaluar todo el vecindario
            for (int j = 0; j < Tam_Vecindario; j++) {
                int Pos1 = Main.random.nextInt(Tam);
                int Pos2;
                do {
                    Pos2 = Main.random.nextInt(Tam);
                } while (Pos1 == Pos2);

                // Posible coste en caso de hacer el movimiento
                double Coste = Factorizacion(Array_Aux, Mejor_Coste, Matriz_Distancias, Tam, Pos1, Pos2);
                if (Coste < Coste_Mejor) {
                    Coste_Mejor = Coste;
                    swap(Array_Aux, Pos1, Pos2);
                    Mejor_Vecino = new ArrayList<>(Array_Aux);
                }
            }

            // Comprobamos si hay un vecino mejor a la solución actual
            if (Coste_Mejor < Mejor_Coste) {
                Iteraccion++;
                Solucion.clear();
                Solucion.addAll(Mejor_Vecino);
                Mejor_Coste = Coste_Mejor;
                Mejora = true;
            }

            // Calcular el entorno y modificarlo mediante el descenso
            if (Iteraccion % Reducir == 0) {
                Tam_Vecindario = (int) (Tam_Vecindario * (1 - Reduccion));
                //System.out.println(" Cambio del tamaño de vecindario");
            }
        }

        System.out.println("Iteraciones: " + Iteraccion + " Tam_Vecindario: " + Tam_Vecindario);
    }

    static double Factorizacion(ArrayList<Integer> Array_Aux, Double Mejor_Coste, final double[][] Matriz_Distancias, int Tam, int Pos1, int Pos2) {
        // Variables para almacenar los costes antes y después del intercambio
        double costeAntes = 0.0;
        double costeDespues = 0.0;

        // Obtener las ciudades involucradas en el intercambio
        int ciudadA = Array_Aux.get(Pos1)-1;
        int ciudadB = Array_Aux.get(Pos2)-1;

        // 1. Calcular el coste de los enlaces antes del intercambio
        // Calcular el enlace que precede a ciudadA
        if (Pos1 == 0) {
            costeAntes += Matriz_Distancias[Array_Aux.getLast()-1][ciudadA]; // Ciudad en el extremo izquierdo, conecta con la última
        } else {
            costeAntes += Matriz_Distancias[Array_Aux.get(Pos1 - 1)-1][ciudadA];
        }

        // Calcular el enlace que sigue a ciudadA (si no es Pos2)
        if (Pos1 + 1 != Pos2) {
            costeAntes += Matriz_Distancias[ciudadA][(Pos1 == Tam - 1) ? Array_Aux.getFirst()-1 : Array_Aux.get(Pos1 + 1)-1];
        }

        // Calcular el enlace que sigue a ciudadB (si Pos2 no es el último)
        if (Pos2 == Tam - 1) {
            costeAntes += Matriz_Distancias[ciudadB][Array_Aux.getFirst()-1]; // Ciudad en el extremo derecho, conecta con la primera
        } else {
            costeAntes += Matriz_Distancias[ciudadB][Array_Aux.get(Pos2 + 1)-1];
        }

        // Calcular el enlace que precede a ciudadB (si no es Pos1)
        if (Pos2 - 1 != Pos1) {
            costeAntes += Matriz_Distancias[Array_Aux.get((Pos2 - 1 + Tam) % Tam)-1][ciudadB];
        }

        // 2. Calcular el coste de los enlaces después del intercambio
        // Ahora, ciudadA y ciudadB están intercambiadas, calcular el nuevo coste

        // Calcular el enlace que precede a ciudadB (ahora en Pos1)
        if (Pos1 == 0) {
            costeDespues += Matriz_Distancias[Array_Aux.getLast()-1][ciudadB];
        } else {
            costeDespues += Matriz_Distancias[Array_Aux.get(Pos1 - 1)-1][ciudadB];
        }

        // Calcular el enlace que sigue a ciudadB (si no es Pos2)
        if (Pos1 + 1 != Pos2) {
            costeDespues += Matriz_Distancias[ciudadB][(Pos1 == Tam - 1) ? Array_Aux.getFirst()-1 : Array_Aux.get(Pos1 + 1)-1];
        }

        // Calcular el enlace que sigue a ciudadA (ahora en Pos2)
        if (Pos2 == Tam - 1) {
            costeDespues += Matriz_Distancias[ciudadA][Array_Aux.getFirst()-1];
        } else {
            costeDespues += Matriz_Distancias[ciudadA][Array_Aux.get(Pos2 + 1)-1];
        }

        // Calcular el enlace que precede a ciudadA (si no es Pos1)
        if (Pos2 - 1 != Pos1) {
            costeDespues += Matriz_Distancias[Array_Aux.get((Pos2 - 1 + Tam) % Tam)-1][ciudadA];
        }

        // 3. Calcular el nuevo coste total después del intercambio
        double nuevoCoste = Mejor_Coste - costeAntes + costeDespues;

        return nuevoCoste;
    }


    /**
     * Método que calcula el coste total de una solución apartir de una matriz de distancias.
     * @param Solucion Una lista de enteros     que representa una posible solución, donde cada entero corresponde a una ciudad y el orden indica la secuencia de la ruta.
     * @param Matriz_Distancias Una matriz que representa la distancia entre la ciudad i y la ciudad j.
     * @param Tam Número entero que representa el tamaño de la solución.
     * @return El coste total de recorrer la solución proporcionada, según las distancias en la matriz.
     */
    static double Calculo_Coste(ArrayList<Integer> Solucion, final double[][] Matriz_Distancias, int Tam) {
        double Distancia_Total = 0.0;

        //Unimos las ciudades de la solucion sumandoles la distancia
        for (int i = 0; i < Tam - 1; i++) {
            Distancia_Total = Distancia_Total + Matriz_Distancias[Solucion.get(i) - 1][Solucion.get(i + 1) -1];
        }

        //Sumamos el coste de ir de la ultima a la primera ciudad para cerrar el ciclo
        return Distancia_Total + Matriz_Distancias[Solucion.getFirst() - 1][Solucion.get(Tam - 1) - 1];
    }

    /**
     * Clase Pair con la que guardaremos la suma del camino hasta ella y la propia ciudad
     */
    static class Pair implements Comparable<Pair> {
        private final Integer ciudad;
        private final Double suma;

        Pair( Integer ciudad, Double suma ){
            this.suma = suma;
            this.ciudad = ciudad;
        }

        @Override
        public int compareTo( Pair p ){
            if( this.suma - p.suma <= 0){ return -1; }
            return 1;
        }
    }
}