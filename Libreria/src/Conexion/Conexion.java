package Conexion;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Permite establecer conexiones entre el servidor y los clientes
 *
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Conexion {

    private Socket conexionClienteServidor;
    private DataOutputStream flujoEscritor;
    private BufferedReader lector;

    /**
     * Crea una conexión cliente - servidor
     * 
     * @param host ip del servidor
     * @param puerto puerto de escucha del servidor
     * @throws IOException en caso de no poder abrir el flujo de datos
     */
    public Conexion(String host, int puerto) throws IOException {
        this.conexionClienteServidor = new Socket(host, puerto);
        abrirFlujo();
    }
    
    public Conexion( Inet4Address ip, int puerto ) throws IOException {
        this.conexionClienteServidor = new Socket( ip, puerto );
        abrirFlujo();
    }

    /**
     * Crea una conexión cliente - servidor
     * 
     * @param conexionClienteServidor socket de conexión cliente - servidor
     */
    public Conexion(Socket conexionClienteServidor) {
        this.conexionClienteServidor = conexionClienteServidor;
        abrirFlujo();
    }

    private void abrirFlujo() {
        InputStreamReader lectorFlujo;
        try {
            this.flujoEscritor = new DataOutputStream(this.conexionClienteServidor.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            lectorFlujo = new InputStreamReader(this.conexionClienteServidor.getInputStream());
            this.lector = new BufferedReader(lectorFlujo);
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cierra los flujos de datos y la conexión
     * 
     * @throws IOException en caso de no poder realizar la operación de cierre
     */
    public void cerrarConexion() throws IOException {
        this.flujoEscritor.close();
        this.lector.close();
        this.conexionClienteServidor.close();
    }

    /**
     * Lee un mensaje del flujo de datos
     * 
     * @return mensaje leído del flujo de datos
     */
    public String leerMensaje() {
        String mensaje = "";
        String linea = "";
        try {
            if (lector.ready()) {
                if ((linea = lector.readLine()) != null) {
                    mensaje += linea;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mensaje;
    }
    
    /**
     * Pasa el mensaje a bytes en formato UTF-8 y lo escribe en el flujo de datos
     * 
     * @param mensaje String a enviar
     */
    public void escribirMensaje(String mensaje){
        try {
            byte[] mensajeBytes = (mensaje+"\n").getBytes("UTF-8");
            flujoEscritor.write(mensajeBytes);
            flujoEscritor.flush();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
