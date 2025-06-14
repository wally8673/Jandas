package jandas.operaciones.filtros;

import jandas.base.data.Fila;
import jandas.excepciones.JandasException;

/**
 * Implementa la interfaz {@link Condicion} para representar la operación lógica OR
 * entre dos condiciones.
 * <p>
 * Evalúa una fila devolviendo {@code true} si al menos una de las dos condiciones
 * contenidas es verdadera.
 * </p>
 */
public class CondicionOr implements Condicion {

    /** Primera condición */
    private Condicion cond1;

    /** Segunda condición */
    private Condicion cond2;

    /**
     * Constructor que inicializa las dos condiciones para la operación OR.
     *
     * @param cond1 Primera condición; no puede ser {@code null}
     * @param cond2 Segunda condición; no puede ser {@code null}
     * @throws JandasException si alguna condición es {@code null}
     */
    public CondicionOr(Condicion cond1, Condicion cond2) {
        if (cond1 == null || cond2 == null) {
            throw new JandasException("Las condiciones no pueden ser null");
        }
        this.cond1 = cond1;
        this.cond2 = cond2;
    }

    /**
     * Evalúa la operación lógica OR entre las dos condiciones para la fila dada.
     *
     * @param fila Fila sobre la cual se evalúan las condiciones
     * @return {@code true} si al menos una de las condiciones es {@code true},
     * {@code false} en caso contrario
     */
    @Override
    public boolean evaluar(Fila fila) {
        return cond1.evaluar(fila) || cond2.evaluar(fila);
    }
}



