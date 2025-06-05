package jandas.core.data;

public interface Celda<T> {

    boolean esNA();
    T getValor();
    void setValor(T valor);
    String toString();
}
