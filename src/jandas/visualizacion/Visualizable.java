package jandas.visualizacion;

import jandas.core.data.DataFrame;

/**
 * Interface for visualization strategies in the Jandas library.
 * Implementations of this interface provide different ways to visualize DataFrame objects.
 */
public interface Visualizable {

    /**
     * Visualizes a DataFrame with specified constraints.
     *
     * @param dataFrame The DataFrame to visualize
     * @param maxFilas Maximum number of rows to display
     * @param maxColumnas Maximum number of columns to display
     * @param maxLargoCadena Maximum length of string values in cells
     */
    void visualizar(DataFrame dataFrame, int maxFilas, int maxColumnas, int maxLargoCadena);

    /**
     * Visualizes a DataFrame with default constraints.
     *
     * @param dataFrame The DataFrame to visualize
     */
    default void visualizar(DataFrame dataFrame) {
        visualizar(dataFrame, 10, 10, 20);
    }
}
