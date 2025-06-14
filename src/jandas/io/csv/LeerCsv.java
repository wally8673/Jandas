package jandas.io.csv;

import jandas.base.data.*;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;
import jandas.io.LeerArchivo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para leer archivos CSV y convertirlos en objetos {@link Tabla} de Jandas.
 * <p>
 * Soporta archivos con o sin encabezado, inferencia automática del tipo de dato por columna
 * y manejo de valores nulos.
 * <p>
 * La configuración del lector (separador, si tiene encabezado, etc.) se maneja mediante {@link CsvConfig}.
 */
public class LeerCsv implements LectorCsv {

    /**
     * Configuración para la lectura del archivo CSV, como separador y si tiene encabezado.
     */
    private CsvConfig config;

    /**
     * Constructor por defecto que inicializa la configuración con valores predeterminados.
     */
    public LeerCsv() {
        this.config = new CsvConfig();
    }

    /**
     * Lee un archivo CSV desde la ruta especificada y lo convierte en un objeto {@link Tabla}.
     * <p>
     * El archivo puede contener encabezados (nombres de columnas) o no, según la configuración.
     * Los datos se agrupan por columna y se infiere automáticamente el tipo de dato de cada columna.
     * <p>
     * También mide el tiempo de ejecución del proceso de lectura.
     *
     * @param rutaArchivo Ruta completa del archivo CSV a leer.
     * @return Objeto {@link Tabla} que contiene los datos del CSV.
     * @throws JandasException si el archivo está vacío o hay errores en la lectura.
     */
    @Override
    public Tabla leer(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            long startTime = System.nanoTime();

            List<String[]> lineasDatos = new ArrayList<>();
            String linea;
            String[] encabezados = null;

            // Leer primera línea para encabezados o datos
            if ((linea = br.readLine()) != null) {
                if (config.isTieneEncabezado()) {
                    encabezados = linea.split(config.getSeparador(), -1);
                } else {
                    lineasDatos.add(linea.split(config.getSeparador(), -1));
                }
            } else {
                throw new JandasException("El archivo CSV está vacío");
            }

            // Leer resto de líneas
            while ((linea = br.readLine()) != null) {
                lineasDatos.add(linea.split(config.getSeparador(), -1));
            }

            if (lineasDatos.isEmpty() && !config.isTieneEncabezado()) {
                throw new JandasException("No hay datos en el archivo CSV");
            }

            int numColumnas = config.isTieneEncabezado() ? encabezados.length : lineasDatos.get(0).length;

            // Agrupar datos por columnas
            List<List<Object>> datosPorColumna = new ArrayList<>();
            List<Etiqueta> etiquetasColumnas = new ArrayList<>();

            for (int i = 0; i < numColumnas; i++) {
                String nombreCol = config.isTieneEncabezado() ? encabezados[i] : "" + i;
                etiquetasColumnas.add(new EtiquetaString(nombreCol));
                datosPorColumna.add(new ArrayList<>());
            }

            // Convertir y agrupar datos fila por fila
            for (String[] fila : lineasDatos) {
                for (int j = 0; j < numColumnas; j++) {
                    Object valor;
                    if (j < fila.length) {
                        valor = convertirValor(fila[j].trim());
                    } else {
                        valor = null; // valor nulo si falta dato
                    }
                    datosPorColumna.get(j).add(valor);
                }
            }

            // Crear columnas con tipos inferidos
            List<Columna<?>> columnas = new ArrayList<>();
            for (int i = 0; i < numColumnas; i++) {
                Etiqueta etiqueta = etiquetasColumnas.get(i);
                List<Object> valoresColumna = datosPorColumna.get(i);

                Class<?> tipoInferido = inferirTipoColumna(valoresColumna);
                Columna<?> columna = crearColumnaConTipo(etiqueta, tipoInferido, valoresColumna);
                columnas.add(columna);
            }

            // Crear etiquetas de filas
            List<Etiqueta> etiquetasFilas = new ArrayList<>();
            for (int i = 0; i < lineasDatos.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }

            Tabla tabla = new Tabla(etiquetasColumnas, columnas);
            tabla.setEtiquetasFilas(etiquetasFilas);

            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1_000_000_000.0;
            System.out.printf("Tiempo de ejecución: %.3f segundos %n", duration);

            return tabla;

        } catch (IOException e) {
            throw new JandasException("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    /**
     * Infiera el tipo de dato predominante de una columna basada en sus valores no nulos.
     * <p>
     * El tipo puede ser {@code Integer}, {@code Double}, {@code Boolean} o {@code String}.
     * Si hay mezcla de enteros y decimales, se elige {@code Double}.
     * Si no hay datos no nulos, se retorna {@code Object.class}.
     *
     * @param valores Lista de valores de la columna (puede contener null).
     * @return Clase representando el tipo inferido.
     */
    private Class<?> inferirTipoColumna(List<Object> valores) {
        if (valores.isEmpty()) {
            return Object.class;
        }

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

        if (contadorInteger == totalNoNulos) {
            return Integer.class;
        } else if (contadorDouble == totalNoNulos) {
            return Double.class;
        } else if (contadorBoolean == totalNoNulos) {
            return Boolean.class;
        } else if (contadorInteger + contadorDouble == totalNoNulos) {
            return Double.class;
        } else {
            return String.class;
        }
    }

    /**
     * Crea una columna tipada a partir de la etiqueta, tipo inferido y lista de valores.
     * <p>
     * Convierte cada valor al tipo correspondiente, manejando conversiones y valores nulos.
     *
     * @param etiqueta Etiqueta de la columna.
     * @param tipo Tipo inferido para la columna.
     * @param valores Valores a incluir en la columna.
     * @return Objeto {@link Columna} con los valores convertidos.
     */
    @SuppressWarnings("unchecked")
    private Columna<?> crearColumnaConTipo(Etiqueta etiqueta, Class<?> tipo, List<Object> valores) {
        if (tipo == Integer.class) {
            Columna<Integer> columna = new Columna<>(etiqueta, Integer.class);
            for (Object valor : valores) {
                Integer valorInt = null;
                if (valor != null) {
                    if (valor instanceof Integer) {
                        valorInt = (Integer) valor;
                    } else {
                        try {
                            valorInt = Integer.valueOf(valor.toString());
                        } catch (NumberFormatException e) {
                            // mantener null si no se puede convertir
                        }
                    }
                }
                columna.agregarCelda(new Celda<>(valorInt));
            }
            return columna;

        } else if (tipo == Double.class) {
            Columna<Double> columna = new Columna<>(etiqueta, Double.class);
            for (Object valor : valores) {
                Double valorDouble = null;
                if (valor != null) {
                    if (valor instanceof Double) {
                        valorDouble = (Double) valor;
                    } else if (valor instanceof Float) {
                        valorDouble = ((Float) valor).doubleValue();
                    } else if (valor instanceof Integer) {
                        valorDouble = ((Integer) valor).doubleValue();
                    } else {
                        try {
                            valorDouble = Double.valueOf(valor.toString());
                        } catch (NumberFormatException e) {
                            // mantener null si no se puede convertir
                        }
                    }
                }
                columna.agregarCelda(new Celda<>(valorDouble));
            }
            return columna;

        } else if (tipo == Boolean.class) {
            Columna<Boolean> columna = new Columna<>(etiqueta, Boolean.class);
            for (Object valor : valores) {
                Boolean valorBoolean = null;
                if (valor != null) {
                    if (valor instanceof Boolean) {
                        valorBoolean = (Boolean) valor;
                    } else {
                        valorBoolean = Boolean.valueOf(valor.toString());
                    }
                }
                columna.agregarCelda(new Celda<>(valorBoolean));
            }
            return columna;

        } else {
            // Por defecto String
            Columna<String> columna = new Columna<>(etiqueta, String.class);
            for (Object valor : valores) {
                String valorString = valor == null ? null : valor.toString();
                columna.agregarCelda(new Celda<>(valorString));
            }
            return columna;
        }
    }

    /**
     * Lee un archivo CSV usando una configuración personalizada.
     *
     * @param rutaArchivo Ruta del archivo CSV.
     * @param config Configuración específica para lectura.
     * @return Tabla con los datos leídos.
     */
    @Override
    public Tabla leer(String rutaArchivo, CsvConfig config) {
        this.config = config;
        return leer(rutaArchivo);
    }

    /**
     * Lee un archivo CSV indicando si tiene encabezado o no.
     *
     * @param rutaArchivo Ruta del archivo CSV.
     * @param encabezado {@code true} si el archivo tiene encabezado; {@code false} en caso contrario.
     * @return Tabla con los datos leídos.
     */
    @Override
    public Tabla leer(String rutaArchivo, boolean encabezado) {
        config.setTieneEncabezado(encabezado);
        return leer(rutaArchivo);
    }

    /**
     * Lee un archivo CSV indicando si tiene encabezado y el separador a usar.
     *
     * @param rutaArchivo Ruta del archivo CSV.
     * @param encabezado {@code true} si el archivo tiene encabezado.
     * @param separador Separador de columnas (ej: ",", ";", "\t").
     * @return Tabla con los datos leídos.
     */
    @Override
    public Tabla leer(String rutaArchivo, boolean encabezado, String separador) {
        config.setSeparador(separador);
        config.setTieneEncabezado(encabezado);
        return leer(rutaArchivo);
    }

    /**
     * Convierte un valor de tipo {@link String} a un objeto del tipo más adecuado.
     * <p>
     * Intenta convertir a {@link Boolean}, {@link Integer} o {@link Double}.
     * Si no se puede convertir, devuelve el valor como {@link String}.
     * Los valores vacíos se interpretan como {@code null}.
     *
     * @param valor Valor en texto a convertir.
     * @return Objeto convertido o {@code null} si el valor está vacío.
     */
    private Object convertirValor(String valor) {
        if (valor.isEmpty()) {
            return null;
        }

        try {
            if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(valor);
            }
            if (valor.contains(".")) {
                return Double.parseDouble(valor);
            } else {
                return Integer.parseInt(valor);
            }
        } catch (NumberFormatException e) {
            return valor;
        }
    }
}
