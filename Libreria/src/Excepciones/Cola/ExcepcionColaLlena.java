package Excepciones.Cola;

/**
 * Lanza una excepci�n en caso de que la cola est� llena y se intente insertar alg�n elemento m�s
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class ExcepcionColaLlena extends Exception{

	/**
	 * Constructor
	 */
    public ExcepcionColaLlena() {
    }

    /**
     * Constructor
     * 
     * @param excepcion [String] mensaje que se desea mostrar en la excepci�n
     */
    public ExcepcionColaLlena(String excepcion) {
        super(excepcion);
    }
    
}
