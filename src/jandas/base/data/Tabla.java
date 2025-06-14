package jandas.base.data;

import jandas.operaciones.Agrupable;
import jandas.operaciones.estadisticas.AgruparTabla;
import jandas.operaciones.estadisticas.OperacionEstadistica;
import jandas.operaciones.filtros.*;
import jandas.visualizacion.*;
import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.operaciones.Muestreable;
import jandas.operaciones.concatenacion.ConcatenarTabla;
import jandas.operaciones.muestreo.MuestreadorTabla;
import jandas.operaciones.Ordenable;
import jandas.operaciones.ordenamiento.OrdenadorTabla;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.Orden;
import jandas.operaciones.Concatenable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Representa una tabla de datos que implementa múltiples operaciones como ordenamiento,
 * muestreo, concatenación y agrupación. Esta clase es el componente principal para
 * manejar datos tabulares en el framework Jandas.
 *
 * <p>Una tabla consiste en columnas tipadas, etiquetas de filas y etiquetas de columnas.
 * Soporta diferentes tipos de datos incluyendo Integer, Double, String y Boolean.</p>
 *
 * @author Jandas Framework
 * @version 1.0
 */
public class Tabla implements
        Ordenable,
        Muestreable,
        Concatenable,
        Agrupable {

    /**
     * Lista de columnas que conforman la tabla
     */
    private List<Columna<?>> columnas;

    /**
     * Lista de etiquetas que identifican cada fila
     */
    private List<Etiqueta> etiquetasFilas;

    /**
     * Lista de etiquetas que identifican cada columna
     */
    private List<Etiqueta> etiquetasColumnas;

    /**
     * Constructor por defecto que inicializa una tabla vacía.
     * Crea listas vacías para columnas, etiquetas de filas y etiquetas de columnas.
     */
    public Tabla() {
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
    }

    /**
     * Constructor que crea una tabla basada en etiquetas de columnas y lista de columnas.
     *
     * @param etiquetasColumnas Lista de etiquetas para las columnas
     * @param columnas          Lista de columnas que conformarán la tabla
     */
    public Tabla(List<Etiqueta> etiquetasColumnas, List<Columna<?>> columnas) {
        this.columnas = new ArrayList<>(columnas);
        this.etiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        this.etiquetasFilas = generarEtiquetaFilas();

    }

    /**
     * Constructor que crea una tabla a partir de una matriz de objetos.
     * La primera fila se interpreta como encabezados de columna.
     *
     * @param datos Matriz bidimensional donde la primera fila contiene los encabezados
     *              y las siguientes filas contienen los datos
     * @throws JandasException si los datos son inválidos o inconsistentes
     */
    public Tabla(Object[][] datos) {
        this.columnas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
        this.etiquetasFilas = generarEtiquetaFilas();

        validarDatos(datos);
        procesarMatriz(datos);
    }

    /**
     * Constructor para crear tabla desde una secuencia lineal de strings
     *
     * @param datos       Lista de strings con los datos en secuencia lineal
     * @param numColumnas Número de columnas para organizar los datos
     * @throws JandasException si la cantidad de datos no es divisible por el número de columnas
     */
    public Tabla(List<String> datos, int numColumnas) {
        this.columnas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
        this.etiquetasFilas = generarEtiquetaFilas();

        validarDatosLineales(datos, numColumnas);
        Object[][] matriz = construirMatriz(datos, numColumnas);
        procesarMatriz(matriz);
    }

    /**
     * Valida que los datos lineales sean consistentes con el número de columnas especificado.
     *
     * @param datos       Lista de datos a validar
     * @param numColumnas Número de columnas esperado
     * @throws JandasException si los datos son nulos, vacíos o no divisibles por numColumnas
     */
    private void validarDatosLineales(List<String> datos, int numColumnas) {
        if (datos == null || datos.isEmpty()) {
            throw new JandasException("La lista de datos no puede ser null o vacía");
        }

        if (numColumnas <= 0) {
            throw new JandasException("El número de columnas debe ser mayor a 0");
        }

        if (datos.size() % numColumnas != 0) {
            throw new JandasException(String.format(
                    "La cantidad de datos (%d) no es divisible por el número de columnas (%d)",
                    datos.size(), numColumnas));
        }
    }

    /**
     * Construye una matriz bidimensional a partir de una lista lineal de datos.
     *
     * @param datos       Lista de datos en secuencia lineal
     * @param numColumnas Número de columnas para la matriz resultante
     * @return Matriz bidimensional organizada por filas y columnas
     */
    private Object[][] construirMatriz(List<String> datos, int numColumnas) {
        int numFilas = datos.size() / numColumnas;
        Object[][] matriz = new Object[numFilas][numColumnas];

        for (int i = 0; i < datos.size(); i++) {
            int fila = i / numColumnas;
            int columna = i % numColumnas;
            matriz[fila][columna] = datos.get(i);
        }

        return matriz;
    }

    /**
     * Valida que la matriz de datos tenga al menos una fila de encabezado y una de datos,
     * y que todas las filas tengan la misma cantidad de columnas.
     *
     * @param datos Matriz de datos a validar
     * @throws JandasException si la matriz es inválida
     */
    private void validarDatos(Object[][] datos) {
        if (datos == null || datos.length < 2) {
            throw new JandasException("Se necesita al menos una fila de encabezado y una de datos.");
        }

        int columnasEsperadas = datos[0].length;
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].length != columnasEsperadas) {
                throw new JandasException("Todas las filas deben tener la misma cantidad de columnas.");
            }
        }
    }

    /**
     * Procesa una matriz de datos creando las columnas correspondientes.
     * La primera fila se usa como encabezados y el resto como datos.
     * Infiere automáticamente los tipos de datos de cada columna.
     *
     * @param datos Matriz de datos a procesar
     */
    private void procesarMatriz(Object[][] datos) {
        int cantidadFilas = datos.length;
        int cantidadColumnas = datos[0].length;
        // Procesar datos por columna
        for (int j = 0; j < cantidadColumnas; j++) {
            // Crear etiqueta del encabezado
            Object encabezado = datos[0][j];
            Etiqueta etiqueta = crearEtiquetaDesdeObjeto(encabezado);

            // Recopilar valores de la columna (excluyendo encabezado)
            List<Object> valoresColumna = new ArrayList<>();
            for (int i = 1; i < cantidadFilas; i++) {
                Object valorCrudo = datos[i][j];
                if (valorCrudo == null) {
                    valoresColumna.add(null);
                } else if (valorCrudo instanceof String) {
                    valoresColumna.add(parsearValor((String) valorCrudo));
                } else {
                    valoresColumna.add(valorCrudo); // mantener tipo original
                }


            }

            // Inferir el tipo de la columna y crearla directamente
            Class<?> tipoColumna = inferirTipoColumna(valoresColumna);
            crearYAgregarColumna(etiqueta, tipoColumna, valoresColumna);
        }

    }

    /**
     * Parsea un string intentando convertirlo al tipo de dato más apropiado.
     * Intenta convertir en orden: Integer, Double, Boolean, y finalmente String.
     *
     * @param s String a parsear
     * @return Objeto del tipo más apropiado o null si el string está vacío
     */
    private Object parsearValor(String s) {
        if (s == null || s.trim().isEmpty()) return null;

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e2) {
                if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
                    return Boolean.parseBoolean(s);
                }
            }
        }

        return s; // default: sigue siendo string
    }

    /**
     * Crea una etiqueta apropiada basada en el tipo del objeto.
     *
     * @param obj Objeto para crear la etiqueta
     * @return EtiquetaInt si es Integer, EtiquetaString en caso contrario
     */
    private Etiqueta crearEtiquetaDesdeObjeto(Object obj) {
        if (obj instanceof Integer) {
            return new EtiquetaInt((Integer) obj);
        } else {
            return new EtiquetaString(obj.toString());
        }
    }

    /**
     * Infiere el tipo de datos más apropiado para una columna basándose en sus valores.
     * Analiza todos los valores no nulos y determina el tipo predominante.
     *
     * @param valores Lista de valores de la columna
     * @return Clase que representa el tipo de datos más apropiado
     */
    public Class<?> inferirTipoColumna(List<Object> valores) {
        if (valores.isEmpty()) {
            return Object.class;
        }

        // Contar tipos no nulos
        int contadorInteger = 0;
        int contadorDouble = 0;
        int contadorString = 0;
        int contadorBoolean = 0;
        int totalNoNulos = 0;

        for (Object valor : valores) {
            if (valor != null) {
                totalNoNulos++;
                if (valor instanceof Integer) {
                    contadorInteger++;
                } else if (valor instanceof Double || valor instanceof Float) {
                    contadorDouble++;
                } else if (valor instanceof Boolean) {
                    contadorBoolean++;
                } else {
                    contadorString++;
                }
            }
        }

        if (totalNoNulos == 0) {
            return Object.class;
        }

        // Determinar tipo predominante
        if (contadorInteger == totalNoNulos) {
            return Integer.class;
        } else if (contadorDouble == totalNoNulos) {
            return Double.class;
        } else if (contadorBoolean == totalNoNulos) {
            return Boolean.class;
        } else if (contadorInteger + contadorDouble == totalNoNulos) {
            // Mezcla de enteros y decimales -> usar Double
            return Double.class;
        } else {
            // Cualquier otra combinación -> String
            return String.class;
        }
    }

    /**
     * Crea una columna con el tipo especificado y la agrega a la tabla.
     * Maneja la conversión de tipos y la creación de etiquetas de filas si es necesario.
     *
     * @param etiqueta Etiqueta para la nueva columna
     * @param tipo     Clase que representa el tipo de datos de la columna
     * @param valores  Lista de valores para la columna
     */
    @SuppressWarnings("unchecked")
    public void crearYAgregarColumna(Etiqueta etiqueta, Class<?> tipo, List<Object> valores) {
        if (tipo == Integer.class) {
            List<Integer> valoresInt = new ArrayList<>();
            for (Object valor : valores) {
                if (valor == null) {
                    valoresInt.add(null);
                } else if (valor instanceof Integer) {
                    valoresInt.add((Integer) valor);
                } else {
                    try {
                        valoresInt.add(Integer.valueOf(valor.toString()));
                    } catch (NumberFormatException e) {
                        valoresInt.add(null);
                    }
                }
            }
            agregarColumna(etiqueta, Integer.class, valoresInt);

        } else if (tipo == Double.class) {
            List<Double> valoresDouble = new ArrayList<>();
            for (Object valor : valores) {
                if (valor == null) {
                    valoresDouble.add(null);
                } else if (valor instanceof Double) {
                    valoresDouble.add((Double) valor);
                } else if (valor instanceof Float) {
                    valoresDouble.add(((Float) valor).doubleValue());
                } else if (valor instanceof Integer) {
                    valoresDouble.add(((Integer) valor).doubleValue());
                } else {
                    try {
                        valoresDouble.add(Double.valueOf(valor.toString()));
                    } catch (NumberFormatException e) {
                        valoresDouble.add(null);
                    }
                }
            }
            agregarColumna(etiqueta, Double.class, valoresDouble);

        } else if (tipo == Boolean.class) {
            List<Boolean> valoresBoolean = new ArrayList<>();
            for (Object valor : valores) {
                if (valor == null) {
                    valoresBoolean.add(null);
                } else if (valor instanceof Boolean) {
                    valoresBoolean.add((Boolean) valor);
                } else {
                    valoresBoolean.add(Boolean.valueOf(valor.toString()));
                }
            }
            agregarColumna(etiqueta, Boolean.class, valoresBoolean);

        } else {
            // String por defecto
            List<String> valoresString = new ArrayList<>();
            for (Object valor : valores) {
                valoresString.add(valor == null ? null : valor.toString());
            }
            Columna<String> nuevaColumna = new Columna<>(etiqueta, String.class);
            for (String valor : valoresString) {
                nuevaColumna.agregarCelda(new Celda<>(valor));
            }
            agregarColumna(etiqueta, String.class, valoresString);
        }

        // Generar etiquetas de filas solo la primera vez
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < valores.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }
    }

    /**
     * Agrega una nueva columna a la tabla con los valores especificados.
     * Valida que las dimensiones coincidan con las filas existentes.
     *
     * @param <T>             Tipo de datos de la columna
     * @param etiquetaColumna Etiqueta identificadora de la columna
     * @param tipo            Clase que representa el tipo de datos
     * @param valores         Lista de valores para la columna
     * @throws JandasException si las dimensiones no coinciden
     */
    public <T> void agregarColumna(Etiqueta etiquetaColumna, Class<T> tipo, List<T> valores) {
        if (!columnas.isEmpty() && valores.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Dimensiones no coinciden. Se esperaban %d filas, pero se recibieron %d",
                    cantFilas(), valores.size()));
        }

        Columna<T> nuevaColumna = new Columna<>(etiquetaColumna, tipo);
        for (T valor : valores) {
            nuevaColumna.agregarCelda(new Celda<>(valor));
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

    /**
     * Agrega una nueva columna usando una lista de celdas directamente.
     *
     * @param <T>             Tipo de datos de la columna
     * @param etiquetaColumna Etiqueta identificadora de la columna
     * @param tipo            Clase que representa el tipo de datos
     * @param celdas          Lista de celdas para la columna
     * @throws JandasException si las dimensiones no coinciden
     */
    public <T> void agregarColumnaCeldas(Etiqueta etiquetaColumna, Class<T> tipo, List<Celda<T>> celdas) {
        if (!columnas.isEmpty() && celdas.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Dimensiones no coinciden. Se esperaban %d filas, pero se recibieron %d",
                    cantFilas(), celdas.size()));
        }

        Columna<T> nuevaColumna = new Columna<>(etiquetaColumna, tipo);
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

    /**
     * Agrega una columna ya creada a la tabla.
     *
     * @param <T>     Tipo de datos de la columna
     * @param columna Columna a agregar
     * @throws JandasException si las dimensiones no coinciden
     */
    public <T> void agregarColumna(Columna<T> columna) {
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

    /**
     * Agrega una nueva fila a la tabla.
     *
     * @param etiquetaFila Etiqueta identificadora de la fila
     * @param celdas       Lista de celdas que conforman la fila
     * @throws JandasException si el número de celdas no coincide con el número de columnas
     */
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

    /**
     * Genera etiquetas automáticas para las filas basándose en índices numéricos.
     *
     * @return Lista de etiquetas de filas generadas automáticamente
     */
    private List<Etiqueta> generarEtiquetaFilas() {
        if (!columnas.isEmpty()) {
            int cantFilas = columnas.get(0).size();
            List<Etiqueta> etiquetas = new ArrayList<>();
            for (int i = 0; i < cantFilas; i++) {
                etiquetas.add(new EtiquetaInt(i));
            }
            return etiquetas;
        }
        return new ArrayList<>();
    }

    /**
     * Duplica una columna existente con un nuevo nombre.
     *
     * @param <T>        Tipo de datos de la columna
     * @param colunueva  Nombre para la nueva columna
     * @param coluorigen Nombre de la columna a duplicar
     * @throws JandasException si la columna origen no existe
     */
    public <T> void duplicarColumna(String colunueva, String coluorigen) {
        EtiquetaString etiquetaColumnaOrigen = new EtiquetaString(coluorigen);
        EtiquetaString nuevaEtiqueta = new EtiquetaString(colunueva);
        Columna<?> columnaOrigen = getColumna(etiquetaColumnaOrigen);

        // Crear nueva columna con el mismo tipo que la columna origen
        Class<T> tipoColumna = (Class<T>) columnaOrigen.getTipoDato();
        Columna<T> nuevaColumna = new Columna<>(nuevaEtiqueta, tipoColumna);

        // Copiar todas las celdas de la columna origen
        for (Celda<?> celdaOrigen : columnaOrigen.getCeldas()) {
            T valor = (T) celdaOrigen.getValor();
            nuevaColumna.agregarCelda(new Celda<>(valor));
        }

        // Agregar la nueva columna a la tabla
        columnas.add(nuevaColumna);
        etiquetasColumnas.add(nuevaEtiqueta);
    }

    /**
     * Inserta una nueva columna desde una secuencia de valores.
     *
     * @param <T>          Tipo de datos de la columna
     * @param valorColumna Nombre de la nueva columna
     * @param tipo         Clase que representa el tipo de datos
     * @param secuencia    Lista de valores para la columna
     * @throws JandasException si las dimensiones no coinciden o el tipo no es válido
     */
    public <T> void insertarColumnaDesdeSecuencia(String valorColumna, Class<T> tipo, List<T> secuencia) {
        EtiquetaString etiquetaColumna = new EtiquetaString(valorColumna);
        // Validar que la secuencia tenga la cantidad correcta de elementos
        if (!columnas.isEmpty() && secuencia.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Dimensiones no coinciden. Se esperaban %d elementos, pero se recibieron %d",
                    cantFilas(), secuencia.size()));
        }

        // Validar que el tipo sea permitido
        if (!tipo.equals(String.class) && !tipo.equals(Integer.class) && !tipo.equals(Double.class) && !tipo.equals(Boolean.class)) {
            throw new JandasException("Tipo no permitido. Solo se permiten: String, Integer, Double, Boolean");
        }

        // Si la tabla está vacía, crear etiquetas de filas automáticamente
        if (columnas.isEmpty()) {
            for (int i = 0; i < secuencia.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }

        // Crear nueva columna
        Columna<T> nuevaColumna = new Columna<>(etiquetaColumna, tipo);
        for (T valor : secuencia) {
            nuevaColumna.agregarCelda(new Celda<>(valor));
        }

        // Agregar columna a la tabla
        columnas.add(nuevaColumna);
        etiquetasColumnas.add(etiquetaColumna);
    }

    /**
     * Elimina una columna de la tabla por su nombre.
     *
     * @param valor Nombre de la columna a eliminar
     * @throws JandasException si la tabla está vacía o la columna no existe
     */
    public void eliminarColumna(String valor) {
        EtiquetaString etiqueta = new EtiquetaString(valor);
        // Verificar que la tabla no esté vacía
        if (columnas.isEmpty()) {
            throw new JandasException("No se puede eliminar columna: la tabla está vacía");
        }

        // Buscar el índice de la columna a eliminar
        int indiceColumna = -1;
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            if (etiquetasColumnas.get(i).getValor().equals(etiqueta.getValor())) {
                indiceColumna = i;
                break;
            }
        }

        // Verificar que la columna existe
        if (indiceColumna == -1) {
            throw new JandasException("Columna no encontrada: " + etiqueta.getValor());
        }

        // Eliminar la columna y su etiqueta
        columnas.remove(indiceColumna);
        etiquetasColumnas.remove(indiceColumna);

        // Si eliminamos todas las columnas, también limpiamos las etiquetas de filas
        if (columnas.isEmpty()) {
            etiquetasFilas.clear();
        }
    }

    /**
     * Elimina una fila de la tabla por su índice.
     *
     * @param valor Índice de la fila a eliminar
     * @throws JandasException si la tabla está vacía o la fila no existe
     */
    public void eliminarFila(int valor) {
        EtiquetaInt etiqueta = new EtiquetaInt(valor);
        // Verificar que la tabla no esté vacía
        if (columnas.isEmpty() || etiquetasFilas.isEmpty()) {
            throw new JandasException("No se puede eliminar fila: la tabla está vacía");
        }

        // Buscar el índice de la fila a eliminar
        int indiceFila = -1;
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            if (etiquetasFilas.get(i).getValor().equals(etiqueta.getValor())) {
                indiceFila = i;
                break;
            }
        }

        // Verificar que la fila existe
        if (indiceFila == -1) {
            throw new JandasException("Fila no encontrada: " + etiqueta.getValor());
        }

        // Eliminar la celda correspondiente de cada columna
        for (Columna<?> columna : columnas) {
            columna.getCeldas().remove(indiceFila);
        }

        // Eliminar la etiqueta de la fila
        etiquetasFilas.remove(indiceFila);

        // Si eliminamos todas las filas, también limpiamos las etiquetas de filas
        if (etiquetasFilas.isEmpty()) {
            // Las columnas quedan vacías pero mantienen su estructura
            for (Columna<?> columna : columnas) {
                columna.getCeldas().clear();
            }
        }
    }

    /**
     * Obtiene el valor de una celda específica por nombre de columna e índice de fila.
     *
     * @param nombreColumna Nombre de la columna
     * @param indiceFila    Índice de la fila
     * @return Valor de la celda especificada
     * @throws JandasException si la columna o fila no existen
     */
    public Object getCeldaColYFila(String nombreColumna, int indiceFila) {
        EtiquetaString etiquetaColumna = new EtiquetaString(nombreColumna);
        EtiquetaInt etiquetaFila = new EtiquetaInt(indiceFila);

        // Buscar el índice de la columna
        int indiceColumna = -1;
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            if (etiquetasColumnas.get(i).getValor().equals(etiquetaColumna.getValor())) {
                indiceColumna = i;
                break;
            }
        }

        // Verificar que la columna existe
        if (indiceColumna == -1) {
            throw new JandasException("Columna no encontrada: " + nombreColumna);
        }

        // Buscar el índice de la fila
        int indiceFilaReal = -1;
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            if (etiquetasFilas.get(i).getValor().equals(etiquetaFila.getValor())) {
                indiceFilaReal = i;
                break;
            }
        }

        // Verificar que la fila existe
        if (indiceFilaReal == -1) {
            throw new JandasException("Fila no encontrada: " + indiceFila);
        }

        // Obtener la celda
        Columna<?> columna = columnas.get(indiceColumna);
        Celda<?> celda = columna.getCeldas().get(indiceFilaReal);

        return celda.getValor();
    }

    /**
     * Establece el valor de una celda específica en la tabla mediante coordenadas de fila y columna.
     *
     * @param valorfila    El índice de la fila donde se encuentra la celda
     * @param valorcolumna El nombre de la columna donde se encuentra la celda
     * @param valor        El nuevo valor a asignar a la celda
     * @throws JandasException Si la etiqueta de fila no se encuentra en la tabla
     * @throws JandasException Si la etiqueta de columna no se encuentra en la tabla
     * @throws JandasException Si el tipo del valor no es compatible con el tipo de dato de la columna
     */
    public void setValoresCelda(int valorfila, String valorcolumna, Object valor) {
        EtiquetaInt fila = new EtiquetaInt(valorfila);
        EtiquetaString columna = new EtiquetaString(valorcolumna);

        int idxfilas = etiquetasFilas.indexOf(fila);
        if (idxfilas == -1) {
            throw new JandasException("Etiqueta de fila no encontrada: " + fila.getValor());
        }

        for (Columna<?> col : columnas) {
            if (col.getEtiqueta().getValor().equals(columna.getValor())) {
                if (!col.getTipoDato().isInstance(valor)) {
                    throw new JandasException(String.format(
                            "Tipo incompatible. Se esperaba %s pero se recibió %s.",
                            col.getTipoDato().getSimpleName(),
                            valor == null ? "null" : valor.getClass().getSimpleName()
                    ));
                }
                Celda celda = col.getCeldas().get(idxfilas);
                celda.setValor(valor);  // Actualiza directamente la celda
                return;
            }
        }
        throw new JandasException("Etiqueta de columna no encontrada: " + columna.getValor());
    }

