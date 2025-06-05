package jandas.visualizacion;

/**
 * Configuration class for DataFrame visualization.
 * Contains settings that control how DataFrames are displayed.
 */
public class DisplayConfiguration {
    private int maxFilas;
    private int maxColumnas;
    private int maxLargoCadena;
    private String naRepresentation;
    private String cellSeparator;
    private String headerSeparator;
    private boolean showRowNumbers;
    
    /**
     * Creates a new DisplayConfiguration with default settings.
     */
    public DisplayConfiguration() {
        this.maxFilas = 10;
        this.maxColumnas = 10;
        this.maxLargoCadena = 20;
        this.naRepresentation = "NA";
        this.cellSeparator = " | ";
        this.headerSeparator = "-";
        this.showRowNumbers = true;
    }
    
    /**
     * Creates a new DisplayConfiguration with custom settings.
     * 
     * @param maxFilas Maximum number of rows to display
     * @param maxColumnas Maximum number of columns to display
     * @param maxLargoCadena Maximum length of string values in cells
     */
    public DisplayConfiguration(int maxFilas, int maxColumnas, int maxLargoCadena) {
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
    
    public String getNaRepresentation() {
        return naRepresentation;
    }
    
    public void setNaRepresentation(String naRepresentation) {
        this.naRepresentation = naRepresentation;
    }
    
    public String getCellSeparator() {
        return cellSeparator;
    }
    
    public void setCellSeparator(String cellSeparator) {
        this.cellSeparator = cellSeparator;
    }
    
    public String getHeaderSeparator() {
        return headerSeparator;
    }
    
    public void setHeaderSeparator(String headerSeparator) {
        this.headerSeparator = headerSeparator;
    }
    
    public boolean isShowRowNumbers() {
        return showRowNumbers;
    }
    
    public void setShowRowNumbers(boolean showRowNumbers) {
        this.showRowNumbers = showRowNumbers;
    }
}