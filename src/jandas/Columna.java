package jandas;

import jandas.Etiquetas.Etiqueta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Columna<T> {

    private List<Celda<T>> celdas;
    private Etiqueta etiqueta;
    private Class<T> tipo;

    public Columna(Etiqueta etiqueta, Class<T> tipo) {
        if (etiqueta == null) {
            throw Excepciones.etiquetaNula();
        }
        if (tipo == null) {
            throw Excepciones.tipoNulo();
        }

        this.etiqueta = etiqueta;
        this.tipo = tipo;
        this.celdas = new ArrayList<>();
    }

    public void agregarCeldas(List<Celda<T>> nuevasCeldas){
        if (nuevasCeldas == null) {
            throw new Excepciones("La lista de celdas no puede ser null");
        }
        celdas.addAll(nuevasCeldas);
    }

    public void agregarCelda(Celda<T> nuevaCelda){
        if (nuevaCelda == null) {
            throw new Excepciones("La celda no puede ser null");
        }
        celdas.add(nuevaCelda);
    }

    public void agregarValor(T valor){
        if (valor != null && !tipo.isInstance(valor)) {
            throw Excepciones.tipoIncompatible(tipo, valor.getClass());
        }
        celdas.add(new Celda<>(valor));
    }

    public Celda<T> getCelda(int indice) {
        if (indice < 0 || indice >= celdas.size()) {
            throw Excepciones.indiceInvalido(indice, celdas.size());
        }
        return celdas.get(indice);
    }

    public void setCelda(int indice, Celda<T> celda) {
        if (indice < 0 || indice >= celdas.size()) {
            throw Excepciones.indiceInvalido(indice, celdas.size());
        }
        if (celda == null) {
            throw new Excepciones("La celda no puede ser null");
        }
        celdas.set(indice, celda);
    }

    private void validarTipo(T valor) {
        if (valor != null && !tipo.isInstance(valor)) {
            throw Excepciones.tipoIncompatible(tipo, valor.getClass());
        }
    }

    public Class<T> getTipo() {return tipo;}

    public List<Celda<T>> getCeldas() {return new ArrayList<>(celdas);} //copia defensiva

    public Etiqueta getEtiqueta() {return etiqueta;}

    public int size() {return celdas.size();}

    public boolean isEmpty() {return celdas.isEmpty();}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Columna<?> other = (Columna<?>) obj;
        return Objects.equals(etiqueta, other.etiqueta) &&
                Objects.equals(tipo, other.tipo) &&
                Objects.equals(celdas, other.celdas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(etiqueta, tipo, celdas);
    }
}
