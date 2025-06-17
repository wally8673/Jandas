package tests;

import jandas.base.data.Celda;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.base.etiquetas.EtiquetaString;
import jandas.io.LeerArchivo;
import jandas.io.csv.LectorCsv;
import jandas.io.csv.LeerCsv;
import jandas.visualizacion.VConfig;
import jandas.visualizacion.VConsola;

import java.util.Arrays;
import java.util.List;

/**
 * Demo class showing how to use the visualization package.
 */
public class TestVisualizacion {
    
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
        VConsola consola = new VConsola();
        
        // al no pasar parametros, por sobrecargar carga unos por defecto
        System.out.println("Visualización con configuración por defecto:");
        consola.visualizar(df);
        
        // Ahora visualizo con parametros personalizados
        System.out.println("Visualización con configuración personalizada:");
        consola.visualizar(df, 3, 2, 5);
        
        // Visualizacion con objeto de configuracion personalizado
        VConfig config = new VConfig();
        config.setMaxFilas(4);
        config.setMaxColumnas(3);
        config.setMaxLargoCadena(10);
        config.setNa("N/A");
        config.setMostrarEtiquetaFila(true);
        
        VConsola consolaCustom = new VConsola(config);
        System.out.println("Visualización con objeto de configuración personalizado:");
        consolaCustom.visualizarConConfig(df, config);

        //Archivo csv

        String data = "df/flights_september.csv";

        //Inicializo con lector general
        LeerArchivo lector = new LeerCsv();
        Tabla df2 = lector.leer(data);
        consola.visualizar(df2);

        // Inicializo con lectorcsv y Lee archivo csv default
        LectorCsv csv = new LeerCsv();

        Tabla df3 = csv.leer(data);
        consola.visualizar(df3);

        // leer csv con config custom
        Tabla df4 = csv.leer(data, false, ",");
        consola.visualizar(df4);


        // Por arreglo nativo de java

        Object[][] datos = new Object[][] {{"id", "nombre", "altura"},
            {4, "hernan", 1.80},
            {7, "fernando", 9.4}};

        Tabla matriz = new Tabla(datos);
        consola.visualizar(matriz);

        System.out.println("Tipo de dato de la columna altura:" + matriz.getColumna("altura").getTipoDato());

        // Crear tabla desde secuencia lineal de strings
        List<String> secuencia = Arrays.asList(
                "id", "nombre", "altura",
                "1", "Julian", "1.78",
                "4", "Maria", "1.5");
        Tabla secuenciaTabla = new Tabla(secuencia, 3);
        consola.visualizar(secuenciaTabla);

        System.out.println("Tipo de dato de la columna altura:" + secuenciaTabla.getColumna("altura").getTipoDato());
        consola.visualizar(matriz);

    }
}