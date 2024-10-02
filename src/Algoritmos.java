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

    public static void Busqueda_Local( ArrayList<Integer> Mejor_Solucion, int Tam, final double[][] Matriz_Distancias, long Iteraciones, float Porcentaje_Interacciones, float Entorno, float Reduccion ) {
        Mejor_Solucion.clear();
        GreedyAleatorio(Mejor_Solucion,Tam,Matriz_Distancias);
        ArrayList<Integer> Array_Aux;
        ArrayList<Integer> Mejor_Vecino = new ArrayList<>(Mejor_Solucion);
        double Mejor_Coste = Calculo_Coste(Mejor_Solucion, Matriz_Distancias, Tam);
        double Coste_Mejor_Vecino = Mejor_Coste;
        int Iteraccion = 0;
        boolean Mejora = true;
        int Reducir = (int) (Iteraciones * Porcentaje_Interacciones);
        int Tam_Vecindario = (int) (Iteraciones * Entorno);
        // Comienzan las vueltas del bucle principal (no iteraciones)
        while (Iteraccion < Iteraciones && Mejora) {
            Coste_Mejor_Vecino = Double.MAX_VALUE;
            Mejora = false;

            // Evaluar todo el vecindario
            for (int j = 0; j < Tam_Vecindario; j++) {
                int Pos1 = Main.random.nextInt(Tam);
                int Pos2;
                do {
                    Pos2 = Main.random.nextInt(Tam);
                } while (Pos1 == Pos2);

                // Posible coste en caso de hacer el movimiento
                Array_Aux = new ArrayList<>(Mejor_Solucion);

                double Coste = Factorizacion(Array_Aux, Mejor_Coste, Matriz_Distancias, Tam, Pos1, Pos2);
                if (Coste < Coste_Mejor_Vecino) {
                    Coste_Mejor_Vecino = Coste;
                    swap(Array_Aux, Pos1, Pos2);
                    Mejor_Vecino = new ArrayList<>(Array_Aux);
                }
            }

            // Comprobamos si hay un vecino mejor a la solución actual
            if (Coste_Mejor_Vecino < Mejor_Coste) {
                Iteraccion++;
                Mejor_Solucion.clear();
                Mejor_Solucion.addAll(Mejor_Vecino);
                Mejor_Coste = Coste_Mejor_Vecino;
                Mejora = true;

                // Calcular el entorno y modificarlo mediante el descenso
                if (Iteraccion % Reducir == 0) {
                    Tam_Vecindario = (int) (Tam_Vecindario * (1 - Reduccion));
                    //System.out.println("Iteraciones = "+Iteraccion+": Cambio de vecindario");
                }
            }

        }

        System.out.println("Iteraciones: " + Iteraccion + " Tam_Vecindario: " + Tam_Vecindario);
    }

    static double Factorizacion(ArrayList<Integer> Array_Aux, Double Mejor_Coste, final double[][] Matriz_Distancias, int Tam, int Pos1, int Pos2) {
        // Variables para almacenar los costes antes y después del intercambio
        double Coste_Anterior = 0.0;
        double Coste_Despues = 0.0;

        // Obtener las ciudades involucradas en el intercambio
        int Ciudad_Pos1 = Array_Aux.get(Pos1)-1;
        int Ciudad_Pos2 = Array_Aux.get(Pos2)-1;

        // 1. Calcular el coste de los enlaces antes del intercambio
        // Calcular el enlace que precede a Ciudad_Pos1
        if (Pos1 == 0) {
            Coste_Anterior += Matriz_Distancias[Array_Aux.getLast()-1][Ciudad_Pos1]; // Ciudad en el extremo izquierdo, conecta con la última
        } else {
            Coste_Anterior += Matriz_Distancias[Array_Aux.get(Pos1 - 1)-1][Ciudad_Pos1];
        }

        // Calcular el enlace que sigue a Ciudad_Pos1 (si no es Pos2)
        if (Pos1 + 1 != Pos2) {
            if (Pos1 == Tam - 1) {
                Coste_Anterior += Matriz_Distancias[Ciudad_Pos1][Array_Aux.getFirst() - 1];
            } else {
                Coste_Anterior += Matriz_Distancias[Ciudad_Pos1][Array_Aux.get(Pos1 + 1) - 1];
            }
        }

        // Calcular el enlace que sigue a Ciudad_Pos2 (si Pos2 no es el último)
        if (Pos2 == Tam - 1) {
            Coste_Anterior += Matriz_Distancias[Ciudad_Pos2][Array_Aux.getFirst()-1]; // Ciudad en el extremo derecho, conecta con la primera
        } else {
            Coste_Anterior += Matriz_Distancias[Ciudad_Pos2][Array_Aux.get(Pos2 + 1)-1];
        }

        // Calcular el enlace que precede a Ciudad_Pos2 (si no es Pos1)
        if (Pos2 - 1 != Pos1) {
            Coste_Anterior += Matriz_Distancias[Array_Aux.get((Pos2 - 1 + Tam) % Tam)-1][Ciudad_Pos2];
        }

        // 2. Calcular el coste de los enlaces después del intercambio
        // Ahora, Ciudad_Pos1 y Ciudad_Pos2 están intercambiadas, calcular el nuevo coste

        // Calcular el enlace que precede a Ciudad_Pos2 (ahora en Pos1)
        if (Pos1 == 0) {
            Coste_Despues += Matriz_Distancias[Array_Aux.getLast()-1][Ciudad_Pos2];
        } else {
            Coste_Despues += Matriz_Distancias[Array_Aux.get(Pos1 - 1)-1][Ciudad_Pos2];
        }

        // Calcular el enlace que sigue a Ciudad_Pos2 (si no es Pos2)
        if (Pos1 + 1 != Pos2) {
            if (Pos1 == Tam - 1) {
                Coste_Despues += Matriz_Distancias[Ciudad_Pos2][Array_Aux.getFirst() - 1];
            } else {
                Coste_Despues += Matriz_Distancias[Ciudad_Pos2][Array_Aux.get(Pos1 + 1) - 1];
            }
        }

        // Calcular el enlace que sigue a Ciudad_Pos1 (ahora en Pos2)
        if (Pos2 == Tam - 1) {
            Coste_Despues += Matriz_Distancias[Ciudad_Pos1][Array_Aux.getFirst()-1];
        } else {
            Coste_Despues += Matriz_Distancias[Ciudad_Pos1][Array_Aux.get(Pos2 + 1)-1];
        }

        // Calcular el enlace que precede a Ciudad_Pos1 (si no es Pos1)
        if (Pos2 - 1 != Pos1) {
            Coste_Despues += Matriz_Distancias[Array_Aux.get((Pos2 - 1 + Tam) % Tam)-1][Ciudad_Pos1];
        }

        // 3. Calcular el nuevo coste total después del intercambio
        double Nuevo_Coste = Mejor_Coste-Coste_Anterior+Coste_Despues;
        return Nuevo_Coste;
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