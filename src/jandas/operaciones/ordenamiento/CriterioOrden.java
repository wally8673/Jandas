package jandas.operaciones.ordenamiento;

import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaString;

/**
 * Representa un criterio de ordenamiento para una columna de una tabla.
 * <p>
 * Un criterio de ordenamiento está definido por la columna (representada
 * por una etiqueta) y la dirección del orden (ascendente o descendente).
 * </p>
 */
public class CriterioOrden {

    /** Etiqueta que identifica la columna a ordenar */
    private Etiqueta etiqueta;

    /** Dirección del ordenamiento: ascendente o descendente */
    private Orden direccion;

    /**
     * Construye un criterio de orden con etiqueta y dirección especificadas.
     *
     * @param etiqueta Etiqueta que identifica la columna para ordenar.
     * @param direccion Dirección del orden (ascendente o descendente).
     */
    public CriterioOrden(Etiqueta etiqueta, Orden direccion) {
        this.etiqueta = etiqueta;
        this.direccion = direccion;
    }

    /**
     * Construye un criterio de orden a partir del nombre de la columna y dirección.
     * <p>
     * Internamente crea una etiqueta de tipo cadena para la columna.
     * </p>
     *
     * @param nombreColumna Nombre de la columna para ordenar.
     * @param direccion Dirección del orden (ascendente o descendente).
     */
    public CriterioOrden(String nombreColumna, Orden direccion) {
        this.etiqueta = new EtiquetaString(nombreColumna);
        this.direccion = direccion;
    }

    /**
     * Construye un criterio de orden con etiqueta dada y orden ascendente por defecto.
     *
     * @param etiqueta Etiqueta que identifica la columna para ordenar.
     */
    public CriterioOrden(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
        this.direccion = Orden.ASCENDENTE;
    }

    /**
     * Obtiene la etiqueta que identifica la columna a ordenar.
     *
     * @return La etiqueta de la columna.
     */
    public Etiqueta getEtiqueta() {
        return etiqueta;
    }

    /**
     * Obtiene la dirección del ordenamiento (ascendente o descendente).
     *
     * @return La dirección del orden.
     */
    public Orden getTipoOrden() {
        return direccion;
    }
}
