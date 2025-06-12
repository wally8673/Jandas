package tests;

import jandas.base.data.Tabla;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.Orden;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

import java.util.Arrays;
import java.util.List;

public class TestOrdenar {
    public static void main(String[] args) {

        // 1 Creo una tabla, este caso mediante matriz nativa
        Object[][] datos =
                {{"Nombre", "Edad", "Altura", "Salario", "Ciudad"},
                        {"Agustin", 22, 1.98, 3000, "Buenos Aires"},
                        {"Juan", 25, 1.75, 2500, "Buenos Aires"},
                        {"Maria", 30, 1.65, 4000, "Rosario"},
                        {"Pedro", 28, 1.80, 3500, "Mendoza"},
                        {"Ana", 35, 1.70, 4500, "Buenos Aires"},
                        {"Luis", 40, 1.85, 5000, "Córdoba"}};
        Tabla df = new Tabla(datos);

        // Creo visualizador de consola
        Visualizable consola = new VConsola();

        // 2 Ordeno por una columna
        Tabla dfv = df.ordenar("Salario DESC");
        consola.visualizar(dfv);

        // 3 Dentro de visualizar puedo pasarle el tipo de ordenamiento
        consola.visualizar(df.ordenar("Edad ASC"));

        // 4 Ordeno por múltiples criterios

        Tabla df_criterios = df.ordenar("Ciudad ASC", "Salario DESC");
        consola.visualizar(df_criterios);

    }
}
