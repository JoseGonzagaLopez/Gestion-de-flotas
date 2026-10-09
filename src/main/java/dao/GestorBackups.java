/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import model.Vehiculo;

/**
 * Gestiona los backups binarios nativos de la flota de vehículos mediante
 * serialización Java.
 *
 * <p>Guarda y restaura simultáneamente el {@link ArrayList} y el {@link HashMap}
 * de vehículos en el fichero {@code datos/backups/flota_backup.ser}.
 * Los objetos {@link Vehiculo} deben implementar {@link java.io.Serializable}.</p>
 *
 * @author josef
 */
public class GestorBackups {

    /** Ruta del fichero de backup serializado. */
    private static final File ruta = new File("datos/backups/flota_backup.ser");

    /** Lista de vehículos restaurada desde el backup. */
    private ArrayList<Vehiculo> vehiculos = new ArrayList<>();

    /** Mapa de vehículos indexado por matrícula, restaurado desde el backup. */
    private HashMap<String, Vehiculo> mapVehiculos = new HashMap<>();

    /**
     * Serializa el {@link ArrayList} y el {@link HashMap} de vehículos en el
     * fichero de backup {@code flota_backup.ser}.
     *
     * <p>Crea los directorios padre si no existen antes de escribir.</p>
     *
     * @param vehiculos    lista de vehículos a guardar
     * @param mapVehiculos mapa de vehículos indexado por matrícula a guardar
     * @throws IOException si ocurre un error al crear directorios o al escribir el fichero
     */
    public void guardarBackup(ArrayList<Vehiculo> vehiculos, HashMap<String, Vehiculo> mapVehiculos) throws IOException {
        Files.createDirectories(ruta.toPath().getParent());

        try(ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(ruta))){
            oos.writeObject(vehiculos);
            oos.writeObject(mapVehiculos);
        }
    }

    /**
     * Deserializa el fichero de backup y restaura el {@link ArrayList} y el
     * {@link HashMap} de vehículos en los atributos internos de esta clase.
     *
     * @return {@code true} si la restauración fue exitosa; {@code false} en caso de error
     */
    @SuppressWarnings("unchecked")
    public boolean restaurarBackup(){

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))){
            vehiculos = (ArrayList<Vehiculo>) ois.readObject();
            mapVehiculos = (HashMap<String, Vehiculo>) ois.readObject();
            return true;
        }catch (FileNotFoundException e){
            System.out.println("No existe el backup: " + ruta);
        }catch (ClassNotFoundException e){
            System.out.println("Clase no encontrada al deserializar: " + e.getMessage());
        }catch (IOException e){
            System.out.println("Error en el backup: " + e.getMessage());
        }

        return false;
    }

    /**
     * Devuelve la lista de vehículos restaurada desde el backup.
     *
     * @return {@link ArrayList} de vehículos
     */
    public ArrayList<Vehiculo> getVehiculos(){
        return vehiculos;
    }

    /**
     * Devuelve el mapa de vehículos indexado por matrícula, restaurado desde el backup.
     *
     * @return {@link HashMap} con clave matrícula y valor {@link Vehiculo}
     */
    public HashMap<String, Vehiculo> getMapVehiculos() {
        return mapVehiculos;
    }

    /**
     * Establece el mapa de vehículos indexado por matrícula.
     *
     * @param mapVehiculos nuevo mapa de vehículos
     */
    public void setMapVehiculos(HashMap<String, Vehiculo> mapVehiculos) {
        this.mapVehiculos = mapVehiculos;
    }
}
