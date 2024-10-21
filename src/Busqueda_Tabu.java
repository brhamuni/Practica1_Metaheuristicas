import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import static java.util.Collections.swap;

public class Busqueda_Tabu {
    public static void Busqueda_Tabu(ArrayList<Integer> solucion_actual, int Tam, final double[][] Matriz_Distancias, long Iteraciones, float Porcentaje_Interacciones, float Entorno, float Reduccion, float Estancamiento, int Tenencia, float Oscilacion, FileWriter Archivo, StringBuilder Log) throws IOException {

        solucion_actual.clear();
        Greedy_Aleatorio.GreedyAleatorio(solucion_actual,Tam,Matriz_Distancias,Archivo,Log);
        ArrayList<Integer> Array_Aux = null;
        ArrayList<Integer> mejor_momento_actual = new ArrayList<>(solucion_actual);
        ArrayList<Integer> Mejor_Global = new ArrayList<>(solucion_actual);

        double Mejor_Actual = Utils.Calculo_Coste(solucion_actual, Matriz_Distancias, Tam);
        double Coste_Mejor_Momento_Actual;
        int Iteracion = 0;
        int Reducir = (int) (Iteraciones * Porcentaje_Interacciones);
        int Tam_Vecindario = (int) (Iteraciones * Entorno);
        int Estanca = (int) (Iteraciones * Estancamiento);
        double Coste_Mejor_Momento_Anterior = Double.MAX_VALUE;
        double Mejor_Coste_Global = Mejor_Actual;
        int Empeoramientos = 0;

        ArrayList<ArrayList<Integer>> Memoria = new ArrayList<>(Collections.nCopies(Tam, new ArrayList<>(Collections.nCopies(Tam,0))));
        Integer Pos1_Tabu=-1, Pos2_Tabu=-1, Ciudad_Tabu1=-1, Ciudad_Tabu2=-1;
        LinkedList<Utils.Pair_Tabu> Lista_Tabu = new LinkedList<>();
        for (int i = 0; i < Tenencia; i++) {
            Lista_Tabu.add(new Utils.Pair_Tabu(-1, -1, -1, -1));
        }

        while (Iteracion < Iteraciones) {
            Log.append("El mejor coste global es: "+ Mejor_Coste_Global +"\n");
            Log.append("El coste del mejor momento anterior es: "+ Coste_Mejor_Momento_Anterior +"\n");
            Coste_Mejor_Momento_Actual = Double.MAX_VALUE;

            // Evaluamos el vecindario
            for (int i = 0; i < Tam_Vecindario; i++) {
                int Pos1 = Main.random.nextInt(Tam);
                int Pos2;
                do {
                    Pos2 = Main.random.nextInt(Tam);
                } while (Pos1 == Pos2);

                boolean Es_Tabu = false;

                // Aseguramos que Pos1 < Pos2 y Ciudad1 < Ciudad2 para comparación
                int Pos1_Nueva = Pos1, Pos2_Nueva = Pos2, Ciudad1_Nueva = solucion_actual.get(Pos1)-1, Ciudad2_Nueva = solucion_actual.get(Pos2)-1;

                if (Pos1_Nueva > Pos2_Nueva) {
                    int aux = Pos1_Nueva;
                    Pos1_Nueva = Pos2_Nueva;
                    Pos2_Nueva = aux;
                }
                if (Ciudad1_Nueva > Ciudad2_Nueva) {
                    int aux = Ciudad1_Nueva;
                    Ciudad1_Nueva = Ciudad2_Nueva;
                    Ciudad2_Nueva = aux;
                }

                for (Utils.Pair_Tabu tabu : Lista_Tabu) {
                    if (Utils.Pair_Tabu.iguales(new Utils.Pair_Tabu(Ciudad1_Nueva, Ciudad2_Nueva, Pos1_Nueva, Pos2_Nueva), tabu)) {
                        Es_Tabu = true;  // El movimiento es Tabú
                        break;
                    }
                }

                if (!Es_Tabu) {
                    Array_Aux = new ArrayList<>(solucion_actual);
                    double Coste_Permutado = Utils.Factorizacion(Array_Aux, Mejor_Actual, Matriz_Distancias, Tam, Pos1, Pos2);

                    // Verificar que el coste no es negativo ni incorrecto
                    if (Coste_Permutado < Coste_Mejor_Momento_Actual) {
                        Coste_Mejor_Momento_Actual = Coste_Permutado;
                        swap(Array_Aux, Pos1, Pos2);  // Solo permutamos después de confirmar que es mejor
                        mejor_momento_actual = new ArrayList<>(Array_Aux);
                        Pos1_Tabu = Pos1;
                        Pos2_Tabu = Pos2;
                        Ciudad_Tabu1 = Array_Aux.get(Pos1) + 1;
                        Ciudad_Tabu2 = Array_Aux.get(Pos2) + 1;
                    }
                }
            }

            Log.append("El coste del mejor momento actual es: "+ Coste_Mejor_Momento_Actual +"\n");

            // Comprobamos si hay un vecino mejor que la solución actual
            if (Coste_Mejor_Momento_Actual < Mejor_Actual) { // Verificamos que sea válido
                solucion_actual.clear();
                solucion_actual.addAll(mejor_momento_actual);
                Mejor_Actual = Coste_Mejor_Momento_Actual;

                if (Coste_Mejor_Momento_Actual < Mejor_Coste_Global) {
                    Mejor_Coste_Global = Coste_Mejor_Momento_Actual;
                    Mejor_Global = new ArrayList<>(solucion_actual);
                }

                if (Coste_Mejor_Momento_Actual < Coste_Mejor_Momento_Anterior) {
                    Coste_Mejor_Momento_Anterior = Coste_Mejor_Momento_Actual;
                    Empeoramientos = 0;
                } else {
                    Empeoramientos++;
                }
            } else {
                Empeoramientos++;
                solucion_actual.clear();
                solucion_actual.addAll(mejor_momento_actual);
            }

            Actualizar_Memorias(Memoria, Tam, solucion_actual, Lista_Tabu, Pos1_Tabu, Pos2_Tabu, Ciudad_Tabu1, Ciudad_Tabu2);

            if (Empeoramientos == Estanca) {
                Empeoramientos = 0;
                Log.append("Estancamiento, se reinicia la busqueda\n");
                System.out.println("Estancamiento");
                int Estrategia = new Random().nextInt(101);
                if (Estrategia <= Oscilacion * 100) {/*
                    Utils.menosVisitados(Memoria, Array_Aux, Tam);
                    Log.append("Se opta por Diversificar\n");*/
                    Utils.menosVisitados(Memoria, Array_Aux, Tam);
                    Log.append("Se opta por Intensificar\n");
                } else {
                    Utils.masVisitados(Memoria, Array_Aux, Tam);
                    Log.append("Se opta por Intensificar\n");
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

    static void Actualizar_Memorias(ArrayList<ArrayList<Integer>> Memoria, int Tam, ArrayList<Integer> solucion_actual, LinkedList<Utils.Pair_Tabu> Lista_Tabu, int Pos1_Tabu, int Pos2_Tabu, int Ciudad_Tabu1, int Ciudad_Tabu2) {
        int Pos_Ciudad1, Pos_Ciudad2;

        // Actualizar la memoria de visitas
        for (int i = 0; i < Tam - 1; i++) {
            Pos_Ciudad1 = solucion_actual.get(i) - 1;   // Convertir a índice de 0
            Pos_Ciudad2 = solucion_actual.get(i + 1) - 1; // Convertir a índice de 0

            // Actualizamos la memoria con los pares de ciudades visitadas
            Memoria.get(Pos_Ciudad1).set(Pos_Ciudad2, Memoria.get(Pos_Ciudad1).get(Pos_Ciudad2) + 1);
            Memoria.get(Pos_Ciudad2).set(Pos_Ciudad1, Memoria.get(Pos_Ciudad2).get(Pos_Ciudad1) + 1);
        }

        // Último arco, conectando el último con el primero (ciclo cerrado)
        Pos_Ciudad1 = solucion_actual.get(Tam - 1) - 1; // Convertir a índice de 0
        Pos_Ciudad2 = solucion_actual.get(0) - 1; // Convertir a índice de 0
        Memoria.get(Pos_Ciudad1).set(Pos_Ciudad2, Memoria.get(Pos_Ciudad1).get(Pos_Ciudad2) + 1);
        Memoria.get(Pos_Ciudad2).set(Pos_Ciudad1, Memoria.get(Pos_Ciudad2).get(Pos_Ciudad1) + 1);


        // ACTUALIZAR MEMORIA TABU
        // Siempre se deben insertar los pares ordenados en la lista Tabu
        if (Pos1_Tabu > Pos2_Tabu) {
            int aux = Pos1_Tabu;
            Pos1_Tabu = Pos2_Tabu;
            Pos2_Tabu = aux;
        }
        if (Ciudad_Tabu1 > Ciudad_Tabu2) {
            int aux = Ciudad_Tabu1;
            Ciudad_Tabu1 = Ciudad_Tabu2;
            Ciudad_Tabu2 = aux;
        }

        // Insertar en la lista Tabu
        Lista_Tabu.add(new Utils.Pair_Tabu(Ciudad_Tabu1, Ciudad_Tabu2, Pos1_Tabu, Pos2_Tabu));

        // Sacar el primer elemento de la lista Tabu si es necesario (en caso de que haya un límite en el tamaño)
        // Este comportamiento puede depender de la implementación, así que ajusta esta parte si es necesario
        if (Lista_Tabu.size() > 10) { // Supongamos que el tamaño máximo es 10, ajusta según tu caso
            Lista_Tabu.removeFirst(); // Eliminar el más antiguo si se excede el tamaño de la lista
        }
    }

}