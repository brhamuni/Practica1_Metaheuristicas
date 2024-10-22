import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static java.util.Collections.swap;

public class Busqueda_Tabu {

    public static void Busqueda_Tabu(ArrayList<Integer> solucion_actual, int Tam, final double[][] Matriz_Distancias, long Iteraciones, float Porcentaje_Interacciones, float Entorno, float Reduccion, float Estancamiento, int Tenencia, float Oscilacion, FileWriter Archivo, StringBuilder Log) throws IOException {

        solucion_actual.clear();
        Greedy_Aleatorio.GreedyAleatorio(solucion_actual, Tam, Matriz_Distancias, Archivo, Log);
        ArrayList<Integer> Array_Aux;
        ArrayList<Integer> mejor_momento_actual = new ArrayList<>( solucion_actual );
        ArrayList<Integer> Mejor_Global = new ArrayList<>( solucion_actual );

        double Mejor_Actual = Utils.Calculo_Coste( solucion_actual, Matriz_Distancias, Tam );
        double Coste_Mejor_Momento_Actual;
        int Iteracion = 0;
        int Reducir = (int) (Iteraciones * Porcentaje_Interacciones);
        int Tam_Vecindario = (int) (Iteraciones * Entorno);
        int Estanca = (int) (Iteraciones * Estancamiento);
        double Coste_Mejor_Momento_Anterior = Double.MAX_VALUE;
        double Mejor_Coste_Global = Mejor_Actual;
        int Empeoramientos = 0;

        int[][] Memoria = new int[Tam][Tam];
        for (int i = 0; i < Tam; i++) {
            for (int j = 0; j < Tam; j++) {
                Memoria[i][j]=0;
            }
        }

        Integer Pos1_Tabu=-1, Pos2_Tabu=-1, Ciudad_Tabu1=-1, Ciudad_Tabu2=-1;
        LinkedList<Utils.Pair_Tabu> Lista_Tabu = new LinkedList<>();
        for (int i = 0; i < Tenencia; ++i) {
            Lista_Tabu.add(new Utils.Pair_Tabu(-1, -1, -1, -1));
        }

        while (Iteracion < Iteraciones) {
            Log.append("El mejor coste global es: "+ Mejor_Coste_Global +"\n");
            Log.append("El coste del mejor momento anterior es: "+ Coste_Mejor_Momento_Anterior +"\n");

            Coste_Mejor_Momento_Actual = Double.MAX_VALUE;
            // Evaluamos el vecindario
            for (int i = 0; i < Tam_Vecindario; ++i) {
                int Pos1 = Main.random.nextInt(Tam);
                int Pos2;
                do {
                    Pos2 = Main.random.nextInt(Tam);
                } while (Pos1 == Pos2);
                boolean Es_Tabu = false;

                // Aseguramos que Pos1 < Pos2 y Ciudad1 < Ciudad2 para comparación
                int Pos1_Nueva = Pos1, Pos2_Nueva = Pos2, Ciudad1_Nueva = solucion_actual.get(Pos1), Ciudad2_Nueva = solucion_actual.get(Pos2);

                if (Pos1_Nueva > Pos2_Nueva) {
                    Intercambiar(Pos1_Nueva, Pos2_Nueva);
                }
                if (Ciudad1_Nueva > Ciudad2_Nueva) {
                    Intercambiar(Ciudad1_Nueva, Ciudad2_Nueva);
                }

                for (int j=0;j<Lista_Tabu.size(); ++j) {
                    if (Utils.Pair_Tabu.iguales(new Utils.Pair_Tabu(Ciudad1_Nueva, Ciudad2_Nueva, Pos1_Nueva, Pos2_Nueva), Lista_Tabu.get(j))) {
                        Es_Tabu = true;  // El movimiento es Tabú
                        //System.out.println("Tabu");
                        break;
                    }
                }

                if (!Es_Tabu) {
                    Array_Aux = new ArrayList<>( solucion_actual );
                    double Coste_Permutado = Utils.Factorizacion(Array_Aux, Mejor_Actual, Matriz_Distancias, Tam, Pos1, Pos2);
                    // Verificar que el coste no es negativo ni incorrecto
                    if (Coste_Permutado < Coste_Mejor_Momento_Actual) {
                        Coste_Mejor_Momento_Actual = Coste_Permutado;
                        swap(Array_Aux, Pos1, Pos2);  // Solo permutamos después de confirmar que es mejor
                        mejor_momento_actual = Array_Aux;
                        Pos1_Tabu = Pos1;
                        Pos2_Tabu = Pos2;
                        Ciudad_Tabu1 = Array_Aux.get(Pos1);
                        Ciudad_Tabu2 = Array_Aux.get(Pos2);
                    }
                }
            }

            Log.append("El coste del mejor momento actual es: "+ Coste_Mejor_Momento_Actual +"\n");

            // Comprobamos si hay un vecino mejor que la solución actual
            if (Coste_Mejor_Momento_Actual < Mejor_Actual) {
                solucion_actual.clear();
                solucion_actual.addAll(mejor_momento_actual);

                Mejor_Actual = Coste_Mejor_Momento_Actual;

                if (Coste_Mejor_Momento_Actual < Mejor_Coste_Global) {
                    Mejor_Coste_Global = Mejor_Actual;
                    Mejor_Global = new ArrayList<>( solucion_actual );
                }

                if (Mejor_Actual < Coste_Mejor_Momento_Anterior) {
                    Coste_Mejor_Momento_Anterior = Mejor_Actual;
                    Empeoramientos = 0;  // Resetear si hubo una mejora
                } else {
                    Empeoramientos++;
                }

            } else {
                // Solo incrementar el contador de empeoramientos si no hemos mejorado
                Empeoramientos++;
                Mejor_Actual = Coste_Mejor_Momento_Actual;
                solucion_actual.clear();
                solucion_actual.addAll(mejor_momento_actual);
            }

            Actualizar_Memorias(Memoria, Tam, solucion_actual, Lista_Tabu, Pos1_Tabu, Pos2_Tabu, Ciudad_Tabu1, Ciudad_Tabu2);

            if (Empeoramientos == Estanca) {
                Empeoramientos = 0;
                Log.append("Estancamiento, se reinicia la busqueda en otro punto\n");
                //System.out.println("Estancamiento");
                int Num_Aleatorio = Main.random.nextInt(101);
                if (Num_Aleatorio <= Oscilacion*100){
                    //System.out.println("Diversifica");
                    Log.append("Se opta por Diversificar\n");
                    Array_Aux = new ArrayList<>(Tam);
                    Utils.menosVisitados(Memoria, Array_Aux, Tam);
                }else{
                    //System.out.println("Intensifica");
                    Log.append("Se opta por Intensificar\n");
                    Array_Aux = new ArrayList<>(Tam);
                    Utils.masVisitados(Memoria, Array_Aux, Tam);
                }
                solucion_actual.clear();
                solucion_actual.addAll(Array_Aux);
                Mejor_Actual = Utils.Calculo_Coste(solucion_actual, Matriz_Distancias, Tam);
                Coste_Mejor_Momento_Anterior = Mejor_Actual;

                if (Mejor_Actual < Mejor_Coste_Global) {
                    Mejor_Coste_Global = Mejor_Actual;
                    Mejor_Global = new ArrayList<>(solucion_actual);
                }
                Lista_Tabu.clear();
                for (int i = 0; i < Tenencia; i++) {
                    Lista_Tabu.add(new Utils.Pair_Tabu(-1, -1, -1, -1));
                }
            }

            Iteracion++;

            // Calculamos el nuevo entorno si es necesario
            if (Iteracion % Reducir == 0) {
                Tam_Vecindario = (int) (Tam_Vecindario * (1 - Reduccion));
            }
            Log.append("\n");
            Archivo.write(Log.toString());
            Log.delete(0, Log.length());
        }

        solucion_actual.clear();
        solucion_actual.addAll(Mejor_Global);
        Log.append("La mejor solucion final es: "+ Mejor_Coste_Global + "\n");
    }

