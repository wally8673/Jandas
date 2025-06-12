package jandas.operaciones.filtros;

import jandas.base.data.Fila;

public interface Condicion {
    boolean evaluar(Fila fila);

    static Condicion and(Condicion cond1, Condicion cond2) {
        return new CondicionAnd(cond1, cond2);
    }

    static Condicion or(Condicion cond1, Condicion cond2) {
        return new CondicionOr(cond1, cond2);
    }

    static Condicion not(Condicion condicion) {
        return new CondicionNot(condicion);
    }
}

