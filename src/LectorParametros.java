import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LectorParametros {
    private final String ruta;
    private Integer nFicheros;
    private Integer numeroK;
    private Integer nSemillas;
    private String[] rutas;
    private Integer[] semillas;
    private Integer Iteraciones;
    private Float Porcentaje_Interacciones;
    private Float Entorno;
    private Float Reduccion;

    public Float getEstancamiento() {
        return Estancamiento;
    }

    private Float Estancamiento;

    public LectorParametros(String ruta) {
        this.ruta=ruta;
        this.nSemillas=0;
        this.nFicheros=0;
        String Linea_Archivo = null; FileReader f = null; BufferedReader Archivo = null;

        try{
            f = new FileReader(ruta);
            Archivo = new BufferedReader(f);
            Linea_Archivo = Avanzar_Linea(Archivo);
        }catch (IOException ex) {
            Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] split;
        split = Linea_Archivo.split(" ");

        this.nFicheros = Integer.parseInt(split[1]);

        this.rutas = new String[this.nFicheros];
        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");

        for (int i = 0; i < nFicheros; i++) {
            this.rutas[i] = split[i];
        }

        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");

        nSemillas = Integer.parseInt(split[1]);

        this.semillas = new Integer[nSemillas];
        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");

        for (int i = 0; i < nSemillas; i++) {
            this.semillas[i]=Integer.parseInt(split[i]);
        }

        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");

        this.numeroK = Integer.parseInt(split[1]);

        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");
        this.Iteraciones = Integer.parseInt(split[1]);

        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");
        this.Porcentaje_Interacciones = Float.parseFloat(split[1]);

        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");
        this.Entorno = Float.parseFloat(split[1]);

        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");
        this.Reduccion = Float.parseFloat(split[1]);

        Linea_Archivo = Avanzar_Linea(Archivo);
        split = Linea_Archivo.split(" ");
        this.Estancamiento = Float.parseFloat(split[1]);

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

    public Integer getIteraciones() { return Iteraciones; }
    public Float getEntorno() { return Entorno; }
    public Float getPorcentaje_Interacciones() { return Porcentaje_Interacciones; }
    public Float getReduccion() { return Reduccion; }
    public Integer getnFicheros() { return nFicheros; }
    public Integer getNumeroK() { return numeroK; }
    public Integer getnSemillas() { return nSemillas; }
    public String[] getRutas() { return rutas; }
    public Integer[] getSemillas() { return semillas; }
}