package jandas.base.data;

import jandas.base.etiquetas.Etiqueta;

import java.util.List;

/**
 * Representa una fila en una estructura de datos tabular.
 * <p>
 * Una fila está compuesta por una etiqueta de fila, una lista de celdas (una por columna)
 * y una lista de etiquetas correspondientes a las columnas.
 */
public class Fila {

    private Etiqueta etiquetaFila;
    private List<Celda<?>> celdas;
    private List<Etiqueta> etiquetaColumnas;

    /**
     * Crea una nueva fila con su etiqueta, celdas y etiquetas de columnas asociadas.
     *
     * @param etiquetaFila Etiqueta que identifica esta fila (por ejemplo, un índice numérico o textual).
     * @param celdas Lista de celdas que conforman esta fila. Cada celda puede contener un valor o NA.
     * @param etiquetaColumnas  Lista de etiquetas que identifican las columnas a las que pertenece cada celda.
     */
    public Fila(Etiqueta etiquetaFila, List<Celda<?>> celdas, List<Etiqueta> etiquetaColumnas) {
        this.etiquetaFila = etiquetaFila;
        this.celdas = celdas;
        this.etiquetaColumnas = etiquetaColumnas;
    }

    /**
     * Devuelve una representación en texto de esta fila.
     * Incluye la etiqueta de la fila y los valores de todas las celdas.
     *
     * @return Cadena de texto formateada con la estructura: "*etiquetaFila* | celda1 | celda2 | ..."
     */
    @Override
    public String toString() {
        String salida = "*" + etiquetaFila + "*" + " | ";
        for (Celda<?> celda : celdas) {
            salida += celda + " | ";
        }
        return salida;
    }

    /**
     * Devuelve la lista de etiquetas correspondientes a las columnas de esta fila.
     *
     * @return Lista de etiquetas de columnas.
     */
    public List<Etiqueta> getEtiquetasColumnas() {
        return etiquetaColumnas;
    }

    /**
     * Devuelve la etiqueta que identifica esta fila.
     *
     * @return Etiqueta de la fila.
     */
    public Etiqueta getEtiquetaFila() {
        return etiquetaFila;
    }

    /**
     * Devuelve la lista de celdas que forman esta fila.
     *
     * @return Lista de objetos {@link Celda}, uno por cada columna.
     */
    public List<Celda<?>> getCeldasFila() {
        return celdas;
    }
}
