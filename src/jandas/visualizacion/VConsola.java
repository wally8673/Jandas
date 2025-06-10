package jandas.visualizacion;

import jandas.base.data.Tabla;
import jandas.base.data.Fila;
import jandas.base.etiquetas.Etiqueta;

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
    public void visualizar(Tabla tabla, int maxFilas, int maxColumnas, int maxLargoCadena) {
        // Crea una configuracion temporal con los parametros proporcionados
        VConfig tempConfig = new VConfig(maxFilas, maxColumnas, maxLargoCadena);
        visualizarConConfig(tabla, tempConfig);
    }

    /**
     * Visualiza el dataframe utilizando la configuracion especificada.
     *
     * @param tabla El DataFrame a visualizar
     * @param config La configuracion de visualizacion a utilizar
     */
    public void visualizarConConfig(Tabla tabla, VConfig config) {
        if (tabla == null) {
            System.out.println("DataFrame es null");
            return;
        }

        if (tabla.cantColumnas() == 0) {
            System.out.println("DataFrame vacío");
            return;
        }

        // Imprime informacion del DataFrame
        System.out.println("DataFrame: " + tabla.cantFilas() + " filas × " + tabla.cantColumnas() + " columnas");

        // Calcula ancho de columnas
        List<Integer> anchos = FormatoTabla.calcularAnchos(tabla, config.getMaxColumnas(), config.getMaxLargoCadena());

        // crea separador de encabezado
        String separador = FormatoTabla.crearLineaSeparadora(anchos, config);

        // imprime encabezado
        System.out.println(separador);
        System.out.println(FormatoTabla.formatearEncabezado(tabla, anchos, config));
        System.out.println(separador);

        // imprime filas
        List<Etiqueta> etiquetasFilas = tabla.getEtiquetasFilas();
        int numFilas = Math.min(etiquetasFilas.size(), config.getMaxFilas());

        for (int i = 0; i < numFilas; i++) {
            Etiqueta etiquetaFila = etiquetasFilas.get(i);
            Fila fila = tabla.getFila(etiquetaFila);
            System.out.println(FormatoTabla.formatearFila(fila, i, anchos, config));
        }

        System.out.println(separador);

        // Si muestra menos filas o columnas que las originales, avisar por pantalla
        if (tabla.cantFilas() > config.getMaxFilas() || tabla.cantColumnas() > config.getMaxColumnas()) {
            System.out.println("Tabla acotada. Mostrando " +
                Math.min(tabla.cantFilas(), config.getMaxFilas()) + " de " + tabla.cantFilas() + " filas y " +
                Math.min(tabla.cantColumnas(), config.getMaxColumnas()) + " de " + tabla.cantColumnas() + " columnas.");
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