// operaciones

// // Ordenar

    /**
     * Ordena la tabla por una columna específica en la dirección especificada.
     *
     * @param nombreColumna El nombre de la columna por la cual ordenar
     * @param direccion     La dirección del ordenamiento (ASCENDENTE o DESCENDENTE)
     * @return Una nueva tabla ordenada según los criterios especificados
     */
    @Override
    public Tabla ordenar(String nombreColumna, Orden direccion) {
        Columna<?> columna = getColumna(nombreColumna);
        return OrdenadorTabla.ordenar(this, nombreColumna, direccion);
    }

    /**
     * Ordena la tabla por múltiples criterios especificados como strings.
     * Cada criterio puede incluir el nombre de la columna y opcionalmente "DESC" para orden descendente.
     *
     * @param criterios Array de strings con los criterios de ordenamiento (ej: "Ciudad DESC", "Edad")
     * @return Una nueva tabla ordenada según los criterios especificados
     */
    @Override
    public Tabla ordenar(String... criterios) {
        List<CriterioOrden> lista = new ArrayList<>();
        for (String criterio : criterios) {
            String[] partes = criterio.trim().split("\\s+"); // Ej: "Ciudad DESC"
            String nombre = partes[0];
            Orden orden = (partes.length > 1 && partes[1].equalsIgnoreCase("DESC"))
                    ? Orden.DESCENDENTE
                    : Orden.ASCENDENTE;
            lista.add(new CriterioOrden(new EtiquetaString(nombre), orden));
        }
        return this.ordenarPorCriterios(lista);
    }

    /**
     * Ordena la tabla según una lista de criterios de ordenamiento específicos.
     *
     * @param criterios Lista de objetos CriterioOrden que especifican las columnas y direcciones de ordenamiento
     * @return Una nueva tabla ordenada según los criterios especificados
     */
    @Override
    public Tabla ordenarPorCriterios(List<CriterioOrden> criterios) {
        return OrdenadorTabla.ordenarPorCriterios(this, criterios);
    }

