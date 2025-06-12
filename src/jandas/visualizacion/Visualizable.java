package jandas.visualizacion;

import jandas.base.data.Tabla;

public interface Visualizable {
    /**
     * Visualiza el objeto con parámetros personalizados
     *
     * @param maxFilas Número máximo de filas a mostrar
     * @param maxColumnas Número máximo de columnas a mostrar
     * @param maxLargoCadena Longitud máxima para los valores de las celdas
     */
    void visualizar(Tabla tabla,int maxFilas, int maxColumnas, int maxLargoCadena);

    /**
     * Visualiza el objeto con parámetros por defecto
     */
    void visualizar(Tabla tabla);
}

