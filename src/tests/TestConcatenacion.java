package tests;


import jandas.base.data.Tabla;
import jandas.io.csv.LectorCsv;
import jandas.io.csv.LeerCsv;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

public class TestConcatenacion {

    public static void main(String[] args) {



        LectorCsv csv = new LeerCsv();
        Visualizable consola = new VConsola();


        //Visualizar primera tabla
        String datos = "df/tablaprueba.csv";
        consola.visualizar(csv.leer(datos,true,";"));


        // tabla a concatenar verticalmente

        Object[][] datos2 = {
                {"Nombre", "Edad", "Altura"},
                {"Joseando", 2, 1.75},
                {"Robertin", 3, 1.80},
                {"Maria Jose", 5, 1.70}};

        Tabla concatenar = new Tabla(datos2);
        consola.visualizar(concatenar);

        // concatenar tablas
        consola.visualizar(csv.leer(datos,true,";").concatenacion(concatenar));



    }
}
