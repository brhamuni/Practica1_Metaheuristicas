import java.util.*;
import static java.util.Collections.swap;

public class Busqueda_Tabu {
    public static void Busqueda_Tabu(ArrayList<Integer> Mejor_Solucion, int Tam, final double[][] Matriz_Distancias, long Iteraciones, float Porcentaje_Interacciones, float Entorno, float Reduccion, float Estancamiento ) {

        Mejor_Solucion.clear();
        Greedy_Aleatorio.GreedyAleatorio(Mejor_Solucion,Tam,Matriz_Distancias);
        ArrayList<Integer> Array_Aux;
        ArrayList<Integer> Mejor_Vecino = new ArrayList<>(Mejor_Solucion);
        ArrayList<Integer> Mejor_Global = new ArrayList<>(Mejor_Solucion);
        double Mejor_Coste = Utils.Calculo_Coste(Mejor_Solucion, Matriz_Distancias, Tam);
        double Coste_Mejor_Vecino;
        int Iteracion = 0;
        int Reducir = (int) (Iteraciones * Porcentaje_Interacciones);
        int Tam_Vecindario = (int) (Iteraciones * Entorno);
        int Estanca = (int) (Iteraciones*Estancamiento);
        double Coste_Mejor_Momento_Anterior = Double.MAX_VALUE;
        double Mejor_Coste_Global = Mejor_Coste;
        int Empeoramientos = 0;

        ArrayList<ArrayList<Integer>> Memoria = new ArrayList<>(Collections.nCopies(Tam, new ArrayList<>(Collections.nCopies(Tam,0))));
        Integer Pos1_Tabu, Pos2_Tabu;

        while (Iteracion < Iteraciones) {

            Coste_Mejor_Vecino = Double.MAX_VALUE;

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

            // Comprobamos si hay un vecino mejor que la solución actual
            if (Coste_Mejor_Vecino < Mejor_Coste) {

                Mejor_Solucion.clear();
                Mejor_Solucion.addAll(Mejor_Vecino);
                Mejor_Coste = Coste_Mejor_Vecino;

                if (Coste_Mejor_Vecino < Mejor_Coste_Global) {
                    Mejor_Coste_Global = Mejor_Coste;
                    Mejor_Global = new ArrayList<>(Mejor_Solucion);
                }

                if (Mejor_Coste < Coste_Mejor_Momento_Anterior) {
                    //Mejora el mejor coste anterior por tanto empeoramientos = 0
                    Empeoramientos = 0;
                    Coste_Mejor_Momento_Anterior = Mejor_Coste;
                } else {
                    Empeoramientos++;
                }

            } else {

                Empeoramientos++;

                if (Empeoramientos == Estanca) {
                    Empeoramientos = 0;
                    System.out.println("Estancamiento");
                    Greedy_Aleatorio.GreedyAleatorio(Mejor_Solucion, Tam, Matriz_Distancias);
                    Mejor_Coste = Utils.Calculo_Coste(Mejor_Solucion, Matriz_Distancias, Tam);
                    Coste_Mejor_Momento_Anterior = Mejor_Coste;
                } else {
                    Mejor_Solucion.clear();
                    Mejor_Solucion.addAll(Mejor_Vecino);
                    Mejor_Coste = Coste_Mejor_Vecino;
                }
            }

            // Calculamos el nuevo entorno si es necesario
            if (Iteracion % Reducir == 0) {
                Tam_Vecindario = (int) (Tam_Vecindario * (1 - Reduccion));
                //System.out.println(" Cambio vecindario " + Tam_Vecindario);
            }
            Iteracion++;
        }

        //System.out.println("**Iteraciones: " + Iteracion + " TamVecindario: " + Tam_Vecindario);

        Mejor_Solucion.clear();
        Mejor_Solucion.addAll(Mejor_Global);
    }

}