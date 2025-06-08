package jandas;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Definir parámetros del CSV
        String rutaEntrada = "C:\\Users\\wvill\\Downloads\\arboles.csv";  // Ruta del archivo CSV de entrada
        String rutaSalida = "C:\\Users\\wvill\\Downloads\\arboles.csv"; // Ruta del archivo CSV de salida
        char delimitador = ',';  // Delimitador de columnas
        boolean tieneEncabezado = true;  // Indicar si el archivo tiene encabezado

        try {
            // Crear lector y cargar el DataFrame desde el CSV
            LeerArchivos lector = new CsvReader(delimitador, tieneEncabezado);
            Dataframe df = lector.leer(rutaEntrada);

            // Mostrar datos en consola
            Visualizador visualizador = new VisualizadorConsola(10, 5, 15);
            visualizador.mostrar(df);

            // Guardar el DataFrame en un nuevo archivo CSV
            EscritorArchivos escritor = new CsvWriter(delimitador, tieneEncabezado);
            escritor.escribir(df, rutaSalida);

            System.out.println("Datos guardados en: " + rutaSalida);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        // Crear etiquetas para las columnas
        Etiqueta<String> etiquetaNombre = new Etiqueta<>("Nombre");
        Etiqueta<String> etiquetaEdad = new Etiqueta<>("Edad");
        Etiqueta<String> etiquetaActivo = new Etiqueta<>("Activo");

        List<Celda<String>> datosNombre = List.of(new Celda<>("Juan"), new Celda<>("Maria"), new Celda<>("Carlos"));
        List<Celda<Integer>> datosEdad = List.of(new Celda<>(30), new Celda<>(25), new Celda<>(40));
        List<Celda<Boolean>> datosActivo = List.of(new Celda<>(true), new Celda<>(false), new Celda<>(true));

        Columna<String> columnaNombre = new Columna<>(new Etiqueta<>("Nombre"), datosNombre);
        Columna<Integer> columnaEdad = new Columna<>(new Etiqueta<>("Edad"), datosEdad);
        Columna<Boolean> columnaActivo = new Columna<>(new Etiqueta<>("Activo"), datosActivo);


        // Crear etiquetas de filas
        List<Etiqueta<?>> etiquetasFilas = List.of(new Etiqueta<>(0), new Etiqueta<>(1), new Etiqueta<>(2));

        // Crear el Dataframe manualmente
        Dataframe df = new Dataframe(List.of(columnaNombre, columnaEdad, columnaActivo), etiquetasFilas);

        // Mostrar los datos en consola
        Visualizador visualizador = new VisualizadorConsola(10, 5, 15);
        visualizador.mostrar(df);

        System.out.println("Dataframe creado manualmente con éxito.");

    }
}
