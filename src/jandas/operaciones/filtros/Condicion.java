package jandas.operaciones.filtros;

import jandas.base.data.Fila;

public interface Condicion {
    boolean evaluar(Fila fila);
}
