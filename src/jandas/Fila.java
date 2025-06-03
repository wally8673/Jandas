package jandas;

import jandas.Etiquetas.Etiqueta;

import java.util.List;

public class Fila {

    private Etiqueta etiquetaFila;
    private List<Celda<?>> celdas;
    private List<Etiqueta> etiquetaColumnas;

    public Fila(Etiqueta etiquetaFila, List<Celda<?>> celdas, List<Etiqueta> etiquetaColumnas) {
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

}
