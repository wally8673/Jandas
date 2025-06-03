package tests;

import jandas.Dataframe;

public class leerCsv {

    public static void main(String[] args) {

        Dataframe df = Dataframe.leerCSV("data\\movies.csv", true, ",");
    }
}
