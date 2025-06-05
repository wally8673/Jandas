package jandas.visualizacion;

import jandas.core.data.DataFrame;
import jandas.core.data.Fila;
import jandas.core.etiquetas.Etiqueta;

import java.util.List;

/**
 * Implementacion de la visualizacion por consola del dataframe.
 * Muestra los datos en forma de tablas formateadas en la consola.
 */
public class VConsola implements Visualizable {

    private VConfig config;

    /**
     * Crea una nueva visualizacion por consola con configuracion por defecto.
     */
    public VConsola() {
        this.config = new VConfig();
    }

    /**
     * Crea una nueva visualizacion por consola con la configuracion proporcionada.
     *
     * @param config La configuracion de visualizacion a utilizar
     */
    public VConsola(VConfig config) {
        this.config = config;
    }

    @Override
    public void visualizar(DataFrame dataFrame, int maxFilas, int maxColumnas, int maxLargoCadena) {
        // Crea una configuracion temporal con los parametros proporcionados
        VConfig tempConfig = new VConfig(maxFilas, maxColumnas, maxLargoCadena);
        visualizarConConfig(dataFrame, tempConfig);
    }

    /**
     * Visualiza el dataframe utilizando la configuracion especificada.
     *
     * @param dataFrame El DataFrame a visualizar
     * @param config La configuracion de visualizacion a utilizar
     */
    public void visualizarConConfig(DataFrame dataFrame, VConfig config) {
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
        List<Integer> anchos = FormatearTabla.calcularAnchos(dataFrame, config.getMaxColumnas(), config.getMaxLargoCadena());

        // Create separator line
        String separador = FormatearTabla.crearLineaSeparadora(anchos, config);

        // Print header
        System.out.println(separador);
        System.out.println(FormatearTabla.formatearEncabezado(dataFrame, anchos, config));
        System.out.println(separador);

        // Print rows
        List<Etiqueta> etiquetasFilas = dataFrame.getEtiquetasFilas();
        int numFilas = Math.min(etiquetasFilas.size(), config.getMaxFilas());

        for (int i = 0; i < numFilas; i++) {
            Etiqueta etiquetaFila = etiquetasFilas.get(i);
            Fila fila = dataFrame.getFila(etiquetaFila);
            System.out.println(FormatearTabla.formatearFila(fila, i, anchos, config));
        }

        System.out.println(separador);

        // Si muestra menos filas o columnas que las originales, avisar por pantalla
        if (dataFrame.cantFilas() > config.getMaxFilas() || dataFrame.cantColumnas() > config.getMaxColumnas()) {
            System.out.println("Nota: Tabla acotada. Mostrando " +
                Math.min(dataFrame.cantFilas(), config.getMaxFilas()) + " de " + dataFrame.cantFilas() + " filas y " +
                Math.min(dataFrame.cantColumnas(), config.getMaxColumnas()) + " de " + dataFrame.cantColumnas() + " columnas.");
        }
    }

    /**
     * Obtiene la configuracion actual de visualizacion.
     *
     * @return La configuracion actual de visualizacion
     */
    public VConfig getConfig() {
        return config;
    }

    /**
     * Se establece una nueva configuracion de visualizacion.
     *
     * @param config La nueva configuracion de visualizacion
     */
    public void setConfig(VConfig config) {
        this.config = config;
    }
}
