import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LectorDatos {
    private final String ruta;
    private final Double ciudades[][];
    private final Double distancias[][];
    
    public LectorDatos(String ruta) {
        this.ruta = ruta;//.split("\\.")[0];
        
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
        
        while(!linea.split(":")[0].equals("DIMENSION")){
            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int tam = Integer.parseInt(linea.split(":")[1].replace(" ", ""));

        ciudades = new Double[tam][2];
            
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
                    linea = linea.substring(0,i)+linea.substring(i+1,linea.length());
                    i--;
                }
                i++;
            }

            i = 0;

            String[] split = linea.split(" ");
            ciudades[Integer.parseInt(split[0]) - 1][i++] = Double.parseDouble(split[1]);
            ciudades[Integer.parseInt(split[0]) - 1][i] = Double.parseDouble(split[2]);
            try {
                linea = b.readLine();
            } catch (IOException ex) {
                Logger.getLogger(LectorDatos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        distancias = new Double[tam][tam];
        

        for(int i=0;i<tam;i++){
            for(int j=i;j<tam;j++){
                if(i == j){
                    distancias[i][j] = Double.POSITIVE_INFINITY;
                }else{
                    distancias[i][j] = distancias[j][i] = Math.sqrt(Math.pow(ciudades[i][0] - ciudades[j][0], 2) + Math.pow(ciudades[i][1] - ciudades[j][1], 2));
                }
            }
        }
        
    }

    public String getRuta() { return ruta; }

    public Double[][] getCiudades() { return ciudades; }

    public Double[][] getDistancias() { return distancias; }
    
}