// // Muestrear

    /**
     * Obtiene una muestra aleatoria de la tabla basada en un porcentaje.
     *
     * @param porcentaje El porcentaje de filas a incluir en la muestra (0-100)
     * @return Una nueva tabla con la muestra seleccionada
     */
    @Override
    public Tabla muestrear(int porcentaje) {
        return MuestreadorTabla.muestrear(this, porcentaje);
    }

    /**
     * Obtiene una muestra de la tabla con una cantidad específica de filas.
     *
     * @param cantidadFilas El número exacto de filas a incluir en la muestra
     * @param exacto        Si true, la muestra tendrá exactamente la cantidad especificada
     * @return Una nueva tabla con la muestra seleccionada
     */
    @Override
    public Tabla muestrear(int cantidadFilas, boolean exacto) {
        return MuestreadorTabla.muestrear(this, cantidadFilas, exacto);
    }

    /**
     * Realiza un muestreo estratificado basado en los valores de una columna específica.
     *
     * @param nombreColumna El nombre de la columna a usar para la estratificación
     * @param porcentaje    El porcentaje de filas a incluir en cada estrato
     * @return Una nueva tabla con la muestra estratificada
     */
    @Override
    public Tabla muestrearEstratificado(String nombreColumna, int porcentaje) {
        return MuestreadorTabla.muestrearEstratificado(this, nombreColumna, porcentaje);
    }

