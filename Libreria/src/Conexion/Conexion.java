package Conexion;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
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
	 * @param host
	 *            ip del servidor
	 * @param puerto
	 *            puerto de escucha del servidor
	 * @throws IOException
	 *             en caso de no poder abrir el flujo de datos
	 */
	public Conexion(String host, int puerto) {

		try {
			InetAddress addr = InetAddress.getByName(host);
			SocketAddress sockaddr = new InetSocketAddress(addr,puerto);
			this.conexionClienteServidor = new Socket();
			this.conexionClienteServidor.connect(sockaddr, 3000);
		} catch (Exception e) {

		} 
		if (conexionClienteServidor != null) {
			try {
				abrirFlujo();
			} catch (IOException e) {

			} catch (NullPointerException e) {

			}
		}
	}

	public Conexion(Inet4Address ip, int puerto) throws IOException {
		this.conexionClienteServidor = new Socket(ip, puerto);
		abrirFlujo();
	}

	/**
	 * Crea una conexión cliente - servidor
	 * 
	 * @param conexionClienteServidor
	 *            socket de conexión cliente - servidor
	 */
	public Conexion(Socket conexionClienteServidor) {
		this.conexionClienteServidor = conexionClienteServidor;
		try {
			abrirFlujo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void abrirFlujo() throws IOException, NullPointerException {
		InputStreamReader lectorFlujo;
		this.flujoEscritor = new DataOutputStream(
				this.conexionClienteServidor.getOutputStream());
		lectorFlujo = new InputStreamReader(
				this.conexionClienteServidor.getInputStream());
		this.lector = new BufferedReader(lectorFlujo);
	}

	/**
	 * Cierra los flujos de datos y la conexión
	 * 
	 * @throws IOException
	 *             en caso de no poder realizar la operación de cierre
	 */
	public void cerrarConexion() throws IOException {
		try {
		this.flujoEscritor.close();
		this.lector.close();
		this.conexionClienteServidor.close();
		} catch (NullPointerException e){
			
		}
	}

	/**
	 * Lee un mensaje del flujo de datos
	 * 
	 * @return mensaje leído del flujo de datos
	 */
	public String leerMensaje() {
		if (conexionClienteServidor != null) {
			String mensaje = "";
			String linea = "";
			try {
				if (lector.ready()) {
					if ((linea = lector.readLine()) != null) {
						mensaje += linea;
					}
				}
			} catch (NullPointerException ex) {
				return null;
			} catch (IOException ex) {

			}
			return mensaje;
		} else {
			return null;
		}
	}

	/**
	 * Pasa el mensaje a bytes en formato UTF-8 y lo escribe en el flujo de
	 * datos
	 * 
	 * @param mensaje
	 *            String a enviar
	 */
	public void escribirMensaje(String mensaje) {
		if (conexionClienteServidor != null) {
			try {
				byte[] mensajeBytes = (mensaje + "\n").getBytes("UTF-8");
				flujoEscritor.write(mensajeBytes);
				flujoEscritor.flush();
			} catch (IOException ex) {


			} catch (NullPointerException ex) {

			}
		}
	}
	
	public static boolean hacerPing(String ip){
		boolean retorno = false;
		InetSocketAddress host = new InetSocketAddress(ip, 27000);
		retorno = !host.isUnresolved();
		
		return retorno;
	}

}
