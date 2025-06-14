package jandas.operaciones;

import jandas.base.data.Tabla;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.Orden;

import java.util.List;

/**
 * Interfaz que define métodos para ordenar las filas de una {@link Tabla} según uno o varios criterios.
 */
public interface Ordenable {

    /**
     * Ordena la tabla según una columna específica y una dirección de ordenamiento.
     *
     * @param nombreColumna Nombre de la columna sobre la que se realizará el ordenamiento.
     * @param direccion Dirección del orden: ascendente o descendente ({@link Orden}).
     * @return Nueva instancia de {@link Tabla} con las filas ordenadas según el criterio indicado.
     * @throws IllegalArgumentException si la columna no existe o la dirección es nula.
     */
    Tabla ordenar(String nombreColumna, Orden direccion);

    /**
     * Ordena la tabla según varios criterios expresados como cadenas de texto.
     * Cada criterio puede indicar la columna y la dirección (por ejemplo, "edad:asc", "nombre:desc").
     *
     * @param criterios Array de criterios de ordenamiento en formato String.
     * @return Nueva instancia de {@link Tabla} ordenada según los criterios proporcionados.
     * @throws IllegalArgumentException si alguno de los criterios tiene formato inválido o referencia columnas inexistentes.
     */
    Tabla ordenar(String... criterios);

    /**
     * Ordena la tabla usando una lista de criterios de ordenamiento más estructurados.
     *
     * @param criterios Lista de objetos {@link CriterioOrden} que definen las columnas y direcciones
     * de ordenamiento en orden de prioridad.
     * @return Nueva instancia de {@link Tabla} ordenada según los criterios especificados.
     * @throws IllegalArgumentException si la lista es nula, vacía o contiene criterios inválidos.
     */
    Tabla ordenarPorCriterios(List<CriterioOrden> criterios);
}

