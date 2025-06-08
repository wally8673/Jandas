package jandas;

import java.util.List;

public class VisualizadorConsola implements Visualizador {

    private final int maxFilas;
    private final int maxColumnas;
    private final int maxAnchoCelda;

    public VisualizadorConsola(int maxFilas, int maxColumnas, int maxAnchoCelda) {
        this.maxFilas = maxFilas;
        this.maxColumnas = maxColumnas;
        this.maxAnchoCelda = maxAnchoCelda;
    }

    @Override
    public void mostrar(Dataframe df) {
        int filas = Math.min(df.cantFilas(), maxFilas);
        int columnas = Math.min(df.cantColumnas(), maxColumnas);

        // Imprimir encabezado
        List<Object> nombres = df.etiquetasColum();
        for (int j = 0; j < columnas; j++) {
            String nombre = nombres.get(j).toString();
            System.out.print(formatear(nombre) + " ");
        }
        System.out.println();

        // Imprimir filas
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Celda<?> celda = df.getColumnas().get(j).getCeldas().get(i);
                Object valor = celda.getValor();
                String texto = valor != null ? valor.toString() : "NA";
                System.out.print(formatear(texto) + " ");
            }
            System.out.println();
        }

        if (df.cantFilas() > maxFilas) {
            System.out.println("... (" + (df.cantFilas() - maxFilas) + " filas más)");
        }
        if (df.cantColumnas() > maxColumnas) {
            System.out.println("... (" + (df.cantColumnas() - maxColumnas) + " columnas más)");
        }
    }

    private String formatear(String texto) {
        if (texto.length() > maxAnchoCelda) {
            return texto.substring(0, maxAnchoCelda - 3) + "...";
        } else {
            return String.format("%-" + maxAnchoCelda + "s", texto);
        }
    }
}
