package jandas;

import java.util.ArrayList;
import java.util.List;

public class Columna<T> {
    private Etiqueta nombre;
    private List<Celda<T>> celdas = new ArrayList<>();


    public Columna(Etiqueta nombre, List<Celda<T>> celdas){
        this.nombre = nombre;
        this.celdas = celdas;
    }

    public Etiqueta getNombre(){
        return nombre;
    }

    public List<Celda<T>> getCeldas(){
        return celdas;
    }

    public String obtenerTipoDato() {
        for (Celda<T> celda : celdas) {
            if (!celda.esNA()) {
                Object valor = celda.getValor();
                if (valor instanceof Integer || valor instanceof Double || valor instanceof Number) {
                    return "Numérico";
                } else if (valor instanceof Boolean) {
                    return "Booleano";
                } else if (valor instanceof String) {
                    return "Cadena";
                }
            }
        }
        return "Desconocido";
    }
}
