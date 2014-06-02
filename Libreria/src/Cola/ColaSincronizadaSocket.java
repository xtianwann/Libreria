package Cola;

import Excepciones.Cola.ExcepcionColaLlena;
import Excepciones.Cola.ExcepcionColaVacia;
import Excepciones.ExcepcionInesperada;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ColaSincronizadaSocket {
    
    private volatile ArrayList<Socket> listaSocket;
    private boolean ocupado;
    private int maxSize;
    private int intentos;
    
    public ColaSincronizadaSocket(){
        inicializar();
        this.maxSize = 0;
        this.intentos = 6;
    }
    
    public ColaSincronizadaSocket(int maxSize){
        inicializar();
        this.maxSize = maxSize;
        this.intentos = 6;
    }
    
    public ColaSincronizadaSocket(int maxSize, int tiempoAgotado){
        inicializar();
        this.maxSize = maxSize;
        this.intentos = 6;
    }
    
    private void inicializar(){
        this.listaSocket = new ArrayList<>();
        this.ocupado = false;
    }
    
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
				// TODO Auto-generated catch block
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
    
    /* AÃ±ade un socket a la cola */
    public void addSocket(Socket socket) throws ExcepcionColaLlena, TimeoutException, ExcepcionInesperada{
        int contador = 0;
        if(isListaLimitada() && listaSocket.size() > maxSize)
            throw new ExcepcionColaLlena("La cola estÃ¡ llena");
        while(isOcupado()){
            if(contador > intentos){
                throw new TimeoutException("Tiempo excedido para retirar un socket de la cola");
            }
            contador++;
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
    
    /* Getter y Setter */
    public ArrayList<Socket> getListaSocket() {
        return listaSocket;
    }

    public void setListaSocket(ArrayList<Socket> listaSocket) {
        this.listaSocket = listaSocket;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }
    
    public boolean isListaLimitada(){
        return maxSize > 0;
    }
    
    public boolean isListaVacia(){
        return listaSocket.isEmpty();
    }
    
}
