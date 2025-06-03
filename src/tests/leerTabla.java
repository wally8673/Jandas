package tests;

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
        Celda<Float> c14 = new Celda<>(0.002);
        Celda<Float> c15 = new Celda<>(1234.2345);
        Celda<Float> c16 = new Celda<>(12.0);

    }
}
