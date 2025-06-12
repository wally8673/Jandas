package jandas.base.etiquetas;

public class EtiquetaString implements Etiqueta {

    private String etiqueta;

    public EtiquetaString(String etiqueta){this.etiqueta = etiqueta;}

    @Override
    public String getValor(){return etiqueta;}

    @Override
    public String toString(){return String.valueOf(etiqueta);}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EtiquetaString other = (EtiquetaString) obj;
        return etiqueta.equals(other.etiqueta);
    }

    @Override
    public int hashCode() {
        return etiqueta.hashCode();
    }


}
