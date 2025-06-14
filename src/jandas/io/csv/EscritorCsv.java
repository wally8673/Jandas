package jandas.io.csv;

import jandas.base.data.Tabla;
import jandas.io.EscribirArchivo;

/**
 * Interfaz para escribir objetos {@link Tabla} en archivos CSV.
 * Define métodos para exportar tablas con diferentes configuraciones,
 * como separador personalizado y representación de valores nulos.
 */
public interface EscritorCsv extends EscribirArchivo {

    /**
     * Escribe la tabla en un archivo CSV utilizando configuración por defecto.
     *
     * @param tabla tabla con los datos a escribir
     * @param rutaArchivo ruta (path) del archivo CSV destino
     */
    void escribir(Tabla tabla, String rutaArchivo);

    /**
     * Escribe la tabla en un archivo CSV usando un separador personalizado.
     *
     * @param tabla tabla con los datos a escribir
     * @param rutaArchivo ruta del archivo CSV destino
     * @param separador cadena que separa los valores en el archivo CSV
     */
    void escribir(Tabla tabla, String rutaArchivo, String separador);

    /**
     * Escribe la tabla en un archivo CSV usando separador personalizado
     * y una cadena para representar valores nulos.
     *
     * @param tabla tabla con los datos a escribir
     * @param rutaArchivo ruta del archivo CSV destino
     * @param separador cadena que separa los valores en el archivo CSV
     * @param valorNulo cadena que representa valores nulos en el archivo CSV
     */
    void escribir(Tabla tabla, String rutaArchivo, String separador, String valorNulo);
}

