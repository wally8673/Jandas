package jandas.base.etiquetas;

/**
 * Representa una etiqueta utilizada para identificar filas o columnas en una estructura de datos tabular.
 * <p>
 * Una etiqueta puede ser de cualquier tipo (entero, cadena, etc.) y debe poder ser convertida a texto.
 * Las implementaciones concretas de esta interfaz deben definir cómo se representa y accede al valor de la etiqueta.
 */
public interface Etiqueta {

    /**
     * Devuelve el valor asociado a esta etiqueta.
     *
     * @return Objeto que representa el valor de la etiqueta (por ejemplo, un {@code String}, {@code Integer}, etc.).
     */
    Object getValor();

    /**
     * Devuelve una representación en texto de la etiqueta.
     *
     * @return Cadena que representa la etiqueta.
     */
    @Override
    String toString();
}

