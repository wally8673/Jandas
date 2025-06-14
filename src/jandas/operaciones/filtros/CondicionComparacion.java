package jandas.operaciones.filtros;

import jandas.base.data.Celda;
import jandas.base.data.Fila;
import jandas.base.etiquetas.Etiqueta;
import jandas.excepciones.JandasException;

import java.util.List;

/**
 * Implementa la interfaz {@link Condicion} para evaluar una comparación
 * entre el valor de una columna específica en una fila y un valor dado.
 * <p>
 * Soporta operadores de comparación: {@code >}, {@code <}, {@code =}, {@code >=}, {@code <=}.
 * La comparación se realiza usando la interfaz {@link Comparable}.
 * </p>
 */
public class CondicionComparacion implements Condicion {

    /** Nombre de la columna sobre la cual se realiza la comparación */
    private String nombreColumna;

    /** Operador de comparación (>, <, =, >=, <=) */
    private String operador;

    /** Valor contra el cual se compara el valor de la columna */
    private Object valor;

    /**
     * Constructor que inicializa la condición con la columna, operador y valor a comparar.
     *
     * @param nombreColumna Nombre de la columna en la fila para evaluar la condición
     * @param operador Operador de comparación: {@code >}, {@code <}, {@code =}, {@code >=}, {@code <=}
     * @param valor Valor contra el cual se compara el contenido de la columna
     */
    public CondicionComparacion(String nombreColumna, String operador, Object valor) {
        this.nombreColumna = nombreColumna;
        this.operador = operador;
        this.valor = valor;
    }

    /**
     * Evalúa la condición en la fila dada.
     * <p>
     * Busca la columna con el nombre indicado, obtiene su valor y realiza la comparación
     * según el operador definido. Si la celda está vacía (NA), devuelve {@code false}.
     * Si la columna no existe o el operador no es válido, lanza una excepción {@link JandasException}.
     * </p>
     *
     * @param fila Fila sobre la cual se evalúa la condición
     * @return {@code true} si la condición se cumple, {@code false} en caso contrario
     * @throws JandasException si la columna no se encuentra o el operador no es válido
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean evaluar(Fila fila) {
        int indiceColumna = -1;
        List<Etiqueta> etiquetasColumnas = fila.getEtiquetasColumnas();
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            if (etiquetasColumnas.get(i).getValor().toString().equals(nombreColumna)) {
                indiceColumna = i;
                break;
            }
        }

        if (indiceColumna == -1) {
            throw new JandasException("Columna no encontrada: " + nombreColumna);
        }

        Celda<?> celda = fila.getCeldasFila().get(indiceColumna);
        if (celda.esNA()) return false;

        Comparable<Object> valorCelda = (Comparable<Object>) celda.getValor();
        Comparable<Object> valorComparar = (Comparable<Object>) valor;

        switch (operador) {
            case ">": return valorCelda.compareTo(valorComparar) > 0;
            case "<": return valorCelda.compareTo(valorComparar) < 0;
            case "=": return valorCelda.compareTo(valorComparar) == 0;
            case ">=": return valorCelda.compareTo(valorComparar) >= 0;
            case "<=": return valorCelda.compareTo(valorComparar) <= 0;
            default: throw new JandasException("Operador no válido: " + operador);
        }
    }
}


