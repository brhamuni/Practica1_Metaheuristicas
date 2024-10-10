import java.util.ArrayList;

public class Utils {

    /**
     * Método que calcula el coste total de una solución apartir de una matriz de distancias.
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
     * Método que calcula el coste despues de aplicar 2-opt para la Busqueda Local.
     * @param Array_Aux Lista auxiliar de enteros utilizada para almacenar elementos relacionados con la factorización.
     * @param Mejor_Coste Número que representa el mejor coste actual obtenido en la operación.
     * @param Matriz_Distancias Matriz de distancias donde cada valor (i,j) es la distancia entre dos elementos.
     * @param Tam Tamaño de la lista auxiliar y de la matriz de distancias.
     * @param Pos1 Primera posición del array a ser modificada en el proceso de factorización.
     * @param Pos2 Segunda posición del array a ser modificada en el proceso de factorización.
     * @return El valor del mejor coste calculado después de realizar la factorización.
     */
    static double Factorizacion(ArrayList<Integer> Array_Aux, Double Mejor_Coste, final double[][] Matriz_Distancias, int Tam, int Pos1, int Pos2) {
        // Variables para almacenar los costes antes y después del intercambio
        double Coste_Anterior = 0.0;
        double Coste_Despues = 0.0;

        int Ciudad_Pos1 = Array_Aux.get(Pos1)-1;
        int Ciudad_Pos2 = Array_Aux.get(Pos2)-1;

        // 1. Calcular el coste de los enlaces antes de la permutacion

        // Calcular el enlace antes de Ciudad_Pos1
        if (Pos1 == 0) { //En el caso que sea la primera, unimos con el final
            Coste_Anterior += Matriz_Distancias[Array_Aux.getLast()-1][Ciudad_Pos1];
        } else {
            Coste_Anterior += Matriz_Distancias[Array_Aux.get(Pos1 - 1)-1][Ciudad_Pos1];
        }

        // Calcular el enlace que sigue a Ciudad_Pos1 sino es Pos2
        if (Pos1 + 1 != Pos2) {
            if (Pos1 == Tam - 1) { //Si es la ultima unimos con la primera
                Coste_Anterior += Matriz_Distancias[Ciudad_Pos1][Array_Aux.getFirst() - 1];
            } else { //Sino es la ultima, conectamos por el medio
                Coste_Anterior += Matriz_Distancias[Ciudad_Pos1][Array_Aux.get(Pos1 + 1) - 1];
            }
        }

        // Calcular el enlace que sigue a Ciudad_Pos2
        if (Pos2 == Tam - 1) { //Si es el ultimo conectamos con el primero
            Coste_Anterior += Matriz_Distancias[Ciudad_Pos2][Array_Aux.getFirst()-1]; // Ciudad en el extremo derecho, conecta con la primera
        } else { //Sino conectamos con el medio
            Coste_Anterior += Matriz_Distancias[Ciudad_Pos2][Array_Aux.get(Pos2 + 1)-1];
        }

        // Calcular el enlace antes de Ciudad_Pos2 si no es Pos1
        if (Pos2 - 1 != Pos1) { //Sino es Pos1, si lo es ya estaria conectado y no hacemos nada
            //calculamos con mod por si es el ultimo
            Coste_Anterior += Matriz_Distancias[Array_Aux.get((Pos2 - 1 + Tam) % Tam)-1][Ciudad_Pos2];
        }

        // Calculamos el coste de los enlaces después de la permutacion

        // Calcular el enlace antes de Ciudad_Pos2 que ahora seria Pos1
        if (Pos1 == 0) { //Si es el primero unimos con el ultimo
            Coste_Despues += Matriz_Distancias[Array_Aux.getLast()-1][Ciudad_Pos2];
        } else { //Sino unimos con el anterior
            Coste_Despues += Matriz_Distancias[Array_Aux.get(Pos1 - 1)-1][Ciudad_Pos2];
        }

        // Calcular el enlace que sigue a Ciudad_Pos2 si no es Pos2
        if (Pos1 + 1 != Pos2) {
            if (Pos1 == Tam - 1) {  //Si es la ultima unimos por el principio
                Coste_Despues += Matriz_Distancias[Ciudad_Pos2][Array_Aux.getFirst() - 1];
            } else { //Sino unimos por el medio
                Coste_Despues += Matriz_Distancias[Ciudad_Pos2][Array_Aux.get(Pos1 + 1) - 1];
            }
        }

        // Calcular el enlace que sigue a Ciudad_Pos1 ahora seria Pos2
        if (Pos2 == Tam - 1) { //Si es el ultimo conectamos con el primero
            Coste_Despues += Matriz_Distancias[Ciudad_Pos1][Array_Aux.getFirst()-1];
        } else { //Sino es el ultimo unimos por el medio
            Coste_Despues += Matriz_Distancias[Ciudad_Pos1][Array_Aux.get(Pos2 + 1)-1];
        }

        // Calcular el enlace que precede a Ciudad_Pos1 si no es Pos1
        if (Pos2 - 1 != Pos1) { //Sino es la anterior, si lo fuera ya estaria conectado
            // calculamos con mod por si es el ultimo
            Coste_Despues += Matriz_Distancias[Array_Aux.get((Pos2 - 1 + Tam) % Tam)-1][Ciudad_Pos1];
        }

        // Calculamos el nuevo coste despues de la permutacion
        double Nuevo_Coste = Mejor_Coste-Coste_Anterior+Coste_Despues;
        return Nuevo_Coste;
    }

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

        public Integer getCiudad() { return ciudad; }
        public Double getSuma() { return suma; }


    }
}