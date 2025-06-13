package jandas.operaciones.filtros;

import jandas.base.data.Fila;
import jandas.excepciones.JandasException;

public class CondicionAnd implements Condicion {
    private Condicion cond1;
    private Condicion cond2;

    public CondicionAnd(Condicion cond1, Condicion cond2) {
        if (cond1 == null || cond2 == null) {
            throw new JandasException("Las condiciones no pueden ser null");
        }
        this.cond1 = cond1;
        this.cond2 = cond2;
    }

    @Override
    public boolean evaluar(Fila fila) {
        return cond1.evaluar(fila) && cond2.evaluar(fila);
    }
}


