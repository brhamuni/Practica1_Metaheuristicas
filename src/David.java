import java.io.IOException;
import java.util.*;

import static java.util.Collections.swap;

public class David {
    static class Par {
        int c1, c2, p1, p2;

        public Par(int c1_, int c2_, int p1_, int p2_) {
            this.c1 = c1_;
            this.c2 = c2_;
            this.p1 = p1_;
            this.p2 = p2_;
        }

        // Será un movimiento tabú si las ciudades o posiciones coinciden
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Par p = (Par) obj;
            return (c1 == p.c1 && c2 == p.c2) || (p1 == p.p1 && p2 == p.p2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(c1, c2, p1, p2);
        }
    }

    public class BTabu {

        // Función para encontrar los más visitados
        public static void masVisitados(ArrayList<ArrayList<Integer>> mat, ArrayList<Integer> nuevaSol, int tam) {
            nuevaSol.clear();
            boolean[] marcaf = new boolean[tam];

            // La primera ciudad se elige aleatoriamente
            Random random = new Random();
            int c = random.nextInt(tam);
            nuevaSol.add(c + 1);  // Las ciudades van de 1 a tam
            marcaf[c] = true;

            // Para las restantes tam-1 ciudades
            for (int k = 1; k < tam; k++) {
                long menor = -1;
                for (int i = 0; i < tam; i++) {
                    if (!marcaf[i] && mat.get(nuevaSol.get(k - 1) - 1).get(i) > menor) {
                        menor = mat.get(nuevaSol.get(k - 1) - 1).get(i);
                        c = i;
                    }
                }
                nuevaSol.add(c + 1);  // Convertimos posición a ciudad
                marcaf[c] = true;
            }
        }

        // Función para encontrar los menos visitados
        public static void menosVisitados(ArrayList<ArrayList<Integer>> mat, ArrayList<Integer> nuevaSol, int tam) {
            nuevaSol.clear();
            boolean[] marcaf = new boolean[tam];

            Random random = new Random();
            int c = random.nextInt(tam);
            nuevaSol.add(c + 1);  // Las ciudades van de 1 a tam
            marcaf[c] = true;

            for (int k = 1; k < tam; k++) {
                long mayor = Long.MAX_VALUE;
                for (int i = 0; i < tam; i++) {
                    if (!marcaf[i] && mat.get(nuevaSol.get(k - 1) - 1).get(i) < mayor) {
                        mayor = mat.get(nuevaSol.get(k - 1) - 1).get(i);
                        c = i;
                    }
                }
                nuevaSol.add(c + 1);  // Convertimos posición a ciudad
                marcaf[c] = true;
            }
        }

        // Función principal de Búsqueda Tabú
        public static void BTabu(ArrayList<Integer> SolActual, int n, double[][] mat,
                                 long iteraciones, float aplica, float entorno,
                                 float descenso, float empeora, float oscila, int tenencia) throws IOException {

            Greedy_Aleatorio.GreedyAleatorio(SolActual,n,mat);
            ArrayList<Integer> mejorVecino = new ArrayList<>();
            ArrayList<Integer> mejorGlobal = new ArrayList<>(SolActual);
            ArrayList<Integer> nuevaSol = new ArrayList<>();
            double CosteActual = Utils.Calculo_Coste(SolActual, mat, n);
            double mejorCosteGlobal = CosteActual;
            double costeMejorVecino;
            double CosteMejorMomentoAnt = 0;

            // Parámetros Tabú
            int cambio = (int) (iteraciones * aplica);
            int tamVecindario = (int) (iteraciones * entorno);
            int estancamiento = (int) (iteraciones * empeora);
            int numEmpeora = 0;
            int p1t = -1, p2t = -1, c1t = -1, c2t = -1;

            // Memoria de frecuencias
            ArrayList<ArrayList<Integer>> memorias = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                memorias.add(new ArrayList<>(Collections.nCopies(n, 0)));
            }

