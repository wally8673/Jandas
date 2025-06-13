package jandas.operaciones;

import jandas.base.data.Tabla;
import jandas.operaciones.estadisticas.OperacionEstadistica;

import java.util.Map;

public interface Agrupable {

    /**
     * Agrupa las filas por una columna y aplica operaciones de agregación
     * @param nombreColumna Nombre de la columna para agrupar
     * @param operaciones Mapa de operaciones a aplicar por columna
     * @return Nueva tabla con los resultados agregados
     */
    Tabla agruparPor(String nombreColumna, Map<String, OperacionEstadistica> operaciones);

    /**
     * Agrupa las filas por múltiples columnas y aplica operaciones de agregación
     * @param nombresColumnas Nombres de las columnas para agrupar
     * @param operaciones Mapa de operaciones a aplicar por columna
     * @return Nueva tabla con los resultados agregados
     */
    Tabla agruparPor(String[] nombresColumnas, Map<String, OperacionEstadistica> operaciones);

    /**
     * Agrupa por una columna y aplica la misma operación a todas las columnas numéricas
     * @param nombreColumna Nombre de la columna para agrupar
     * @param operacion Operación a aplicar
     * @return Nueva tabla con los resultados agregados
     */
    Tabla agruparPor(String nombreColumna, OperacionEstadistica operacion);
}
