package XML;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Esta clase abstracta proporciona métodos genéricos para poder trabajar con
 * ficheros XML.
 *
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor 
 */
public abstract class XML {

    protected Document document;

    /**
     * Inicializa la cabecera de los documentos XML y añade un nodo raíz
     */
    protected void init() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            document = docBuilder.newDocument();
            addNodo("paquete", null, null);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Devuelve el árbol DOM generado
     *
     * @return [Document] árbol DOM generado
     */
    public Document getDOM() {
        return document;
    }

    /**
     * Agrega un nodo al documento XML
     *
     * @param nombre [String] nombre de la etiqueta
     * @param contenido [String] contenido de la etiqueta
     * @param nodoPadre [String] nodo del que es descendiente, null si es el nodo raíz
     */
    protected void addNodo(String nombre, String contenido, String nodoPadre) {
        Element element = document.createElement(nombre);

        if (contenido != null) {
            element.appendChild(document.createTextNode(contenido));
        }
        if (nodoPadre != null) {
            NodeList n = document.getElementsByTagName(nodoPadre);
            n.item(n.getLength() - 1).appendChild(element);
        } else {
            document.appendChild(element);
        }
    }

    /**
     * Agrega un nodo con atributos al documento XML
     * 
     * @param nombre [String] nombre de la etiqueta
     * @param nombreAtributo [String[ ]] array con los nombres de los atributos que va a recibir
     * @param valorAtributo [String[ ]] array con los valores de los atributos que va a recibir
     * @param contenido [String] contenido de la etiqueta
     * @param nodoPadre [String] nodo del que es descendiente, null si es el nodo raíz
     */
    protected void addNodoConAtributos(String nombre, String[] nombreAtributo, String[] valorAtributo, String contenido, String nodoPadre) {
        Element element = document.createElement(nombre);

        if (nombreAtributo != null && valorAtributo != null) {
            for (int i = 0; i < nombreAtributo.length; i++) {
                element.setAttribute(nombreAtributo[i], valorAtributo[i]);
            }
        }
        if (contenido != null) {
            element.appendChild(document.createTextNode(contenido));
        }
        if (nodoPadre != null) {
            NodeList n = document.getElementsByTagName(nodoPadre);
            n.item(n.getLength() - 1).appendChild(element);
        } else {
            document.appendChild(element);
        }
    }

    /**
     * Realiza una suma resumen con el algoritmo SHA1.
     *
     * @param input [String] cadena origen.
     * @return [String] cadena con la suma resumen SHA1.
     * @throws NoSuchAlgorithmException No está el método que hace la suma resumen.
     */
    protected String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Devuelve el contenido de una etiqueta
     *
     * @param etiqueta [String] etiqueta de la que queremos obtener el valor
     * @return [String] cadena de texto con el valor de la etiqueta
     */
    public String getContenido(String etiqueta) {
        return document.getElementsByTagName(etiqueta).item(0).getChildNodes().item(0).getNodeValue();
    }

    /**
     * Si el contenido de una etiqueta es un entero lo recibimos como tal
     *
     * @param etiqueta [String] etiqueta de la que queremos obtener el valor
     * @return contenido [int] contenido de la etiqueta como un entero
     */
    public int getContenidoEntero(String etiqueta) {
        return Integer.parseInt(getContenido(etiqueta));
    }

    /**
     * Transforma la información de un xml en una cadena de texto
     *
     * @param doc [Document] objeto de la clase Document que será convertido a cadena
     * @return [String] cadena que contiene toda la información del xml
     */
    public String xmlToString(Document doc) {
        String devolver = "";

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(stringWriter));
            devolver = stringWriter.getBuffer().toString();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return devolver;
    }

    /**
     * Transforma una cadena (con información válida) a xml
     *
     * @param cadena [String] cadena a convertir en xml
     * @return [Document] objeto de la clase Document
     */
    public static Document stringToXml(String cadena) {
        String aux;
        Document doc = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            aux = cadena.substring(cadena.indexOf("<"));
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(aux)));
        } catch (SAXException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }
}
