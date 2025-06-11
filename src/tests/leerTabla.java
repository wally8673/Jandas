package tests;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.EtiquetaString;

import java.util.Arrays;

public class leerTabla {

    public static void main(String[] args) {

        //Columna 1

        Celda<String> c1 = new Celda<>("a");
        Celda<String> c2 = new Celda<>("b");
        Celda<String> c3 = new Celda<>("c");
        Celda<String> c4 = new Celda<>("d");

        // Columna 2 (manejo de NAs)

        Celda<Integer> c5 = new Celda<>(1);
        Celda<Integer> c6 = new Celda<>(2);
        Celda<Integer> c7 = new Celda<>();
        Celda<Integer> c8 = new Celda<>(2);

        // Columna 3

        Celda<Boolean> c9 = new Celda<>(true);
        Celda<Boolean> c10 = new Celda<>(false);
        Celda<Boolean> c11 = new Celda<>(true);
        Celda<Boolean> c12 = new Celda<>();

        // Columna 4

        Celda<Float> c13 = new Celda<>();
        Celda<Float> c14 = new Celda<>(0.002f);
        Celda<Float> c15 = new Celda<>(1234.2345f);
        Celda<Float> c16 = new Celda<>(12.0f);

        // Fila 5

        Celda<String> c17 = new Celda<>("e");
        Celda<Integer> c18 = new Celda<>(12);
        Celda<Boolean> c19 = new Celda<>(true);
        Celda<Float> c20 = new Celda<>(0.001f);


        Tabla df = new Tabla();

        // agrego columnas

        // Metodo 1

        df.agregarColumna(
                new EtiquetaString("Letras"),
                String.class,
                Arrays.asList("a","b","1","d")
        );


        // Metodo 2

        df.agregarColumnaCeldas(
                new EtiquetaString("Numeros"),
                Integer.class,
                Arrays.asList(c5,c6,c7,c8)
        );

        // Metodo 3

        Columna<Boolean> columna3 = new Columna<>(
                new EtiquetaString("Booleanos"),
                Boolean.class
        );

        columna3.agregarCeldas(Arrays.asList(c9,c10,c11,c12));

        df.agregarColumna(columna3);

        // Agrego filas

        df.agregarFila(
                new EtiquetaString("Fila 5"),
                Arrays.asList(c17,c18,c19)
        );

        //df.visualizar(5,3);

    }
}
