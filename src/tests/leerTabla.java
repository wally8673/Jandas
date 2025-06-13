package tests;

import jandas.base.data.Celda;
import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.base.etiquetas.EtiquetaString;
import jandas.operaciones.filtros.*;
import jandas.visualizacion.VConsola;

import jandas.io.LeerArchivo;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

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

        // Visualizo por consola

        Visualizable consola = new VConsola();
        consola.visualizar(df);
        //tabla matriz
        Object[][] datos = {
                {"Nombre", "Edad", "Promedio", "Aprobado"},
                {"Ana", 20, 8.5, true},
                {"Juan", 22, 7.0, true},
                {"Pedro", 19, 4.5, false},
                {"María", 21, 9.0, true},
                {"Luis", 20, 5.5, false}
        };

        Object[][] datos2 = {
                {"Nombre", "Edad", "Promedio"},
                {"Ana", 20, null},
                {"Juan", null, 7.5},
                {"María", 22, null}
        };


        Tabla estudiantes = new Tabla(datos);
        System.out.println("Tabla Datos:");

        VConsola visualizador1 = new VConsola();
        visualizador1.visualizar(estudiantes);

        // Usando AND (2 condiciones)
        Condicion mayoresDe20 = new CondicionComparacion("Edad", ">", 20);
        Condicion aprobados = new CondicionComparacion("Aprobado", "=", true);
        Condicion promedioAlto = new CondicionComparacion("Promedio", ">=", 7.0);
        Tabla filtro1 = estudiantes.filtrar(Condicion.and(mayoresDe20, aprobados));
        System.out.println("filtro mayores y aprobados");
        visualizador1.visualizar(filtro1);

        // Usando AND (1 condicion)
        Tabla tablaCopia = estudiantes.copiar();
        System.out.println("copia profunda de la tabla estudiantes");

        // Visualizo por consola acotada

        consola.visualizar(df, 3, 2, 5);

        // tipo de dato de la columna letras
        System.out.println(df.getColumna("Letras").getTipoDato());
        Condicion menoresDe20Copia = new CondicionComparacion("Edad", "<", 20);
        Tabla filtro2 = tablaCopia.filtrar(menoresDe20Copia);
        visualizador1.visualizar(filtro2);

        //PROBANDO LA IMPUTACION
        System.out.println("Tabla Datos:");
        Tabla df2 = new Tabla(datos2);
        visualizador1.visualizar(df2);

        // de la columna edad, cambio los NA por 0 y mantiene el tipo de dato
        df2.imputarColumna(new EtiquetaString("Edad"), 0);
        df2.imputarDefault();
        visualizador1.visualizar(df2);

        visualizador1.visualizar(df2.tail(2));
    }
}

