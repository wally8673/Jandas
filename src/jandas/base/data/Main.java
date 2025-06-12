package jandas.base.data;

import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.base.etiquetas.EtiquetaString;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Datos con encabezado
        Object[][] datos = {
                {"Nombre", "Edad", "Altura", "Activo"},   // Encabezado
                {"Ana", 23, 1.65, true},
                {"Luis", 30, 1.75, false},
                {"Zoe", 27, 1.60, true}
        };

        // Crear la tabla
        Tabla tabla = new Tabla(datos);

        // Mostrar cantidad de filas y columnas
        System.out.println("Cantidad de filas: " + tabla.cantFilas());
        System.out.println("Cantidad de columnas: " + tabla.cantColumnas());

        // Mostrar etiquetas de columna
        System.out.println("Etiquetas de columnas:");
        for (Etiqueta etiqueta : tabla.getEtiquetasColumnas()) {
            System.out.println(" - " + etiqueta.getValor());
        }

        // Mostrar etiquetas de fila
        System.out.println("Etiquetas de filas:");
        for (Etiqueta etiqueta : tabla.getEtiquetasFilas()) {
            System.out.println(" - " + etiqueta.getValor());
        }
        tabla.setValoresCelda(new EtiquetaInt(0), new EtiquetaString("Nombre"), "juan");
        EtiquetaString etiquetaColumna = new EtiquetaString("Nombre");
        Columna<?> columnaNombre = tabla.getColumna(etiquetaColumna);

        System.out.println("Contenido columna 'Nombre':");
        for (int i = 0; i < columnaNombre.getCeldas().size(); i++) {
            System.out.println("Fila " + i + ": " + columnaNombre.getCeldas().get(i).getValor());
        }

        // Insertar nueva columna copiando la columna "Nombre"
        EtiquetaString nuevaEtiqueta = new EtiquetaString("Nombre Copiado");
        tabla.insertarColumnaDesdeColumna(nuevaEtiqueta, etiquetaColumna);

        // Mostrar contenido de la columna copiada
        Columna<?> columnaNombreCopiado = tabla.getColumna(nuevaEtiqueta);
        System.out.println("Contenido columna 'Nombre Copiado':");
        for (int i = 0; i < columnaNombreCopiado.getCeldas().size(); i++) {
            System.out.println("Fila " + i + ": " + columnaNombreCopiado.getCeldas().get(i).getValor());
        }
        tabla.setValoresCelda(new EtiquetaInt(0), new EtiquetaString("Nombre Copiado"), "Pedro");
        EtiquetaString etiquetaNombreCopiado = new EtiquetaString("Nombre Copiado");
        columnaNombreCopiado = tabla.getColumna(etiquetaNombreCopiado);

// Mostrar el contenido de la columna copiada
        System.out.println("Contenido columna 'Nombre Copiado':");
        for (int i = 0; i < columnaNombreCopiado.getCeldas().size(); i++) {
            System.out.println("Fila " + i + ": " + columnaNombreCopiado.getCeldas().get(i).getValor());
        }
        List<Integer> puntajes = Arrays.asList(85, 90, 78);  // Debe tener la misma cantidad de elementos que filas
        EtiquetaString etiquetaPuntaje = new EtiquetaString("Puntaje");
        tabla.insertarColumnaDesdeSecuencia(etiquetaPuntaje, Integer.class, puntajes);

        // Mostrar valores de la nueva columna
        Columna<?> columnaPuntaje = tabla.getColumna(etiquetaPuntaje);
        System.out.println("Contenido columna 'Puntaje':");
        for (int i = 0; i < columnaPuntaje.getCeldas().size(); i++) {
            System.out.println("Fila " + i + ": " + columnaPuntaje.getCeldas().get(i).getValor());
        }

        System.out.println("Columnas antes de eliminar:");
        for (Etiqueta etiqueta : tabla.getEtiquetasColumnas()) {
            System.out.println(" - " + etiqueta.getValor());
        }

        // Eliminar la columna "Altura"
        EtiquetaString etiquetaAltura = new EtiquetaString("Altura");
        tabla.eliminarColumna(etiquetaAltura);

        // Mostrar etiquetas de columna después de eliminar
        System.out.println("\nColumnas después de eliminar 'Altura':");
        for (Etiqueta etiqueta : tabla.getEtiquetasColumnas()) {
            System.out.println(" - " + etiqueta.getValor());
        }
        // Mostrar contenido antes de eliminar
        System.out.println("Contenido antes de eliminar:");
        imprimirTabla(tabla);

        // Eliminar fila con etiqueta 1 (corresponde a Luis)
        EtiquetaInt etiquetaFilaEliminar = new EtiquetaInt(1);
        tabla.eliminarFila(etiquetaFilaEliminar);

        // Mostrar contenido después de eliminar
        System.out.println("\nContenido después de eliminar la fila con etiqueta 1:");
        imprimirTabla(tabla);
    }

    private static void imprimirTabla(Tabla tabla) {
        // Imprimir encabezado
        for (Etiqueta etiquetaCol : tabla.getEtiquetasColumnas()) {
            System.out.print(etiquetaCol.getValor() + "\t");
        }
        System.out.println();

        // Imprimir filas
        for (int i = 0; i < tabla.cantFilas(); i++) {
            Etiqueta filaEtiqueta = tabla.getEtiquetasFilas().get(i);
            for (Etiqueta colEtiqueta : tabla.getEtiquetasColumnas()) {
                Object valor = tabla.getValorCelda(filaEtiqueta, colEtiqueta);
                System.out.print(valor + "\t");
            }
            System.out.println();
        }
    }
}


