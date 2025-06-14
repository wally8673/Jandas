package jandas.io;

import jandas.base.data.Tabla;

/**
 * Interfaz que define el contrato para escribir datos tabulares en un archivo.
 * <p>
 * Implementaciones concretas deben proporcionar la lógica para persistir
 * una {@link Tabla} en un archivo ubicado en la ruta especificada.
 */
public interface EscribirArchivo {

    /**
     * Escribe el contenido de una tabla en un archivo.
     *
     * @param tabla Objeto {@link Tabla} que contiene los datos a escribir.
     * @param rutaArchivo Ruta del archivo donde se desea guardar la tabla.
     */
    void escribir(Tabla tabla, String rutaArchivo);
}

