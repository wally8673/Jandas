package jandas.core.data;

public class CeldaGenerica<T> implements Celda<T> {
    private T valor;

    public CeldaGenerica(T valor) {this.valor = valor;}

    public CeldaGenerica() {this.valor = null;}

    //metodos

    @Override
    public boolean esNA(){
        return valor == null;
    }
    @Override
    public T getValor(){return valor;}

    @Override
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

        CeldaGenerica<?> other = (CeldaGenerica<?>) obj;
        return valor != null ? valor.equals(other.valor) : other.valor == null;
    }

    @Override
    public int hashCode() {
        return valor != null ? valor.hashCode() : 0;
    }

}
