package jandas.operaciones.estadisticas;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que proporciona métodos para agrupar filas de una tabla según una o más columnas,
 * y aplicar operaciones estadísticas de agregación sobre columnas numéricas.
 */
public class AgruparTabla {

    /**
     * Agrupa las filas por una sola columna y aplica operaciones estadísticas
     * distintas a cada columna numérica.
     *
     * @param tabla Tabla de entrada.
     * @param nombreColumna Nombre de la columna por la cual agrupar.
     * @param operaciones Mapa que indica qué operación aplicar a cada columna numérica.
     * @return Tabla resultante con los valores agregados por grupo.
     */
    public static Tabla agruparPor(Tabla tabla, String nombreColumna, Map<String, OperacionEstadistica> operaciones) {
        return agruparPor(tabla, new String[]{nombreColumna}, operaciones);
    }

    /**
     * Agrupa las filas por múltiples columnas y aplica operaciones estadísticas
     * distintas a cada columna numérica.
     *
     * @param tabla Tabla de entrada.
     * @param nombresColumnas Nombres de las columnas por las cuales agrupar.
     * @param operaciones Mapa que indica qué operación aplicar a cada columna numérica.
     * @return Tabla con resultados agrupados y agregados.
     * @throws JandasException si alguna columna indicada no es numérica o no existe.
     */
    public static Tabla agruparPor(Tabla tabla, String[] nombresColumnas, Map<String, OperacionEstadistica> operaciones) {
        if (tabla.cantFilas() == 0) {
            return new Tabla();
        }

        // Validar columnas de agrupamiento
        List<Columna<?>> columnasAgrupamiento = new ArrayList<>();
        for (String nombre : nombresColumnas) {
            columnasAgrupamiento.add(tabla.getColumna(nombre));
        }

        List<Columna<?>> columnasNumericas = obtenerColumnasNumericas(tabla, Arrays.asList(nombresColumnas));
        validarOperaciones(operaciones, columnasNumericas);
        Map<String, List<Integer>> grupos = crearGrupos(tabla, columnasAgrupamiento);
        return crearTablaAgregada(tabla, grupos, columnasNumericas, operaciones);
    }

    /**
     * Agrupa por una columna y aplica la misma operación estadística
     * a todas las columnas numéricas.
     *
     * @param tabla Tabla de entrada.
     * @param nombreColumna Nombre de la columna de agrupamiento.
     * @param operacion Operación a aplicar a todas las columnas numéricas.
     * @return Tabla agrupada y agregada.
     */
    public static Tabla agruparPor(Tabla tabla, String nombreColumna, OperacionEstadistica operacion) {
        if (tabla.cantFilas() == 0) {
            return new Tabla();
        }

        List<Columna<?>> columnasNumericas = obtenerColumnasNumericas(tabla, Arrays.asList(nombreColumna));

        Map<String, OperacionEstadistica> operaciones = new HashMap<>();
        for (Columna<?> columna : columnasNumericas) {
            operaciones.put(columna.getEtiqueta().getValor().toString(), operacion);
        }

        return agruparPor(tabla, nombreColumna, operaciones);
    }

    /**
     * Obtiene columnas numéricas que no estén en la lista de exclusión.
     *
     * @param tabla Tabla fuente.
     * @param excluir Lista de nombres de columnas a excluir.
     * @return Lista de columnas numéricas.
     */
    private static List<Columna<?>> obtenerColumnasNumericas(Tabla tabla, List<String> excluir) {
        List<Columna<?>> columnasNumericas = new ArrayList<>();

        for (Columna<?> columna : tabla.getColumnas()) {
            String nombreColumna = columna.getEtiqueta().getValor().toString();

            if (excluir.contains(nombreColumna)) {
                continue;
            }

            Class<?> tipo = columna.getTipoDato();
            if (esNumerico(tipo)) {
                columnasNumericas.add(columna);
            }
        }

        return columnasNumericas;
    }

