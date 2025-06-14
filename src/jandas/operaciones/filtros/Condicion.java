package jandas.operaciones.filtros;

import jandas.base.data.Fila;

/**
 * Interfaz funcional que representa una condición booleana que puede ser evaluada
 * sobre una fila de datos {@link Fila}.
 * <p>
 * Esta interfaz permite definir filtros o predicados que se aplican para decidir
 * si una fila cumple con cierta condición.
 * </p>
 * <p>
 * También provee métodos estáticos para combinar condiciones mediante operadores
 * lógicos {@code and}, {@code or} y {@code not}, retornando nuevas condiciones
 * compuestas.
 * </p>
 */
public interface Condicion {

    /**
     * Evalúa la condición sobre una fila dada.
     *
     * @param fila La fila sobre la que se evalúa la condición
     * @return {@code true} si la fila cumple la condición; {@code false} en caso contrario
     */
    boolean evaluar(Fila fila);

    /**
     * Devuelve una condición que representa la conjunción lógica ("AND") entre dos condiciones.
     * La condición resultante se cumple solo si ambas condiciones originales se cumplen.
     *
     * @param cond1 Primera condición
     * @param cond2 Segunda condición
     * @return Nueva condición que representa {@code cond1 AND cond2}
     */
    static Condicion and(Condicion cond1, Condicion cond2) {
        return new CondicionAnd(cond1, cond2);
    }

    /**
     * Devuelve una condición que representa la disyunción lógica ("OR") entre dos condiciones.
     * La condición resultante se cumple si al menos una de las condiciones originales se cumple.
     *
     * @param cond1 Primera condición
     * @param cond2 Segunda condición
     * @return Nueva condición que representa {@code cond1 OR cond2}
     */
    static Condicion or(Condicion cond1, Condicion cond2) {
        return new CondicionOr(cond1, cond2);
    }

    /**
     * Devuelve una condición que representa la negación lógica ("NOT") de una condición dada.
     * La condición resultante se cumple si la condición original NO se cumple.
     *
     * @param condicion Condición a negar
     * @return Nueva condición que representa {@code NOT condicion}
     */
    static Condicion not(Condicion condicion) {
        return new CondicionNot(condicion);
    }
}


