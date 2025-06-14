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

/**
 * Clase para escribir objetos {@link Tabla} en archivos CSV.
 * Permite configurar separador, encabezados y valor para celdas nulas.
 * <p>
 */
public class EscribirCsv implements EscritorCsv {

    /**
     * Configuración para la escritura del CSV
     */
    private CsvConfig config;

    /**
     * Constructor por defecto que usa configuración estándar.
     */
    public EscribirCsv() {
        this.config = new CsvConfig();
    }

    /**
     * Constructor que permite especificar una configuración personalizada.
     *
     * @param config configuración para escribir el CSV
     */
    public EscribirCsv(CsvConfig config) {
        this.config = config;
    }

    /**
     * Escribe la tabla a un archivo CSV usando la configuración por defecto.
     *
     * @param tabla objeto {@link Tabla} que contiene los datos a escribir
     * @param rutaArchivo ruta (path) del archivo donde se escribirá el CSV
     * @throws JandasException si la tabla o ruta son inválidas o ocurre un error IO
     */
    @Override
    public void escribir(Tabla tabla, String rutaArchivo) {
        escribir(tabla, rutaArchivo, this.config);
    }

    /**
     * Escribe la tabla a un archivo CSV usando un separador personalizado y
     * configuración por defecto para encabezados y valores nulos.
     *
     * @param tabla objeto {@link Tabla} que contiene los datos a escribir
     * @param rutaArchivo ruta del archivo CSV
     * @param separador carácter o cadena que separa los valores en el CSV
     * @throws JandasException si la tabla o ruta son inválidas o ocurre un error IO
     */
    @Override
    public void escribir(Tabla tabla, String rutaArchivo, String separador) {
        CsvConfig tempConfig = new CsvConfig(separador, this.config.isTieneEncabezado(), this.config.getValorNulo());
        escribir(tabla, rutaArchivo, tempConfig);
    }

    /**
     * Escribe la tabla a un archivo CSV usando separador y valor nulo personalizados.
     *
     * @param tabla objeto {@link Tabla} que contiene los datos a escribir
     * @param rutaArchivo ruta del archivo CSV
     * @param separador carácter o cadena separadora
     * @param valorNulo cadena que representará valores nulos en el CSV
     * @throws JandasException si la tabla o ruta son inválidas o ocurre un error IO
     */
    @Override
    public void escribir(Tabla tabla, String rutaArchivo, String separador, String valorNulo) {
        CsvConfig tempConfig = new CsvConfig(separador, this.config.isTieneEncabezado(), valorNulo);
        escribir(tabla, rutaArchivo, tempConfig);
    }

    /**
     * Método principal para escribir la tabla en un archivo CSV con configuración específica.
     *
     * @param tabla objeto {@link Tabla} con los datos
     * @param rutaArchivo ruta destino del archivo CSV
     * @param config configuración específica para escribir CSV (separador, encabezado, valor nulo)
     * @throws JandasException si la tabla o ruta son inválidas o si ocurre error de IO
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

    /**
     * Escribe los encabezados (nombres de columnas) en el archivo CSV.
     *
     * @param tabla tabla con los datos
     * @param writer objeto BufferedWriter para escribir el archivo
     * @param config configuración de escritura CSV
     * @throws IOException si hay error de escritura
     */
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

    /**
     * Escribe todas las filas de datos en el archivo CSV.
     *
     * @param tabla tabla con los datos
     * @param writer BufferedWriter para la escritura
     * @param config configuración CSV
     * @throws IOException si ocurre error de escritura
     */
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

    /**
     * Obtiene el índice numérico de una etiqueta de fila dentro de la lista de etiquetas.
     *
     * @param etiquetaBuscada etiqueta que se busca
     * @param etiquetasFilas lista de etiquetas de filas
     * @return índice entero correspondiente a la posición de la etiqueta
     * @throws JandasException si la etiqueta no se encuentra en la lista
     */
    private int obtenerIndiceFila(Etiqueta etiquetaBuscada, List<Etiqueta> etiquetasFilas) {
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            if (etiquetasFilas.get(i).getValor().equals(etiquetaBuscada.getValor())) {
                return i;
            }
        }
        throw new JandasException("Etiqueta de fila no encontrada: " + etiquetaBuscada.getValor());
    }

    /**
     * Formatea el valor de una celda, reemplazando valores NA por el valor nulo configurado.
     *
     * @param celda celda a formatear
     * @param config configuración CSV para valor nulo
     * @return cadena con el valor formateado
     */
    private String formatearValorCelda(Celda<?> celda, CsvConfig config) {
        if (celda.esNA()) {
            return config.getValorNulo();
        }
        return celda.toString();
    }

    /**
     * Escapa el valor para cumplir con el estándar CSV RFC 4180.
     * Encierra el valor entre comillas dobles si contiene el separador, comillas o saltos de línea.
     * Además, dobla las comillas dobles internas para escapar correctamente.
     *
     * @param valor cadena que se desea escapar
     * @param config configuración CSV para obtener separador y valor nulo
     * @return cadena escapada lista para ser escrita en CSV
     */
    private String escaparValor(String valor, CsvConfig config) {
        if (valor == null) {
            return config.getValorNulo();
        }

        // Si contiene separador, comillas dobles o saltos de línea, debe escaparse
        if (valor.contains(config.getSeparador()) ||
                valor.contains("\"") ||
                valor.contains("\n") ||
                valor.contains("\r")) {

            String valorEscapado = valor.replace("\"", "\"\"");
            return "\"" + valorEscapado + "\"";
        }

        return valor;
    }

    /**
     * Obtiene la configuración actual para la escritura CSV.
     *
     * @return configuración CSV utilizada
     */
    public CsvConfig getConfig() {
        return config;
    }

    /**
     * Establece una configuración personalizada para la escritura CSV.
     *
     * @param config configuración a establecer
     */
    public void setConfig(CsvConfig config) {
        this.config = config;
    }
}

