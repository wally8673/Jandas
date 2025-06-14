package jandas.operaciones.filtros;

import jandas.base.data.Fila;
import jandas.excepciones.JandasException;

/**
 * Implementación de la interfaz {@link Condicion} que representa la conjunción lógica ("AND")
 * entre dos condiciones.
 * <p>
 * La condición resultante se cumple solo si ambas condiciones internas se cumplen para una fila dada.
 * </p>
 */
public class CondicionAnd implements Condicion {

    /** Primera condición en la conjunción */
    private Condicion cond1;

    /** Segunda condición en la conjunción */
    private Condicion cond2;

    /**
     * Constructor que recibe dos condiciones para combinar con un AND lógico.
     *
     * @param cond1 Primera condición (no puede ser {@code null})
     * @param cond2 Segunda condición (no puede ser {@code null})
     * @throws JandasException si alguna de las condiciones es {@code null}
     */
    public CondicionAnd(Condicion cond1, Condicion cond2) {
        if (cond1 == null || cond2 == null) {
            throw new JandasException("Las condiciones no pueden ser null");
        }
        this.cond1 = cond1;
        this.cond2 = cond2;
    }

    /**
     * Evalúa la condición AND sobre la fila dada.
     * Devuelve {@code true} solo si ambas condiciones internas se cumplen.
     *
     * @param fila La fila sobre la que se evalúa la condición
     * @return {@code true} si {@code cond1} y {@code cond2} se cumplen para la fila, {@code false} en caso contrario
     */
    @Override
    public boolean evaluar(Fila fila) {
        return cond1.evaluar(fila) && cond2.evaluar(fila);
    }
}



