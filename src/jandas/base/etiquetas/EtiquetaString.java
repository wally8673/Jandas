package jandas.base.etiquetas;

/**
 * Implementación de la interfaz {@link Etiqueta} que representa una etiqueta basada en una cadena de texto.
 * <p>
 * Esta clase se utiliza para etiquetar filas o columnas en estructuras tabulares con valores tipo {@code String}.
 */
public class EtiquetaString implements Etiqueta {

    private String etiqueta;

    /**
     * Crea una nueva etiqueta de tipo {@code String}.
     *
     * @param etiqueta Texto que representa la etiqueta. No debe ser {@code null}.
     */
    public EtiquetaString(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Devuelve el valor de la etiqueta.
     *
     * @return La cadena de texto que representa esta etiqueta.
     */
    @Override
    public String getValor() {
        return etiqueta;
    }

    /**
     * Devuelve una representación en texto de esta etiqueta.
     *
     * @return El valor de la etiqueta como {@code String}.
     */
    @Override
    public String toString() {
        return String.valueOf(etiqueta);
    }

    /**
     * Compara esta etiqueta con otro objeto para verificar si son iguales.
     *
     * @param obj Objeto con el que se compara.
     * @return {@code true} si ambos objetos son instancias de {@code EtiquetaString} y sus etiquetas son iguales; {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EtiquetaString other = (EtiquetaString) obj;
        return etiqueta.equals(other.etiqueta);
    }

    /**
     * Devuelve el código hash basado en el valor de la etiqueta.
     *
     * @return Código hash de la cadena de texto.
     */
    @Override
    public int hashCode() {
        return etiqueta.hashCode();
    }
}

