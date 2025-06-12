package jandas.operaciones;

import jandas.base.data.Tabla;

public interface Muestreable {

    /**
     * Obtiene una muestra aleatoria de la tabla basada en un porcentaje
     * @param porcentaje Porcentaje de filas a incluir en la muestra (1-100)
     * @return Nueva tabla con la muestra aleatoria
     */
    Tabla muestrear(int porcentaje);

    /**
     * Obtiene una muestra aleatoria de tamaño fijo
     * @param cantidadFilas Cantidad exacta de filas a incluir en la muestra
     * @return Nueva tabla con la muestra aleatoria
     */
    Tabla muestrear(int cantidadFilas, boolean exacto);

    /**
     * Obtiene una muestra aleatoria estratificada por una columna
     * @param nombreColumna Nombre de la columna para estratificar
     * @param porcentaje Porcentaje de cada estrato a incluir
     * @return Nueva tabla con muestra estratificada
     */
    Tabla muestrearEstratificado(String nombreColumna, int porcentaje);
}