// // Concatenacion

    /**
     * Concatena esta tabla con otra tabla, combinando sus filas.
     *
     * @param otra La tabla a concatenar con esta tabla
     * @return Una nueva tabla que contiene las filas de ambas tablas
     */
    @Override
    public Tabla concatenacion(Tabla otra) {
        return ConcatenarTabla.concatenacion(this, otra);
    }

// // Filtrado

    /**
     * Filtra las filas de la tabla basándose en una condición específica.
     *
     * @param condicion La condición que deben cumplir las filas para ser incluidas en el resultado
     * @return Una nueva tabla que contiene solo las filas que cumplen la condición
     */
    public Tabla filtrar(Condicion condicion) {
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        List<Etiqueta> nuevasEtiquetasFilas = new ArrayList<>();

        // Crear nuevas columnas vacías con sus tipos correctos
        for (Columna<?> columnaOriginal : columnas) {
            Columna<?> nuevaColumna = new Columna<>(
                    columnaOriginal.getEtiqueta(),
                    columnaOriginal.getTipoDato()
            );
            nuevasColumnas.add(nuevaColumna);
        }

        // Evaluar cada fila y agregar las que cumplen la condición
        for (int i = 0; i < cantFilas(); i++) {
            Fila fila = getFila(etiquetasFilas.get(i));
            if (condicion.evaluar(fila)) {
                // Agregar la fila a las nuevas columnas
                for (int j = 0; j < cantColumnas(); j++) {
                    Celda<?> celdaOriginal = fila.getCeldasFila().get(j);
                    Columna columna = nuevasColumnas.get(j);
                    columna.agregarCelda(new Celda<>(celdaOriginal.getValor()));
                }
                nuevasEtiquetasFilas.add(etiquetasFilas.get(i));
            }
        }

        Tabla resultado = new Tabla(etiquetasColumnas, nuevasColumnas);
        resultado.setEtiquetasFilas(nuevasEtiquetasFilas);
        return resultado;
    }

