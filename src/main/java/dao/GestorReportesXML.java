/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.Vehiculo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Gestiona la generación y lectura de informes de flota en formato XML.
 *
 * <p>Utiliza la API DOM para generar el fichero {@code /datos/reports/informe_flota.xml}
 * a partir de la lista de vehículos en memoria, y la API SAX para leerlo
 * de forma eficiente y extraer las categorías de mantenimiento únicas.</p>
 *
 * @author josef
 */
public class GestorReportesXML {

    /** Ruta del fichero XML de informe de flota. */
    private static final File ruta = new File("/datos/reports/informe_flota.xml");

    /** Conjunto de categorías de mantenimiento únicas extraídas del XML. */
    private final HashSet<String> categorias = new HashSet<>();

    /**
     * Genera el fichero XML {@code informe_flota.xml} a partir de la lista de
     * vehículos usando la API DOM.
     *
     * <p>El documento XML tiene un elemento raíz {@code <flota>} con un
     * {@code <vehiculo>} por cada vehículo, incluyendo sus campos como
     * elementos hijo. Crea los directorios padre si no existen.</p>
     *
     * @param vehiculos lista de vehículos a incluir en el informe
     * @return {@code true} si el XML se generó correctamente; {@code false} si
     *         ocurrió algún error
     */
    public boolean generarXML(ArrayList<Vehiculo> vehiculos){

        try{
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.newDocument();

            //raiz
            Element raiz = doc.createElement("flota");
            doc.appendChild(raiz);

            //un nodo vehiculo por cada vehiculo
            for(Vehiculo v : vehiculos){
                Element vehiculo = doc.createElement("vehiculo");
                vehiculo.setAttribute("matricula", v.getMatricula());

                raiz.appendChild(vehiculo);

                agregarCampo(doc, vehiculo, "marca", v.getMarca());
                agregarCampo(doc, vehiculo, "modelo", v.getModelo());
                agregarCampo(doc, vehiculo, "anio", String.valueOf(v.getAnio()));
                agregarCampo(doc,  vehiculo, "kms_recorridos", String.valueOf(v.getKilometraje()));
                agregarCampo(doc, vehiculo, "categoriaManteminiento", v.getCategoria_de_manteminiento());
            }

            //escribir el arbol a fichero
            Files.createDirectories(ruta.toPath().getParent());

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(ruta));

            return true;

        }catch(ParserConfigurationException e){
            System.err.println("Error de configuracion del parser DOM: " + e.getMessage());
        }catch(TransformerException e){
            System.err.println("Error al escribir el xml: " + e.getMessage());
        }catch(IOException e){
            System.err.println("Error al crear el directorio: " + e.getMessage());
        }

        return false;
    }

    /**
     * Crea un elemento XML hijo con el texto indicado y lo añade al elemento padre.
     *
     * @param doc    documento DOM al que pertenecen los elementos
     * @param padre  elemento padre al que se añadirá el nuevo elemento hijo
     * @param nombre nombre de la etiqueta del nuevo elemento
     * @param valor  texto que se asignará como contenido del elemento;
     *               si es {@code null} se usa una cadena vacía
     */
    private void agregarCampo(Document doc, Element padre, String nombre, String valor){
        Element e = doc.createElement(nombre);
        e.setTextContent(valor == null ? "" : valor);
        padre.appendChild(e);
    }

    /**
     * Lee el fichero {@code informe_flota.xml} con SAX y extrae las categorías
     * de mantenimiento únicas, almacenándolas en el {@link HashSet} interno.
     *
     * @return número de categorías de mantenimiento únicas encontradas
     */
    public int leerXML(){
        categorias.clear();

        try{
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(ruta, new ManejadorCategorias());
        }catch(ParserConfigurationException e){
            System.err.println("Error de configuracion del parser SAX: " + e.getMessage());
        }catch(SAXException e){
            System.err.println("XML mal formado: " + e.getMessage());
        }catch(IOException e){
            System.err.println("Error de lectura del XML: " + e.getMessage());
        }
        return categorias.size();
    }

    /**
     * Manejador SAX interno que extrae las categorías de mantenimiento únicas
     * del XML de la flota.
     *
     * <p>Escucha los eventos de inicio y fin del elemento
     * {@code <categoriaManteminiento>} y acumula su contenido de texto en
     * el {@link HashSet} de la clase externa.</p>
     */
    private class ManejadorCategorias extends DefaultHandler{

        /** Buffer para acumular el texto del elemento actual. */
        private final StringBuilder texto = new StringBuilder();

        /** Indica si el parser está dentro de un elemento {@code <categoriaManteminiento>}. */
        private boolean enCategoria = false;

        /**
         * Detecta el inicio del elemento {@code <categoriaManteminiento>} y
         * activa la captura de texto.
         *
         * @param uri       URI del espacio de nombres (puede estar vacía)
         * @param localName nombre local del elemento
         * @param qName     nombre cualificado del elemento
         * @param attrs     atributos del elemento
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs){
            if (qName.equals("categoriaManteminiento")){
                enCategoria = true;
                texto.setLength(0);
            }
        }

        /**
         * Acumula los caracteres de texto cuando el parser está dentro de un
         * elemento {@code <categoriaManteminiento>}.
         *
         * @param ch     array de caracteres del documento
         * @param start  posición inicial del fragmento en el array
         * @param length longitud del fragmento
         */
        @Override
        public void characters(char[] ch, int start, int length){
            if (enCategoria){
                texto.append(ch, start, length);
            }
        }

        /**
         * Detecta el fin del elemento {@code <categoriaManteminiento>}, añade
         * la categoría al conjunto si no está vacía y desactiva la captura.
         *
         * @param uri       URI del espacio de nombres (puede estar vacía)
         * @param localName nombre local del elemento
         * @param qName     nombre cualificado del elemento
         */
        @Override
        public void endElement(String uri, String localName, String qName){
            if (qName.equals("categoriaManteminiento")){
                String categoria = texto.toString().trim();
                if (!categoria.isEmpty()){
                    categorias.add(categoria);
                }
                enCategoria = false;
            }
        }

    }

    /**
     * Devuelve el conjunto de categorías de mantenimiento únicas extraídas
     * tras la última llamada a {@link #leerXML()}.
     *
     * @return {@link HashSet} con las categorías de mantenimiento
     */
    public HashSet<String> getCategorias(){
        return categorias;
    }

}
