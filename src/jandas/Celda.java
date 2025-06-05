package jandas;

public class Celda<T> {
    private T valor;
    private boolean esNA;


    public Celda() {
        this.valor = null;
        this.esNA = true;
    }

    public Celda(T valor) {
        if (valor == null) {
            this.esNA = true;
            this.valor = null;
        }
        else {
            this.valor = valor;
            this.esNA = false;
        }
    }

    public boolean esNA() {
        return esNA;
    }

    public T getValor() {
        return valor;
    }

    @Override
    public String toString() {
        if (esNA) {
            return "NA";
        } else {
            return valor.toString();
        }
    }
}


