import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static java.util.Collections.swap;

public class Busqueda_Local {
    public static void Busqueda_Local(ArrayList<Integer> Mejor_Solucion, int Tam, final double[][] Matriz_Distancias, long Iteraciones, float Porcentaje_Interacciones, float Entorno, float Reduccion, FileWriter Archivo, StringBuilder Log ) throws IOException {
        Mejor_Solucion.clear();
        Greedy_Aleatorio.GreedyAleatorio(Mejor_Solucion,Tam,Matriz_Distancias, Archivo, Log);
        ArrayList<Integer> Array_Aux;
        ArrayList<Integer> Mejor_Vecino = new ArrayList<>(Mejor_Solucion);
        double Mejor_Coste = Utils.Calculo_Coste(Mejor_Solucion, Matriz_Distancias, Tam);
        double Coste_Mejor_Vecino = Mejor_Coste;
        int Iteraccion = 0;
        boolean Mejora = true;
        int Reducir = (int) (Iteraciones * Porcentaje_Interacciones);
        int Tam_Vecindario = (int) (Iteraciones * Entorno);

        // Comienzan las vueltas del bucle principal (no iteraciones)
        while (Iteraccion < Iteraciones && Mejora) {
            Coste_Mejor_Vecino = Double.MAX_VALUE;
            Mejora = false;

            // Evaluamos el vecindario
            for (int j = 0; j < Tam_Vecindario; j++) {
                int Pos1 = Main.random.nextInt(Tam);
                int Pos2;
                do {
                    Pos2 = Main.random.nextInt(Tam);
                } while (Pos1 == Pos2);

                Array_Aux = new ArrayList<>(Mejor_Solucion);
                double Coste_Permutado = Utils.Factorizacion(Array_Aux, Mejor_Coste, Matriz_Distancias, Tam, Pos1, Pos2);

                if (Coste_Permutado < Coste_Mejor_Vecino) {
                    Coste_Mejor_Vecino = Coste_Permutado;
                    swap(Array_Aux, Pos1, Pos2);
                    Mejor_Vecino = new ArrayList<>(Array_Aux);
                }
            }

            // Comprobamos si hay un vecino mejor a la solución
            if (Coste_Mejor_Vecino < Mejor_Coste) {
                Iteraccion++;
                Mejor_Solucion.clear();
                Mejor_Solucion.addAll(Mejor_Vecino);
                Mejor_Coste = Coste_Mejor_Vecino;
                Mejora = true;

                // Calcular el entorno
                if (Iteraccion % Reducir == 0) {
                    Tam_Vecindario = (int) (Tam_Vecindario * (1 - Reduccion));
                }
            }
        }
        System.out.println("Total de iteraciones: " + Iteraccion + " con un tamaño de vecindario final: " + Tam_Vecindario);
    }
}