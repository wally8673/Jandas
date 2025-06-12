package tests;

import jandas.base.data.Tabla;
import jandas.io.EscribirArchivo;
import jandas.io.csv.EscribirCsv;
import jandas.io.csv.EscritorCsv;

public class TestEscribirCsv {
    public static void main(String[] args) {

        // 1 Creo una tabla, este caso mediante matriz nativa
        Object[][] datos =
                {{"Nombre", "Edad", "Altura"},
                        {"Agustin", 22, 1.98},
                        {"Juan", 25, 1.75},
                        {"Maria", 30, 1.65}};
        Tabla df = new Tabla(datos);

        // inicializo escritor

        EscribirArchivo escritor = new EscribirCsv();

        // escribo
        escritor.escribir(df,"df/tablaprueba.csv");

        // inicializo escritor csv con config custom

        EscritorCsv csv = new EscribirCsv();
        csv.escribir(df,"df/tablaprueba.csv",";");

    }
}
