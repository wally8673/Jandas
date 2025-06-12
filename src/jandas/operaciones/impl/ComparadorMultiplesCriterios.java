package jandas.operaciones.impl;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.TipoOrden;

import java.util.Comparator;
import java.util.List;

public class ComparadorMultiplesCriterios implements Comparator<Integer> {
    private final List<CriterioOrden> criterios;
    private final Tabla tabla;

    public ComparadorMultiplesCriterios(List<CriterioOrden> criterios, Tabla tabla) {
        this.criterios = criterios;
        this.tabla = tabla;
    }

    @Override
    public int compare(Integer indice1, Integer indice2) {
        for (CriterioOrden criterio : criterios) {
            Columna<?> columna = tabla.getColumna(criterio.getEtiqueta());
            Celda<?> celda1 = columna.getCelda(indice1);
            Celda<?> celda2 = columna.getCelda(indice2);

            int resultado = compararCeldas(celda1, celda2);

            if (resultado != 0) {
                return criterio.getTipoOrden() == TipoOrden.DESCENDENTE ? -resultado : resultado;
            }
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    private int compararCeldas(Celda<?> celda1, Celda<?> celda2) {
        // Manejar valores nulos (NA)
        if (celda1.esNA() && celda2.esNA()) return 0;
        if (celda1.esNA()) return 1; // Los NA van al final
        if (celda2.esNA()) return -1;

        Object valor1 = celda1.getValor();
        Object valor2 = celda2.getValor();

        // Comparar valores del mismo tipo
        if (valor1 instanceof Comparable && valor2 instanceof Comparable) {
            try {
                return ((Comparable<Object>) valor1).compareTo(valor2);
            } catch (ClassCastException e) {
                // Si no se pueden comparar directamente, convertir a string
                return valor1.toString().compareTo(valor2.toString());
            }
        }

        // Fallback: comparar como strings
        return valor1.toString().compareTo(valor2.toString());
    }
}

