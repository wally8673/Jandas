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

public class LeerCsv implements LeerArchivo {

    private CsvConfig config;

    public LeerCsv() {
        this.config = new CsvConfig();
    }

    @Override
    public Tabla leer(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
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

            // Crear columnas y etiquetas
            List<Columna<?>> columnas = new ArrayList<>();
            List<Etiqueta> etiquetasColumnas = new ArrayList<>();

            for (int i = 0; i < numColumnas; i++) {
                String nombreCol = config.isTieneEncabezado() ? encabezados[i] : "" + i;
                etiquetasColumnas.add(new EtiquetaString(nombreCol));
                // CORRECCIÓN: Crear columna como Object en lugar de String
                columnas.add(new Columna<>(new EtiquetaString(nombreCol), Object.class));
            }

            // Llenar las columnas con datos
            List<Etiqueta> etiquetasFilas = new ArrayList<>();
            for (int i = 0; i < lineasDatos.size(); i++) {
                String[] fila = lineasDatos.get(i);
                etiquetasFilas.add(new EtiquetaInt(i)); // CORRECCIÓN: Usar EtiquetaInt en lugar de EtiquetaString

                // Procesar cada celda
                for (int j = 0; j < Math.min(numColumnas, fila.length); j++) {
                    String valorStr = fila[j].trim();
                    Object valor = convertirValor(valorStr);

                    // CORRECCIÓN: Cast seguro porque creamos las columnas como Object
                    @SuppressWarnings("unchecked")
                    Columna<Object> columna = (Columna<Object>) columnas.get(j);
                    columna.agregarCelda(new Celda<>(valor));
                }

                // Si hay menos valores que columnas, rellenar con nulos
                if (fila.length < numColumnas) {
                    for (int j = fila.length; j < numColumnas; j++) {
                        @SuppressWarnings("unchecked")
                        Columna<Object> columna = (Columna<Object>) columnas.get(j);
                        columna.agregarCelda(new Celda<>(null));
                    }
                }
            }

            // CORRECCIÓN: Crear la tabla correctamente
            Tabla tabla = new Tabla(etiquetasColumnas, columnas);
            tabla.setEtiquetasFilas(etiquetasFilas);
            return tabla;

        } catch (IOException e) {
            throw new JandasException("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public Tabla leer(String rutaArchvio, CsvConfig config) {
        this.config = config;
        return leer(rutaArchvio);
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
