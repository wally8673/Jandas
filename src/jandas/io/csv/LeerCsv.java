package jandas.io.csv;

import jandas.base.data.Tabla;
import jandas.base.data.TablaGenerica;
import jandas.base.etiquetas.Etiqueta;
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

    public LeerCsv(CsvConfig config) {
        this.config = config;
    }

    @Override
    public Tabla leer(String rutaArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            List<String[]> filas = new ArrayList<>();
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] valores = linea.split(config.getSeparador());
                filas.add(valores);
            }

            if (filas.isEmpty()) {
                throw new JandasException("El archivo CSV está vacío");
            }

            return construirDataFrame(filas);

        } catch (IOException e) {
            throw new JandasException("Error al leer el archivo CSV: " + e.getMessage());
        }
    }


    public Tabla leer(String rutaArchvio, CsvConfig config) {
        this.config = config;
        return leer(rutaArchvio);
    }

    private Tabla construirDataFrame(List<String[]> filas) {
        Tabla tabla = new TablaGenerica();

        // Primera fila como encabezados si está configurado
        int inicioFilas = 0;
        List<Etiqueta> etiquetasColumnas = new ArrayList<>();

        if (config.isTieneEncabezado()) {
            String[] encabezados = filas.get(0);
            for (String encabezado : encabezados) {
                etiquetasColumnas.add(new EtiquetaString(encabezado.trim()));
            }
            inicioFilas = 1;
        } else {
            // Generar etiquetas automáticas
            int numColumnas = filas.get(0).length;
            for (int i = 0; i < numColumnas; i++) {
                etiquetasColumnas.add(new EtiquetaString("Col" + i));
            }
        }

        // Convertir datos a matriz
        int numFilas = filas.size() - inicioFilas;
        int numColumnas = etiquetasColumnas.size();
        Object[][] datos = new Object[numFilas][numColumnas];

        for (int i = inicioFilas; i < filas.size(); i++) {
            String[] fila = filas.get(i);
            for (int j = 0; j < Math.min(fila.length, numColumnas); j++) {
                datos[i - inicioFilas][j] = convertirValor(fila[j].trim());
            }
        }

        tabla.cargarDesdeMatriz(datos, etiquetasColumnas);
        return tabla;
    }

    private Object convertirValor(String valor) {
        if (valor.isEmpty() || valor.equalsIgnoreCase(config.getValorNulo())) {
            return null;
        }

        // Intentar convertir a número
        try {
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
