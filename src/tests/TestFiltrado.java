package tests;

import jandas.base.data.Columna;
import jandas.base.data.Tabla;
import jandas.base.etiquetas.EtiquetaString;
import jandas.operaciones.filtros.Condicion;
import jandas.operaciones.filtros.CondicionComparacion;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

public class TestFiltrado {
    public static void main(String[] args) {

        // inicializo visualizacion por consola

        Visualizable consola = new VConsola();

        //tabla matriz 1 y 2
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
        consola.visualizar(estudiantes);

        // Usando AND (2 condiciones)
        Condicion mayoresDe20 = new CondicionComparacion("Edad", ">", 20);
        Condicion aprobados = new CondicionComparacion("Aprobado", "=", true);
        Condicion promedioAlto = new CondicionComparacion("Promedio", ">=", 7.0);
        Tabla filtro1 = estudiantes.filtrar(Condicion.and(mayoresDe20, aprobados));
        System.out.println("filtro mayores y aprobados");
        consola.visualizar(filtro1);

        // Usando AND (1 condicion)
        Tabla tablaCopia = estudiantes.copiar();
        System.out.println("copia profunda de la tabla estudiantes");
        consola.visualizar(tablaCopia);


        // tipo de dato de la columna letras
        System.out.println(estudiantes.getColumna("Nombre").getTipoDato());
        Condicion menoresDe20Copia = new CondicionComparacion("Edad", "<", 20);
        Tabla filtro2 = tablaCopia.filtrar(menoresDe20Copia);
        consola.visualizar(filtro2);

        //PROBANDO LA IMPUTACION
        System.out.println("Tabla Datos:");
        Tabla df2 = new Tabla(datos2);
        consola.visualizar(df2);

        System.out.println("Tipos de dato de las columnas:");
        for (Columna<?> col : df2.getColumnas()) {
            System.out.printf("Columna '%s' => Tipo: %s%n", col.getEtiqueta().toString(), col.getTipoDato().getSimpleName());
        }

        // de la columna edad, cambio los NA por 0 y mantiene el tipo de dato
        df2.imputarColumna(new EtiquetaString("Edad"), 0);
        df2.imputarDefault();
        consola.visualizar(df2);

        consola.visualizar(df2.tail(2));


    }
}
