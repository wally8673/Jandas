package jandas.base.data;

import jandas.base.etiquetas.Etiqueta;

import java.util.List;

public interface Fila {

    Etiqueta getEtiquetaFila();
    List<Etiqueta> getEtiquetasColumnas();
    List<Celda<?>> getCeldasFila();
    String toString();
}
