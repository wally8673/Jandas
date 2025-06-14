package jandas.operaciones.concatenacion;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;
import jandas.excepciones.JandasException;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria que proporciona operaciones de concatenación para objetos de tipo {@link Tabla}.
 * <p>
 * Esta clase permite combinar dos tablas siempre que cumplan ciertas condiciones de compatibilidad
 * en cantidad de columnas, etiquetas y tipos de datos.
 */
public class ConcatenarTabla {

    /**
     * Concatena verticalmente dos tablas, uniendo sus filas bajo la condición de que ambas
     * tengan la misma estructura.
     * <p>
     * Reglas de validación:
     * <ul>
     *     <li>Las tablas deben tener la misma cantidad de columnas.</li>
     *     <li>Las etiquetas de las columnas deben coincidir en orden y contenido.</li>
     *     <li>Los tipos de datos de las columnas deben coincidir, o bien permitir
     *         una conversión implícita entre {@code Integer} y {@code Double}.</li>
     * </ul>
     *
     * @param esta La tabla base a la que se le concatenarán nuevas filas.
     * @param otra La tabla cuyos datos se agregarán debajo de la primera.
     * @return Una nueva {@link Tabla} que contiene las filas de ambas tablas combinadas.
     * @throws JandasException Si:
     * <ul>
     *     <li>Las tablas tienen diferente cantidad de columnas.</li>
     *     <li>Las etiquetas de columna no coinciden en la misma posición.</li>
     *     <li>Los tipos de datos no son compatibles.</li>
     *     <li>Alguna de las tablas está vacía.</li>
     * </ul>
     */
    public static Tabla concatenacion(Tabla esta, Tabla otra) {
        // Validación: misma cantidad de columnas
        if (esta.cantColumnas() != otra.cantColumnas()) {
            throw new JandasException("No se pueden concatenar: las tablas tienen diferente cantidad de columnas");
        }

        // Validación: no concatenar si una está vacía
        if (otra.cantColumnas() == 0) {
            throw new JandasException("No se pueden concatenar tablas vacías");
        }

        // Validación de etiquetas y tipos
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

            // Permitir Integer vs Double (conversión implícita a Double)
            boolean mezclaNumerica = (tipoEsta == Integer.class && tipoOtra == Double.class) ||
                    (tipoEsta == Double.class && tipoOtra == Integer.class);

            if (!tipoEsta.equals(tipoOtra) && !mezclaNumerica) {
                throw new JandasException(String.format(
                        "Los tipos de datos no coinciden en la columna '%s': %s vs %s",
                        etiquetaEsta.getValor(), tipoEsta.getSimpleName(), tipoOtra.getSimpleName()));
            }
        }

        // Construcción de la tabla resultante
        Tabla resultado = new Tabla();

        for (int i = 0; i < esta.cantColumnas(); i++) {
            Columna<?> columnaEsta = esta.getColumnas().get(i);
            Columna<?> columnaOtra = otra.getColumnas().get(i);
            Etiqueta etiquetaColumna = esta.getEtiquetasColumnas().get(i);

            List<Object> valoresCombinados = new ArrayList<>();

            // Agrega valores de la primera tabla
            for (Celda<?> celda : columnaEsta.getCeldas()) {
                valoresCombinados.add(celda.getValor());
            }

            // Agrega valores de la segunda tabla
            for (Celda<?> celda : columnaOtra.getCeldas()) {
                valoresCombinados.add(celda.getValor());
            }

            // Inferir tipo final (Integer + Double => Double, etc.)
            Class<?> tipoInferido = resultado.inferirTipoColumna(valoresCombinados);
            resultado.crearYAgregarColumna(etiquetaColumna, tipoInferido, valoresCombinados);
        }

        return resultado;
    }
}

