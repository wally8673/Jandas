package jandas.operaciones.ordenamiento;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;

import java.util.Comparator;
import java.util.List;

/**
 * Comparador para ordenar filas de una tabla según múltiples criterios.
 * <p>
 * Compara dos índices de filas basándose en una lista de criterios de orden,
 * evaluando cada criterio en orden hasta encontrar una diferencia.
 * </p>
 * <p>
 * Los valores nulos (NA) se consideran mayores y por ende se ordenan al final.
 * </p>
 */
public class ComparadorMultiplesCriterios implements Comparator<Integer> {

    /** Lista de criterios para ordenar */
    private final List<CriterioOrden> criterios;

    /** Tabla sobre la cual se realiza la comparación */
    private final Tabla tabla;

    /**
     * Construye un comparador con los criterios y la tabla indicados.
     *
     * @param criterios Lista de criterios de ordenamiento; cada criterio especifica
     * la columna y el tipo de orden (ascendente o descendente).
     * @param tabla Tabla cuyos índices de fila serán comparados.
     */
    public ComparadorMultiplesCriterios(List<CriterioOrden> criterios, Tabla tabla) {
        this.criterios = criterios;
        this.tabla = tabla;
    }

    /**
     * Compara dos índices de filas según los criterios establecidos.
     *
     * @param indice1 Índice de la primera fila a comparar.
     * @param indice2 Índice de la segunda fila a comparar.
     * @return Un entero negativo, cero o positivo si la primera fila es menor,
     * igual o mayor que la segunda según los criterios.
     */
    @Override
    public int compare(Integer indice1, Integer indice2) {
        for (CriterioOrden criterio : criterios) {
            Columna<?> columna = tabla.getColumna(criterio.getEtiqueta());
            Celda<?> celda1 = columna.getCelda(indice1);
            Celda<?> celda2 = columna.getCelda(indice2);

            int resultado = compararCeldas(celda1, celda2);

            if (resultado != 0) {
                // Si el criterio es descendente, invertir el resultado
                return criterio.getTipoOrden() == Orden.DESCENDENTE ? -resultado : resultado;
            }
        }
        return 0;
    }

    /**
     * Compara dos celdas considerando valores nulos y tipos comparables.
     *
     * @param celda1 Primera celda a comparar.
     * @param celda2 Segunda celda a comparar.
     * @return Un entero negativo, cero o positivo según el orden relativo.
     */
    @SuppressWarnings("unchecked")
    private int compararCeldas(Celda<?> celda1, Celda<?> celda2) {
        // Manejo de valores NA (nulos)
        if (celda1.esNA() && celda2.esNA()) return 0;
        if (celda1.esNA()) return 1; // NA van al final
        if (celda2.esNA()) return -1;

        Object valor1 = celda1.getValor();
        Object valor2 = celda2.getValor();

        // Comparar si ambos son comparables
        if (valor1 instanceof Comparable && valor2 instanceof Comparable) {
            try {
                return ((Comparable<Object>) valor1).compareTo(valor2);
            } catch (ClassCastException e) {
                // En caso de tipos incompatibles, comparar como strings
                return valor1.toString().compareTo(valor2.toString());
            }
        }

        // Caso por defecto: comparación lexicográfica de strings
        return valor1.toString().compareTo(valor2.toString());
    }
}

