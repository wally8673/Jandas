package jandas.visualizacion;

import jandas.base.data.Fila;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;

import java.util.List;

/**
 * Implementación de la visualización por consola para un objeto {@link Tabla}.
 * <p>
 * Esta clase permite imprimir el contenido de una tabla en formato tabular en la consola,
 * con soporte para configuraciones de visualización como límite de filas, columnas
 * y longitud de celdas mostradas.
 * </p>
 */
public class VConsola implements Visualizable {

    /** Configuración de visualización actual */
    private VConfig config;

    /**
     * Crea una nueva instancia de visualización por consola
     * con la configuración por defecto.
     */
    public VConsola() {
        this.config = new VConfig();
    }

    /**
     * Crea una nueva instancia de visualización por consola
     * con una configuración personalizada.
     *
     * @param config La configuración de visualización a utilizar.
     */
    public VConsola(VConfig config) {
        this.config = config;
    }

    /**
     * Visualiza una tabla utilizando parámetros personalizados.
     *
     * @param tabla La tabla a visualizar.
     * @param maxFilas Número máximo de filas a mostrar.
     * @param maxColumnas Número máximo de columnas a mostrar.
     * @param maxLargoCadena  Número máximo de caracteres por celda.
     */
    @Override
    public void visualizar(Tabla tabla, int maxFilas, int maxColumnas, int maxLargoCadena) {
        VConfig tempConfig = new VConfig(maxFilas, maxColumnas, maxLargoCadena);
        visualizarConConfig(tabla, tempConfig);
    }

    /**
     * Visualiza una tabla utilizando la configuración predeterminada.
     *
     * @param tabla La tabla a visualizar.
     */
    @Override
    public void visualizar(Tabla tabla) {
        visualizarConConfig(tabla, config);
    }

    /**
     * Visualiza la tabla utilizando una configuración específica.
     *
     * @param tabla  La tabla a visualizar.
     * @param config Configuración de visualización a utilizar.
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

        System.out.println("DataFrame con " + tabla.cantFilas() + " filas × " + tabla.cantColumnas() + " columnas");

        List<Integer> anchos = FormatoTabla.calcularAnchos(tabla, config.getMaxColumnas(), config.getMaxLargoCadena());
        String separador = FormatoTabla.crearLineaSeparadora(anchos, config);

        System.out.println(separador);
        System.out.println(FormatoTabla.formatearEncabezado(tabla, anchos, config));
        System.out.println(separador);

        List<Etiqueta> etiquetasFilas = tabla.getEtiquetasFilas();
        int numFilas = Math.min(etiquetasFilas.size(), config.getMaxFilas());

        for (int i = 0; i < numFilas; i++) {
            Etiqueta etiquetaFila = etiquetasFilas.get(i);
            Fila fila = tabla.getFila(etiquetaFila);
            System.out.println(FormatoTabla.formatearFila(fila, etiquetaFila, anchos, config));
        }

        System.out.println(separador);

        if (tabla.cantFilas() > config.getMaxFilas() || tabla.cantColumnas() > config.getMaxColumnas()) {
            System.out.println("Tabla acotada. Mostrando " +
                    Math.min(tabla.cantFilas(), config.getMaxFilas()) + " de " + tabla.cantFilas() + " filas y " +
                    Math.min(tabla.cantColumnas(), config.getMaxColumnas()) + " de " + tabla.cantColumnas() + " columnas.\n");
        } else {
            System.out.println("Mostrando todas las filas y columnas.\n");
        }
    }

    /**
     * Devuelve la configuración de visualización actual.
     *
     * @return La configuración actual.
     */
    public VConfig getConfig() {
        return config;
    }

    /**
     * Establece una nueva configuración de visualización.
     *
     * @param config Nueva configuración a utilizar.
     */
    public void setConfig(VConfig config) {
        this.config = config;
    }
}

