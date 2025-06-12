package jandas.visualizacion;

import jandas.base.data.Tabla;

/**
 * Interfaz para visualizar en la libreria Jandas.
 * Las implementaciones de esta interfaz provee diferentes formas de visualizar objetos DataFrame
 */
public interface Visualizable {

    /**
     * Visualiza el dataframe con parametros personalizados
     *
     * @param tabla El dataframe que visualiza
     * @param maxFilas Numero maximo de filas a mostrar
     * @param maxColumnas Numero maximo de columnas a mostrar
     * @param maxLargoCadena valor maximo de caracteres en cada celda
     */
    void visualizar(Tabla tabla, int maxFilas, int maxColumnas, int maxLargoCadena);

    /**
     * Visualiza el dataframe con parametros default
     *
     * @param tabla The DataFrame to visualize
     */
    void visualizar(Tabla tabla);
}
