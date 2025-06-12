package jandas.io.csv;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.Etiqueta;
import jandas.excepciones.JandasException;
import jandas.io.EscribirArchivo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EscribirCsv implements EscritorCsv {

    private CsvConfig config;

    public EscribirCsv() {
        this.config = new CsvConfig();
    }

    public EscribirCsv(CsvConfig config) {
        this.config = config;
    }

    @Override
    public void escribir(Tabla tabla, String rutaArchivo) {
        escribir(tabla, rutaArchivo, this.config);
    }

    @Override
    public void escribir(Tabla tabla, String rutaArchivo, String separador) {
        CsvConfig tempConfig = new CsvConfig(separador, this.config.isTieneEncabezado(), this.config.getValorNulo());
        escribir(tabla, rutaArchivo, tempConfig);
    }

    @Override
    public void escribir(Tabla tabla, String rutaArchivo, String separador, String valorNulo) {
        CsvConfig tempConfig = new CsvConfig(separador, this.config.isTieneEncabezado(), valorNulo);
        escribir(tabla, rutaArchivo, tempConfig);
    }

    /**
     * Escribe una tabla a un archivo CSV con configuración específica
     */
    public void escribir(Tabla tabla, String rutaArchivo, CsvConfig config) {
        if (tabla == null) {
            throw new JandasException("La tabla no puede ser null");
        }

        if (rutaArchivo == null || rutaArchivo.trim().isEmpty()) {
            throw new JandasException("La ruta del archivo no puede ser null o vacía");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            long startTime = System.nanoTime();

            // Escribir encabezados si está configurado
            if (config.isTieneEncabezado()) {
                escribirEncabezados(tabla, writer, config);
            }

            // Escribir datos
            escribirDatos(tabla, writer, config);

            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1_000_000_000.0;
            System.out.printf("Archivo CSV escrito en: %.3f segundos %n", duration);

        } catch (IOException e) {
            throw new JandasException("Error al escribir el archivo CSV: " + e.getMessage(), e);
        }
    }

    private void escribirEncabezados(Tabla tabla, BufferedWriter writer, CsvConfig config) throws IOException {
        List<Etiqueta> etiquetasColumnas = tabla.getEtiquetasColumnas();
        StringBuilder lineaEncabezado = new StringBuilder();

        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            if (i > 0) {
                lineaEncabezado.append(config.getSeparador());
            }
            String valorEtiqueta = etiquetasColumnas.get(i).toString();
            lineaEncabezado.append(escaparValor(valorEtiqueta, config));
        }

        writer.write(lineaEncabezado.toString());
        writer.newLine();
    }

    private void escribirDatos(Tabla tabla, BufferedWriter writer, CsvConfig config) throws IOException {
        List<Etiqueta> etiquetasFilas = tabla.getEtiquetasFilas();

        for (Etiqueta etiquetaFila : etiquetasFilas) {
            StringBuilder lineaDatos = new StringBuilder();
            List<Columna<?>> columnas = tabla.getColumnas();

            for (int i = 0; i < columnas.size(); i++) {
                if (i > 0) {
                    lineaDatos.append(config.getSeparador());
                }

                // Obtener la celda correspondiente a esta fila y columna
                Columna<?> columna = columnas.get(i);
                int indiceFila = obtenerIndiceFila(etiquetaFila, tabla.getEtiquetasFilas());
                Celda<?> celda = columna.getCelda(indiceFila);

                String valorCelda = formatearValorCelda(celda, config);
                lineaDatos.append(escaparValor(valorCelda, config));
            }

            writer.write(lineaDatos.toString());
            writer.newLine();
        }
    }

    private int obtenerIndiceFila(Etiqueta etiquetaBuscada, List<Etiqueta> etiquetasFilas) {
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            if (etiquetasFilas.get(i).getValor().equals(etiquetaBuscada.getValor())) {
                return i;
            }
        }
        throw new JandasException("Etiqueta de fila no encontrada: " + etiquetaBuscada.getValor());
    }

    private String formatearValorCelda(Celda<?> celda, CsvConfig config) {
        if (celda.esNA()) {
            return config.getValorNulo();
        }
        return celda.toString();
    }

    /**
     * Escapa valores que contienen el separador, comillas o saltos de línea
     * siguiendo el estándar RFC 4180 para CSV
     */
    private String escaparValor(String valor, CsvConfig config) {
        if (valor == null) {
            return config.getValorNulo();
        }

        // Si el valor contiene el separador, comillas dobles o saltos de línea, debe ser escapado
        if (valor.contains(config.getSeparador()) ||
                valor.contains("\"") ||
                valor.contains("\n") ||
                valor.contains("\r")) {

            // Reemplazar comillas dobles por dobles comillas dobles
            String valorEscapado = valor.replace("\"", "\"\"");
            // Envolver en comillas dobles
            return "\"" + valorEscapado + "\"";
        }

        return valor;
    }

    // Getters y setters para la configuración
    public CsvConfig getConfig() {
        return config;
    }

    public void setConfig(CsvConfig config) {
        this.config = config;
    }
}
