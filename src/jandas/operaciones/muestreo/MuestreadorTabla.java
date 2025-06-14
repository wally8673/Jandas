package jandas.operaciones.muestreo;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;
import jandas.excepciones.JandasException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;


/**
 * Clase utilitaria que proporciona métodos para realizar muestreo aleatorio
 * sobre una {@link Tabla}. Incluye funcionalidades para muestreo simple,
 * muestreo con tamaño fijo, muestreo estratificado y control de semilla
 * para reproducibilidad.
 * <p>
 * Los métodos de esta clase generan nuevas instancias de {@code Tabla} con
 * subconjuntos de filas seleccionadas de forma aleatoria.
 * </p>
 *
 * <p>Tipos de muestreo disponibles:</p>
 * <ul>
 *     <li>Muestreo aleatorio simple por porcentaje.</li>
 *     <li>Muestreo aleatorio simple por cantidad fija de filas.</li>
 *     <li>Muestreo estratificado por una columna específica.</li>
 * </ul>
 *
 * <p>Los métodos respetan los tipos de datos de las columnas y copian
 * las etiquetas de fila correspondientes a las filas seleccionadas.</p>
 */

public class MuestreadorTabla {

    private static final Random random = new Random();

    /**
     * Devuelve una muestra aleatoria de una tabla, seleccionando un porcentaje
     * de las filas de forma uniforme y sin reemplazo.
     *
     * @param tabla Tabla original de la cual obtener la muestra
     * @param porcentaje Porcentaje de filas a seleccionar (1-100)
     * @return Una nueva tabla que contiene una muestra aleatoria de las filas
     * @throws JandasException si el porcentaje no está en el rango válido
     */
    public static Tabla muestrear(Tabla tabla, int porcentaje) {
        if (porcentaje < 1 || porcentaje > 100) {
            throw new JandasException("El porcentaje debe estar entre 1 y 100");
        }

        if (tabla.cantFilas() == 0) {
            return new Tabla(); // Retorna tabla vacía si no hay filas
        }

        // Calcular cantidad de filas a muestrear
        int totalFilas = tabla.cantFilas();
        int filasAMuestrear = Math.max(1, (int) Math.round(totalFilas * porcentaje / 100.0));

        // Si se pide más del 100%, retornar toda la tabla
        if (filasAMuestrear >= totalFilas) {
            return copiarTablaCompleta(tabla);
        }

        // Generar índices aleatorios únicos
        List<Integer> indicesAleatorios = generarIndicesAleatorios(totalFilas, filasAMuestrear);

        // Crear nueva tabla con la muestra
        return crearTablaMuestreada(tabla, indicesAleatorios);
    }

    /**
     * Devuelve una muestra aleatoria de tamaño fijo de una tabla.
     *
     * @param tabla Tabla original
     * @param cantidadFilas Número de filas a seleccionar
     * @param exacto (parámetro ignorado en esta implementación)
     * @return Nueva tabla con una muestra de tamaño fijo
     * @throws JandasException si la cantidad es negativa
     */
    public static Tabla muestrear(Tabla tabla, int cantidadFilas, boolean exacto) {
        if (cantidadFilas < 0) {
            throw new JandasException("La cantidad de filas debe ser mayor o igual a 0");
        }

        if (tabla.cantFilas() == 0) {
            return new Tabla(); // Retorna tabla vacía si no hay filas
        }

        int totalFilas = tabla.cantFilas();
        int filasAMuestrear = Math.min(cantidadFilas, totalFilas);

        if (filasAMuestrear == 0) {
            return new Tabla(); // Retorna tabla vacía
        }

        if (filasAMuestrear >= totalFilas) {
            return copiarTablaCompleta(tabla);
        }

        // Generar índices aleatorios únicos
        List<Integer> indicesAleatorios = generarIndicesAleatorios(totalFilas, filasAMuestrear);

        // Crear nueva tabla con la muestra
        return crearTablaMuestreada(tabla, indicesAleatorios);
    }

    /**
     * Devuelve una muestra aleatoria estratificada, agrupando por los valores
     * de una columna especificada. Dentro de cada grupo (estrato), se toma un
     * porcentaje de las filas.
     *
     * @param tabla Tabla original
     * @param nombreColumna Nombre de la columna usada para estratificar
     * @param porcentaje Porcentaje de cada estrato a incluir (1-100)
     * @return Una nueva tabla con la muestra estratificada
     * @throws JandasException si el porcentaje está fuera del rango válido
     */
    public static Tabla muestrearEstratificado(Tabla tabla, String nombreColumna, int porcentaje) {
        if (porcentaje < 1 || porcentaje > 100) {
            throw new JandasException("El porcentaje debe estar entre 1 y 100");
        }

        if (tabla.cantFilas() == 0) {
            return new Tabla();
        }

        // Obtener la columna para estratificar
        Columna<?> columnaEstrato = tabla.getColumna(nombreColumna);

        // Agrupar índices por valor de la columna
        List<List<Integer>> estratos = agruparPorValor(columnaEstrato);

        // Muestrear cada estrato
        List<Integer> indicesMuestreados = new ArrayList<>();
        for (List<Integer> estrato : estratos) {
            int filasEstrato = Math.max(1, (int) Math.round(estrato.size() * porcentaje / 100.0));
            List<Integer> indicesEstrato = generarIndicesAleatorios(estrato, filasEstrato);
            indicesMuestreados.addAll(indicesEstrato);
        }

        // Mezclar los índices finales para evitar agrupación por estrato
        Collections.shuffle(indicesMuestreados, random);

        return crearTablaMuestreada(tabla, indicesMuestreados);
    }

