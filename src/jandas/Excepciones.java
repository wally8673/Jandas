package jandas;

public class Excepciones extends RuntimeException {

    // Constructor con solo mensaje (el que ya tienes y funciona)
    public Excepciones(String message) {
        super(message);
    }

    // Constructor sin parámetros
    public Excepciones() {
        super();
    }

    // Constructor con mensaje y causa
    public Excepciones(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor con solo causa
    public Excepciones(Throwable cause) {
        super(cause);
    }

    // Métodos específicos para diferentes tipos de errores del Dataframe
    public static Excepciones dimensionMismatch(int expected, int actual) {
        return new Excepciones(String.format(
            "Dimensiones no coinciden. Se esperaban %d filas/columnas, pero se recibieron %d",
            expected, actual));
    }

    public static Excepciones etiquetaNoEncontrada(String etiqueta) {
        return new Excepciones("Etiqueta no encontrada: " + etiqueta);
    }

    public static Excepciones indiceInvalido(int indice, int tamaño) {
        return new Excepciones(String.format(
            "Índice inválido: %d. Debe estar entre 0 y %d", indice, tamaño - 1));
    }

    public static Excepciones dataframeVacio() {
        return new Excepciones("El Dataframe está vacío");
    }

    public static Excepciones tipoIncompatible(Class<?> esperado, Class<?> actual) {
        return new Excepciones(String.format(
            "Tipo incompatible. Se esperaba %s, pero se recibió %s",
            esperado.getSimpleName(), actual.getSimpleName()));
    }
}
