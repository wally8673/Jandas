package jandas.operaciones.ordenamiento;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Clase que provee métodos para ordenar tablas según uno o varios criterios.
 * <p>
 * Permite ordenar tablas por columnas individuales o múltiples columnas,
 * con dirección ascendente o descendente.
 * </p>
 */
public class OrdenadorTabla {

    /**
     * Ordena una tabla por una columna específica con una dirección dada.
     *
     * @param tabla La tabla a ordenar.
     * @param etiqueta La etiqueta que identifica la columna por la que ordenar.
     * @param direccion Dirección del ordenamiento (ascendente o descendente).
     * @return Una nueva tabla ordenada según el criterio especificado.
     */
    public static Tabla ordenar(Tabla tabla, Etiqueta etiqueta, Orden direccion) {
        CriterioOrden criterio = new CriterioOrden(etiqueta, direccion);
        return ordenarPorCriterios(tabla, Arrays.asList(criterio));
    }

    /**
     * Ordena una tabla por una columna específica usando el nombre de la columna.
     *
     * @param tabla La tabla a ordenar.
     * @param nombreColumna Nombre de la columna por la cual ordenar.
     * @param direccion Dirección del ordenamiento (ascendente o descendente).
     * @return Una nueva tabla ordenada según el criterio especificado.
     */
    public static Tabla ordenar(Tabla tabla, String nombreColumna, Orden direccion) {
        Etiqueta etiqueta = new EtiquetaString(nombreColumna);
        return ordenar(tabla, etiqueta, direccion);
    }

    /**
     * Ordena una tabla por una columna específica en orden ascendente (por defecto).
     *
     * @param tabla La tabla a ordenar.
     * @param nombreColumna Nombre de la columna por la cual ordenar.
     * @return Una nueva tabla ordenada ascendentemente según la columna especificada.
     */
    public static Tabla ordenar(Tabla tabla, String nombreColumna) {
        return ordenar(tabla, nombreColumna, Orden.ASCENDENTE);
    }

    /**
     * Ordena una tabla por múltiples criterios de ordenamiento.
     * <p>
     * Cada criterio indica una columna y la dirección de orden (ascendente o descendente).
     * </p>
     *
     * @param tabla La tabla a ordenar.
     * @param criterios Lista de criterios de ordenamiento.
     * @return Una nueva tabla ordenada según los criterios indicados.
     * @throws JandasException si la lista de criterios está vacía o si alguna columna no existe.
     */
    public static Tabla ordenarPorCriterios(Tabla tabla, List<CriterioOrden> criterios) {
        if (criterios == null || criterios.isEmpty()) {
            throw new JandasException("Debe proporcionar al menos un criterio de ordenamiento");
        }

        // Validar que todas las columnas existen en la tabla
        for (CriterioOrden criterio : criterios) {
            try {
                tabla.getColumna(criterio.getEtiqueta());
            } catch (JandasException e) {
                throw new JandasException("No se encontró la columna: " + criterio.getEtiqueta().getValor());
            }
        }

        // Crear lista de índices (filas) para ordenar
        List<Integer> indices = IntStream.range(0, tabla.cantFilas())
                .boxed()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // Ordenar índices según los criterios usando ComparadorMultiplesCriterios
        indices.sort(new ComparadorMultiplesCriterios(criterios, tabla));

        // Construir y devolver nueva tabla ordenada con los índices ordenados
        return crearTablaOrdenada(tabla, indices);
    }

    /**
     * Ordena una tabla por múltiples columnas usando arreglos de nombres y direcciones.
     *
     * @param tabla La tabla a ordenar.
     * @param nombresColumnas Arreglo con nombres de las columnas por las que ordenar.
     * @param direcciones Arreglo con las direcciones de orden para cada columna.
     * @return Una nueva tabla ordenada según los criterios combinados.
     * @throws JandasException si la cantidad de columnas y direcciones no coincide.
     */
    public static Tabla ordenarPorColumnas(Tabla tabla, String[] nombresColumnas, Orden[] direcciones) {
        if (nombresColumnas.length != direcciones.length) {
            throw new JandasException("El número de columnas debe coincidir con el número de direcciones");
        }

        List<CriterioOrden> criterios = new ArrayList<>();
        for (int i = 0; i < nombresColumnas.length; i++) {
            criterios.add(new CriterioOrden(new EtiquetaString(nombresColumnas[i]), direcciones[i]));
        }

        return ordenarPorCriterios(tabla, criterios);
    }

