package jandas;

public class Celda<T> {
    private T valor;

    public Celda(T valor) {
        this.valor = valor;
    }

    // Overwrite

    public Celda() {
        this.valor = null;
    }

    public boolean esNA(){
        return valor == null;
    }

    @Override
    public String toString(){
        if (esNA()){
            return "NA";
        }
        else return String.valueOf(valor);
    }

}
