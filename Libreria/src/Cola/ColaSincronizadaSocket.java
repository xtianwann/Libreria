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
    private int tiempoAgotado;
    
    public ColaSincronizadaSocket(){
        inicializar();
        this.maxSize = 0;
        this.tiempoAgotado = 10000;
    }
    
    public ColaSincronizadaSocket(int maxSize){
        inicializar();
        this.maxSize = maxSize;
        this.tiempoAgotado = 10000;
    }
    
    public ColaSincronizadaSocket(int maxSize, int tiempoAgotado){
        inicializar();
        this.maxSize = maxSize;
        this.tiempoAgotado = tiempoAgotado;
    }
    
    private void inicializar(){
        this.listaSocket = new ArrayList<>();
        this.ocupado = false;
    }
    
    public Socket getSocket() throws TimeoutException, ExcepcionColaVacia, ExcepcionInesperada{
        Socket socket = null;
        Calendar ahora = Calendar.getInstance();
        long tiempoLimite = ahora.getTimeInMillis() + this.tiempoAgotado;
        
        if(listaSocket.isEmpty())
            throw new ExcepcionColaVacia("La cola está vacía");
        while(isOcupado()){
            if(tiempoAgotado > ahora.getTimeInMillis()){
                throw new TimeoutException("Tiempo excedido para retirar un socket de la cola");
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
    
    /* Añade un socket a la cola */
    public void addSocket(Socket socket) throws ExcepcionColaLlena, TimeoutException, ExcepcionInesperada{
        Calendar ahora = Calendar.getInstance();
        long tiempoLimite = ahora.getTimeInMillis() + this.tiempoAgotado;
        if(isListaLimitada() && listaSocket.size() > maxSize)
            throw new ExcepcionColaLlena("La cola está llena");
        while(isOcupado()){
            if(tiempoAgotado > ahora.getTimeInMillis())
                throw new TimeoutException("Tiempo excedido para añadir un socket a la cola");
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

    public int getTiempoAgotado() {
        return tiempoAgotado;
    }

    public void setTiempoAgotado(int tiempoAgotado) {
        this.tiempoAgotado = tiempoAgotado;
    }
    
    public boolean isListaLimitada(){
        return maxSize > 0;
    }
    
    public boolean isListaVacia(){
        return listaSocket.isEmpty();
    }
    
}