    static void Actualizar_Memorias(int[][] Memoria, int Tam, ArrayList<Integer> solucion_actual, LinkedList<Utils.Pair_Tabu> Lista_Tabu, int Pos1_Tabu, int Pos2_Tabu, int Ciudad_Tabu1, int Ciudad_Tabu2) {
        int Pos_Ciudad1, Pos_Ciudad2;

        // Actualizar la memoria de visitas
        for (int i = 0; i < Tam - 1; ++i) {
            Pos_Ciudad1 = solucion_actual.get(i)-1;   // Convertir a índice de 0
            Pos_Ciudad2 = solucion_actual.get(i + 1)-1; // Convertir a índice de 0

            // Actualizamos la memoria con los pares de ciudades visitadas
            Memoria[Pos_Ciudad1][Pos_Ciudad2] ++;
            Memoria[Pos_Ciudad2][Pos_Ciudad1] ++;
        }

        // Último arco, conectando el último con el primero (ciclo cerrado)
        Pos_Ciudad1 = solucion_actual.get(Tam - 1)-1; // Convertir a índice de 0
        Pos_Ciudad2 = solucion_actual.get(0)-1; // Convertir a índice de 0
        Memoria[Pos_Ciudad1][Pos_Ciudad2] ++;
        Memoria[Pos_Ciudad2][Pos_Ciudad1] ++;

        // ACTUALIZAR MEMORIA TABU
        // Siempre se deben insertar los pares ordenados en la lista Tabu
        if (Pos1_Tabu > Pos2_Tabu) {
            Intercambiar(Pos1_Tabu,Pos2_Tabu);
        }
        if (Ciudad_Tabu1 > Ciudad_Tabu2) {
            Intercambiar(Ciudad_Tabu1,Ciudad_Tabu2);
        }

        // Insertar en la lista Tabu
        Lista_Tabu.add(new Utils.Pair_Tabu(Ciudad_Tabu1, Ciudad_Tabu2, Pos1_Tabu, Pos2_Tabu));
        Lista_Tabu.removeFirst(); // Eliminar el más antiguo si se excede el tamaño de la lista
    }


    /**
     * Método que intercambia dos
     * @param Pos1
     * @param Pos2
     */
    static void Intercambiar(int Pos1, int Pos2){
        int aux = Pos1;
        Pos1 = Pos2;
        Pos2 = aux;
    }

}