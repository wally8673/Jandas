package jandas.core.data;

import jandas.exception.JandasException;
import jandas.core.etiquetas.Etiqueta;
import jandas.core.etiquetas.EtiquetaInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DataFrameGenerico implements DataFrame {

    private List<ColumnaGenerica<?>> columnas;
    private List<Etiqueta> etiquetasFilas;
    private List<Etiqueta> etiquetasColumnas;

    public DataFrameGenerico() {
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
    }

    // Métodos para agregar columnas

    @Override
    public <T> void agregarColumna(Etiqueta etiquetaColumna, Class<T> tipo, List<T> valores) {
        if (!columnas.isEmpty() && valores.size() != cantFilas()) {
            throw new JandasException(String.format(
                "Dimensiones no coinciden. Se esperaban %d filas, pero se recibieron %d",
                cantFilas(), valores.size()));
        }

        ColumnaGenerica<T> nuevaColumna = new ColumnaGenerica<>(etiquetaColumna, tipo);
        for (T valor : valores) {
            nuevaColumna.setValor(valor);
        }

        columnas.add(nuevaColumna);
        etiquetasColumnas.add(etiquetaColumna);

        // Si es la primera columna, genera etiquetas de filas automáticamente
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < valores.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }
    }

    @Override
    public <T> void agregarColumnaCeldas(Etiqueta etiquetaColumna, Class<T> tipo, List<Celda<T>> celdas) {
        if (!columnas.isEmpty() && celdas.size() != cantFilas()) {
            throw new JandasException(String.format(
                "Dimensiones no coinciden. Se esperaban %d filas, pero se recibieron %d",
                cantFilas(), celdas.size()));
        }

        ColumnaGenerica<T> nuevaColumna = new ColumnaGenerica<>(etiquetaColumna, tipo);
        nuevaColumna.agregarCeldas(celdas);

        columnas.add(nuevaColumna);
        etiquetasColumnas.add(etiquetaColumna);

        // Si es la primera columna, generar etiquetas de filas
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < celdas.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }
    }

    @Override
    public <T> void agregarColumna(ColumnaGenerica<T> columna) {
        if (!columnas.isEmpty() && columna.size() != cantFilas()) {
            throw new JandasException(String.format(
                "Dimensiones no coinciden. Se esperaban %d filas, pero se recibieron %d",
                cantFilas(), columna.size()));
        }

        columnas.add(columna);
        etiquetasColumnas.add(columna.getEtiqueta());

        // Si es la primera columna, generar etiquetas de filas
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < columna.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }

    }

    @Override
    public void agregarFila(Etiqueta etiquetaFila, List<Celda<?>> celdas) {
        if (celdas.size() != cantColumnas()) {
            throw new JandasException(String.format(
                "Dimensiones no coinciden. Se esperaban %d columnas, pero se recibieron %d",
                cantColumnas(), celdas.size()));
        }

        for (int i = 0; i < columnas.size(); i++) {
            Columna<Object> columna = (Columna<Object>) columnas.get(i);
            Celda<Object> celda = (Celda<Object>) celdas.get(i);
            columna.agregarCelda(celda);
        }

        etiquetasFilas.add(etiquetaFila);
    }

    private int getIndex(Etiqueta etiqueta, List<Etiqueta> etiquetas) {
        for (int i = 0; i < etiquetas.size(); i++) {
            if (etiquetas.get(i).getValor().equals(etiqueta.getValor())) {
                return i;
            }
        }
        throw new JandasException("Etiqueta no encontrada: " + etiqueta.getValor());
    }

    @Override
    public Fila getFila(Etiqueta etiquetaFila) {
        int indexFila = getIndex(etiquetaFila, etiquetasFilas);
        List<Celda<?>> celdasFilas = new ArrayList<>();
        for (ColumnaGenerica<?> col : columnas) {
            celdasFilas.add(col.getCeldas().get(indexFila));
        }
        return new FilaGenerica(etiquetaFila, celdasFilas, etiquetasColumnas);
    }

    @Override
    public void setEtiquetasFilas(List<Etiqueta> nuevasEtiquetas) {
        if (nuevasEtiquetas.size() != cantFilas()) {
            throw new JandasException(String.format(
                "Debe proporcionar exactamente %d etiquetas", cantFilas()));
        }
        this.etiquetasFilas = new ArrayList<>(nuevasEtiquetas);
    }

    @Override
    public List<ColumnaGenerica<?>> getColumnas() {
        return new ArrayList<>(columnas); // copia defensiva
    }

    @Override
    public Columna<?> getColumna(Etiqueta etiqueta) {
        int index = getIndex(etiqueta, etiquetasColumnas);
        return columnas.get(index);
    }

    @Override
    public int cantColumnas() {
        return columnas.size();
    }

    @Override
    public int cantFilas() {
        return columnas.isEmpty() ? 0 : columnas.get(0).size();
    }

    @Override
    public List<Etiqueta> getEtiquetasColumnas() {
        return new ArrayList<>(etiquetasColumnas);
    }

    @Override
    public List<Etiqueta> getEtiquetasFilas() {
        return new ArrayList<>(etiquetasFilas);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        DataFrameGenerico other = (DataFrameGenerico) obj;
        return Objects.equals(columnas, other.columnas) &&
               Objects.equals(etiquetasFilas, other.etiquetasFilas) &&
               Objects.equals(etiquetasColumnas, other.etiquetasColumnas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnas, etiquetasFilas, etiquetasColumnas);
    }

}
