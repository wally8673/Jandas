package jandas.base.etiquetas;

import java.util.Objects;

public class EtiquetaInt implements Etiqueta {

    private int etiqueta;

    public EtiquetaInt(int etiqueta){this.etiqueta = etiqueta;}

    @Override
    public Integer getValor(){return etiqueta;}

    @Override
    public String toString(){return String.valueOf(etiqueta);}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EtiquetaInt other = (EtiquetaInt) obj;
        return etiqueta == other.etiqueta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(etiqueta);
    }


}
