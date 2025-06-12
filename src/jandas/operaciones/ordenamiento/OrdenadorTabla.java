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

public class OrdenadorTabla {

    /**
     * Ordena una tabla por una columna específica
     * @param tabla Tabla a ordenar
     * @param etiqueta Etiqueta de la columna por la cual ordenar
     * @param direccion Dirección del ordenamiento
     * @return Nueva tabla ordenada
     */
    public static Tabla ordenar(Tabla tabla, Etiqueta etiqueta, Orden direccion) {
        CriterioOrden criterio = new CriterioOrden(etiqueta, direccion);
        return ordenarPorCriterios(tabla, Arrays.asList(criterio));
    }

    /**
     * Ordena una tabla por una columna específica usando nombre de columna
     * @param tabla Tabla a ordenar
     * @param nombreColumna Nombre de la columna por la cual ordenar
     * @param direccion Dirección del ordenamiento
     * @return Nueva tabla ordenada
     */
    public static Tabla ordenar(Tabla tabla, String nombreColumna, Orden direccion) {
        Etiqueta etiqueta = new EtiquetaString(nombreColumna);
        return ordenar(tabla, etiqueta, direccion);
    }

    /**
     * Ordena una tabla por una columna específica en orden ascendente
     * @param tabla Tabla a ordenar
     * @param nombreColumna Nombre de la columna por la cual ordenar
     * @return Nueva tabla ordenada
     */
    public static Tabla ordenar(Tabla tabla, String nombreColumna) {
        return ordenar(tabla, nombreColumna, Orden.ASCENDENTE);
    }

    /**
     * Ordena una tabla por múltiples criterios
     * @param tabla Tabla a ordenar
     * @param criterios Lista de criterios de ordenamiento
     * @return Nueva tabla ordenada
     */
    public static Tabla ordenarPorCriterios(Tabla tabla, List<CriterioOrden> criterios) {
        if (criterios == null || criterios.isEmpty()) {
            throw new JandasException("Debe proporcionar al menos un criterio de ordenamiento");
        }

        // Validar que todas las columnas existen
        for (CriterioOrden criterio : criterios) {
            try {
                tabla.getColumna(criterio.getEtiqueta());
            } catch (JandasException e) {
                throw new JandasException("No se encontró la columna: " + criterio.getEtiqueta().getValor());
            }
        }

        // Crear lista de índices para ordenar
        List<Integer> indices = IntStream.range(0, tabla.cantFilas())
                .boxed()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // Ordenar los índices basándose en los criterios
        indices.sort(new ComparadorMultiplesCriterios(criterios, tabla));

        // Crear nueva tabla con los datos ordenados
        return crearTablaOrdenada(tabla, indices);
    }

    /**
     * Ordena por múltiples columnas usando arrays
     * @param tabla Tabla a ordenar
     * @param nombresColumnas Nombres de las columnas
     * @param direcciones Direcciones de ordenamiento para cada columna
     * @return Nueva tabla ordenada
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
     * Crea una nueva tabla con los datos ordenados según los índices proporcionados
     */
    private static Tabla crearTablaOrdenada(Tabla tablaOriginal, List<Integer> indicesOrdenados) {
        Tabla tablaOrdenada = new Tabla();

        // Copiar columnas con datos reordenados
        for (int i = 0; i < tablaOriginal.cantColumnas(); i++) {
            Columna<?> columnaOriginal = tablaOriginal.getColumna(i);
            Etiqueta etiquetaColumna = columnaOriginal.getEtiqueta();
            Class<?> tipo = columnaOriginal.getTipoDato();

            // Crear nueva columna con datos ordenados
            List<Object> valoresOrdenados = new ArrayList<>();
            for (Integer indice : indicesOrdenados) {
                Celda<?> celda = columnaOriginal.getCelda(indice);
                valoresOrdenados.add(celda.esNA() ? null : celda.getValor());
            }

            // Agregar columna usando el método genérico
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
     * Método auxiliar para agregar columna ordenada manteniendo tipos
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
     * Métodos auxiliares estáticos para crear criterios de ordenamiento fácilmente
     */
    public static CriterioOrden asc(String nombreColumna) {
        return new CriterioOrden(new EtiquetaString(nombreColumna), Orden.ASCENDENTE);
    }

    public static CriterioOrden desc(String nombreColumna) {
        return new CriterioOrden(new EtiquetaString(nombreColumna), Orden.DESCENDENTE);
    }

    public static CriterioOrden asc(Etiqueta etiqueta) {
        return new CriterioOrden(etiqueta, Orden.ASCENDENTE);
    }

    public static CriterioOrden desc(Etiqueta etiqueta) {
        return new CriterioOrden(etiqueta, Orden.DESCENDENTE);
    }
}