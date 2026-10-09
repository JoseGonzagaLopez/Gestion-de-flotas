/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Gestiona la configuración de la empresa en un fichero binario de datos
 * primitivos ({@code /datos/config/empresa.dat}).
 *
 * <p>Utiliza {@link DataOutputStream} para escribir y {@link DataInputStream}
 * para leer los campos: nombre de empresa, versión del software, total de
 * vehículos y kilometraje acumulado. Los datos se almacenan en orden fijo
 * y se leen en el mismo orden.</p>
 *
 * @author josef
 */
public class GestorConfiguracion {

    /** Ruta del fichero de configuración binario. */
    private static final File archivo = new File("/datos/config/empresa.dat");

    /** Nombre de la empresa de logística. */
    private String nombreEmpresa;

    /** Número de versión del software. */
    private int versionSoftware;

    /** Número total de vehículos registrados en la flota. */
    private int totalVehiculos = 0;

    /** Suma total de kilómetros recorridos por toda la flota. */
    private double kilometrajeAcumulado = 0;

    /**
     * Crea una instancia de {@code GestorConfiguracion} con todos los campos inicializados.
     *
     * @param nombreEmpresa        nombre de la empresa
     * @param versionSoftware      versión del software
     * @param totalVehiculos       número total de vehículos
     * @param kilometrajeAcumulado kilometraje total acumulado de la flota
     */
    public GestorConfiguracion(String nombreEmpresa, int versionSoftware, int totalVehiculos, double kilometrajeAcumulado) {
        this.nombreEmpresa = nombreEmpresa;
        this.versionSoftware = versionSoftware;
        this.totalVehiculos = totalVehiculos;
        this.kilometrajeAcumulado = kilometrajeAcumulado;
    }

    /**
     * Constructor vacío. Crea una instancia con valores por defecto.
     */
    public GestorConfiguracion() {
    }

    /**
     * Devuelve el nombre de la empresa.
     *
     * @return nombre de la empresa
     */
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    /**
     * Establece el nombre de la empresa.
     *
     * @param nombreEmpresa nuevo nombre de la empresa
     */
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * Devuelve la versión del software.
     *
     * @return versión del software
     */
    public int getVersionSoftware() {
        return versionSoftware;
    }

    /**
     * Establece la versión del software.
     *
     * @param versionSoftware nueva versión del software
     */
    public void setVersionSoftware(int versionSoftware) {
        this.versionSoftware = versionSoftware;
    }

    /**
     * Devuelve el número total de vehículos de la flota.
     *
     * @return total de vehículos
     */
    public int getTotalVehiculos() {
        return totalVehiculos;
    }

    /**
     * Establece el número total de vehículos de la flota.
     *
     * @param totalVehiculos nuevo total de vehículos
     */
    public void setTotalVehiculos(int totalVehiculos) {
        this.totalVehiculos = totalVehiculos;
    }

    /**
     * Devuelve el kilometraje acumulado de toda la flota.
     *
     * @return kilometraje acumulado en kilómetros
     */
    public double getKilometrajeAcumulado() {
        return kilometrajeAcumulado;
    }

    /**
     * Establece el kilometraje acumulado de toda la flota.
     *
     * @param kilometrajeAcumulado nuevo kilometraje acumulado en kilómetros
     */
    public void setKilometrajeAcumulado(double kilometrajeAcumulado) {
        this.kilometrajeAcumulado = kilometrajeAcumulado;
    }

    /**
     * Persiste la configuración actual en el fichero binario {@code empresa.dat}.
     *
     * <p>Crea los directorios padre si no existen. Los datos se escriben en este
     * orden: {@code nombreEmpresa} (UTF), {@code versionSoftware} (int),
     * {@code totalVehiculos} (int), {@code kilometrajeAcumulado} (double).</p>
     */
    public void guardarConfig(){

        //crea los directorios padre si no existen
       if (archivo.getParentFile() != null){
           archivo.getParentFile().mkdirs();
       }

       try(DataOutputStream dos = new DataOutputStream(new FileOutputStream(archivo))){

           dos.writeUTF(nombreEmpresa);
           dos.writeInt(versionSoftware);
           dos.writeInt(totalVehiculos);
           dos.writeDouble(kilometrajeAcumulado);

           System.out.println("Configuración guardada con éxito en: " + archivo);
        }catch (IOException e){
            System.out.println("Error al guardar: "  + e.getMessage());
        }
    }

    /**
     * Lee la configuración desde el fichero binario {@code empresa.dat} y
     * actualiza los atributos de la instancia.
     *
     * <p>Si el fichero no existe, muestra un aviso y termina sin lanzar excepción.
     * El bucle de lectura finaliza al detectar un {@link EOFException}.</p>
     */
    public void leerConfiguracion(){

        if(!archivo.exists()){
            System.out.println("El archivo aun no existe");
            return;
        }

        try(DataInputStream dis = new DataInputStream(new FileInputStream(archivo))){
            System.out.println("LEYENDO CONFIGURACION");

            while(true){
                try{
                    //se leen los datos
                    this.nombreEmpresa = dis.readUTF();
                    this.versionSoftware = dis.readInt();
                    this.totalVehiculos = dis.readInt();
                    this.kilometrajeAcumulado = dis.readDouble();

                    //se muestran los datos leidos
                    System.out.println("Empresa: " + this.nombreEmpresa);
                    System.out.println("Version: " + this.versionSoftware);
                    System.out.println("Total Vehículos: " + this.totalVehiculos);
                    System.out.println("Kilometraje: " + this.kilometrajeAcumulado);
                }catch(EOFException e){
                    //cuando se acaban los datos, se lanza un EOFException
                    //lo finalizamos con un break
                    System.out.println("Fin");
                    break;
                }
            }
        }catch (IOException e){
            //capturar cualquier otro error de lectura
            System.out.println("Error: " + e.getMessage());
        }

    }
}