    /**
     * Verifica si una clase representa un tipo numérico.
     *
     * @param tipo Tipo de dato.
     * @return true si es Integer, Double, Float o Long.
     */
    private static boolean esNumerico(Class<?> tipo) {
        return tipo == Integer.class || tipo == Double.class || tipo == Float.class || tipo == Long.class;
    }

    /**
     * Valida que las operaciones sean aplicables a las columnas numéricas.
     *
     * @param operaciones Mapa de operaciones por columna.
     * @param columnasNumericas Lista de columnas numéricas disponibles.
     * @throws JandasException si alguna operación está asociada a una columna inválida.
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
     * Agrupa índices de filas por combinación de valores de las columnas de agrupamiento.
     *
     * @param tabla Tabla de entrada.
     * @param columnasAgrupamiento Lista de columnas a usar para agrupar.
     * @return Mapa de clave de grupo a lista de índices de fila.
     */
    private static Map<String, List<Integer>> crearGrupos(Tabla tabla, List<Columna<?>> columnasAgrupamiento) {
        Map<String, List<Integer>> grupos = new LinkedHashMap<>();

        for (int i = 0; i < tabla.cantFilas(); i++) {
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
     * Crea una nueva tabla con los resultados de la agregación por grupo.
     *
     * @param tablaOriginal Tabla original con los datos.
     * @param grupos Mapa de grupo a índices de filas.
     * @param columnasNumericas Columnas numéricas a agregar.
     * @param operaciones Mapa de operación a aplicar por columna.
     * @return Tabla con columnas agrupadas y valores agregados.
     */
    private static Tabla crearTablaAgregada(Tabla tablaOriginal, Map<String, List<Integer>> grupos,
                                            List<Columna<?>> columnasNumericas, Map<String, OperacionEstadistica> operaciones) {
        Tabla tablaResultado = new Tabla();

        List<String> valoresGrupo = new ArrayList<>(grupos.keySet());
        tablaResultado.agregarColumna(new EtiquetaString("Grupo"), String.class, valoresGrupo);

        for (Columna<?> columna : columnasNumericas) {
            String nombreColumna = columna.getEtiqueta().getValor().toString();
            OperacionEstadistica operacion = operaciones.get(nombreColumna);

            if (operacion != null) {
                List<Double> valoresAgregados = new ArrayList<>();

                for (List<Integer> indicesGrupo : grupos.values()) {
                    double valorAgregado = calcularAgregacion(columna, indicesGrupo, operacion);
                    valoresAgregados.add(valorAgregado);
                }

                tablaResultado.agregarColumna(
                        new EtiquetaString(nombreColumna),
                        Double.class,
                        valoresAgregados
                );
            }
        }

        List<Etiqueta> etiquetasFilas = grupos.keySet().stream()
                .map(EtiquetaString::new)
                .collect(Collectors.toList());

        if (!etiquetasFilas.isEmpty()) {
            tablaResultado.setEtiquetasFilas(etiquetasFilas);
        }

        return tablaResultado;
    }

    /**
     * Aplica una operación estadística sobre un grupo de celdas de una columna.
     *
     * @param columna Columna a procesar.
     * @param indices Índices de las filas del grupo.
     * @param operacion Tipo de operación estadística.
     * @return Resultado numérico de la operación.
     * @throws JandasException si la operación no es reconocida.
     */
    private static double calcularAgregacion(Columna<?> columna, List<Integer> indices, OperacionEstadistica operacion) {
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
            return Double.NaN;
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
     * Calcula la varianza muestral de una lista de valores.
     *
     * @param valores Lista de valores numéricos.
     * @return Varianza muestral de los valores.
     */
    private static double calcularVarianza(List<Double> valores) {
        if (valores.size() < 2) {
            return Double.NaN;
        }

        double media = valores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double sumaCuadrados = valores.stream()
                .mapToDouble(valor -> Math.pow(valor - media, 2))
                .sum();

        return sumaCuadrados / (valores.size() - 1);
    }
}