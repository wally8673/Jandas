package jandas;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter implements EscritorArchivos {
    private final char delimitador;
    private final boolean escribirEncabezado;

    public CsvWriter(char delimitador, boolean escribirEncabezado) {
        this.delimitador = delimitador;
        this.escribirEncabezado = escribirEncabezado;
    }

    @Override
    public void escribir(Dataframe df, String rutaArchivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
            if (escribirEncabezado) {
                List<Object> nombres = df.etiquetasColum();
                for (int i = 0; i < nombres.size(); i++) {
                    bw.write(nombres.get(i).toString());
                    if (i < nombres.size() - 1) {
                        bw.write(delimitador);
                    }
                }
                bw.newLine();
            }

            for (int i = 0; i < df.cantFilas(); i++) {
                for (int j = 0; j < df.cantColumnas(); j++) {
                    Columna<?> col = df.getColumnas().get(j);
                    Celda<?> celda = col.getCeldas().get(i);
                    Object valor = celda.getValor();
                    bw.write(valor != null ? valor.toString() : "NA");
                    if (j < df.cantColumnas() - 1) {
                        bw.write(delimitador);
                    }
                }
                bw.newLine();
            }
        }
    }
}

