package jandas.operaciones.filtros;

import jandas.base.data.Fila;

public class CondicionOr implements Condicion {
    private Condicion cond1;
    private Condicion cond2;

    public CondicionOr(Condicion cond1, Condicion cond2) {
        this.cond1 = cond1;
        this.cond2 = cond2;
    }

    @Override
    public boolean evaluar(Fila fila) {
        return cond1.evaluar(fila) || cond2.evaluar(fila);
    }
}

