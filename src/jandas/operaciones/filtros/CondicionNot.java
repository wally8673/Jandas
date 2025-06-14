package jandas.operaciones.filtros;

import jandas.base.data.Fila;
import jandas.excepciones.JandasException;

/**
 * Implementa la interfaz {@link Condicion} para representar la negación lógica
 * de otra condición.
 * <p>
 * Evalúa una fila devolviendo el valor booleano inverso de la condición
 * que contiene.
 * </p>
 */
public class CondicionNot implements Condicion {

    /** Condición que será negada */
    private Condicion condicion;

    /**
     * Constructor que inicializa la condición negada.
     *
     * @param condicion Condición a negar; no puede ser null
     * @throws JandasException si la condición es null
     */
    public CondicionNot(Condicion condicion) {
        if (condicion == null) {
            throw new JandasException("La condición no puede ser null");
        }
        this.condicion = condicion;
    }

    /**
     * Evalúa la condición negada en la fila dada.
     * <p>
     * Retorna el valor booleano contrario al resultado de evaluar
     * la condición contenida sobre la fila.
     * </p>
     *
     * @param fila Fila sobre la cual se evalúa la condición negada
     * @return {@code true} si la condición contenida es {@code false}, y viceversa
     */
    @Override
    public boolean evaluar(Fila fila) {
        return !condicion.evaluar(fila);
    }
}



