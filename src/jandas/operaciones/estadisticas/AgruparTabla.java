package jandas.operaciones.estadisticas;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;

import java.util.*;
import java.util.stream.Collectors;

public class AgruparTabla {

    /**
     * Agrupa las filas por una columna y aplica operaciones de agregación
     */
    public static Tabla agruparPor(Tabla tabla, String nombreColumna, Map<String, OperacionEstadistica> operaciones) {
        return agruparPor(tabla, new String[]{nombreColumna}, operaciones);
    }

    /**
     * Agrupa las filas por múltiples columnas y aplica operaciones de agregación
     */
    public static Tabla agruparPor(Tabla tabla, String[] nombresColumnas, Map<String, OperacionEstadistica> operaciones) {
        if (tabla.cantFilas() == 0) {
            return new Tabla();
        }

        // Validar que las columnas de agrupamiento existen
        List<Columna<?>> columnasAgrupamiento = new ArrayList<>();
        for (String nombre : nombresColumnas) {
            columnasAgrupamiento.add(tabla.getColumna(nombre));
        }

        // Obtener columnas numéricas que no son de agrupamiento
        List<Columna<?>> columnasNumericas = obtenerColumnasNumericas(tabla, Arrays.asList(nombresColumnas));

        // Validar operaciones
        validarOperaciones(operaciones, columnasNumericas);

        // Crear grupos
        Map<String, List<Integer>> grupos = crearGrupos(tabla, columnasAgrupamiento);

        // Crear tabla resultado
        return crearTablaAgregada(tabla, grupos, columnasNumericas, operaciones);
    }

    /**
     * Agrupa por una columna y aplica la misma operación a todas las columnas numéricas
     */
    public static Tabla agruparPor(Tabla tabla, String nombreColumna, OperacionEstadistica operacion) {
        if (tabla.cantFilas() == 0) {
            return new Tabla();
        }

        // Obtener columnas numéricas (excluyendo la de agrupamiento)
        List<Columna<?>> columnasNumericas = obtenerColumnasNumericas(tabla, Arrays.asList(nombreColumna));

        // Crear mapa de operaciones para todas las columnas numéricas
        Map<String, OperacionEstadistica> operaciones = new HashMap<>();
        for (Columna<?> columna : columnasNumericas) {
            operaciones.put(columna.getEtiqueta().getValor().toString(), operacion);
        }

        return agruparPor(tabla, nombreColumna, operaciones);
    }

    /**
     * Obtiene las columnas numéricas que no están en la lista de exclusión
     */
    private static List<Columna<?>> obtenerColumnasNumericas(Tabla tabla, List<String> excluir) {
        List<Columna<?>> columnasNumericas = new ArrayList<>();

        for (Columna<?> columna : tabla.getColumnas()) {
            String nombreColumna = columna.getEtiqueta().getValor().toString();

            // Excluir columnas de agrupamiento
            if (excluir.contains(nombreColumna)) {
                continue;
            }

            // Solo incluir columnas numéricas
            Class<?> tipo = columna.getTipoDato();
            if (esNumerico(tipo)) {
                columnasNumericas.add(columna);
            }
        }

        return columnasNumericas;
    }

    /**
     * Verifica si un tipo es numérico
     */
    private static boolean esNumerico(Class<?> tipo) {
        return tipo == Integer.class || tipo == Double.class ||
                tipo == Float.class || tipo == Long.class;
    }

    /**
     * Valida que las operaciones sean válidas para las columnas
     */
    private static void validarOperaciones(Map<String, OperacionEstadistica> operaciones, List<Columna<?>> columnasNumericas) {
        Set<String> nombresColumnasNumericas = columnasNumericas.stream()
                .map(col -> col.getEtiqueta().getValor().toString())
                .collect(Collectors.toSet());

        for (String nombreColumna : operaciones.keySet()) {
            if (!nombresColumnasNumericas.contains(nombreColumna)) {
                throw new JandasException("La columna '" + nombreColumna + "' no existe o no es numérica");
            }
        }
    }

