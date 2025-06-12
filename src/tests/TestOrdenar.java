package tests;

import jandas.base.data.Tabla;
import jandas.operaciones.ordenamiento.TipoOrden;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

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
        Tabla df_o = df.ordenar("Salario", TipoOrden.DESCENDENTE);
        Visualizable consola = new VConsola();
        consola.visualizar(df_o);

        // Dentro de visualizar puedo pasarle el tipo de ordenamiento
        consola.visualizar(df.ordenar("Edad", TipoOrden.ASCENDENTE));

    }
}
