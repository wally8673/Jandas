package jandas.base.etiquetas;

import java.util.Objects;

/**
 * Implementación de la interfaz {@link Etiqueta} que representa una etiqueta basada en un valor entero.
 * <p>
 * Esta clase se utiliza para etiquetar filas o columnas en estructuras tabulares utilizando números enteros.
 */
public class EtiquetaInt implements Etiqueta {

    private int etiqueta;

    /**
     * Crea una nueva etiqueta entera con el valor especificado.
     *
     * @param etiqueta Valor entero que representa la etiqueta.
     */
    public EtiquetaInt(int etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Devuelve el valor entero de esta etiqueta.
     *
     * @return Valor de la etiqueta como {@link Integer}.
     */
    @Override
    public Integer getValor() {
        return etiqueta;
    }

    /**
     * Devuelve una representación textual de la etiqueta.
     *
     * @return Cadena de texto que representa el valor de la etiqueta.
     */
    @Override
    public String toString() {
        return String.valueOf(etiqueta);
    }

    /**
     * Compara esta etiqueta con otro objeto para verificar si son iguales.
     *
     * @param obj Objeto con el que se compara.
     * @return {@code true} si ambos objetos son instancias de {@code EtiquetaInt} y tienen el mismo valor; {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EtiquetaInt other = (EtiquetaInt) obj;
        return etiqueta == other.etiqueta;
    }

    /**
     * Devuelve el código hash de esta etiqueta basado en su valor entero.
     *
     * @return Código hash del valor entero.
     */
    @Override
    public int hashCode() {
        return Objects.hash(etiqueta);
    }
}