    /**
     * Crea los grupos basándose en las columnas de agrupamiento
     */
    private static Map<String, List<Integer>> crearGrupos(Tabla tabla, List<Columna<?>> columnasAgrupamiento) {
        Map<String, List<Integer>> grupos = new LinkedHashMap<>();

        for (int i = 0; i < tabla.cantFilas(); i++) {
            // Crear clave del grupo concatenando valores de las columnas de agrupamiento
            StringBuilder claveGrupo = new StringBuilder();
            boolean primerValor = true;

            for (Columna<?> columna : columnasAgrupamiento) {
                if (!primerValor) {
                    claveGrupo.append(", ");
                }

                Celda<?> celda = columna.getCelda(i);
                String valor = celda.esNA() ? "NA" : celda.getValor().toString();
                claveGrupo.append(valor);
                primerValor = false;
            }

            String clave = claveGrupo.toString();
            grupos.computeIfAbsent(clave, k -> new ArrayList<>()).add(i);
        }

        return grupos;
    }

    /**
     * Crea la tabla agregada con los resultados
     */
    private static Tabla crearTablaAgregada(Tabla tablaOriginal, Map<String, List<Integer>> grupos,
                                            List<Columna<?>> columnasNumericas, Map<String, OperacionEstadistica> operaciones) {
        Tabla tablaResultado = new Tabla();
        // Agregar primero la columna de agrupamiento (Grupo)
        List<String> valoresGrupo = new ArrayList<>(grupos.keySet());
        tablaResultado.agregarColumna(
                new EtiquetaString("Grupo"),
                String.class,
                valoresGrupo
        );

        // Procesar cada columna numérica
        for (Columna<?> columna : columnasNumericas) {
            String nombreColumna = columna.getEtiqueta().getValor().toString();
            OperacionEstadistica operacion = operaciones.get(nombreColumna);

            if (operacion != null) {
                // Calcular valores agregados para cada grupo
                List<Double> valoresAgregados = new ArrayList<>();

                for (List<Integer> indicesGrupo : grupos.values()) {
                    double valorAgregado = calcularAgregacion(columna, indicesGrupo, operacion);
                    valoresAgregados.add(valorAgregado);
                }

                // Agregar columna resultado
                tablaResultado.agregarColumna(
                        new EtiquetaString(nombreColumna),
                        Double.class,
                        valoresAgregados
                );
            }
        }

        // Establecer etiquetas de filas con los nombres de los grupos
        List<Etiqueta> etiquetasFilas = grupos.keySet().stream()
                .map(EtiquetaString::new)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (!etiquetasFilas.isEmpty()) {
            tablaResultado.setEtiquetasFilas(etiquetasFilas);
        }

        return tablaResultado;
    }

    /**
     * Calcula el valor agregado para un grupo específico
     */
    private static double calcularAgregacion(Columna<?> columna, List<Integer> indices, OperacionEstadistica operacion) {
        // Obtener valores no nulos del grupo
        List<Double> valores = new ArrayList<>();

        for (Integer indice : indices) {
            Celda<?> celda = columna.getCelda(indice);
            if (!celda.esNA()) {
                Object valor = celda.getValor();
                if (valor instanceof Number) {
                    valores.add(((Number) valor).doubleValue());
                }
            }
        }

        if (valores.isEmpty()) {
            return Double.NaN; // Retornar NaN si no hay valores válidos
        }

        switch (operacion) {
            case SUMA:
                return valores.stream().mapToDouble(Double::doubleValue).sum();

            case MAXIMO:
                return valores.stream().mapToDouble(Double::doubleValue).max().orElse(Double.NaN);

            case MINIMO:
                return valores.stream().mapToDouble(Double::doubleValue).min().orElse(Double.NaN);

            case CUENTA:
                return valores.size();

            case MEDIA:
                return valores.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);

            case VARIANZA:
                return calcularVarianza(valores);

            case DESVIO_ESTANDAR:
                return Math.sqrt(calcularVarianza(valores));

            default:
                throw new JandasException("Operación de agregación no soportada: " + operacion);
        }
    }

    /**
     * Calcula la varianza de una lista de valores
     */
    private static double calcularVarianza(List<Double> valores) {
        if (valores.size() < 2) {
            return Double.NaN;
        }

        double media = valores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double sumaCuadrados = valores.stream()
                .mapToDouble(valor -> Math.pow(valor - media, 2))
                .sum();

        return sumaCuadrados / (valores.size() - 1); // Varianza muestral
    }
}