// // Imputar

    /**
     * Remplaza los valores NA (nulos) en una columna específica con un valor de imputación.
     *
     * @param <T>             El tipo de dato de la columna
     * @param etiquetaColumna La etiqueta de la columna a imputar
     * @param valorImputacion El valor a usar para reemplazar los valores NA
     * @throws JandasException Si el tipo del valor de imputación no es compatible con el tipo de la columna
     */
    public <T> void imputarColumna(Etiqueta etiquetaColumna, T valorImputacion) {
        Columna<?> columna = getColumna(etiquetaColumna);

        if (valorImputacion != null &&
                !columna.getTipoDato().isAssignableFrom(valorImputacion.getClass())) {
            throw new JandasException(String.format(
                    "Tipo incompatible. La columna es de tipo %s pero el valor de imputación es %s",
                    columna.getTipoDato().getSimpleName(),
                    valorImputacion.getClass().getSimpleName()));
        }

        @SuppressWarnings("unchecked")
        Columna<T> columnaTyped = (Columna<T>) columna;

        for (int i = 0; i < columnaTyped.size(); i++) {
            Celda<T> celda = columnaTyped.getCelda(i);
            if (celda.esNA()) {
                celda.setValor(valorImputacion);
            }
        }
    }

    /**
     * Rellena todos los valores NA de la tabla con valores por defecto según el tipo de dato de cada columna.
     * Los valores por defecto son: 0 para Integer, 0.0 para Double, "" para String, false para Boolean.
     * Mantiene el tipo de dato original de cada columna.
     */
    public void imputarDefault() {
        for (Columna<?> columna : columnas) {
            Class<?> tipo = columna.getTipoDato();

            if (tipo == Integer.class) {
                imputarColumna(columna.getEtiqueta(), 0);
            } else if (tipo == Double.class) {
                imputarColumna(columna.getEtiqueta(), 0.0);
            } else if (tipo == String.class) {
                imputarColumna(columna.getEtiqueta(), "");
            } else if (tipo == Boolean.class) {
                imputarColumna(columna.getEtiqueta(), false);
            }
        }
    }

