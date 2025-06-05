package jandas.core.etiquetas;

public class EtiquetaString implements Etiqueta {

    private String etiqueta;

    public EtiquetaString(String etiqueta){this.etiqueta = etiqueta;}

    @Override
    public String getValor(){return etiqueta;}

    @Override
    public String toString(){return String.valueOf(etiqueta);}

}
