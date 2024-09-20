import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LectorDatos {

    private final String ruta;
    private final double[][] Matriz_Ciudades;
    private final double[][] Matriz_Distancias;

    public LectorDatos(String ruta) {
        this.ruta = ruta;

        String linea = null;
        FileReader f = null;

        try {
            f = new FileReader(ruta);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedReader b = new BufferedReader(f);

        try {
            linea = b.readLine();
        } catch (IOException ex) {
            Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Buscar la línea que contiene "DIMENSION"
        while (!linea.split(":")[0].equals("DIMENSION")) {
            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int Tamanio_Fichero = Integer.parseInt(linea.split(":")[1].trim());

        // Inicializar las matrices de ciudades y distancias
        Matriz_Ciudades = new double[Tamanio_Fichero][2];
        Matriz_Distancias = new double[Tamanio_Fichero][Tamanio_Fichero];

        try {
            linea = b.readLine();
        } catch (IOException ex) {
            Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Leer las coordenadas hasta encontrar una línea con 3 elementos (número de ciudad, X, Y)
        while (linea.split(" ").length != 3) {
            try {
                linea = b.readLine().trim().replaceAll("\\s+", " ");
            } catch (IOException ex) {
                Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Procesar las coordenadas de las ciudades hasta encontrar "EOF"
        while (!linea.equals("EOF")) {
            String[] split = linea.split(" ");
            int ciudadIndex = Integer.parseInt(split[0]) - 1;
            Matriz_Ciudades[ciudadIndex][0] = Double.parseDouble(split[1]); // Coordenada X
            Matriz_Ciudades[ciudadIndex][1] = Double.parseDouble(split[2]); // Coordenada Y

            try {
                linea = b.readLine().trim().replaceAll("\\s+", " ");
            } catch (IOException ex) {
                Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Calcular las distancias euclidianas entre las ciudades
        for (int i = 0; i < Tamanio_Fichero; i++) {
            for (int j = i; j < Tamanio_Fichero; j++) {
                if (i == j) {
                    Matriz_Distancias[i][j] = Double.POSITIVE_INFINITY; // Distancia de una ciudad consigo misma
                } else {
                    double distancia = Math.sqrt(
                            Math.pow(Matriz_Ciudades[i][0] - Matriz_Ciudades[j][0], 2) + // Diferencia en X
                                    Math.pow(Matriz_Ciudades[i][1] - Matriz_Ciudades[j][1], 2)   // Diferencia en Y
                    );
                    Matriz_Distancias[i][j] = distancia;
                    Matriz_Distancias[j][i] = distancia; // La matriz es simétrica
                }
            }
        }

        System.out.println("Se construyeron las matrices del archivo: " + ruta + " correctamente.");
    }

    public String getRuta() {
        return ruta;
    }

    public double[][] getMatriz_Ciudades() {
        return Matriz_Ciudades;
    }

    public double[][] getMatriz_Distancias() {
        return Matriz_Distancias;
    }
}
