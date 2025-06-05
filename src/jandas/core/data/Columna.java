package jandas.core.data;

import jandas.core.etiquetas.Etiqueta;

import java.util.List;

public interface Columna<T> {

    void agregarCeldas(List<Celda<T>> celdas);
    void agregarCelda(Celda<T> celda);
    void setValor(T valor);
    Celda<T> getCelda(int indice);
    void setCelda(int indice, Celda<T> celda);
    Class<T> getTipoDato();
    List<Celda<T>> getCeldas();
    Etiqueta getEtiqueta();
    int size();
    boolean isEmpty();
}
