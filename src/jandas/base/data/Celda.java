package jandas.base.data;

public class Celda<T>  {
    private T valor;

    public Celda(T valor) {this.valor = valor;}

    public Celda() {this.valor = null;}

    //metodos


    public boolean esNA(){
        return valor == null;
    }

    public T getValor(){return valor;}


    public void setValor(T valor){this.valor = valor;}

    @Override
    public String toString(){
        if (esNA()){
            return "NA";
        }
        else return String.valueOf(valor);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Celda<?> other = (Celda<?>) obj;
        return valor != null ? valor.equals(other.valor) : other.valor == null;
    }

    @Override
    public int hashCode() {
        return valor != null ? valor.hashCode() : 0;
    }

}
