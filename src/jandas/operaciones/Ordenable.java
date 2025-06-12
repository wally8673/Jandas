package jandas.operaciones;

import jandas.base.data.Tabla;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.Orden;

import java.util.List;

public interface Ordenable {

    // default
    Tabla ordenar(String nombreColumna);

    // ordenamiento personalizado
    Tabla ordenar(String nombreColumna, Orden direccion);

    // varios criterios
    Tabla ordenarPorCriterios(List<CriterioOrden> criterios);

}
