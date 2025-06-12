package jandas.operaciones.filtros;

import jandas.base.data.Fila;

public class CondicionNot implements Condicion {
    private Condicion condicion;

    public CondicionNot(Condicion condicion) {
        this.condicion = condicion;
    }

    @Override
    public boolean evaluar(Fila fila) {
        return !condicion.evaluar(fila);
    }
}

