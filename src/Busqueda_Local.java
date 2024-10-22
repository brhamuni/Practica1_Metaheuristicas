import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import static java.util.Collections.swap;

public class Busqueda_Local {
    public static void Busqueda_Local(ArrayList<Integer> Mejor_Solucion, int Tam, final double[][] Matriz_Distancias, long Iteraciones, float Porcentaje_Interacciones, float Entorno, float Reduccion, FileWriter Archivo, StringBuilder Log ) throws IOException {
        Mejor_Solucion.clear();
        Greedy_Aleatorio.GreedyAleatorio(Mejor_Solucion,Tam,Matriz_Distancias, Archivo, Log);
        ArrayList<Integer> Array_Aux;
        ArrayList<Integer> Mejor_Momento_Actual = new ArrayList<>(Mejor_Solucion);
        double Mejor_Coste = Utils.Calculo_Coste(Mejor_Solucion, Matriz_Distancias, Tam);
        double Coste_Mejor_Momento_Actual = Mejor_Coste;
        int Iteraccion = 0;
        boolean Mejora = true;
        int Reducir = (int) (Iteraciones * Porcentaje_Interacciones);
        int Tam_Vecindario = (int) (Iteraciones * Entorno);

        int Pos1 = 0, Pos2 = 0;
        double Coste_Permutado = 0;
        // Comienzan las vueltas del bucle principal (no iteraciones)
        while (Iteraccion < Iteraciones && Mejora) {
            Coste_Mejor_Momento_Actual = Double.MAX_VALUE;
            Mejora = false;

            // Evaluamos el vecindario
            for (int j = 0; j < Tam_Vecindario; j++) {
                Pos1 = Main.random.nextInt(Tam);
                do {
                    Pos2 = Main.random.nextInt(Tam);
                } while (Pos1 == Pos2);

                Array_Aux = new ArrayList<>(Mejor_Solucion);
                Coste_Permutado = Utils.Factorizacion(Array_Aux, Mejor_Coste, Matriz_Distancias, Tam, Pos1, Pos2);
                if (Coste_Permutado < Coste_Mejor_Momento_Actual) {
                    Coste_Mejor_Momento_Actual = Coste_Permutado;
                    swap(Array_Aux, Pos1, Pos2);
                    Mejor_Momento_Actual = new ArrayList<>(Array_Aux);
                }
            }

            // Comprobamos si hay un vecino mejor a la solución
            if (Coste_Mejor_Momento_Actual < Mejor_Coste) {
                Log.append("Permutacion Pos1: ("+ Pos1 +"(Ciudad: " + Mejor_Momento_Actual.get(Pos2) + ")) -> Pos2: (" + Pos2 +"(Ciudad: "+ Mejor_Momento_Actual.get(Pos1) + ")). Coste sin permutar: " + Mejor_Coste + ", Coste con permutacion: " + Coste_Mejor_Momento_Actual + "\n");
                Mejor_Solucion.clear();
                Mejor_Solucion.addAll(Mejor_Momento_Actual);
                Mejor_Coste = Coste_Mejor_Momento_Actual;
                Mejora = true;
                // Calcular el entorno
                if (Iteraccion % Reducir == 0) {
                    Tam_Vecindario = (int) (Tam_Vecindario * (1 - Reduccion));
                }
            }
            ++Iteraccion;
            Log.append("\n");
            Archivo.write(Log.toString());
            Log.delete(0, Log.length());
        }
        System.out.println("Total de iteraciones: " + Iteraccion + " con un tamaño de vecindario final: " + Tam_Vecindario);
    }
}