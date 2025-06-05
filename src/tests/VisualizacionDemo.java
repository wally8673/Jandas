package tests;

import jandas.core.data.DataFrame;
import jandas.core.data.DataFrameGenerico;
import jandas.core.etiquetas.EtiquetaInt;
import jandas.core.etiquetas.EtiquetaString;
import jandas.visualizacion.VConfig;
import jandas.visualizacion.VConsola;

import java.util.Arrays;

/**
 * Demo class showing how to use the visualization package.
 */
public class VisualizacionDemo {
    
    public static void main(String[] args) {
        // Creo dataframe
        DataFrame df = new DataFrameGenerico();
        
        // Agrego columnas usando los 3 metodos

        // metodo 1
        df.agregarColumna(new EtiquetaString("Nombre"),
                String.class,
                Arrays.asList("Juan", "María", "Pedro", "Ana", "Luis"));
        
        df.agregarColumna(new EtiquetaInt(1),
                Integer.class,
                Arrays.asList(25, 30, 22, 28, 35));
        
        df.agregarColumna(new EtiquetaString("Altura"),
                Double.class,
                Arrays.asList(1.75, 1.65, 1.80, 1.70, 1.85));
        
        df.agregarColumna(new EtiquetaString("Activo"),
                Boolean.class,
                Arrays.asList(true, false, true, true, false));
        
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
    }
}