    /**
     * Crea una nueva tabla con los datos ordenados según la lista de índices ordenados.
     *
     * @param tablaOriginal Tabla original antes del ordenamiento.
     * @param indicesOrdenados Lista de índices de filas ordenadas.
     * @return Nueva tabla con filas reordenadas según los índices.
     */
    private static Tabla crearTablaOrdenada(Tabla tablaOriginal, List<Integer> indicesOrdenados) {
        Tabla tablaOrdenada = new Tabla();

        // Copiar columnas con valores ordenados según índices
        for (int i = 0; i < tablaOriginal.cantColumnas(); i++) {
            Columna<?> columnaOriginal = tablaOriginal.getColumna(i);
            Etiqueta etiquetaColumna = columnaOriginal.getEtiqueta();
            Class<?> tipo = columnaOriginal.getTipoDato();

            List<Object> valoresOrdenados = new ArrayList<>();
            for (Integer indice : indicesOrdenados) {
                Celda<?> celda = columnaOriginal.getCelda(indice);
                valoresOrdenados.add(celda.esNA() ? null : celda.getValor());
            }

            agregarColumnaOrdenada(tablaOrdenada, etiquetaColumna, tipo, valoresOrdenados);
        }

        // Reordenar etiquetas de filas si existen
        if (tablaOriginal.getEtiquetasFilas() != null) {
            List<Etiqueta> etiquetasFilasOrdenadas = new ArrayList<>();
            for (Integer indice : indicesOrdenados) {
                etiquetasFilasOrdenadas.add(tablaOriginal.getEtiquetasFilas().get(indice));
            }
            tablaOrdenada.setEtiquetasFilas(etiquetasFilasOrdenadas);
        }

        return tablaOrdenada;
    }

    /**
     * Método auxiliar para agregar una columna a la tabla respetando el tipo de datos.
     *
     * @param tabla Tabla donde se agregará la columna.
     * @param etiqueta Etiqueta que identifica la columna.
     * @param tipo Tipo de dato de la columna.
     * @param valores  Lista de valores ordenados para la columna.
     */
    @SuppressWarnings("unchecked")
    private static void agregarColumnaOrdenada(Tabla tabla, Etiqueta etiqueta, Class<?> tipo, List<Object> valores) {
        if (tipo == Integer.class) {
            List<Integer> valoresInt = new ArrayList<>();
            for (Object valor : valores) {
                valoresInt.add((Integer) valor);
            }
            tabla.agregarColumna(etiqueta, Integer.class, valoresInt);
        } else if (tipo == Double.class) {
            List<Double> valoresDouble = new ArrayList<>();
            for (Object valor : valores) {
                valoresDouble.add((Double) valor);
            }
            tabla.agregarColumna(etiqueta, Double.class, valoresDouble);
        } else if (tipo == Boolean.class) {
            List<Boolean> valoresBoolean = new ArrayList<>();
            for (Object valor : valores) {
                valoresBoolean.add((Boolean) valor);
            }
            tabla.agregarColumna(etiqueta, Boolean.class, valoresBoolean);
        } else {
            List<String> valoresString = new ArrayList<>();
            for (Object valor : valores) {
                valoresString.add(valor == null ? null : valor.toString());
            }
            tabla.agregarColumna(etiqueta, String.class, valoresString);
        }
    }

    /**
     * Crea un criterio de orden ascendente para una columna dada por nombre.
     *
     * @param nombreColumna Nombre de la columna.
     * @return Criterio de orden ascendente.
     */
    public static CriterioOrden asc(String nombreColumna) {
        return new CriterioOrden(new EtiquetaString(nombreColumna), Orden.ASCENDENTE);
    }

    /**
     * Crea un criterio de orden descendente para una columna dada por nombre.
     *
     * @param nombreColumna Nombre de la columna.
     * @return Criterio de orden descendente.
     */
    public static CriterioOrden desc(String nombreColumna) {
        return new CriterioOrden(new EtiquetaString(nombreColumna), Orden.DESCENDENTE);
    }

    /**
     * Crea un criterio de orden ascendente para una columna dada por etiqueta.
     *
     * @param etiqueta Etiqueta que identifica la columna.
     * @return Criterio de orden ascendente.
     */
    public static CriterioOrden asc(Etiqueta etiqueta) {
        return new CriterioOrden(etiqueta, Orden.ASCENDENTE);
    }

    /**
     * Crea un criterio de orden descendente para una columna dada por etiqueta.
     *
     * @param etiqueta Etiqueta que identifica la columna.
     * @return Criterio de orden descendente.
     */
    public static CriterioOrden desc(Etiqueta etiqueta) {
        return new CriterioOrden(etiqueta, Orden.DESCENDENTE);
    }
}
