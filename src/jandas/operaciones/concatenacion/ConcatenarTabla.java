package jandas.operaciones.concatenacion;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;

import java.util.ArrayList;
import java.util.List;

public class ConcatenarTabla {

    public static Tabla concatenacion(Tabla esta, Tabla otra) {
        if (esta.cantColumnas() != otra.cantColumnas()) {
            throw new JandasException("No se pueden concatenar: las tablas tienen diferente cantidad de columnas");
        }

        if (otra.cantColumnas() == 0) {
            throw new JandasException("No se pueden concatenar tablas vacías");
        }

        // Validar columnas
        for (int i = 0; i < esta.cantColumnas(); i++) {
            Etiqueta etiquetaEsta = esta.getEtiquetasColumnas().get(i);
            Etiqueta etiquetaOtra = otra.getEtiquetasColumnas().get(i);
            Class<?> tipoEsta = esta.getColumnas().get(i).getTipoDato();
            Class<?> tipoOtra = otra.getColumnas().get(i).getTipoDato();

            if (!etiquetaEsta.getValor().equals(etiquetaOtra.getValor())) {
                throw new JandasException(String.format(
                        "Las etiquetas de columna no coinciden en la posición %d: '%s' vs '%s'",
                        i, etiquetaEsta.getValor(), etiquetaOtra.getValor()));
            }

            // Aceptar mezcla de Integer y Double (será convertido a Double)
            boolean mezclaNumerica = (tipoEsta == Integer.class && tipoOtra == Double.class) ||
                    (tipoEsta == Double.class && tipoOtra == Integer.class);

            if (!tipoEsta.equals(tipoOtra) && !mezclaNumerica) {
                throw new JandasException(String.format(
                        "Los tipos de datos no coinciden en la columna '%s': %s vs %s",
                        etiquetaEsta.getValor(), tipoEsta.getSimpleName(), tipoOtra.getSimpleName()));
            }
        }

        Tabla resultado = new Tabla();

        for (int i = 0; i < esta.cantColumnas(); i++) {
            Columna<?> columnaEsta = esta.getColumnas().get(i);
            Columna<?> columnaOtra = otra.getColumnas().get(i);
            Etiqueta etiquetaColumna = esta.getEtiquetasColumnas().get(i);

            List<Object> valoresCombinados = new ArrayList<>();
            for (Celda<?> celda : columnaEsta.getCeldas()) {
                valoresCombinados.add(celda.getValor());
            }
            for (Celda<?> celda : columnaOtra.getCeldas()) {
                valoresCombinados.add(celda.getValor());
            }

            Class<?> tipoInferido = resultado.inferirTipoColumna(valoresCombinados);
            resultado.crearYAgregarColumna(etiquetaColumna, tipoInferido, valoresCombinados);
        }

        return resultado;
    }
}
