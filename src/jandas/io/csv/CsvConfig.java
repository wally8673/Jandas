package jandas.io.csv;

public class CsvConfig {
    private String separador;
    private boolean tieneEncabezado;
    private String valorNulo;
    
    public CsvConfig() {
        this.separador = ",";
        this.tieneEncabezado = true;
        this.valorNulo = "NA";
    }

    public CsvConfig(String separador, boolean tieneEncabezado) {
        this.separador = separador;
        this.tieneEncabezado = tieneEncabezado;
        this.valorNulo = "NA";
    }
    
    public CsvConfig(String separador, boolean tieneEncabezado, String valorNulo) {
        this.separador = separador;
        this.tieneEncabezado = tieneEncabezado;
        this.valorNulo = valorNulo;
    }

    // Getters and setters
    public String getSeparador() { return separador; }
    public void setSeparador(String separador) { this.separador = separador; }
    
    public boolean isTieneEncabezado() { return tieneEncabezado; }
    public void setTieneEncabezado(boolean tieneEncabezado) { this.tieneEncabezado = tieneEncabezado; }
    
    public String getValorNulo() { return valorNulo; }
    public void setValorNulo(String valorNulo) { this.valorNulo = valorNulo; }

}