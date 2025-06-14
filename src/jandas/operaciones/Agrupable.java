package jandas.operaciones;

import jandas.base.data.Tabla;
import jandas.operaciones.estadisticas.OperacionEstadistica;

import java.util.Map;

/**
 * Interfaz que define métodos para agrupar filas de una {@link Tabla} y aplicar operaciones
 * estadísticas o agregaciones sobre los grupos formados.
 */
public interface Agrupable {

    /**
     * Agrupa las filas de la tabla según los valores de una columna específica
     * y aplica distintas operaciones estadísticas a columnas seleccionadas.
     *
     * @param nombreColumna Nombre de la columna que define los grupos.
     * Cada valor único de esta columna crea un grupo.
     * @param operaciones Mapa donde las claves son nombres de columnas sobre las que
     * se aplicarán las operaciones, y los valores son las operaciones
     * estadísticas a aplicar ({@link OperacionEstadistica}).
     * @return Nueva instancia de {@link Tabla} que contiene los resultados de las
     * operaciones agregadas para cada grupo.
     * @throws IllegalArgumentException si la columna para agrupar no existe o
     * alguna columna del mapa no está presente en la tabla.
     */
    Tabla agruparPor(String nombreColumna, Map<String, OperacionEstadistica> operaciones);

    /**
     * Agrupa las filas de la tabla según los valores combinados de múltiples columnas
     * y aplica operaciones estadísticas a columnas seleccionadas.
     *
     * @param nombresColumnas Array con los nombres de las columnas usadas para agrupar.
     * Cada combinación única de valores en estas columnas define un grupo.
     * @param operaciones Mapa donde las claves son nombres de columnas sobre las que
     * se aplicarán las operaciones, y los valores son las operaciones
     * estadísticas a aplicar ({@link OperacionEstadistica}).
     * @return Nueva instancia de {@link Tabla} que contiene los resultados de las
     * operaciones agregadas para cada grupo.
     * @throws IllegalArgumentException si alguna columna para agrupar o del mapa
     * no existe en la tabla.
     */
    Tabla agruparPor(String[] nombresColumnas, Map<String, OperacionEstadistica> operaciones);

    /**
     * Agrupa las filas de la tabla según los valores de una columna específica
     * y aplica la misma operación estadística a todas las columnas numéricas.
     *
     * @param nombreColumna Nombre de la columna que define los grupos.
     * Cada valor único crea un grupo.
     * @param operacion Operación estadística a aplicar a todas las columnas numéricas
     * de cada grupo ({@link OperacionEstadistica}).
     * @return Nueva instancia de {@link Tabla} con los resultados de la agregación.
     * @throws IllegalArgumentException si la columna para agrupar no existe.
     */
    Tabla agruparPor(String nombreColumna, OperacionEstadistica operacion);
}

