package tests;

import jandas.base.data.Tabla;
import jandas.io.csv.EscribirCsv;
import jandas.io.csv.EscritorCsv;
import jandas.io.csv.LectorCsv;
import jandas.io.csv.LeerCsv;
import jandas.operaciones.estadisticas.OperacionEstadistica;
import jandas.operaciones.filtros.Condicion;
import jandas.operaciones.filtros.CondicionComparacion;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

import java.util.HashMap;
import java.util.Map;

public class TestDemo {

    public static void main(String[] args) {

        // Creo un objeto en memoria de tipo CSV y declaro la variable con el tipo de interfaz LectorCsv

        LectorCsv csv = new LeerCsv();

        // mi archivo csv

        String datos = "df/results.csv";

        // Leo el csv

        Tabla df1 = csv.leer(datos, true, ",");

        System.out.println(df1); // no muestra la tabla

        // Creo un objeto en memoria de tipo VConsola y declaro la variable con el tipo de interfaz Visualizacion

        Visualizable consola = new VConsola();

        // ahora si visualizo

        consola.visualizar(df1,50,10,30);



        // operaciones sobre tabla

        // // Ordenar
        consola.visualizar(df1.ordenar("local_result DESC", "local_team ASC"),30,10,30);


        // // Filtrado
        Condicion mayor10Locales = new CondicionComparacion("local_result", ">=", 10);
        Condicion mayor10Visitante = new CondicionComparacion("visitor_result", ">=", 10);

        consola.visualizar(df1.filtrar(
                Condicion.or(
                        mayor10Locales,
                        mayor10Visitante))
                ,30,10,30);


        // // Muestreo
        consola.visualizar(df1.muestrear(1));


        // // Concatenacion

        Object[][] valoresFaltantes= new Object[][] {
                {"","date_name","local_team_id","local_team","local_result","visitor_result","visitor_team", "visitor_team_id"},
                {"32854","Final Mundial de Clubes 2029",21, "Chacarita",12,1,"Real Madrid", 96}};

        Tabla filaNueva = new Tabla(valoresFaltantes);

        Tabla df_real = df1.concatenacion(filaNueva);

        consola.visualizar(df_real
                .tail(10),
                10,10,100);

        // // Escribir información verídica

        EscritorCsv escritorCsv = new EscribirCsv();

        escritorCsv.escribir(df_real, "df/results_real.csv",";","NA");






        // Haciendo analisis de datos

        Map<String, OperacionEstadistica> criterios = new HashMap();
        criterios.put("local_result", OperacionEstadistica.SUMA);

        Tabla operaciones1 = df1.
                agruparPor("local_team", criterios).
                ordenar("local_result DESC");
        consola.visualizar(operaciones1,95,2,30);

    }
}