    /**
     * Establece una semilla fija para el generador de números aleatorios,
     * permitiendo que los resultados del muestreo sean reproducibles.
     *
     * @param semilla Valor de la semilla
     */
    public static void setSemilla(long semilla) {
        random.setSeed(semilla);
    }

    /**
     * Genera una lista de índices aleatorios únicos desde un total de filas.
     *
     * @param totalFilas Cantidad total de filas
     * @param cantidadAMuestrear Número de índices a generar
     * @return Lista con índices seleccionados al azar
     */
    private static List<Integer> generarIndicesAleatorios(int totalFilas, int cantidadAMuestrear) {
        // Crear lista con todos los índices posibles
        List<Integer> todosLosIndices = IntStream.range(0, totalFilas)
                .boxed()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // Mezclar la lista y tomar los primeros N elementos
        Collections.shuffle(todosLosIndices, random);

        return new ArrayList<>(todosLosIndices.subList(0, cantidadAMuestrear));
    }

    /**
     * Genera una lista de índices aleatorios únicos desde una lista específica.
     *
     * @param indicesDisponibles Lista de índices posibles
     * @param cantidadAMuestrear Número de índices a generar
     * @return Sublista aleatoria de los índices disponibles
     */
    private static List<Integer> generarIndicesAleatorios(List<Integer> indicesDisponibles, int cantidadAMuestrear) {
        List<Integer> copiaIndices = new ArrayList<>(indicesDisponibles);
        Collections.shuffle(copiaIndices, random);

        int cantidad = Math.min(cantidadAMuestrear, copiaIndices.size());
        return new ArrayList<>(copiaIndices.subList(0, cantidad));
    }

    /**
     * Agrupa los índices de las filas de una columna según su valor,
     * generando listas separadas para cada valor único.
     *
     * @param columna Columna por la cual agrupar
     * @return Lista de grupos de índices (estratos)
     */
    private static List<List<Integer>> agruparPorValor(Columna<?> columna) {
        List<List<Integer>> grupos = new ArrayList<>();
        List<Object> valoresUnicos = new ArrayList<>();

        for (int i = 0; i < columna.size(); i++) {
            Celda<?> celda = columna.getCelda(i);
            Object valor = celda.esNA() ? null : celda.getValor();

            // Buscar si ya existe un grupo para este valor
            int indiceGrupo = -1;
            for (int j = 0; j < valoresUnicos.size(); j++) {
                Object valorExistente = valoresUnicos.get(j);
                if ((valor == null && valorExistente == null) ||
                        (valor != null && valor.equals(valorExistente))) {
                    indiceGrupo = j;
                    break;
                }
            }

            // Si no existe el grupo, crearlo
            if (indiceGrupo == -1) {
                valoresUnicos.add(valor);
                grupos.add(new ArrayList<>());
                indiceGrupo = grupos.size() - 1;
            }

            // Agregar el índice al grupo correspondiente
            grupos.get(indiceGrupo).add(i);
        }

        return grupos;
    }

    /**
     * Crea una nueva tabla a partir de una lista de índices seleccionados.
     *
     * @param tablaOriginal Tabla fuente
     * @param indices Índices de filas a incluir
     * @return Nueva tabla con las filas seleccionadas
     */
    private static Tabla crearTablaMuestreada(Tabla tablaOriginal, List<Integer> indices) {
        Tabla tablaMuestreada = new Tabla();

        // Copiar columnas con datos seleccionados
        for (int i = 0; i < tablaOriginal.cantColumnas(); i++) {
            Columna<?> columnaOriginal = tablaOriginal.getColumna(i);
            Etiqueta etiquetaColumna = columnaOriginal.getEtiqueta();
            Class<?> tipo = columnaOriginal.getTipoDato();

            // Crear nueva columna con datos seleccionados
            List<Object> valoresSeleccionados = new ArrayList<>();
            for (Integer indice : indices) {
                Celda<?> celda = columnaOriginal.getCelda(indice);
                valoresSeleccionados.add(celda.esNA() ? null : celda.getValor());
            }

            // Agregar columna usando el método genérico
            agregarColumnaMuestreada(tablaMuestreada, etiquetaColumna, tipo, valoresSeleccionados);
        }

        // Copiar etiquetas de filas seleccionadas
        if (tablaOriginal.getEtiquetasFilas() != null) {
            List<Etiqueta> etiquetasFilasSeleccionadas = new ArrayList<>();
            for (Integer indice : indices) {
                etiquetasFilasSeleccionadas.add(tablaOriginal.getEtiquetasFilas().get(indice));
            }
            tablaMuestreada.setEtiquetasFilas(etiquetasFilasSeleccionadas);
        }

        return tablaMuestreada;
    }

    /**
     * Método auxiliar que agrega una columna a la tabla muestreada,
     * preservando el tipo de datos original.
     *
     * @param tabla Tabla destino
     * @param etiqueta Etiqueta de la columna
     * @param tipo Tipo de dato (Integer, Double, etc.)
     * @param valores Valores a insertar
     */
    @SuppressWarnings("unchecked")
    private static void agregarColumnaMuestreada(Tabla tabla, Etiqueta etiqueta, Class<?> tipo, List<Object> valores) {
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
     * Copia toda la tabla original generando una nueva instancia.
     *
     * @param tablaOriginal Tabla a copiar
     * @return Copia exacta de la tabla original
     */
    private static Tabla copiarTablaCompleta(Tabla tablaOriginal) {
        List<Integer> todosLosIndices = IntStream.range(0, tablaOriginal.cantFilas())
                .boxed()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        return crearTablaMuestreada(tablaOriginal, todosLosIndices);
    }
}