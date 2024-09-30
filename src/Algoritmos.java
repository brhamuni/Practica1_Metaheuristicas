import java.util.ArrayList;
import java.util.Collections;

import static java.util.Collections.swap;

public class Algoritmos {

    /**
     * Greedy Aleatorio
     * @param Solucion Vector con el camino que hace el viajante
     * @param Tam Numero de ciudades
     * @param Matriz_Distancias Matriz donde se guardan las distancias entre ciudades
     * @return
     */
    static double GreedyAleatorio(ArrayList<Integer> Solucion, int Tam, final double[][] Matriz_Distancias) {
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
        //Ordenamos todos los pairs
        Collections.sort(Posibles_Ciudades);

        // Comenzar a construir la solución aleatoriamente desde las primeras K ciudades
        for (int i = 0; i < Tam; i++) {
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

    public static void Busqueda_Local(ArrayList<Integer> Solucion, int Tam, final double[][] Matriz_Distancias,
                                      long Iteraciones, float Aplica, float Entorno, float Descenso) {

        ArrayList<Integer> Mejor_Vecino = new ArrayList<>(Solucion);
        ArrayList<Integer> nuevo = new ArrayList<>(Solucion);
        double Mejor_Coste = Calculo_Coste(Solucion, Matriz_Distancias, Tam);
        double Coste_Mejor;
        long i = 0;
        boolean Mejora = true;
        int cambio = (int) (Iteraciones * Aplica);
        int tamVecindario = (int) (Iteraciones * Entorno);
        // Comienzan las vueltas del bucle principal (no iteraciones)
        while (i < Iteraciones && Mejora) {
            Coste_Mejor = 99999999999.0;
            Mejora = false;

            // Evaluar todo el vecindario
            for (int j = 0; j < tamVecindario; j++) {
                int p1 = Main.random.nextInt(Tam);
                int p2;
                do {
                    p2 = Main.random.nextInt(Tam);
                } while (p1 == p2);

                nuevo = new ArrayList<>(Solucion);
                // Posible coste en caso de hacer el movimiento
                double Coste = Factorizar(nuevo, Mejor_Coste, Matriz_Distancias, Tam, p1, p2);
                if (Coste < Coste_Mejor) {
                    Coste_Mejor = Coste;
                    swap(nuevo, p1, p2);
                    Mejor_Vecino = new ArrayList<>(nuevo);
                }
            }

            // Comprobamos si hay un vecino mejor a la solución actual
            if (Coste_Mejor < Mejor_Coste) {
                i++;
                Solucion.clear();
                Solucion.addAll(Mejor_Vecino);
                Mejor_Coste = Coste_Mejor;
                Mejora = true;
            }

            // Calcular el entorno y modificarlo mediante el descenso
            if (i % cambio == 0) {
                tamVecindario = (int) (tamVecindario * (1 - Descenso));
                System.out.println(" Cambio vecindario");
            }
        }

        System.out.println("**Iteraciones: " + i + " TamVecindario: " + tamVecindario);
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
