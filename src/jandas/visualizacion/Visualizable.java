package jandas.visualizacion;

import jandas.core.data.DataFrame;

/**
 * Interfaz para visualizar en la libreria Jandas.
 * Las implementaciones de esta interfaz provee diferentes formas de visualizar objetos DataFrame
 */
public interface Visualizable {

    /**
     * Visualiza el dataframe con parametros personalizados
     *
     * @param dataFrame El dataframe que visualiza
     * @param maxFilas Numero maximo de filas a mostrar
     * @param maxColumnas Numero maximo de columnas a mostrar
     * @param maxLargoCadena valor maximo de caracteres en cada celda
     */
    void visualizar(DataFrame dataFrame, int maxFilas, int maxColumnas, int maxLargoCadena);

    /**
     * Visualiza el dataframe con parametros default
     *
     * @param dataFrame The DataFrame to visualize
     */
    default void visualizar(DataFrame dataFrame) {
        visualizar(dataFrame, 10, 10, 20);
    }
}
