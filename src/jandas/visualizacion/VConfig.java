package jandas.visualizacion;

/**
 * Clase para configurar la visualización de DataFrames.
 * Contiene configuraciones que controlan cómo se muestran los DataFrames.
 */
public class VConfig {
    private int maxFilas;
    private int maxColumnas;
    private int maxLargoCadena;
    private String na;
    private String separadorCelda;
    private String separadorHeader;
    private boolean mostrarEtiquetaFila;
    
    /**
     * crea una nueva configuración de visualización con configuraciones predeterminadas.
     */
    public VConfig() {
        this.maxFilas = 10;
        this.maxColumnas = 10;
        this.maxLargoCadena = 20;
        this.na = "NA";
        this.separadorCelda = " | ";
        this.separadorHeader = "-";
        this.mostrarEtiquetaFila = false;
    }
    
    /**
     * crea una nueva configuración de visualización con configuraciones personalizadas.
     * 
     * @param maxFilas Número máximo de filas a mostrar
     * @param maxColumnas Número máximo de columnas a mostrar
     * @param maxLargoCadena Longitud máxima de los valores de cadena en las celdas
     */
    public VConfig(int maxFilas, int maxColumnas, int maxLargoCadena) {
        this();
        this.maxFilas = maxFilas;
        this.maxColumnas = maxColumnas;
        this.maxLargoCadena = maxLargoCadena;
    }
    
    // Getters and setters
    
    public int getMaxFilas() {
        return maxFilas;
    }
    
    public void setMaxFilas(int maxFilas) {
        this.maxFilas = maxFilas;
    }
    
    public int getMaxColumnas() {
        return maxColumnas;
    }
    
    public void setMaxColumnas(int maxColumnas) {
        this.maxColumnas = maxColumnas;
    }
    
    public int getMaxLargoCadena() {
        return maxLargoCadena;
    }
    
    public void setMaxLargoCadena(int maxLargoCadena) {
        this.maxLargoCadena = maxLargoCadena;
    }
    
    public String getNa() {
        return na;
    }
    
    public void setNa(String na) {
        this.na = na;
    }
    
    public String getSeparadorCelda() {
        return separadorCelda;
    }
    
    public void setSeparadorCelda(String separadorCelda) {
        this.separadorCelda = separadorCelda;
    }
    
    public String getSeparadorHeader() {
        return separadorHeader;
    }
    
    public void setSeparadorHeader(String separadorHeader) {
        this.separadorHeader = separadorHeader;
    }
    
    public boolean isMostrarEtiquetaFila() {
        return mostrarEtiquetaFila;
    }
    
    public void setMostrarEtiquetaFila(boolean mostrarEtiquetaFila) {
        this.mostrarEtiquetaFila = mostrarEtiquetaFila;
    }
}