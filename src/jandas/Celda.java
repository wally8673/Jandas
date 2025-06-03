package jandas;

public class Celda<T> {
    private T valor;
    private boolean esNA;

    public Celda(T valor) {
        if (valor == null) {
            this.esNA = true;
            this.valor = null;
        } else if (!(valor instanceof Number) && !(valor instanceof Boolean) && !(valor instanceof String)) {
            throw new IllegalArgumentException("Valor inválido para celda. Solo se permiten números, booleanos o cadenas.");
        } else {
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


