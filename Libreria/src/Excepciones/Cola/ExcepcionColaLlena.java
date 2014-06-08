package Excepciones.Cola;

/**
 * Lanza una excepción en caso de que la cola esté llena y se intente insertar algún elemento más
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
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
     * @param excepcion [String] mensaje que se desea mostrar en la excepción
     */
    public ExcepcionColaLlena(String excepcion) {
        super(excepcion);
    }
    
}
