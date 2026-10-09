/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import model.Vehiculo;

/**
 * Gestiona la persistencia del inventario de vehículos en formato JSON.
 *
 * <p>Utiliza la librería Gson para serializar y deserializar objetos
 * {@link Vehiculo}. Los datos se almacenan en {@code /datos/json/flota.json}.
 * Mantiene en memoria tanto un {@link ArrayList} como un {@link HashMap}
 * indexado por matrícula para permitir búsquedas O(1).</p>
 *
 * @author josef
 */
public class GestionInventarioJSON {

    /** Ruta del fichero JSON donde se persiste la flota. */
    private static final File ruta = new File("/datos/json/flota.json");

    /** Instancia de Gson configurada con impresión con sangría. */
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /** Lista ordenada de vehículos cargados en memoria. */
    private final ArrayList<Vehiculo> vehiculos = new ArrayList<>();

    /** Mapa de vehículos indexado por matrícula para búsqueda rápida. */
    private final HashMap<String, Vehiculo> mapVehiculos = new HashMap<>();

    /**
     * Exporta la lista de vehículos al fichero {@code flota.json}.
     *
     * <p>Crea los directorios padre si no existen antes de escribir.</p>
     *
     * @param vehiculos lista de vehículos a exportar
     * @throws IOException si ocurre un error al crear directorios o al escribir el fichero
     */
    public void exportar(ArrayList<Vehiculo> vehiculos) throws IOException {
        Files.createDirectories(ruta.toPath().getParent());

        try(FileWriter fw = new FileWriter(ruta)){
            gson.toJson(vehiculos, fw);
        }
    }

    /**
     * Lee {@code flota.json} e importa los vehículos al {@link ArrayList} y al
     * {@link HashMap} internos, ignorando entradas duplicadas (misma matrícula)
     * o entradas con matrícula {@code null}.
     *
     * @return número de vehículos cargados correctamente
     */
    public int importar(){
        vehiculos.clear();
        mapVehiculos.clear();

        try(FileReader fr = new FileReader(ruta)){
            Vehiculo[] leidos = gson.fromJson(fr, Vehiculo[].class);

            if(leidos != null){
                for(Vehiculo vehiculo : leidos){
                    if(vehiculo != null && vehiculo.getMatricula() != null
                    && !mapVehiculos.containsKey(vehiculo.getMatricula())){
                        vehiculos.add(vehiculo);
                        mapVehiculos.put(vehiculo.getMatricula(), vehiculo);
                    }
                }
            }

        }catch (FileNotFoundException e){
            System.out.println("No existe el fichero: " + ruta);
        }catch (JsonParseException e ){
            System.out.println("JSON mal formado: " + e.getMessage());
        }catch(IOException e){
            System.out.println("Error de lectura: " + e.getMessage());
        }
        return vehiculos.size();
    }

    /**
     * Realiza una búsqueda directa por matrícula en el mapa interno.
     *
     * @param matricula matrícula del vehículo a buscar
     * @return el {@link Vehiculo} correspondiente, o {@code null} si no existe
     */
    public Vehiculo buscarPorMatricula(String matricula){
        return mapVehiculos.get(matricula);
    }

    /**
     * Devuelve la lista de vehículos cargados en memoria.
     *
     * @return {@link ArrayList} con los vehículos
     */
    public ArrayList<Vehiculo> getVehiculos(){
        return vehiculos;
    }

    /**
     * Devuelve el mapa de vehículos indexado por matrícula.
     *
     * @return {@link HashMap} con clave matrícula y valor {@link Vehiculo}
     */
    public HashMap<String, Vehiculo> getMapVehiculos(){
        return mapVehiculos;
    }

}
