package jandas.visualizacion;

import jandas.core.data.DataFrame;
import jandas.core.data.Fila;
import jandas.core.etiquetas.Etiqueta;

import java.util.List;

/**
 * Console visualization implementation for DataFrames.
 * Displays DataFrames as formatted text tables in the console.
 */
public class VConsola implements Visualizable {

    private DisplayConfiguration config;

    /**
     * Creates a new console visualizer with default configuration.
     */
    public VConsola() {
        this.config = new DisplayConfiguration();
    }

    /**
     * Creates a new console visualizer with custom configuration.
     *
     * @param config The display configuration to use
     */
    public VConsola(DisplayConfiguration config) {
        this.config = config;
    }

    @Override
    public void visualizar(DataFrame dataFrame, int maxFilas, int maxColumnas, int maxLargoCadena) {
        // Create a temporary configuration with the provided parameters
        DisplayConfiguration tempConfig = new DisplayConfiguration(maxFilas, maxColumnas, maxLargoCadena);
        visualizarConConfig(dataFrame, tempConfig);
    }

    /**
     * Visualizes a DataFrame using the specified configuration.
     *
     * @param dataFrame The DataFrame to visualize
     * @param config The display configuration to use
     */
    public void visualizarConConfig(DataFrame dataFrame, DisplayConfiguration config) {
        if (dataFrame == null) {
            System.out.println("DataFrame es null");
            return;
        }

        if (dataFrame.cantColumnas() == 0) {
            System.out.println("DataFrame vacío");
            return;
        }

        // Print DataFrame information
        System.out.println("DataFrame: " + dataFrame.cantFilas() + " filas × " + dataFrame.cantColumnas() + " columnas");

        // Calculate column widths
        List<Integer> anchos = TableFormatter.calcularAnchos(dataFrame, config.getMaxColumnas(), config.getMaxLargoCadena());

        // Create separator line
        String separador = TableFormatter.crearLineaSeparadora(anchos, config);

        // Print header
        System.out.println(separador);
        System.out.println(TableFormatter.formatearEncabezado(dataFrame, anchos, config));
        System.out.println(separador);

        // Print rows
        List<Etiqueta> etiquetasFilas = dataFrame.getEtiquetasFilas();
        int numFilas = Math.min(etiquetasFilas.size(), config.getMaxFilas());

        for (int i = 0; i < numFilas; i++) {
            Etiqueta etiquetaFila = etiquetasFilas.get(i);
            Fila fila = dataFrame.getFila(etiquetaFila);
            System.out.println(TableFormatter.formatearFila(fila, i, anchos, config));
        }

        System.out.println(separador);

        // Show truncation message if necessary
        if (dataFrame.cantFilas() > config.getMaxFilas() || dataFrame.cantColumnas() > config.getMaxColumnas()) {
            System.out.println("Nota: Tabla truncada. Mostrando " +
                Math.min(dataFrame.cantFilas(), config.getMaxFilas()) + " de " + dataFrame.cantFilas() + " filas y " +
                Math.min(dataFrame.cantColumnas(), config.getMaxColumnas()) + " de " + dataFrame.cantColumnas() + " columnas.");
        }
    }

    /**
     * Gets the current display configuration.
     *
     * @return The current display configuration
     */
    public DisplayConfiguration getConfig() {
        return config;
    }

    /**
     * Sets a new display configuration.
     *
     * @param config The new display configuration
     */
    public void setConfig(DisplayConfiguration config) {
        this.config = config;
    }
}
