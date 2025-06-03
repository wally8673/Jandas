package jandas;

import jandas.Etiquetas.Etiqueta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Columna<T> {

    private List<Celda<T>> celdas;
    private Etiqueta etiqueta;
    private Class<T> tipo;

    public Columna(Etiqueta etiqueta, Class<T> tipo) {
        this.etiqueta = etiqueta;
        this.tipo = tipo;
        this.celdas = new ArrayList<>();
    }

    public List<Celda<T>> getCeldas() {return celdas;}

    public Etiqueta getEtiqueta() {return etiqueta;}
}
