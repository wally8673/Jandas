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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Etiqueta<?> otro = (Etiqueta<?>) obj;
        return valor.equals

                (otro.valor);
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }

}

