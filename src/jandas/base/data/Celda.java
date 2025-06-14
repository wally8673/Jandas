package jandas.base.data;

/**
 * Representa una celda individual dentro de una columna.
 * Cada celda puede contener un valor de tipo genérico {@code T}, o representar un valor faltante (NA) si es {@code null}.
 *
 * @param <T> Tipo de dato almacenado en la celda (por ejemplo: Integer, String, Double, etc.).
 */
public class Celda<T> {

    private T valor;

    /**
     * Crea una nueva celda con el valor especificado.
     *
     * @param valor Valor inicial de la celda. Puede ser {@code null} para representar NA.
     */
    public Celda(T valor) {
        this.valor = valor;
    }

    /**
     * Crea una celda vacía (con valor {@code null}, es decir, NA).
     */
    public Celda() {
        this.valor = null;
    }

    /**
     * Indica si la celda contiene un valor faltante (NA).
     *
     * @return {@code true} si el valor es {@code null}, {@code false} en caso contrario.
     */
    public boolean esNA() {
        return valor == null;
    }

    /**
     * Devuelve el valor contenido en la celda.
     *
     * @return El valor de la celda, o {@code null} si es NA.
     */
    public T getValor() {
        return valor;
    }

    /**
     * Asigna un nuevo valor a la celda.
     *
     * @param valor Nuevo valor a establecer. Puede ser {@code null} para representar NA.
     */
    public void setValor(T valor) {
        this.valor = valor;
    }

    /**
     * Devuelve una representación en cadena del contenido de la celda.
     *
     * @return "NA" si el valor es {@code null}, o el resultado de {@code String.valueOf(valor)} si no lo es.
     */
    @Override
    public String toString() {
        if (esNA()) {
            return "NA";
        } else {
            return String.valueOf(valor);
        }
    }

    /**
     * Compara esta celda con otro objeto para determinar si son iguales.
     *
     * @param obj Objeto con el que se compara.
     * @return {@code true} si ambos objetos son celdas con valores iguales (incluyendo ambos {@code null}); {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Celda<?> other = (Celda<?>) obj;
        return valor != null ? valor.equals(other.valor) : other.valor == null;
    }

    /**
     * Calcula el código hash de esta celda basado en su valor.
     *
     * @return Código hash del valor, o 0 si el valor es {@code null}.
     */
    @Override
    public int hashCode() {
        return valor != null ? valor.hashCode() : 0;
    }
}

