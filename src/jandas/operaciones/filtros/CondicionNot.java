package jandas.operaciones.filtros;

import jandas.base.data.Fila;
import jandas.excepciones.JandasException;

public class CondicionNot implements Condicion {
    private Condicion condicion;

    public CondicionNot(Condicion condicion) {
        if (condicion == null) {
            throw new JandasException("La condición no puede ser null");
        }
        this.condicion = condicion;
    }

    @Override
    public boolean evaluar(Fila fila) {
        return !condicion.evaluar(fila);
    }
}


