package Excepciones;

/**
 * Lanza una excepci�n ante un error inesperado
 * 
 * @author Juan G. P�rez Leo
 * @author Cristian Mar�n Honor
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
     * @param excepcion [String] mensaje que se desea mostrar en la excepci�n
     */
    public ExcepcionInesperada(String excepcion) {
        super(excepcion);
    }
    
}