// Agrupar

    /**
     * Agrupa las filas de la tabla por los valores de una columna específica y aplica operaciones estadísticas.
     *
     * @param nombreColumna El nombre de la columna por la cual agrupar
     * @param operaciones   Mapa que especifica las operaciones estadísticas a aplicar a cada columna
     * @return Una nueva tabla con los resultados de las operaciones de agrupamiento
     */
    @Override
    public Tabla agruparPor(String nombreColumna, Map<String, OperacionEstadistica> operaciones) {
        return AgruparTabla.agruparPor(this, nombreColumna, operaciones);
    }

    /**
     * Agrupa las filas de la tabla por los valores de múltiples columnas y aplica operaciones estadísticas.
     *
     * @param nombresColumnas Array con los nombres de las columnas por las cuales agrupar
     * @param operaciones     Mapa que especifica las operaciones estadísticas a aplicar a cada columna
     * @return Una nueva tabla con los resultados de las operaciones de agrupamiento
     */
    @Override
    public Tabla agruparPor(String[] nombresColumnas, Map<String, OperacionEstadistica> operaciones) {
        return AgruparTabla.agruparPor(this, nombresColumnas, operaciones);
    }

    /**
     * Agrupa las filas de la tabla por los valores de una columna específica y aplica una operación estadística.
     *
     * @param nombreColumna El nombre de la columna por la cual agrupar
     * @param operacion     La operación estadística a aplicar
     * @return Una nueva tabla con los resultados de la operación de agrupamiento
     */
    @Override
    public Tabla agruparPor(String nombreColumna, OperacionEstadistica operacion) {
        return AgruparTabla.agruparPor(this, nombreColumna, operacion);
    }

