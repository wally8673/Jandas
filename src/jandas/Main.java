package jandas;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.List;

public class Main {

        public static void main(String[] args) {

            //Columna 1

            Celda<String> c1 = new Celda<>("a");
            Celda<String> c2 = new Celda<>("b");
            Celda<String> c3 = new Celda<>("c");


            // Columna 2 (manejo de NAs)

            Celda<Integer> c5 = new Celda<>(1);
            Celda<Integer> c6 = new Celda<>(2);
            Celda<Double> c8 = new Celda<>(2.0);

            // Columna 3

            Celda<Boolean> c9 = new Celda<>(true);
            Celda<Boolean> c10 = new Celda<>(false);
            Celda<Boolean> c11 = new Celda<>(true);
            Etiqueta Col = new Etiqueta("columna1") ;
            Columna col1 = new Columna<>(Col,);
        }
}
