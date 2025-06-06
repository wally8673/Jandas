package jandas;

import java.io.*;
import java.util.*;

public class CSVWriter {
    private final String delimitador;
    private final boolean incluirEncabezado;

    public CSVWriter(String delimitador, boolean incluirEncabezado) {
        this.delimitador = delimitador;
        this.incluirEncabezado = incluirEncabezado;
    }

    public void escribirCSV(String rutaArchivo, Dataframe dataframe) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            if (incluirEncabezado) {
                bw.write(String.join(delimitador, dataframe.etiquetasColum()));
                bw.newLine();
            }

            for (Etiqueta<?> etiquetaFila : dataframe.etiquetasFila()) {
                List<Object> datosFila = dataframe.datosFilas(etiquetaFila);
                List<String> valores = new ArrayList<>();
                for (Object valor : datosFila) {
                    valores.add(valor != null ? valor.toString() : "NA");
                }
                bw.write(String.join(delimitador, valores));
                bw.newLine();
            }
        }
    }
}
