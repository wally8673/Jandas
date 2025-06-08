package jandas.io.csv;

import jandas.io.LeerConfig;

public class LeerCsvConfig extends LeerConfig{
    private String separador;
    private boolean tieneEncabezado;
    private String valorNulo;
    
    public LeerCsvConfig() {
        this.separador = ",";
        this.tieneEncabezado = true;
        this.valorNulo = "NA";
    }
    
    public LeerCsvConfig(String separador, boolean tieneEncabezado) {
        this.separador = separador;
        this.tieneEncabezado = tieneEncabezado;
        this.valorNulo = "NA";
    }

    // Getters and setters
    public String getSeparador() { return separador; }
    public void setSeparador(String separador) { this.separador = separador; }
    
    public boolean isTieneEncabezado() { return tieneEncabezado; }
    public void setTieneEncabezado(boolean tieneEncabezado) { this.tieneEncabezado = tieneEncabezado; }
    
    public String getValorNulo() { return valorNulo; }
    public void setValorNulo(String valorNulo) { this.valorNulo = valorNulo; }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getDescription() {
        return "";
    }
}