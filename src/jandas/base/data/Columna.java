package jandas.base.data;

import jandas.base.etiquetas.Etiqueta;
import jandas.excepciones.JandasException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa una columna de una tabla en el sistema Jandas.
 * Cada columna tiene una etiqueta, un tipo de dato y una lista de celdas que almacenan los valores.
 *
 * @param <T> Tipo de dato contenido en la columna (Integer, String, etc.).
 */
public class Columna<T> {

    private List<Celda<T>> celdas;
    private Etiqueta etiqueta;
    private Class<T> tipo;

    /**
     * Crea una nueva columna con una etiqueta y un tipo de dato especificado.
     *
     * @param etiqueta Etiqueta asociada a la columna (no debe ser null).
     * @param tipo Clase que representa el tipo de dato de la columna (no debe ser null).
     * @throws JandasException Si la etiqueta o el tipo son null.
     */
    public Columna(Etiqueta etiqueta, Class<T> tipo) {
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

    /**
     * Agrega una lista de celdas a la columna.
     *
     * @param nuevasCeldas Lista de celdas a agregar.
     * @throws JandasException Si la lista es null.
     */
    public void agregarCeldas(List<Celda<T>> nuevasCeldas) {
        if (nuevasCeldas == null) {
            throw new JandasException("La lista de celdas no puede ser null");
        }
        celdas.addAll(nuevasCeldas);
    }

    /**
     * Agrega una sola celda a la columna.
     *
     * @param nuevaCelda Celda a agregar.
     * @throws JandasException Si la celda es null.
     */
    public void agregarCelda(Celda<T> nuevaCelda) {
        if (nuevaCelda == null) {
            throw new JandasException("La celda no puede ser null");
        }
        celdas.add(nuevaCelda);
    }

    /**
     * Agrega un valor como una nueva celda en la columna.
     *
     * @param valor Valor a agregar (puede ser null).
     * @throws JandasException Si el valor no es del tipo esperado.
     */
    public void setValor(T valor) {
        if (valor != null && !tipo.isInstance(valor)) {
            throw new JandasException(String.format(
                "Tipo incompatible. Se esperaba %s, pero se recibió %s",
                tipo.getSimpleName(), valor.getClass().getSimpleName()));
        }
        celdas.add(new Celda<>(valor));
    }

    /**
     * Devuelve la celda en la posición indicada.
     *
     * @param indice Índice de la celda.
     * @return Celda en esa posición.
     * @throws JandasException Si el índice es inválido.
     */
    public Celda<T> getCelda(int indice) {
        if (indice < 0 || indice >= celdas.size()) {
            throw new JandasException(String.format(
                "Índice inválido: %d. Debe estar entre 0 y %d", indice, celdas.size() - 1));
        }
        return celdas.get(indice);
    }

    /**
     * Reemplaza la celda en el índice dado por una nueva.
     *
     * @param indice Índice donde se debe reemplazar la celda.
     * @param celda Nueva celda (no null).
     * @throws JandasException Si el índice es inválido o la celda es null.
     */
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

    /**
     * Devuelve el tipo de dato almacenado en la columna.
     *
     * @return Clase del tipo de dato.
     */
    public Class<T> getTipoDato() {return tipo;}

    /**
     * Devuelve una copia de la lista de celdas de la columna.
     *
     * @return Lista de celdas.
     */
    public List<Celda<T>> getCeldas() {return new ArrayList<>(celdas);}

    /**
     * Devuelve la etiqueta asociada a esta columna.
     *
     * @return Etiqueta de la columna.
     */
    public Etiqueta getEtiqueta() {return etiqueta;}

    /**
     * Devuelve la cantidad de celdas en la columna.
     *
     * @return Tamaño de la columna.
     */
    public int size() {return celdas.size();}

    /**
     * Indica si la columna está vacía.
     *
     * @return true si no hay celdas; false en caso contrario.
     */
    public boolean isEmpty() {return celdas.isEmpty();}

    /**
     * Compara esta columna con otra para verificar si son iguales.
     *
     * @param obj Objeto a comparar.
     * @return true si tienen la misma etiqueta, tipo y celdas; false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Columna<?> other = (Columna<?>) obj;
        return Objects.equals(etiqueta, other.etiqueta) &&
                Objects.equals(tipo, other.tipo) &&
                Objects.equals(celdas, other.celdas);
    }

    /**
     * Devuelve el código hash de esta columna.
     *
     * @return Código hash basado en etiqueta, tipo y celdas.
     */
    @Override
    public int hashCode() {
        return Objects.hash(etiqueta, tipo, celdas);
    }


}
