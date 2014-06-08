package Excepciones;

/**
 * Lanza una excepción ante un error inesperado
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ExcepcionInesperada extends Exception{

	/**
	 * Constructor
	 */
    public ExcepcionInesperada() {
    }

    /**
     * Constructor
     * 
     * @param excepcion [String] mensaje que se desea mostrar en la excepción
     */
    public ExcepcionInesperada(String excepcion) {
        super(excepcion);
    }
    
}
