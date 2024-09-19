import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LectorDatos {

    private final String ruta;
    private final ArrayList<ArrayList<Double>> Matriz_Ciudades;
    private final ArrayList<ArrayList<Double>> Matriz_Distancias;
    
    public LectorDatos(String ruta) {
        this.ruta = ruta;
        
        String linea = null; FileReader f = null;
        
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
        
        while(!linea.split(":")[0].equals("DIMENSION")){
            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int Tamanio_Fichero = Integer.parseInt(linea.split(":")[1].replace(" ", ""));

        Matriz_Ciudades = new ArrayList<>();
        for(int i=0; i<Tamanio_Fichero ; ++i){
            Matriz_Ciudades.add(new ArrayList<>());
        }
            
        try {
            linea = b.readLine();
        } catch (IOException ex) {
            Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        while(linea.split(" ").length != 3){
            try {
                linea = b.readLine().trim();
            } catch (IOException ex) {
                Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        while(!linea.equals("EOF")){
            int i=0;
            linea = linea.trim();
            while(i<linea.length()){
                if (linea.charAt(i)== ' ' && linea.charAt(i+1) == ' '){
                    linea = linea.substring(0,i)+linea.substring(i+1, linea.length());
                    i--;
                }
                i++;
            }

            i = 0;

            String[] split = linea.split(" ");

            //Coordenadas X
            Matriz_Ciudades.get(Integer.parseInt(split[0]) - 1).add(/*i,*/ Double.parseDouble(split[1]));
            //Coordenadas Y
            Matriz_Ciudades.get(Integer.parseInt(split[0]) - 1).add(/*i,*/ Double.parseDouble(split[2]));
            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Matriz_Distancias = new ArrayList<>();
        for(int i=0; i< Tamanio_Fichero; ++i){
            Matriz_Distancias.add(new ArrayList<>(Tamanio_Fichero));

        }

        for(int i=0; i<Tamanio_Fichero; i++){
            for(int j=i;j<Tamanio_Fichero; j++){
                if(i == j){
                    Matriz_Distancias.get(i).add(Double.POSITIVE_INFINITY);
                }else{
                    Matriz_Distancias.get(i).add(j, Math.sqrt( Math.pow(Matriz_Ciudades.get(i).get(0) - Matriz_Ciudades.get(j).get(0), 2) + Math.pow(Matriz_Ciudades.get(i).get(1) - Matriz_Ciudades.get(j).get(1), 2)));
                    Matriz_Distancias.get(j).add(i, Math.sqrt( Math.pow(Matriz_Ciudades.get(i).get(0) - Matriz_Ciudades.get(j).get(0), 2) + Math.pow(Matriz_Ciudades.get(i).get(1) - Matriz_Ciudades.get(j).get(1), 2)));
                }
            }
        }
        
    }

    public String getRuta() { return ruta; }

    public ArrayList<ArrayList<Double>> getMatriz_Ciudades() { return Matriz_Ciudades; }

    public ArrayList<ArrayList<Double>> getMatriz_Distancias() { return Matriz_Distancias; }
    
}