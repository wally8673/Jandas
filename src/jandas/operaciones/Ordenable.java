package jandas.operaciones;

import jandas.base.data.Tabla;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.Orden;

import java.util.List;

public interface Ordenable {



    // ordenamiento personalizado
    Tabla ordenar(String nombreColumna, Orden direccion);

    // string varios criterios

    Tabla ordenar(String... criterios);

    // varios criterios
    Tabla ordenarPorCriterios(List<CriterioOrden> criterios);

}
