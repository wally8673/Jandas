package jandas.base.data;

public interface Celda<T> {

    boolean esNA();
    T getValor();
    void setValor(T valor);
    String toString();
}
