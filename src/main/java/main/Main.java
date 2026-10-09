/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import dao.GestionInventarioJSON;
import dao.GestorBackups;
import dao.GestorConfiguracion;
import dao.GestorReportesXML;
import model.Vehiculo;

/**
 * Clase principal de la aplicación de gestión de flota de vehículos.
 *
 * <p>Orquesta la ejecución del flujo completo:
 * <ol>
 *   <li>Creación de la estructura de directorios necesaria.</li>
 *   <li>Inicialización de vehículos en memoria (con detección de matrículas duplicadas).</li>
 *   <li>Persistencia de la configuración de empresa en formato binario.</li>
 *   <li>Exportación e importación del inventario en formato JSON.</li>
 *   <li>Generación y restauración de backup binario serializado.</li>
 *   <li>Generación del informe XML y lectura de categorías de mantenimiento únicas.</li>
 * </ol>
 * </p>
 *
 * @author josef
 */
public class Main {

    /**
     * Punto de entrada de la aplicación.
     *
     * <p>Crea la estructura de directorios, inicializa la flota de prueba,
     * guarda y lee la configuración binaria, exporta/importa el inventario JSON,
     * realiza el backup serializado y genera el informe XML.</p>
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {

        //estructura de directorios con NIO
        try{
            Files.createDirectories(Path.of("/datos/config"));
            Files.createDirectories(Path.of("/datos/json"));
            Files.createDirectories(Path.of("/datos/backups"));
            Files.createDirectories(Path.of("/datos/reports"));

        }catch (IOException e){
            System.out.println("No se pudo crear la estructura de directorios: " + e.getMessage());
            return;
        }

        //colecciones en memoria
        ArrayList<Vehiculo> vehiculos = new ArrayList<>();
        HashMap<String, Vehiculo> mapVehiculos = new HashMap<>();
        HashSet<String> matriculas = new HashSet<>();

        Vehiculo[] datos = {
            crearVehiculo("1", "Ferrari", "1M", 2020, 50000, "Volante"),
            crearVehiculo("2", "Volsvagen", "2M", 2000, 0, "Puerta"),
            crearVehiculo("2", "Volsvagen", "2M", 2000, 0, "Puerta"), //duplicada
            crearVehiculo("3", "Ferrari", "3M", 33, 100000, "Frenos")
        };

        for (Vehiculo vehiculo : datos){

            //devuelve false si ya existia
            if (matriculas.add(vehiculo.getMatricula())){
                vehiculos.add(vehiculo);
                mapVehiculos.put(vehiculo.getMatricula(), vehiculo );

            }else{
                System.out.println("Matricula duplicada: " + vehiculo.getMatricula());
            }
        }

        double kmTotales = 0;
        for (Vehiculo vehiculo : vehiculos){
            kmTotales += vehiculo.getKilometraje();
        }

        //configuracion binaria
        GestorConfiguracion config = new GestorConfiguracion("Logistica Mediterranea", 1, vehiculos.size(), kmTotales);
        config.guardarConfig();
        config.leerConfiguracion();

        //JSON
        GestionInventarioJSON inventario = new GestionInventarioJSON();
        try{
            inventario.exportar(vehiculos);

        }catch (IOException e){
            System.out.println("Error al exportar el JSON: " + e.getMessage());
        }
        int cargados = inventario.importar();
        System.out.println("Vehiculos cargados desde JSON: " + cargados);
        System.out.println("Busqueda 3: " + inventario.buscarPorMatricula("3"));

        //backup serializado
        GestorBackups backup = new GestorBackups();
        try{
            backup.guardarBackup(inventario.getVehiculos(), inventario.getMapVehiculos());
        }catch (IOException e){
            System.out.println("Error al guardar el backup: " + e.getMessage());
        }

        if(backup.restaurarBackup()){
            System.out.println("Backup restaurado: " + backup.getMapVehiculos().size() + " vehiculos");
        }

        //XML
        GestorReportesXML reportes = new GestorReportesXML();
        if(reportes.generarXML(vehiculos)){
            int total = reportes.leerXML();
            System.out.println("Categorias de manteminiento unicas: " + total + ", " + reportes.getCategorias());
        }
    }

    /**
     * Crea y configura un {@link Vehiculo} con los datos proporcionados.
     *
     * <p>Utiliza el constructor vacío y los setters de {@link Vehiculo} para
     * establecer todos los campos.</p>
     *
     * @param matricula  matrícula del vehículo
     * @param marca      marca del vehículo
     * @param modelo     modelo del vehículo
     * @param anio       año de fabricación
     * @param km         kilómetros recorridos
     * @param categoria  categoría de mantenimiento
     * @return instancia de {@link Vehiculo} completamente inicializada
     */
    private static Vehiculo crearVehiculo(String matricula, String marca, String modelo, int anio, double km, String categoria) {

        Vehiculo v = new Vehiculo();
        v.setMatricula(matricula);
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setAnio(anio);
        v.setKilometraje(km);
        v.setCategoria_de_manteminiento(categoria);
        return v;
    }
}
