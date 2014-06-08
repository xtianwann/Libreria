package Excepciones.Cola;

/**
 * Lanza una excepción en caso de que la cola esté vacía y se esté intentado retirar un elemento
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
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
     * @param excepcion [String] mensaje que se desea mostrar en la excepción
     */
    public ExcepcionColaVacia(String excepcion) {
        super(excepcion);
    }
    
}
