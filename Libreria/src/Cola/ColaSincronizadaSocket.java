package Cola;

import Excepciones.Cola.ExcepcionColaLlena;
import Excepciones.Cola.ExcepcionColaVacia;
import Excepciones.ExcepcionInesperada;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Clase encargada de la gestión de la cola de sockets para las conexiones.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ColaSincronizadaSocket {
    
    private volatile ArrayList<Socket> listaSocket;
    private boolean ocupado;
    private int maxSize;
    private int intentos;
    
    /**
     * Constructor
     */
    public ColaSincronizadaSocket(){
        inicializar();
        this.maxSize = 0;
        this.intentos = 6;
    }
    
    /**
     * Constructor
     * 
     * @param maxSize [int] tamaño máximo de la cola
     */
    public ColaSincronizadaSocket(int maxSize){
        inicializar();
        this.maxSize = maxSize;
        this.intentos = 6;
    }
    
    /**
     * Constructor
     * 
     * @param maxSize [int] tamaño máximo de la cola
     * @param intentos [int] número de intentos para retirar un socket de la cola
     */
    public ColaSincronizadaSocket(int maxSize, int intentos){
        inicializar();
        this.maxSize = maxSize;
        this.intentos = intentos;
    }
    
    /**
     * Inicializa la cola de sockets
     */
    private void inicializar(){
        this.listaSocket = new ArrayList<>();
        this.ocupado = false;
    }
    
    /**
     * Se encarga de retirar un socket de la cola
     * 
     * @return [Socket] socket que acaba de retirar de la cola
     * @throws TimeoutException exepción lanzada cuando se supera el número de intentos máximo retirando el socket de la cola
     * @throws ExcepcionColaVacia excepción lanzada cuando se intenta retirar un socket y la cola está vacía
     * @throws ExcepcionInesperada excepción lanzada cuando se desconoce el motivo del error
     */
    public Socket getSocket() throws TimeoutException, ExcepcionColaVacia, ExcepcionInesperada{
        Socket socket = null;
        int contador = 0;
        if(listaSocket.isEmpty())
            throw new ExcepcionColaVacia("La cola está vacía");
        while(isOcupado()){
            if(contador > intentos){
                throw new TimeoutException("Tiempo excedido para retirar un socket de la cola");
            }
            contador++;
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        ocupado = true;
        try{
            socket = listaSocket.remove(0);
        } catch(IndexOutOfBoundsException e){
            ocupado = false;
            throw new ExcepcionInesperada("No se puede retirar el socket de la cola aunque estaba libre");
        }
        ocupado = false;
        
        return socket;
    }
    
    /**
     * Añade un socket a la cola
     * 
     * @param socket [Socket] socket a añadir a la cola
     * @throws ExcepcionColaLlena excepción lanzada cuando la cola está llena y no se pueden añadir más sockets
     * @throws TimeoutException excepción lanzada cuando se excede el número máximo de intentos
     * @throws ExcepcionInesperada excepción lanzada cuando se desconoce el motivo del error
     */
    public void addSocket(Socket socket) throws ExcepcionColaLlena, TimeoutException, ExcepcionInesperada{
        int contador = 0;
        if(isListaLimitada() && listaSocket.size() > maxSize)
            throw new ExcepcionColaLlena("La cola está llena");
        while(isOcupado()){
            if(contador > intentos){
                throw new TimeoutException("Tiempo excedido para añadir un socket a la cola");
            }
            contador++;
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        ocupado = true;
        if(!listaSocket.add(socket)){
            ocupado = false;
            throw new ExcepcionInesperada("No se ha podido añadir el socket a la cola aunque estaba libre");
        }
        ocupado = false;
    }
    
    /**
     * Permite obtener una instancia de la cola de sockets
     * 
     * @return [ArrayList<Sockets>] cola de sockets
     */
    public ArrayList<Socket> getListaSocket() {
        return listaSocket;
    }

    /**
     * Permite modificar la cola de sockets
     * 
     * @param listaSocket [ArrayList<Socket>] nueva cola de sockets
     */
    public void setListaSocket(ArrayList<Socket> listaSocket) {
        this.listaSocket = listaSocket;
    }

    /**
     * Permite saber si la cola de sockets está ocupada o no
     * 
     * @return [boolean] true si está ocupada, false en caso contrario
     */
    public boolean isOcupado() {
        return ocupado;
    }

    /**
     * Permite modificar el estado ocupado de la cola
     * 
     * @param ocupado  [boolean] para poner el estado como ocupado, false en caso contrario
     */
    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    /**
     * Permite obtener el tamaño máximo de la cola
     * 
     * @return [int] tamaño máximo de la cola
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Permite modificar el tamaño máximo de la cola
     * 
     * @param maxSize [int] tamaño máximo de la cola
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Permite obtener el número de intentos definido
     * 
     * @return [int] número de intentos
     */
    public int getIntentos() {
        return intentos;
    }

    /**
     * Permite modificar el número de intentos
     * 
     * @param intentos [int] número de intentos
     */
    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }
    
    /**
     * Permite saber si hay un tamaño máximo definido
     * @return [boolean] true si el tamaño máximo es mayor que cero, false si es 0
     */
    public boolean isListaLimitada(){
        return maxSize > 0;
    }
    
    /**
     * Permite saber si la lista está vacía
     * 
     * @return [boolean] true en caso de que la cola está vacía, false en caso contrario
     */
    public boolean isListaVacia(){
        return listaSocket.isEmpty();
    }
    
}