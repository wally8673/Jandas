package jandas.operaciones;

import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.TipoOrden;

import java.util.ArrayList;
import java.util.List;

public interface Ordenable {

    // default
    Tabla ordenar(String nombreColumna);

    // ordenamiento personalizado
    Tabla ordenar(String nombreColumna, TipoOrden direccion);

    // varios criterios
    Tabla ordenarPorCriterios(List<CriterioOrden> criterios);

}
