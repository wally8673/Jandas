package jandas.base.data;

import jandas.base.etiquetas.Etiqueta;

import java.util.List;

public interface Tabla {

    <T> void agregarColumna(Etiqueta etiquetaColumna, Class<T> tipo, List<T> valores);
    <T> void agregarColumnaCeldas(Etiqueta etiquetaColumna, Class<T> tipo, List<Celda<T>> celdas);
    <T> void agregarColumna(ColumnaGenerica<T> columna);
    void agregarFila(Etiqueta etiquetaFila, List<Celda<?>> celdas);
    void cargarDesdeMatriz(Object[][] matriz, List<Etiqueta> etiquetas);


    Fila getFila(Etiqueta etiquetaFila);
    Columna<?> getColumna(Etiqueta etiqueta);

    List<ColumnaGenerica<?>> getColumnas();
    List<Etiqueta> getEtiquetasColumnas();
    List<Etiqueta> getEtiquetasFilas();
    void setEtiquetasFilas(List<Etiqueta> nuevasEtiquetas);

    int cantColumnas();
    int cantFilas();


}
