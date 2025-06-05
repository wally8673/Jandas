package jandas.base.data;

import jandas.base.etiquetas.Etiqueta;

import java.util.List;

public class FilaGenerica implements Fila {

    private Etiqueta etiquetaFila;
    private List<Celda<?>> celdas;
    private List<Etiqueta> etiquetaColumnas;

    public FilaGenerica(Etiqueta etiquetaFila, List<Celda<?>> celdas, List<Etiqueta> etiquetaColumnas) {
        this.etiquetaFila = etiquetaFila;
        this.celdas = celdas;
        this.etiquetaColumnas = etiquetaColumnas;
    }

    @Override
    public String toString() {
        String salida = "*" + etiquetaFila + "*" + " | ";
        for (Celda<?> celda : celdas) {
            salida += celda + " | ";
        }
        return salida;
    }

    public List<Etiqueta> getEtiquetasColumnas() {return etiquetaColumnas;}

    public Etiqueta getEtiquetaFila() {return etiquetaFila;}

    public List<Celda<?>> getCeldasFila() {return celdas;}
}
