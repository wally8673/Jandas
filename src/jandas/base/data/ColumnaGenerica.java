package jandas.base.data;

import jandas.base.etiquetas.Etiqueta;
import jandas.exception.JandasException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColumnaGenerica<T> implements Columna<T> {

    private List<Celda<T>> celdas;
    private Etiqueta etiqueta;
    private Class<T> tipo;

    public ColumnaGenerica(Etiqueta etiqueta, Class<T> tipo) {
        if (etiqueta == null) {
            throw new JandasException("La etiqueta no puede ser null");
        }
        if (tipo == null) {
            throw new JandasException("El tipo no puede ser null");
        }

        this.etiqueta = etiqueta;
        this.tipo = tipo;
        this.celdas = new ArrayList<>();
    }

    @Override
    public void agregarCeldas(List<Celda<T>> nuevasCeldas) {
        if (nuevasCeldas == null) {
            throw new JandasException("La lista de celdas no puede ser null");
        }
        celdas.addAll(nuevasCeldas);
    }

    @Override
    public void agregarCelda(Celda<T> nuevaCelda) {
        if (nuevaCelda == null) {
            throw new JandasException("La celda no puede ser null");
        }
        celdas.add(nuevaCelda);
    }

    @Override
    public void setValor(T valor) {
        if (valor != null && !tipo.isInstance(valor)) {
            throw new JandasException(String.format(
                "Tipo incompatible. Se esperaba %s, pero se recibió %s",
                tipo.getSimpleName(), valor.getClass().getSimpleName()));
        }
        celdas.add(new CeldaGenerica<>(valor));
    }

    @Override
    public Celda<T> getCelda(int indice) {
        if (indice < 0 || indice >= celdas.size()) {
            throw new JandasException(String.format(
                "Índice inválido: %d. Debe estar entre 0 y %d", indice, celdas.size() - 1));
        }
        return celdas.get(indice);
    }

    @Override
    public void setCelda(int indice, Celda<T> celda) {
        if (indice < 0 || indice >= celdas.size()) {
            throw new JandasException(String.format(
                "Índice inválido: %d. Debe estar entre 0 y %d", indice, celdas.size() - 1));
        }
        if (celda == null) {
            throw new JandasException("La celda no puede ser null");
        }
        celdas.set(indice, celda);
    }

    @Override
    public Class<T> getTipoDato() {
        return tipo;
    }

    @Override
    public List<Celda<T>> getCeldas() {
        return new ArrayList<>(celdas);
    }

    @Override
    public Etiqueta getEtiqueta() {
        return etiqueta;
    }

    @Override
    public int size() {
        return celdas.size();
    }

    @Override
    public boolean isEmpty() {
        return celdas.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ColumnaGenerica<?> other = (ColumnaGenerica<?>) obj;
        return Objects.equals(etiqueta, other.etiqueta) &&
                Objects.equals(tipo, other.tipo) &&
                Objects.equals(celdas, other.celdas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(etiqueta, tipo, celdas);
    }
}
