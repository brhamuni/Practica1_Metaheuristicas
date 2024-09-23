import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LectorDatos {

    private final String Ruta_Archivo;
    private final double[][] Matriz_Ciudades;
    private final double[][] Matriz_Distancias;

    public LectorDatos(String ruta) {
        this.Ruta_Archivo = ruta;
        String Linea_Archivo = null; FileReader f = null; BufferedReader Archivo = null;

        try{
            f = new FileReader(ruta);
            Archivo = new BufferedReader(f);
            Linea_Archivo = Archivo.readLine();
        }catch (IOException ex) {
            Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Bucle para conocer el tamaño del archivo.
        while ( !Linea_Archivo.split(":")[0].equals("DIMENSION") ){
           Linea_Archivo = Avanzar_Linea(Archivo);
        }
        int Tamanio_Fichero = Integer.parseInt(Linea_Archivo.split(":")[1].trim());

        // Inicializar las matrices de ciudades y distancias
        Matriz_Ciudades = new double[Tamanio_Fichero][2];
        Matriz_Distancias = new double[Tamanio_Fichero][Tamanio_Fichero];

        // Leer las coordenadas hasta encontrar una línea con 3 elementos (número de ciudad, X, Y)
        while( Linea_Archivo.split(" ").length != 3 ){
            Linea_Archivo = Avanzar_Linea(Archivo).trim().replaceAll("\\s+", " ");
        }

        // Procesar las coordenadas de las ciudades hasta encontrar "EOF"
        while ( !Linea_Archivo.equals("EOF") ){
            String[] split = Linea_Archivo.split(" ");
            int ciudadIndex = Integer.parseInt(split[0]) - 1;
            Matriz_Ciudades[ciudadIndex][0] = Double.parseDouble(split[1]); // Coordenada X
            Matriz_Ciudades[ciudadIndex][1] = Double.parseDouble(split[2]); // Coordenada Y
            Linea_Archivo = Avanzar_Linea(Archivo).trim().replaceAll("\\s+", " ");
        }

        // Calcular las distancias euclidianas entre las ciudades
        for (int i = 0; i < Tamanio_Fichero; ++i) {
            for (int j = i; j < Tamanio_Fichero;++j) {
                if (i == j) {
                    Matriz_Distancias[i][j] = Double.POSITIVE_INFINITY; // Distancia de una ciudad consigo misma
                } else {
                    double distancia = Math.sqrt(
                            Math.pow(Matriz_Ciudades[i][0] - Matriz_Ciudades[j][0], 2) + // Diferencia en X
                                    Math.pow(Matriz_Ciudades[i][1] - Matriz_Ciudades[j][1], 2));   // Diferencia en Y);
                    Matriz_Distancias[i][j] = distancia;
                    Matriz_Distancias[j][i] = distancia; // La matriz es simétrica
                }
            }
        }
    }

    public double[][] getMatriz_Distancias() {
        return Matriz_Distancias;
    }

    /**
     * Método para avanzar una línea dentro del archivo que se esta leyendo.
     * @param Archivo Un objeto BufferedReader que representa el archivo del que se desea leer.
     * @return La línea leída del archivo. Si se alcanza el final del archivo, retorna null.
     */
    private String Avanzar_Linea(BufferedReader Archivo){
        String Archivo_Linea = "";
        try {
            Archivo_Linea = Archivo.readLine();
        } catch (IOException ex) {
            Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Archivo_Linea;
    }
}