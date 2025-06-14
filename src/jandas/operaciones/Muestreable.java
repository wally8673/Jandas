package jandas.operaciones;

import jandas.base.data.Tabla;

/**
 * Interfaz que define métodos para obtener muestras aleatorias de una {@link Tabla}.
 * <p>
 * Permite obtener muestras basadas en porcentaje, en cantidad fija, o de forma estratificada.
 * </p>
 */
public interface Muestreable {

    /**
     * Obtiene una muestra aleatoria de la tabla basada en un porcentaje de filas.
     *
     * @param porcentaje Porcentaje de filas a incluir en la muestra. Debe estar entre 1 y 100.
     * @return Nueva instancia de {@link Tabla} con la muestra aleatoria seleccionada.
     * @throws IllegalArgumentException si el porcentaje está fuera del rango válido.
     */
    Tabla muestrear(int porcentaje);

    /**
     * Obtiene una muestra aleatoria con una cantidad fija de filas.
     *
     * @param cantidadFilas Cantidad exacta de filas a incluir en la muestra.
     * @param exacto Indica si la muestra debe contener exactamente la cantidad especificada.
     * @return Nueva instancia de {@link Tabla} con la muestra aleatoria seleccionada.
     * @throws IllegalArgumentException si la cantidadFilas es negativa o mayor que el total de filas.
     */
    Tabla muestrear(int cantidadFilas, boolean exacto);

    /**
     * Obtiene una muestra aleatoria estratificada según una columna específica.
     * <p>
     * Esto significa que se toma una muestra representativa de cada categoría o grupo presente
     * en la columna especificada, basada en el porcentaje dado.
     * </p>
     *
     * @param nombreColumna Nombre de la columna sobre la cual se realizará la estratificación.
     * @param porcentaje Porcentaje de filas a incluir de cada estrato. Debe estar entre 1 y 100.
     * @return Nueva instancia de {@link Tabla} con la muestra estratificada seleccionada.
     * @throws IllegalArgumentException si la columna no existe o el porcentaje no es válido.
     */
    Tabla muestrearEstratificado(String nombreColumna, int porcentaje);
}
