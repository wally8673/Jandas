package jandas.operaciones.estadisticas;

/**
 * Enumeración que representa las distintas operaciones estadísticas
 * que pueden aplicarse sobre columnas numéricas al realizar una
 * agregación grupal en una tabla.
 * <p>
 * Las operaciones disponibles son:
 * <ul>
 *   <li>{@link #SUMA} - Suma de los valores numéricos.</li>
 *   <li>{@link #MAXIMO} - Valor máximo dentro del grupo.</li>
 *   <li>{@link #MINIMO} - Valor mínimo dentro del grupo.</li>
 *   <li>{@link #CUENTA} - Cantidad de valores no nulos (no NA) en el grupo.</li>
 *   <li>{@link #MEDIA} - Promedio (media aritmética) de los valores del grupo.</li>
 *   <li>{@link #VARIANZA} - Varianza muestral de los valores del grupo.</li>
 *   <li>{@link #DESVIO_ESTANDAR} - Desvío estándar muestral del grupo.</li>
 * </ul>
 *
 * Esta enumeración es utilizada por la clase {@link AgruparTabla} para
 * determinar qué operación aplicar al calcular los resultados de
 * agregación en tablas agrupadas.
 */
public enum OperacionEstadistica {
    /**
     * Suma de los valores numéricos.
     */
    SUMA,

    /**
     * Valor máximo dentro del grupo.
     */
    MAXIMO,

    /**
     * Valor mínimo dentro del grupo.
     */
    MINIMO,

    /**
     * Cantidad de valores no nulos en el grupo.
     */
    CUENTA,

    /**
     * Promedio (media aritmética) de los valores del grupo.
     */
    MEDIA,

    /**
     * Varianza muestral de los valores del grupo.
     */
    VARIANZA,

    /**
     * Desvío estándar muestral del grupo.
     */
    DESVIO_ESTANDAR
}
