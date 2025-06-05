package jandas;

import java.util.ArrayList;
import java.util.List;

public class Dataframe {
    private List<Columna<?>> columnas ;
    private List<Etiqueta<?>> etiquetasFilas;


    public Dataframe(List<Columna<?>> columnas, List<Etiqueta<?>> etiquetasFilas) {
        this.columnas = new ArrayList<>(columnas);
        this.etiquetasFilas =new ArrayList<>(etiquetasFilas);

        if (columnas == null || columnas.isEmpty()) {
            throw new IllegalArgumentException("El dataframe debe tener al menos una columna.");
        }

        int filasEsperadas = columnas.get(0).getCeldas().size();
        for (Columna<?> col : columnas) {
            if (col.getCeldas().size() != filasEsperadas) {
                throw new IllegalArgumentException("Todas las columnas deben tener la misma cantidad de celdas.");
            }
        }

        if (etiquetasFilas.size() != filasEsperadas) {
            throw new IllegalArgumentException("La cantidad de etiquetas de filas no coincide con la cantidad de filas.");
        }

    }
    public Dataframe(List<Columna<?>> columnas) {
        this(columnas, generarEtiquetasPorDefecto(columnas.get(0).getCeldas().size()));
    }
    private static List<Etiqueta<?>> generarEtiquetasPorDefecto(int cantidad) {
        List<Etiqueta<?>> etiquetas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            etiquetas.add(new Etiqueta<>(i));
        }
        return etiquetas;
    }

    public int cantColumnas() {
        return columnas.size();
    }

    public int cantFilas() {
        return columnas.get(0).getCeldas().size();
    }

    public List<Object> etiquetasColum() {
        List<Object> nombres = new ArrayList<>();
        for (Columna<?> col : columnas) {
            nombres.add(col.getNombre().getValor());
        }
        return nombres;
    }

    public List<Object> etiquetasFila() {
        List<Object> etiquetas = new ArrayList<>();
        for (Etiqueta<?> etq : etiquetasFilas) {
            etiquetas.add(etq.getValor());
        }
        return etiquetas;
    }

    public List<String> tipoDato() {
        List<String> tipos = new ArrayList<>();
        for (Columna<?> col : columnas) {
            tipos.add(String.valueOf(col.obtenerTipoDato()));
        }
        return tipos;
    }


    public List<?> datosColumna(Etiqueta<?> nombre) {
        for (Columna<?> col : columnas) {
            if (col.getNombre().getValor().equals(nombre.getValor())) {
                return col.getCeldas();
            }
        }
        throw new IllegalArgumentException("Columna no encontrada: " + nombre.getValor());
    }

    public List<Object> datosFilas(Etiqueta<?> fila) {
        int etiquetaidx = etiquetasFilas.indexOf(fila);
        List<Object> filacom = new ArrayList<>();
        for (Columna<?> col : columnas) {
            filacom.add(col.getCeldas().get(etiquetaidx).getValor());
        }
        return filacom;
    }

    public Object valorEnFilaYColumna(Etiqueta<?> fila, Etiqueta<?> columna) {
        int etiquetaidx = etiquetasFilas.indexOf(fila);
        if (etiquetaidx == -1) {
            throw new IllegalArgumentException("Fila no encontrada: " + fila.getValor());
        }
        for (Columna<?> col : columnas) {
            if (col.getNombre().getValor().equals(columna.getValor())) {
                return col.getCeldas().get(etiquetaidx).getValor();
            }
        }
        throw new IllegalArgumentException("Columna no encontrada: " + columna.getValor());
    }
}