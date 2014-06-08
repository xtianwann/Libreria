package Conexion;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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
	 * Constructor: crea una conexión cliente - servidor
	 * 
	 * @param host [String] ip del servidor
	 * @param puerto [int] puerto de escucha del servidor
	 * @throws IOException excepción en caso de no poder abrir el flujo de datos
	 */
	public Conexion(String host, int puerto) throws IOException, NullPointerException {

		InetAddress addr = InetAddress.getByName(host);
		SocketAddress sockaddr = new InetSocketAddress(addr, puerto);
		this.conexionClienteServidor = new Socket();
		this.conexionClienteServidor.connect(sockaddr, 2700);
		if (conexionClienteServidor != null) {
			abrirFlujo();
		}
	}

	/**
	 * Constructor: crea una conexión cliente servidor
	 * 
	 * @param ip [Inet4Address] ip por la que se está estableciendo al conexión
	 * @param puerto [int] puerto por el se está estableciendo la conexión
	 * @throws IOException excepción lanzada en caso de no poder abrir el flujo de datos
	 * @throws NullPointerException excepción lanzada en caso de no poder establecer conexión con el destino
	 */
	public Conexion(Inet4Address ip, int puerto) throws IOException,NullPointerException {
		this.conexionClienteServidor = new Socket(ip, puerto);
		abrirFlujo();
	}

	/**
	 * Constructor: crea una conexión cliente - servidor
	 * 
	 * @param conexionClienteServidor [Socket] socket de conexión cliente - servidor
	 */
	public Conexion(Socket conexionClienteServidor) throws IOException,NullPointerException {
		this.conexionClienteServidor = conexionClienteServidor;
		System.out.println("Constructor "+conexionClienteServidor.getInetAddress()+" "+conexionClienteServidor.getPort());
		abrirFlujo();
	}

	/**
	 * Permite abrir el flujo de comunicación entre cliente y servidor
	 * 
	 * @throws IOException excepción lanzada en caso de haber algún error durante la entrada o la salida de datos
	 * @throws NullPointerException excepción lanzada en caso de no poder abrir el flujo de conexión
	 */
	private void abrirFlujo() throws IOException, NullPointerException {
		InputStreamReader lectorFlujo;
		this.flujoEscritor = new DataOutputStream(this.conexionClienteServidor.getOutputStream());
		lectorFlujo = new InputStreamReader(this.conexionClienteServidor.getInputStream());
		this.lector = new BufferedReader(lectorFlujo);
	}

	/**
	 * Cierra los flujos de datos y la conexión
	 * 
	 * @throws IOException en caso de no poder realizar la operación de cierre
	 */
	public void cerrarConexion() throws IOException, NullPointerException {
		this.flujoEscritor.close();
		this.lector.close();
		this.conexionClienteServidor.close();
	}

	/**
	 * Lee un mensaje del flujo de datos
	 * 
	 * @return [String] mensaje leído del flujo de datos, null en caso de no poder leer nada
	 */
	public String leerMensaje() throws IOException, NullPointerException {
		if (conexionClienteServidor != null) {
			String mensaje = "";
			String linea = "";
			if (lector.ready()) {
				if ((linea = lector.readLine()) != null) {
					mensaje += linea;
				}
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
	public void escribirMensaje(String mensaje) throws IOException,
			NullPointerException {
			byte[] mensajeBytes = (mensaje + "\n").getBytes("UTF-8");
			flujoEscritor.write(mensajeBytes);
			flujoEscritor.flush();
	}

	/**
	 * Método estático que permite comprobar si se puede encontrar una ip pasada por parámetro
	 * 
	 * @param ip [String] ip a la que se va a a hacer ping
	 * @return [boolean] true en caso de que la operación haya sido satisfactoria, false en caso contrario
	 */
	public static boolean hacerPing(String ip) {
		boolean retorno = false;
		InetSocketAddress host = new InetSocketAddress(ip, 27000);
		retorno = !host.isUnresolved();

		return retorno;
	}

}