            // Lista Tabú explícita
            LinkedList<Par> lTabu = new LinkedList<>();
            // Inicializa la lista con pares nulos
            for (int i = 0; i < tenencia; i++) {
                lTabu.add(new Par(-1, -1, -1, -1));
            }

            long i = 0;
            while (i < iteraciones) {
                costeMejorVecino = Double.MAX_VALUE;

                // Evaluamos todo el vecindario
                for (int j = 0; j < tamVecindario; j++) {
                    Random random = new Random();
                    int p1 = random.nextInt(n);
                    int p2;
                    do {
                        p2 = random.nextInt(n);
                    } while (p1 == p2);

                    // Verificar si el movimiento es Tabú
                    boolean tabu = false;
                    int p1p = Math.min(p1, p2), p2p = Math.max(p1, p2);
                    int c1c = SolActual.get(p1), c2c = SolActual.get(p2);
                    for (Par par : lTabu) {
                        if (par.equals(new Par(Math.min(c1c, c2c), Math.max(c1c, c2c), p1p, p2p))) {
                            tabu = true;
                            break;
                        }
                    }

                    if (!tabu) {
                        ArrayList<Integer> nuevo = new ArrayList<>(SolActual);
                        double coste = Utils.Factorizacion(nuevo, CosteActual, mat, n, p1, p2);
                        if (coste < costeMejorVecino) {
                            costeMejorVecino = coste;
                            swap(nuevo, p1, p2);
                            mejorVecino = nuevo;
                            p1t = p1;
                            p2t = p2;
                            c1t = nuevo.get(p1);
                            c2t = nuevo.get(p2);
                        }
                    }
                }

                if (costeMejorVecino < CosteActual) {
                    i++;
                    // Aquí mantenemos la referencia de SolActual
                    SolActual.clear();
                    SolActual.addAll(mejorVecino);
                    CosteActual = costeMejorVecino;
                    if (costeMejorVecino < mejorCosteGlobal) {
                        mejorCosteGlobal = CosteActual;
                        mejorGlobal = new ArrayList<>(SolActual);
                    }
                    if (CosteMejorMomentoAnt > CosteActual) {
                        numEmpeora = 0;
                        CosteMejorMomentoAnt = CosteActual;
                    } else {
                        numEmpeora++;
                    }
                } else {
                    i++;
                    numEmpeora++;
                    // Aquí mantenemos la referencia de SolActual
                    SolActual.clear();
                    SolActual.addAll(mejorVecino);
                    CosteActual = costeMejorVecino;
                }

                // Actualiza memoria de frecuencias
                for (int k = 0; k < n - 1; k++) {
                    int c1 = SolActual.get(k) - 1;
                    int c2 = SolActual.get(k + 1) - 1;
                    memorias.get(c1).set(c2, memorias.get(c1).get(c2) + 1);
                    memorias.get(c2).set(c1, memorias.get(c2).get(c1) + 1);
                }
                memorias.get(SolActual.get(n - 1) - 1).set(SolActual.get(0) - 1,
                        memorias.get(SolActual.get(n - 1) - 1).get(SolActual.get(0) - 1) + 1);

                // Actualiza la lista Tabú
                if (p1t > p2t) {
                    int temp = p1t;
                    p1t = p2t;
                    p2t = temp;
                }
                if (c1t > c2t) {
                    int temp = c1t;
                    c1t = c2t;
                    c2t = temp;
                }
                lTabu.add(new Par(c1t, c2t, p1t, p2t));
                lTabu.removeFirst();

                // Reinicialización si llegamos al límite de empeoramiento
                if (numEmpeora == estancamiento) {
                    if (i % aplica == 0) {
                        Random r = new Random();
                        if (r.nextFloat() < oscila) {
                            masVisitados(memorias, nuevaSol, n);
                        } else {
                            menosVisitados(memorias, nuevaSol, n);
                        }
                        SolActual.clear();
                        SolActual.addAll(nuevaSol);
                    }
                }
            }

            // Devolvemos la mejor solución encontrada
            SolActual.clear();
            SolActual.addAll(mejorGlobal);
        }
    }
}