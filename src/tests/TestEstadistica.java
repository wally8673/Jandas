package tests;

import jandas.base.data.Tabla;
import jandas.operaciones.estadisticas.OperacionEstadistica;
import jandas.visualizacion.VConsola;
import jandas.visualizacion.Visualizable;

import java.util.HashMap;
import java.util.Map;

public class TestEstadistica {
    public static void main(String[] args) {

        Visualizable consola = new VConsola();


        // Crear tabla de prueba
        Object[][] datos = {
                {"Grupo", "Categoria", "Valor1", "Valor2"},
                {"A", "X", 10, 1.5},
                {"A", "Y", 20, 2.5},
                {"B", "X", 15, 1.8},
                {"B", "Y", 25, 2.2},
                {"A", "X", 12, 1.7},
                {"C", "Z", 30, 3.0}
        };

        Tabla tablaTest = new Tabla(datos);


        // Agrupar por "Grupo" y sumar "Valor1"
        Map<String, OperacionEstadistica> operaciones = new HashMap<>();
        operaciones.put("Valor1", OperacionEstadistica.SUMA);

        Tabla resultado = tablaTest.agruparPor("Grupo", operaciones);

        consola.visualizar(resultado);

        consola.visualizar(tablaTest.agruparPor("Grupo", OperacionEstadistica.MEDIA));
        consola.visualizar(tablaTest.agruparPor("Grupo", OperacionEstadistica.MAXIMO));
        consola.visualizar(tablaTest.agruparPor("Grupo", OperacionEstadistica.MINIMO));
        consola.visualizar(tablaTest.agruparPor("Grupo", OperacionEstadistica.CUENTA));
        // Sola hay un C
        consola.visualizar(tablaTest.agruparPor("Grupo", OperacionEstadistica.VARIANZA));
        consola.visualizar(tablaTest.agruparPor("Grupo", OperacionEstadistica.DESVIO_ESTANDAR));
    }
}
