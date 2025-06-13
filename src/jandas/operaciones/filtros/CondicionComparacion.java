package jandas.operaciones.filtros;

import jandas.base.data.Celda;
import jandas.base.data.Fila;
import jandas.base.etiquetas.Etiqueta;
import jandas.excepciones.JandasException;

import java.util.List;

public class CondicionComparacion implements Condicion {
    private String nombreColumna;
    private String operador;
    private Object valor;

    public CondicionComparacion(String nombreColumna, String operador, Object valor) {
        this.nombreColumna = nombreColumna;
        this.operador = operador;
        this.valor = valor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean evaluar(Fila fila) {
        int indiceColumna = -1;
        List<Etiqueta> etiquetasColumnas = fila.getEtiquetasColumnas();
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            if (etiquetasColumnas.get(i).getValor().toString().equals(nombreColumna)) {
                indiceColumna = i;
                break;
            }
        }

        if (indiceColumna == -1) {
            throw new JandasException("Columna no encontrada: " + nombreColumna);
        }

        Celda<?> celda = fila.getCeldasFila().get(indiceColumna);
        if (celda.esNA()) return false;

        Comparable<Object> valorCelda = (Comparable<Object>) celda.getValor();
        Comparable<Object> valorComparar = (Comparable<Object>) valor;

        switch (operador) {
            case ">": return valorCelda.compareTo(valorComparar) > 0;
            case "<": return valorCelda.compareTo(valorComparar) < 0;
            case "=": return valorCelda.compareTo(valorComparar) == 0;
            case ">=": return valorCelda.compareTo(valorComparar) >= 0;
            case "<=": return valorCelda.compareTo(valorComparar) <= 0;
            default: throw new JandasException("Operador no válido: " + operador);
        }
    }
}

