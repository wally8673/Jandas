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

    public void agregarCeldas(List<Celda<T>> nuevasCeldas){
        celdas.addAll(nuevasCeldas);
    }

    public void agregarCelda(Celda<T> nuevaCelda){
        celdas.add(nuevaCelda);
    }

    public void agregarValor(T valor){
        celdas.add(new Celda<>(valor));
    }

    public Class<T> getTipo() {return tipo;}

    public List<Celda<T>> getCeldas() {return celdas;}

    public Etiqueta getEtiqueta() {return etiqueta;}

    public int size() {return celdas.size();}
}
