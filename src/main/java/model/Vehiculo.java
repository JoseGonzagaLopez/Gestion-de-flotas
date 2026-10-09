/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Representa un vehículo de la flota con sus datos básicos de identificación,
 * kilometraje y categoría de mantenimiento.
 *
 * <p>Implementa {@link Serializable} para permitir su serialización binaria
 * (backup) y es compatible con Gson para exportación/importación JSON.</p>
 *
 * @author josef
 */
public class Vehiculo implements Serializable{

    /** Matrícula única que identifica al vehículo. */
    private String matricula;

    /** Marca comercial del vehículo (p. ej. "Ferrari", "Volkswagen"). */
    private String marca;

    /** Modelo específico del vehículo. */
    private String modelo;

    /** Año de fabricación del vehículo. */
    private int anio;

    /**
     * Kilómetros recorridos por el vehículo.
     * Se serializa en JSON con la clave {@code kms_recorridos}.
     */
    @SerializedName("kms_recorridos")
    private double kilometraje;

    /** Categoría de mantenimiento asignada al vehículo (p. ej. "Frenos", "Volante"). */
    private String categoria_de_manteminiento;

    /**
     * Constructor vacío requerido por Gson y por la serialización Java.
     */
    public Vehiculo() {
    }

    /**
     * Devuelve la matrícula del vehículo.
     *
     * @return matrícula del vehículo
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Establece la matrícula del vehículo.
     *
     * @param matricula nueva matrícula
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Devuelve la marca del vehículo.
     *
     * @return marca del vehículo
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca del vehículo.
     *
     * @param marca nueva marca
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * Devuelve el modelo del vehículo.
     *
     * @return modelo del vehículo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Establece el modelo del vehículo.
     *
     * @param modelo nuevo modelo
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Devuelve el año de fabricación del vehículo.
     *
     * @return año de fabricación
     */
    public int getAnio() {
        return anio;
    }

    /**
     * Establece el año de fabricación del vehículo.
     *
     * @param anio año de fabricación
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * Devuelve el kilometraje acumulado del vehículo.
     *
     * @return kilómetros recorridos
     */
    public double getKilometraje() {
        return kilometraje;
    }

    /**
     * Establece el kilometraje acumulado del vehículo.
     *
     * @param kilometraje kilómetros recorridos
     */
    public void setKilometraje(double kilometraje) {
        this.kilometraje = kilometraje;
    }

    /**
     * Devuelve la categoría de mantenimiento del vehículo.
     *
     * @return categoría de mantenimiento
     */
    public String getCategoria_de_manteminiento() {
        return categoria_de_manteminiento;
    }

    /**
     * Establece la categoría de mantenimiento del vehículo.
     *
     * @param categoria_de_manteminiento nueva categoría de mantenimiento
     */
    public void setCategoria_de_manteminiento(String categoria_de_manteminiento) {
        this.categoria_de_manteminiento = categoria_de_manteminiento;
    }

    /**
     * Devuelve una representación en cadena del vehículo con todos sus campos.
     *
     * @return cadena con los datos del vehículo
     */
    @Override
    public String toString() {
        return "Vehiculo{" + "matricula=" + matricula + ", marca=" + marca + ", modelo=" + modelo + ", anio=" + anio + ", kilometraje=" + kilometraje + ", categoria_de_manteminiento=" + categoria_de_manteminiento + '}';
    }


}
