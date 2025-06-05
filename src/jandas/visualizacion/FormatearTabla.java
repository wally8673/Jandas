package jandas.visualizacion;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.DataFrame;
import jandas.base.data.Fila;
import jandas.base.etiquetas.Etiqueta;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase para darle formato a un DataFrame como una tabla de texto.
 * Proporciona métodos para crear cadenas formateadas para la visualización en la consola.
 */
public class FormatearTabla {
    
    /**
     * Da formato a una celda según la configuración de visualización.
     * 
     * @param valor El valor de la celda como cadena
     * @param maxLargoCadena Longitud máxima para la cadena
     * @return Valor de la celda formateado
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
     * Crea una línea de separación para la tabla.
     * 
     * @param anchos Lista de anchos de columnas
     * @param config Configuración de visualización
     * @return Cadena de línea de separación
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
     * Calcula la anchura necesaria para cada columna.
     * 
     * @param dataFrame El DataFrame
     * @param maxColumnas Número máximo de columnas a considerar
     * @param maxLargoCadena Longitud máxima para los valores de las celdas
     * @return Lista de anchuras de columnas
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
     * Formatea una fila de encabezado para la tabla.
     * 
     * @param dataFrame El DataFrame
     * @param anchos Lista de anchuras de columnas
     * @param config Configuración de visualización
     * @return Cadena de fila de encabezado formateada
     */
    public static String formatearEncabezado(DataFrame dataFrame, List<Integer> anchos, VConfig config) {
        StringBuilder encabezado = new StringBuilder();
        List<Etiqueta> etiquetasColumnas = dataFrame.getEtiquetasColumnas();
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
     * Formatea una fila de datos para la tabla.
     * 
     * @param fila La fila a formatear
     * @param indice Índice de la fila
     * @param anchos Lista de anchuras de columnas
     * @param config Configuración de visualización
     * @return Cadena de fila de datos formateada
     */
    public static String formatearFila(Fila fila, int indice, List<Integer> anchos, VConfig config) {
        StringBuilder filaStr = new StringBuilder();
        List<Celda<?>> celdas = fila.getCeldasFila();
        int numColumnas = Math.min(celdas.size(), config.getMaxColumnas());
        
        if (config.isMostrarEtiquetaFila()) {
            filaStr.append("| ").append(String.format("%-4s", indice)).append(" |");
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