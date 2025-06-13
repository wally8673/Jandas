package jandas.base.etiquetas;

import java.util.Objects;

public class EtiquetaInt implements Etiqueta {

    private int etiqueta;

    public EtiquetaInt(int etiqueta){this.etiqueta = etiqueta;}

    @Override
    public Integer getValor(){return etiqueta;}

    @Override
    public String toString(){return String.valueOf(etiqueta);}

}
