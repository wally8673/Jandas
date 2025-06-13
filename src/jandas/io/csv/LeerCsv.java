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

public class LeerCsv implements LectorCsv {

    private CsvConfig config;

    public LeerCsv() {
        this.config = new CsvConfig();
    }

    @Override
    public Tabla leer(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            //Inicializo timer
            long startTime = System.nanoTime();
            // Lista para almacenar todas las líneas del archivo
            List<String[]> lineasDatos = new ArrayList<>();
            String linea;
            String[] encabezados = null;

            // Leer la primera línea (potencialmente encabezados)
            if ((linea = br.readLine()) != null) {
                if (config.isTieneEncabezado()) {
                    encabezados = linea.split(config.getSeparador(), -1);
                } else {
                    lineasDatos.add(linea.split(config.getSeparador(), -1));
                }
            } else {
                // Archivo vacío
                throw new JandasException("El archivo CSV está vacío");
            }

            // Leer el resto de las líneas
            while ((linea = br.readLine()) != null) {
                lineasDatos.add(linea.split(config.getSeparador(), -1));
            }

            // Verificar que haya datos
            if (lineasDatos.isEmpty() && !config.isTieneEncabezado()) {
                throw new JandasException("No hay datos en el archivo CSV");
            }

            // Determinar el número de columnas
            int numColumnas = config.isTieneEncabezado() ? encabezados.length : lineasDatos.get(0).length;

            // NUEVO: Convertir datos y agrupar por columnas
            List<List<Object>> datosPorColumna = new ArrayList<>();
            List<Etiqueta> etiquetasColumnas = new ArrayList<>();

            // Inicializar listas para cada columna
            for (int i = 0; i < numColumnas; i++) {
                String nombreCol = config.isTieneEncabezado() ? encabezados[i] : "" + i;
                etiquetasColumnas.add(new EtiquetaString(nombreCol));
                datosPorColumna.add(new ArrayList<>());
            }

            // Procesar datos fila por fila y agrupar por columnas
            for (String[] fila : lineasDatos) {
                for (int j = 0; j < numColumnas; j++) {
                    Object valor;
                    if (j < fila.length) {
                        valor = convertirValor(fila[j].trim());
                    } else {
                        valor = null; // Rellenar con null si faltan datos
                    }
                    datosPorColumna.get(j).add(valor);
                }
            }

            // NUEVO: Crear columnas con tipos inferidos
            List<Columna<?>> columnas = new ArrayList<>();
            for (int i = 0; i < numColumnas; i++) {
                Etiqueta etiqueta = etiquetasColumnas.get(i);
                List<Object> valoresColumna = datosPorColumna.get(i);

                // Inferir tipo y crear columna tipada
                Class<?> tipoInferido = inferirTipoColumna(valoresColumna);
                Columna<?> columna = crearColumnaConTipo(etiqueta, tipoInferido, valoresColumna);
                columnas.add(columna);
            }

            // Crear etiquetas de filas
            List<Etiqueta> etiquetasFilas = new ArrayList<>();
            for (int i = 0; i < lineasDatos.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }

            // Crear la tabla
            Tabla tabla = new Tabla(etiquetasColumnas, columnas);
            tabla.setEtiquetasFilas(etiquetasFilas);

            // Capturar el tiempo después de la ejecución
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1_000_000_000.0;
            System.out.printf("Tiempo de ejecución: %.3f segundos %n", duration);

            return tabla;

        } catch (IOException e) {
            throw new JandasException("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    // NUEVO: Método para inferir tipo de columna (copiado de Tabla.java)
    private Class<?> inferirTipoColumna(List<Object> valores) {
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

    // NUEVO: Método para crear columna con tipo específico
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
                            // Mantener como null si no se puede convertir
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
                            // Mantener como null si no se puede convertir
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
            // String por defecto
            Columna<String> columna = new Columna<>(etiqueta, String.class);
            for (Object valor : valores) {
                String valorString = valor == null ? null : valor.toString();
                columna.agregarCelda(new Celda<>(valorString));
            }
            return columna;
        }
    }

    @Override
    public Tabla leer(String rutaArchivo, CsvConfig config) {
        this.config = config;
        return leer(rutaArchivo);
    }

    @Override
    public Tabla leer(String rutaArchivo, boolean encabezado) {
        config.setTieneEncabezado(encabezado);
        return leer(rutaArchivo);
    }

    @Override
    public Tabla leer(String rutaArchivo, boolean encabezado, String separador) {
        config.setSeparador(separador);
        config.setTieneEncabezado(encabezado);
        return leer(rutaArchivo);
    }

    private Object convertirValor(String valor) {
        if (valor.isEmpty()) {
            return null;
        }

        // Intentar convertir a número
        try {
            // Verificar si es un booleano
            if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(valor);
            }
            if (valor.contains(".")) {
                return Double.parseDouble(valor);
            } else {
                return Integer.parseInt(valor);
            }
        } catch (NumberFormatException e) {
            // Si no es número, devolver como String
            return valor;
        }
    }
}
