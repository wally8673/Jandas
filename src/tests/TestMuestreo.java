package tests;

import jandas.base.data.Tabla;
import jandas.io.csv.LectorCsv;
import jandas.io.csv.LeerCsv;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

public class TestMuestreo {
    public static void main(String[] args) {

        // 1 Creo una tabla, este caso mediante matriz nativa
        LectorCsv csv = new LeerCsv();
        Tabla df = csv.leer("df/results.csv");

        // 2 Muestro la tabla
        Visualizable consola = new VConsola();
        consola.visualizar(df);

        // 3 Creo una muestra aleatoria del 10% de las filas
        Tabla dfMuestreada = df.muestrear(10); // 10% de las filas
        consola.visualizar(dfMuestreada);

        // 4 Creo una muestra aleatoria de 5 filas
        Tabla dfMuestreadaFija = df.muestrear(5, true); // 5 filas exactas
        consola.visualizar(dfMuestreadaFija);
    }
}
