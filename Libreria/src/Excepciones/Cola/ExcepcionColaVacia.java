package Excepciones.Cola;

/**
 * Lanza una excepci�n en caso de que la cola est� vac�a y se est� intentado retirar un elemento
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
 */
public class ExcepcionColaVacia extends Exception{

	/**
	 * Constructor
	 */
    public ExcepcionColaVacia() {
    }

    /**
     * Constructor
     * 
     * @param excepcion [String] mensaje que se desea mostrar en la excepci�n
     */
    public ExcepcionColaVacia(String excepcion) {
        super(excepcion);
    }
    
}
