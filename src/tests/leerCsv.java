package tests;

import jandas.base.data.Tabla;
import jandas.io.LeerArchivo;
import jandas.io.csv.LectorCsv;
import jandas.io.csv.LeerCsv;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

public class leerCsv {

    public static void main(String[] args) {

        //Archivo csv

        Visualizable consola = new VConsola();

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
    }
}
