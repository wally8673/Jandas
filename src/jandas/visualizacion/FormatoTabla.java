package jandas.visualizacion;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Fila;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para dar formato textual a una {@link Tabla} como una tabla de texto,
 * ideal para su visualización en consola.
 * <p>
 * Contiene métodos estáticos para:
 * <ul>
 *     <li>Formatear valores de celdas truncando cadenas largas</li>
 *     <li>Calcular los anchos de columnas</li>
 *     <li>Generar encabezados y líneas separadoras</li>
 *     <li>Renderizar filas de datos con formato ajustado</li>
 * </ul>
 * El comportamiento de visualización se controla mediante un objeto {@link VConfig}.
 * </p>
 */
public class FormatoTabla {

    /**
     * Da formato a una celda, recortando la cadena si supera la longitud máxima permitida.
     *
     * @param valor El valor de la celda como cadena.
     * @param maxLargoCadena Longitud máxima permitida para la cadena.
     * @return El valor recortado y formateado, con "..." si fue truncado.
     */
    public static String formatearCelda(String valor, int maxLargoCadena) {
        if (valor == null) {
            return "NA";
        }

        if (valor.length() > maxLargoCadena) {
            return valor.substring(0, maxLargoCadena - 3) + "...";
        }

        return valor;
    }

    /**
     * Crea una línea separadora (horizontal) que delimita encabezados y filas.
     *
     * @param anchos Lista de anchos de columnas.
     * @param config Configuración de visualización.
     * @return Cadena que representa la línea separadora.
     */
    public static String crearLineaSeparadora(List<Integer> anchos, VConfig config) {
        StringBuilder separador = new StringBuilder();
        String sep = config.getSeparadorHeader();

        if (config.isMostrarEtiquetaFila()) {
            separador.append("+").append(sep.repeat(6)).append("+");
        } else {
            separador.append("+");
        }

        for (int ancho : anchos) {
            separador.append(sep.repeat(ancho + 2)).append("+");
        }

        return separador.toString();
    }

    /**
     * Calcula el ancho necesario para cada columna en función del contenido
     * y del encabezado, teniendo en cuenta un máximo de longitud por celda.
     *
     * @param tabla El DataFrame a visualizar.
     * @param maxColumnas Máximo número de columnas a considerar.
     * @param maxLargoCadena Longitud máxima por celda.
     * @return Lista de anchos por columna.
     */
    public static List<Integer> calcularAnchos(Tabla tabla, int maxColumnas, int maxLargoCadena) {
        List<Integer> anchos = new ArrayList<>();
        List<Etiqueta> etiquetasColumnas = tabla.getEtiquetasColumnas();
        int numColumnas = Math.min(etiquetasColumnas.size(), maxColumnas);

        for (int i = 0; i < numColumnas; i++) {
            Etiqueta etiqueta = etiquetasColumnas.get(i);
            int ancho = etiqueta.toString().length();

            Columna<?> columna = tabla.getColumna(etiqueta);
            for (int j = 0; j < Math.min(columna.size(), tabla.cantFilas()); j++) {
                Celda<?> celda = columna.getCelda(j);
                String valorStr = celda.toString();
                int largoValor = Math.min(valorStr.length(), maxLargoCadena);
                ancho = Math.max(ancho, largoValor);
            }

            anchos.add(ancho);
        }

        return anchos;
    }

    /**
     * Crea la fila del encabezado (nombres de columnas) con alineación adecuada.
     *
     * @param tabla El DataFrame del cual extraer las etiquetas de columna.
     * @param anchos Lista de anchos calculados por columna.
     * @param config Configuración de visualización.
     * @return Cadena representando la fila de encabezado.
     */
    public static String formatearEncabezado(Tabla tabla, List<Integer> anchos, VConfig config) {
        StringBuilder encabezado = new StringBuilder();
        List<Etiqueta> etiquetasColumnas = tabla.getEtiquetasColumnas();
        int numColumnas = Math.min(etiquetasColumnas.size(), config.getMaxColumnas());

        if (config.isMostrarEtiquetaFila()) {
            encabezado.append("| Idx  |");
        } else {
            encabezado.append("|");
        }

        for (int i = 0; i < numColumnas; i++) {
            String etiqueta = etiquetasColumnas.get(i).toString();
            int ancho = anchos.get(i);
            encabezado.append(" ").append(String.format("%-" + ancho + "s", etiqueta)).append(" |");
        }

        return encabezado.toString();
    }

    /**
     * Formatea una fila individual de datos, alineando cada celda y aplicando la configuración.
     *
     * @param fila Fila a formatear.
     * @param etiquetaFila Etiqueta correspondiente a la fila.
     * @param anchos Lista de anchos de columna.
     * @param config Configuración de visualización.
     * @return Cadena representando la fila formateada.
     */
    public static String formatearFila(Fila fila, Etiqueta etiquetaFila, List<Integer> anchos, VConfig config) {
        StringBuilder filaStr = new StringBuilder();
        List<Celda<?>> celdas = fila.getCeldasFila();
        int numColumnas = Math.min(celdas.size(), config.getMaxColumnas());

        if (config.isMostrarEtiquetaFila()) {
            filaStr.append("| ").append(String.format("%-4s", etiquetaFila.toString())).append(" |");
        } else {
            filaStr.append("|");
        }

        for (int i = 0; i < numColumnas; i++) {
            Celda<?> celda = celdas.get(i);
            String valor = celda.esNA() ? config.getNa() : celda.toString();
            valor = formatearCelda(valor, config.getMaxLargoCadena());
            int ancho = anchos.get(i);
            filaStr.append(" ").append(String.format("%-" + ancho + "s", valor)).append(" |");
        }

        return filaStr.toString();
    }
}
