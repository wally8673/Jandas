package jandas.excepciones;

public class JandasException extends RuntimeException {
    public JandasException(String message) {
        super(message);
    }

    public JandasException() {
        super();
    }

    public JandasException(String message, Throwable cause) {
        super(message, cause);
    }

    public JandasException(Throwable cause) {
        super(cause);
    }

    // Factory methods for specific errors
    public static JandasException dimensionMismatch(int expected, int actual) {
        return new JandasException(String.format(
            "Dimensiones no coinciden. Se esperaban %d filas/columnas, pero se recibieron %d",
            expected, actual));
    }

    public static JandasException etiquetaNoEncontrada(String label) {
        return new JandasException("Etiqueta no encontrada: " + label);
    }

    public static JandasException indiceInvalido(int index, int size) {
        return new JandasException(String.format(
            "Índice inválido: %d. Debe estar entre 0 y %d", index, size - 1));
    }

    public static JandasException dataframeVacio() {
        return new JandasException("El Dataframe está vacío");
    }

    public static JandasException tipoIncompatible(Class<?> expected, Class<?> actual) {
        return new JandasException(String.format(
            "Tipo incompatible. Se esperaba %s, pero se recibió %s",
            expected.getSimpleName(), actual.getSimpleName()));
    }

    public static JandasException etiquetaNula() {
        return new JandasException("La etiqueta no puede ser null");
    }

    public static JandasException tipoNulo() {
        return new JandasException("El tipo no puede ser null");
    }
}