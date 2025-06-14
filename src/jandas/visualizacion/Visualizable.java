package jandas.visualizacion;

import jandas.base.data.Tabla;

/**
 * Interfaz para la visualización de tablas (DataFrames) en la librería Jandas.
 * <p>
 * Las implementaciones de esta interfaz proveen diferentes formas de visualizar
 * objetos de tipo {@link Tabla}, permitiendo personalizar la cantidad de filas,
 * columnas y el ancho máximo de las celdas mostradas.
 * </p>
 */
public interface Visualizable {

    /**
     * Visualiza la tabla con parámetros personalizados.
     *
     * @param tabla La tabla (DataFrame) que se desea visualizar.
     * @param maxFilas Número máximo de filas a mostrar.
     * @param maxColumnas Número máximo de columnas a mostrar.
     * @param maxLargoCadena Valor máximo de caracteres a mostrar en cada celda.
     */
    void visualizar(Tabla tabla, int maxFilas, int maxColumnas, int maxLargoCadena);

    /**
     * Visualiza la tabla con parámetros predeterminados.
     * <p>
     * Este método permite visualizar la tabla completa o con configuraciones
     * por defecto, dependiendo de la implementación.
     * </p>
     *
     * @param tabla La tabla (DataFrame) que se desea visualizar.
     */
    void visualizar(Tabla tabla);
}

