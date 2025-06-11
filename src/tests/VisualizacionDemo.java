package tests;

import jandas.base.data.Celda;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.base.etiquetas.EtiquetaString;
import jandas.io.csv.CsvConfig;
import jandas.io.csv.LeerCsv;
import jandas.visualizacion.VConfig;
import jandas.visualizacion.VConsola;

import java.util.Arrays;

/**
 * Demo class showing how to use the visualization package.
 */
public class VisualizacionDemo {
    
    public static void main(String[] args) {



        //1 Creo dataframe
        Tabla df = new Tabla();
        
        // Agrego columnas

        df.agregarColumna(new EtiquetaString("Nombre"),
                String.class,
                Arrays.asList("Juan", "María", "Pedro" , "Ana", "Luis"));
        
        df.agregarColumna(new EtiquetaInt(1),
                Integer.class,
                Arrays.asList(25, 30, 22, 28, 35));
        
        df.agregarColumna(new EtiquetaString("Altura"),
                Double.class,
                Arrays.asList(1.75, 1.65, 1.80, 1.70, 1.85));
        
        df.agregarColumna(new EtiquetaString("Activo"),
                Boolean.class,
                Arrays.asList(true, false, true, true, false));

        // agrego filas

        Celda<String> c1 = new Celda<>("ramon gutierrez");
        Celda<Integer> c2 = new Celda<>(4);
        Celda<Double> c3 = new Celda<>(1.5);
        Celda<Boolean> c4 = new Celda<>(true);

        df.agregarFila(new EtiquetaString("fila 6"),
                Arrays.asList(c1,c2,c3,c4));
        
        // Creo visualizador de consola
        VConsola visualizador = new VConsola();
        
        // al no pasar parametros, por sobrecargar carga unos por defecto
        System.out.println("Visualización con configuración por defecto:");
        visualizador.visualizar(df);
        
        System.out.println("\n");
        
        // Ahora visualizo con parametros personalizados
        System.out.println("Visualización con configuración personalizada:");
        visualizador.visualizar(df, 3, 2, 5);
        
        System.out.println("\n");
        
        // Visualize with custom configuration object
        VConfig config = new VConfig();
        config.setMaxFilas(4);
        config.setMaxColumnas(3);
        config.setMaxLargoCadena(10);
        config.setNa("N/A");
        config.setMostrarEtiquetaFila(false);
        
        VConsola visualizadorPersonalizado = new VConsola(config);
        System.out.println("Visualización con objeto de configuración personalizado:");
        visualizadorPersonalizado.visualizarConConfig(df, config);


        // iniciañoizo lector CSV
        LeerCsv lectorCsv = new LeerCsv();

        // Lee archivo csv default
        Tabla peliculas = lectorCsv.leer("df/flights_september.csv");
        visualizador.visualizar(peliculas);

        // leer csv con config custom
        Tabla df2 = lectorCsv.leer("df/flights_september.csv", new CsvConfig(",", false));
        visualizador.visualizar(df2);

        Tabla df3 = (new LeerCsv()).leer("df/flights_september.csv", new CsvConfig(",", true));
        visualizador.visualizar(df3);

        // Por arreglo nativo de java

        Object[][] datos = new Object[][] {{"id", "nombre", "altura"},
            {4, "hernan", 1.80},
            {7, "fernando", 9.4}};

        Tabla matriz = new Tabla(datos);
        visualizador.visualizar(matriz);





    }
}