package jandas.Etiquetas;

public class EtiquetaString extends Etiqueta {

    private String etiqueta;

    public EtiquetaString(String etiqueta){this.etiqueta = etiqueta;}

    @Override
    public String getValor(){return etiqueta;}

    @Override
    public String toString(){return String.valueOf(etiqueta);}

}
