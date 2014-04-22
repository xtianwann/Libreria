package Excepciones.Cola;

/**
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class ExcepcionColaLlena extends Exception{

    public ExcepcionColaLlena() {
    }

    public ExcepcionColaLlena(String excepcion) {
        super(excepcion);
    }
    
}
