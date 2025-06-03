package jandas;

public class Etiqueta<T>{
    private T valor;

    public Etiqueta(T valor){
        if (!(valor instanceof String) &&  !(valor instanceof Number)) {
            throw new IllegalArgumentException("Etiqueta solo acepta String o Number");
        }
        this.valor = valor;
    }

    public T getValor() {
        return valor;
    }
}
