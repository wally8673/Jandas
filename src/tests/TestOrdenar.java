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
                {{"Nombre", "Edad", "Altura", "Salario"},
                        {"Agustin", 22, 1.98, 3000},
                        {"Juan", 25, 1.75, 2500},
                        {"Maria", 30, 1.65, 4000},
                        {"Pedro", 28, 1.80, 3500},
                        {"Ana", 35, 1.70, 4500}};
        Tabla df = new Tabla(datos);

        // 2 Ordeno por una columna
        Tabla df_o = df.ordenar("Salario", Orden.DESCENDENTE);
        Visualizable consola = new VConsola();
        consola.visualizar(df_o);

        // 3 Dentro de visualizar puedo pasarle el tipo de ordenamiento
        consola.visualizar(df.ordenar("Edad", Orden.ASCENDENTE));

        // 4 Ordeno por múltiples criterios

        List<CriterioOrden> criterios = Arrays.asList(
                new CriterioOrden("Edad", Orden.ASCENDENTE),
                new CriterioOrden("Salario", Orden.DESCENDENTE));
        Tabla df_multi = df.ordenarPorCriterios(criterios);
        consola.visualizar(df_multi);

    }
}
