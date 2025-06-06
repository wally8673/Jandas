package jandas;

import java.io.*;
import java.util.*;

public class CSVReader {
    private final String delimitador;
    private final boolean tieneEncabezado;

    public CSVReader(String delimitador, boolean tieneEncabezado) {
        this.delimitador = delimitador;
        this.tieneEncabezado = tieneEncabezado;
    }

    public Dataframe leerCSV(String rutaArchivo) throws IOException {
        List<Columna<?>> columnas = new ArrayList<>();
        List<Etiqueta<?>> etiquetasFilas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            String[] encabezados = null;
            int filaActual = 0;

            while ((linea = br.readLine()) != null) {
                String[] valores = linea.split(delimitador, -1); // -1 para mantener valores vacíos

                // Si es la primera fila y hay encabezado
                if (filaActual == 0 && tieneEncabezado) {
                    encabezados = valores;
                    for (String nombreColumna : valores) {
                        columnas.add(new Columna<>(new Etiqueta<>(nombreColumna), new ArrayList<>()));
                    }
                } else {
                    // Validación: todas las filas deben tener la misma cantidad de columnas
                    int columnasEsperadas = tieneEncabezado ? columnas.size() : valores.length;
                    if (valores.length != columnasEsperadas) {
                        throw new IllegalArgumentException("Error en fila " + filaActual +
                                ": se esperaban " + columnasEsperadas + " columnas pero se encontraron " + valores.length);
                    }

                    etiquetasFilas.add(new Etiqueta<>(filaActual));

                    for (int i = 0; i < valores.length; i++) {
                        Celda<?> celda;
                        String valor = valores[i];

                        if (valor.equalsIgnoreCase("NA") || valor.isEmpty()) {
                            celda = new Celda<>();
                        } else {
                            Object dato = inferirTipo(valor);
                            celda = new Celda<>(dato);
                        }

                        // Si no hay encabezado, crear columnas al vuelo con nombres genéricos
                        if (columnas.size() < valores.length && !tieneEncabezado) {
                            columnas.add(new Columna<>(new Etiqueta<>("Col" + i), new ArrayList<>()));
                        }

                        columnas.get(i).agregarCelda(celda);
                    }
                }
                filaActual++;
            }
        }

        return new Dataframe(columnas, etiquetasFilas);
    }

    private Object inferirTipo(String valor) {
        if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(valor);
        }
        try {
            if (valor.contains(".")) {
                return Double.parseDouble(valor);
            } else {
                return Integer.parseInt(valor);
            }
        } catch (NumberFormatException e) {
            return valor; // Si no es número ni booleano, dejar como string
        }
    }
}




