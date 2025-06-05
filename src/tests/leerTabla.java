package tests;

import jandas.base.data.CeldaGenerica;
import jandas.base.data.ColumnaGenerica;
import jandas.base.data.DataFrameGenerico;
import jandas.base.etiquetas.EtiquetaString;

import java.util.Arrays;

public class leerTabla {

    public static void main(String[] args) {

        //Columna 1

        CeldaGenerica<String> c1 = new CeldaGenerica<>("a");
        CeldaGenerica<String> c2 = new CeldaGenerica<>("b");
        CeldaGenerica<String> c3 = new CeldaGenerica<>("c");
        CeldaGenerica<String> c4 = new CeldaGenerica<>("d");

        // Columna 2 (manejo de NAs)

        CeldaGenerica<Integer> c5 = new CeldaGenerica<>(1);
        CeldaGenerica<Integer> c6 = new CeldaGenerica<>(2);
        CeldaGenerica<Integer> c7 = new CeldaGenerica<>();
        CeldaGenerica<Integer> c8 = new CeldaGenerica<>(2);

        // Columna 3

        CeldaGenerica<Boolean> c9 = new CeldaGenerica<>(true);
        CeldaGenerica<Boolean> c10 = new CeldaGenerica<>(false);
        CeldaGenerica<Boolean> c11 = new CeldaGenerica<>(true);
        CeldaGenerica<Boolean> c12 = new CeldaGenerica<>();

        // Columna 4

        CeldaGenerica<Float> c13 = new CeldaGenerica<>();
        CeldaGenerica<Float> c14 = new CeldaGenerica<>(0.002f);
        CeldaGenerica<Float> c15 = new CeldaGenerica<>(1234.2345f);
        CeldaGenerica<Float> c16 = new CeldaGenerica<>(12.0f);

        // Fila 5

        CeldaGenerica<String> c17 = new CeldaGenerica<>("e");
        CeldaGenerica<Integer> c18 = new CeldaGenerica<>(12);
        CeldaGenerica<Boolean> c19 = new CeldaGenerica<>(true);
        CeldaGenerica<Float> c20 = new CeldaGenerica<>(0.001f);


        DataFrameGenerico df = new DataFrameGenerico();

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

        ColumnaGenerica<Boolean> columna3 = new ColumnaGenerica<>(
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
