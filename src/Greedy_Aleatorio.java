import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Greedy_Aleatorio {

    /**
     * Método que genera una solución al problema utilizando un enfoque Greedy Aleatorio.
     * @param Solucion Lista de enteros donde se almacenará la solución generada.
     * @param Tam Número entero que representa el número total de ciudades
     * @param Matriz_Distancias Una matriz que representa la distancia entre la ciudad i y la ciudad j.
     * @return El coste total de la solución generada utilizando el algoritmo Greedy Aleatorio.
     */
    static double GreedyAleatorio(ArrayList< Integer > Solucion, int Tam, final double[][] Matriz_Distancias) throws IOException {
        Solucion.clear();
        ArrayList<Utils.Pair> Posibles_Ciudades = new ArrayList<>();
        ArrayList<Boolean> Ciudades_Visitadas = new ArrayList<>(Collections.nCopies(Tam,false));

        for (int i = 0; i < Tam; ++i) {
            double Suma_Total = 0.0;
            for (int j = 0; j < Tam; ++j) {
                if (i != j) {
                    Suma_Total = Suma_Total + Matriz_Distancias[i][j];
                }
            }
            Posibles_Ciudades.add( new Utils.Pair(i, Suma_Total) );
        }
        Collections.sort(Posibles_Ciudades);

        // Comenzar a construir la solución aleatoriamente desde las primeras K ciudades
        for (int i = 0; i < Tam; ++i) {
            //Hacemos el minimo por si hay menos que k y entrar a una posicion correcta
            int K_Correcta = Math.min(Main.lectorParametros.getNumeroK(), Posibles_Ciudades.size());
            int Pos = Main.random.nextInt(K_Correcta);

            Utils.Pair Pair_Pos = Posibles_Ciudades.get(Pos);
            Solucion.add(Pair_Pos.getCiudad()+ 1);
            Ciudades_Visitadas.set(Pair_Pos.getCiudad(), true);

            Posibles_Ciudades.remove(Pos);
        }
        return Utils.Calculo_Coste(Solucion, Matriz_Distancias, Tam);
    }
}