// Seleccionar

    /**
     * Obtiene las primeras n filas de la tabla.
     *
     * @param n El número de filas a obtener desde el inicio de la tabla
     * @return Una nueva tabla con las primeras n filas
     * @throws JandasException Si n es menor o igual a 0
     */
    public Tabla head(int n) {
        if (n <= 0) {
            throw new JandasException("El número de filas debe ser positivo");
        }

        // Ajustar n si es mayor que el número total de filas
        n = Math.min(n, cantFilas());

        // Crear nuevas columnas con las primeras n filas
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        for (Columna<?> columnaOriginal : getColumnas()) {
            @SuppressWarnings("unchecked")
            Columna<Object> nuevaColumna = new Columna<>(
                    columnaOriginal.getEtiqueta(),
                    (Class<Object>) columnaOriginal.getTipoDato()
            );

            List<? extends Celda<?>> celdasOriginales = columnaOriginal.getCeldas();
            for (int i = 0; i < Math.min(n, celdasOriginales.size()); i++) {
                Celda<?> celdaOriginal = celdasOriginales.get(i);
                @SuppressWarnings("unchecked")
                Celda<Object> nuevaCelda = new Celda<>(celdaOriginal.getValor());
                nuevaColumna.agregarCelda(nuevaCelda);
            }
            nuevasColumnas.add(nuevaColumna);
        }

        // Crear nueva tabla con las etiquetas de columnas y las nuevas columnas
        Tabla resultado = new Tabla(getEtiquetasColumnas(), nuevasColumnas);

        // Establecer las etiquetas de filas correspondientes
        List<Etiqueta> nuevasEtiquetasFilas = getEtiquetasFilas().subList(0, n);
        resultado.setEtiquetasFilas(nuevasEtiquetasFilas);

        return resultado;
    }

    /**
     * Obtiene las últimas n filas de la tabla.
     *
     * @param n El número de filas a obtener desde el final de la tabla
     * @return Una nueva tabla con las últimas n filas
     * @throws JandasException Si n es menor o igual a 0
     */
    public Tabla tail(int n) {
        if (n <= 0) {
            throw new JandasException("El número de filas debe ser positivo");
        }

        // Ajustar n si es mayor que el número total de filas
        n = Math.min(n, cantFilas());

        // Calcular el índice de inicio
        int inicio = cantFilas() - n;

        // Crear nuevas columnas con las últimas n filas
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        for (Columna<?> columnaOriginal : getColumnas()) {
            @SuppressWarnings("unchecked")
            Columna<Object> nuevaColumna = new Columna<>(
                    columnaOriginal.getEtiqueta(),
                    (Class<Object>) columnaOriginal.getTipoDato()
            );

            List<? extends Celda<?>> celdasOriginales = columnaOriginal.getCeldas();
            for (int i = inicio; i < cantFilas(); i++) {
                Celda<?> celdaOriginal = celdasOriginales.get(i);
                @SuppressWarnings("unchecked")
                Celda<Object> nuevaCelda = new Celda<>(celdaOriginal.getValor());
                nuevaColumna.agregarCelda(nuevaCelda);
            }
            nuevasColumnas.add(nuevaColumna);
        }

        // Crear nueva tabla con las etiquetas de columnas y las nuevas columnas
        Tabla resultado = new Tabla(getEtiquetasColumnas(), nuevasColumnas);

        // Establecer las etiquetas de filas correspondientes
        List<Etiqueta> nuevasEtiquetasFilas = getEtiquetasFilas().subList(inicio, cantFilas());
        resultado.setEtiquetasFilas(nuevasEtiquetasFilas);

        return resultado;
    }

    /**
     * Crea una copia profunda de la tabla, incluyendo todas sus columnas, filas y etiquetas.
     * Todos los objetos son copiados de manera independiente.
     *
     * @return Una nueva tabla que es una copia profunda de esta tabla
     */
    public Tabla copiar() {
        // Crear nuevas listas para la copia
        List<Columna<?>> columnasCopiadas = new ArrayList<>();
        List<Etiqueta> etiquetasColumnasCopiadas = new ArrayList<>();
        List<Etiqueta> etiquetasFilasCopiadas = new ArrayList<>();

        // Copiar etiquetas de columnas
        for (Etiqueta etiqueta : etiquetasColumnas) {
            if (etiqueta instanceof EtiquetaString) {
                etiquetasColumnasCopiadas.add(new EtiquetaString(etiqueta.getValor().toString()));
            } else if (etiqueta instanceof EtiquetaInt) {
                etiquetasColumnasCopiadas.add(new EtiquetaInt((Integer) etiqueta.getValor()));
            }
        }

        // Copiar etiquetas de filas
        for (Etiqueta etiqueta : etiquetasFilas) {
            if (etiqueta instanceof EtiquetaString) {
                etiquetasFilasCopiadas.add(new EtiquetaString(etiqueta.getValor().toString()));
            } else if (etiqueta instanceof EtiquetaInt) {
                etiquetasFilasCopiadas.add(new EtiquetaInt((Integer) etiqueta.getValor()));
            }
        }

        // Copiar cada columna
        for (Columna<?> columnaOriginal : columnas) {
            columnasCopiadas.add(copiarColumna(columnaOriginal));
        }

        // Crear nueva tabla con las copias
        Tabla tablaCopia = new Tabla(etiquetasColumnasCopiadas, columnasCopiadas);
        tablaCopia.setEtiquetasFilas(etiquetasFilasCopiadas);

        return tablaCopia;
    }

    /**
     * Crea una copia profunda de una columna específica.
     *
     * @param <T>             El tipo de dato de la columna
     * @param columnaOriginal La columna original a copiar
     * @return Una nueva columna que es una copia profunda de la original
     */
    private <T> Columna<T> copiarColumna(Columna<T> columnaOriginal) {
        Columna<T> columnaCopia = new Columna<>(
                copiarEtiqueta(columnaOriginal.getEtiqueta()),
                columnaOriginal.getTipoDato()
        );

        // Copiar cada celda de la columna
        for (Celda<T> celdaOriginal : columnaOriginal.getCeldas()) {
            columnaCopia.agregarCelda(copiarCelda(celdaOriginal));
        }

        return columnaCopia;
    }

    /**
     * Crea una copia profunda de una celda específica.
     *
     * @param <T>           El tipo de dato de la celda
     * @param celdaOriginal La celda original a copiar
     * @return Una nueva celda que es una copia de la original
     */
    private <T> Celda<T> copiarCelda(Celda<T> celdaOriginal) {
        // Crear nueva celda con el valor de la original
        return new Celda<>(celdaOriginal.getValor());
    }

    /**
     * Crea una copia de una etiqueta específica.
     *
     * @param etiqueta La etiqueta original a copiar
     * @return Una nueva etiqueta que es una copia de la original
     * @throws JandasException Si el tipo de etiqueta no es soportado
     */
    private Etiqueta copiarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta instanceof EtiquetaString) {
            return new EtiquetaString(etiqueta.getValor().toString());
        } else if (etiqueta instanceof EtiquetaInt) {
            return new EtiquetaInt((Integer) etiqueta.getValor());
        }
        throw new JandasException("Tipo de etiqueta no soportado");
    }

    /**
     * Obtiene el índice de una etiqueta específica en una lista de etiquetas.
     *
     * @param etiqueta  La etiqueta a buscar
     * @param etiquetas La lista de etiquetas donde buscar
     * @return El índice de la etiqueta en la lista
     * @throws JandasException Si la etiqueta no se encuentra en la lista
     */
    private int getIndex(Etiqueta etiqueta, List<Etiqueta> etiquetas) {
        for (int i = 0; i < etiquetas.size(); i++) {
            if (etiquetas.get(i).getValor().equals(etiqueta.getValor())) {
                return i;
            }
        }
        throw new JandasException("Etiqueta no encontrada: " + etiqueta.getValor());
    }

    /**
     * Obtiene una fila específica de la tabla por su etiqueta.
     *
     * @param etiquetaFila La etiqueta de la fila a obtener
     * @return Un objeto Fila que contiene todas las celdas de la fila especificada
     * @throws JandasException Si la etiqueta de fila no se encuentra
     */
    public Fila getFila(Etiqueta etiquetaFila) {
        int indexFila = getIndex(etiquetaFila, etiquetasFilas);
        List<Celda<?>> celdasFilas = new ArrayList<>();
        for (Columna<?> col : columnas) {
            celdasFilas.add(col.getCeldas().get(indexFila));
        }
        return new Fila(etiquetaFila, celdasFilas, etiquetasColumnas);
    }

    /**
     * Establece nuevas etiquetas para las filas de la tabla.
     *
     * @param nuevasEtiquetas Lista de nuevas etiquetas para las filas
     * @throws JandasException Si el número de etiquetas no coincide con el número de filas
     */
    public void setEtiquetasFilas(List<Etiqueta> nuevasEtiquetas) {
        if (nuevasEtiquetas.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Debe proporcionar exactamente %d etiquetas", cantFilas()));
        }
        this.etiquetasFilas = new ArrayList<>(nuevasEtiquetas);
    }

    /**
     * Obtiene una copia defensiva de la lista de columnas de la tabla.
     *
     * @return Una nueva lista que contiene todas las columnas de la tabla
     */
    public List<Columna<?>> getColumnas() {
        return new ArrayList<>(columnas); // copia defensiva
    }

    /**
     * Obtiene una columna específica por su etiqueta.
     *
     * @param etiqueta La etiqueta de la columna a obtener
     * @return La columna correspondiente a la etiqueta especificada
     * @throws JandasException Si la etiqueta no se encuentra
     */
    public Columna<?> getColumna(Etiqueta etiqueta) {
        int index = getIndex(etiqueta, etiquetasColumnas);
        return columnas.get(index);
    }

    /**
     * Obtiene una columna específica por el nombre de su etiqueta.
     *
     * @param nombreEtiqueta El nombre de la etiqueta de la columna a obtener
     * @return La columna correspondiente al nombre de etiqueta especificado
     * @throws JandasException Si no se encuentra una columna con la etiqueta especificada
     */
    public Columna<?> getColumna(String nombreEtiqueta) {
        for (Columna<?> columna : columnas) {
            if (columna.getEtiqueta().getValor().equals(nombreEtiqueta)) {
                return columna;
            }
        }
        throw new JandasException("No se encontró una columna con la etiqueta: " + nombreEtiqueta);
    }

    /**
     * Obtiene una columna específica por su índice.
     *
     * @param index El índice de la columna a obtener (basado en 0)
     * @return La columna en el índice especificado
     * @throws JandasException Si el índice está fuera del rango válido
     */
    public Columna<?> getColumna(int index) {
        if (index < 0 || index >= columnas.size()) {
            throw new JandasException("Índice de columna fuera de rango: " + index);
        }
        return columnas.get(index);
    }

    /**
     * Obtiene el número total de columnas en la tabla.
     *
     * @return El número de columnas
     */
    public int cantColumnas() {
        return columnas.size();
    }

    /**
     * Obtiene el número total de filas en la tabla.
     *
     * @return El número de filas, o 0 si no hay columnas
     */
    public int cantFilas() {
        return columnas.isEmpty() ? 0 : columnas.get(0).size();
    }

    /**
     * Obtiene una copia defensiva de la lista de etiquetas de columnas.
     *
     * @return Una nueva lista que contiene todas las etiquetas de columnas
     */
    public List<Etiqueta> getEtiquetasColumnas() {
        return new ArrayList<>(etiquetasColumnas);
    }

    /**
     * Obtiene una copia defensiva de la lista de etiquetas de filas.
     *
     * @return Una nueva lista que contiene todas las etiquetas de filas
     */
    public List<Etiqueta> getEtiquetasFilas() {
        return new ArrayList<>(etiquetasFilas);
    }

    /**
     * Compara esta tabla con otro objeto para determinar si son iguales.
     * Dos tablas son iguales si tienen las mismas columnas, etiquetas de filas y etiquetas de columnas.
     *
     * @param obj El objeto a comparar con esta tabla
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Tabla other = (Tabla) obj;
        return Objects.equals(columnas, other.columnas) &&
                Objects.equals(etiquetasFilas, other.etiquetasFilas) &&
                Objects.equals(etiquetasColumnas, other.etiquetasColumnas);
    }

    /**
     * Calcula el código hash para esta tabla basado en sus columnas y etiquetas.
     *
     * @return El código hash de la tabla
     */
    @Override
    public int hashCode() {
        return Objects.hash(columnas, etiquetasFilas, etiquetasColumnas);
    }
}





