package jandas.visualizacion;

import jandas.core.data.Celda;
import jandas.core.data.Columna;
import jandas.core.data.DataFrame;
import jandas.core.data.Fila;
import jandas.core.etiquetas.Etiqueta;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for formatting DataFrames as text tables.
 * Provides methods to create formatted strings for console display.
 */
public class TableFormatter {
    
    /**
     * Formats a cell value according to display configuration.
     * 
     * @param valor The cell value as string
     * @param maxLargoCadena Maximum length for the string
     * @return Formatted cell value
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
     * Creates a separator line for the table.
     * 
     * @param anchos List of column widths
     * @param config Display configuration
     * @return Separator line string
     */
    public static String crearLineaSeparadora(List<Integer> anchos, DisplayConfiguration config) {
        StringBuilder separador = new StringBuilder();
        String sep = config.getHeaderSeparator();
        
        if (config.isShowRowNumbers()) {
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
     * Calculates the width needed for each column.
     * 
     * @param dataFrame The DataFrame
     * @param maxColumnas Maximum number of columns to consider
     * @param maxLargoCadena Maximum length for cell values
     * @return List of column widths
     */
    public static List<Integer> calcularAnchos(DataFrame dataFrame, int maxColumnas, int maxLargoCadena) {
        List<Integer> anchos = new ArrayList<>();
        List<Etiqueta> etiquetasColumnas = dataFrame.getEtiquetasColumnas();
        int numColumnas = Math.min(etiquetasColumnas.size(), maxColumnas);
        
        for (int i = 0; i < numColumnas; i++) {
            Etiqueta etiqueta = etiquetasColumnas.get(i);
            int ancho = etiqueta.toString().length();
            
            Columna<?> columna = dataFrame.getColumna(etiqueta);
            for (int j = 0; j < Math.min(columna.size(), dataFrame.cantFilas()); j++) {
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
     * Formats a header row for the table.
     * 
     * @param dataFrame The DataFrame
     * @param anchos List of column widths
     * @param config Display configuration
     * @return Formatted header row string
     */
    public static String formatearEncabezado(DataFrame dataFrame, List<Integer> anchos, DisplayConfiguration config) {
        StringBuilder encabezado = new StringBuilder();
        List<Etiqueta> etiquetasColumnas = dataFrame.getEtiquetasColumnas();
        int numColumnas = Math.min(etiquetasColumnas.size(), config.getMaxColumnas());
        
        if (config.isShowRowNumbers()) {
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
     * Formats a data row for the table.
     * 
     * @param fila The row to format
     * @param indice Row index
     * @param anchos List of column widths
     * @param config Display configuration
     * @return Formatted data row string
     */
    public static String formatearFila(Fila fila, int indice, List<Integer> anchos, DisplayConfiguration config) {
        StringBuilder filaStr = new StringBuilder();
        List<Celda<?>> celdas = fila.getCeldasFila();
        int numColumnas = Math.min(celdas.size(), config.getMaxColumnas());
        
        if (config.isShowRowNumbers()) {
            filaStr.append("| ").append(String.format("%-4s", indice)).append(" |");
        } else {
            filaStr.append("|");
        }
        
        for (int i = 0; i < numColumnas; i++) {
            Celda<?> celda = celdas.get(i);
            String valor = celda.esNA() ? config.getNaRepresentation() : celda.toString();
            valor = formatearCelda(valor, config.getMaxLargoCadena());
            int ancho = anchos.get(i);
            filaStr.append(" ").append(String.format("%-" + ancho + "s", valor)).append(" |");
        }
        
        return filaStr.toString();
    }
}