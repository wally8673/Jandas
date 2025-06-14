package jandas.excepciones;

/**
 * Excepción personalizada para errores relacionados con la librería Jandas.
 * <p>
 * Esta clase extiende {@link RuntimeException} y proporciona constructores estándar
 * así como métodos de fábrica para generar excepciones específicas con mensajes claros.
 */
public class JandasException extends RuntimeException {

    /**
     * Crea una nueva excepción con un mensaje específico.
     *
     * @param message Mensaje descriptivo del error.
     */
    public JandasException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción sin mensaje ni causa.
     */
    public JandasException() {
        super();
    }

    /**
     * Crea una nueva excepción con un mensaje y una causa.
     *
     * @param message Mensaje descriptivo del error.
     * @param cause Causa que originó esta excepción.
     */
    public JandasException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una nueva excepción con una causa específica.
     *
     * @param cause Causa que originó esta excepción.
     */
    public JandasException(Throwable cause) {
        super(cause);
    }

    // Métodos de fábrica para errores específicos

    /**
     * Genera una excepción por desajuste de dimensiones esperadas y reales.
     *
     * @param expected Número de elementos esperados.
     * @param actual Número de elementos recibidos.
     * @return Excepción con mensaje detallado del desajuste.
     */
    public static JandasException dimensionMismatch(int expected, int actual) {
        return new JandasException(String.format(
                "Dimensiones no coinciden. Se esperaban %d filas/columnas, pero se recibieron %d",
                expected, actual));
    }

    /**
     * Genera una excepción cuando no se encuentra una etiqueta.
     *
     * @param label Etiqueta buscada.
     * @return Excepción con mensaje de etiqueta no encontrada.
     */
    public static JandasException etiquetaNoEncontrada(String label) {
        return new JandasException("Etiqueta no encontrada: " + label);
    }

    /**
     * Genera una excepción por acceso a un índice inválido.
     *
     * @param index Índice intentado.
     * @param size Tamaño máximo válido.
     * @return Excepción con mensaje de rango inválido.
     */
    public static JandasException indiceInvalido(int index, int size) {
        return new JandasException(String.format(
                "Índice inválido: %d. Debe estar entre 0 y %d", index, size - 1));
    }

    /**
     * Genera una excepción cuando un {@code Dataframe} está vacío.
     *
     * @return Excepción con mensaje de dataframe vacío.
     */
    public static JandasException dataframeVacio() {
        return new JandasException("El Dataframe está vacío");
    }

    /**
     * Genera una excepción cuando los tipos de datos no coinciden.
     *
     * @param expected Tipo esperado.
     * @param actual Tipo recibido.
     * @return Excepción con mensaje de tipo incompatible.
     */
    public static JandasException tipoIncompatible(Class<?> expected, Class<?> actual) {
        return new JandasException(String.format(
                "Tipo incompatible. Se esperaba %s, pero se recibió %s",
                expected.getSimpleName(), actual.getSimpleName()));
    }

    /**
     * Genera una excepción cuando una etiqueta es {@code null}.
     *
     * @return Excepción con mensaje de etiqueta nula.
     */
    public static JandasException etiquetaNula() {
        return new JandasException("La etiqueta no puede ser null");
    }

    /**
     * Genera una excepción cuando un tipo de dato es {@code null}.
     *
     * @return Excepción con mensaje de tipo nulo.
     */
    public static JandasException tipoNulo() {
        return new JandasException("El tipo no puede ser null");
    }
}
