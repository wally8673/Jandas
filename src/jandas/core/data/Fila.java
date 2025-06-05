package jandas.core.data;

import jandas.core.etiquetas.Etiqueta;

import java.util.List;

public interface Fila {

    Etiqueta getEtiquetaFila();
    List<Etiqueta> getEtiquetasColumnas();
    List<Celda<?>> getCeldasFila();
    String toString();
}
