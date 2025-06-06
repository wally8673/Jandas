package jandas;

import java.util.ArrayList;
import java.util.List;

public class Columna<T> {
    private Etiqueta nombre;
    private TipoDato tipoDato = TipoDato.DESCONOCIDO;
    private List<Celda<T>> celdas;


    public Columna(Etiqueta nombre, List<Celda<T>> celdasIniciales) {
        this.nombre = nombre;
        this.celdas = new ArrayList<>();

        for (Celda<T> celda : celdasIniciales) {
            this.agregarCelda(celda);
        }
    }

    public void agregarCelda(Celda<T> celda) {
        if (!celda.esNA()) {
            TipoDato nuevoTipo = determinarTipoDato(celda.getValor());
            if (nuevoTipo == TipoDato.DESCONOCIDO) {
                throw new IllegalArgumentException("Tipo de dato no permitido: " + celda.getValor().getClass().getSimpleName());
            }

            if (tipoDato == TipoDato.DESCONOCIDO) {
                tipoDato = nuevoTipo;
            } else if (tipoDato != nuevoTipo) {
                throw new IllegalArgumentException("Tipo de celda no coincide con el tipo de la columna.");
            }
        }
        celdas.add(celda);
    }

    public Etiqueta getNombre(){
        return nombre;
    }

    public List<Celda<T>> getCeldas(){
        return new ArrayList<>(celdas);
    }

    private TipoDato determinarTipoDato(Object valor) {
        if (valor instanceof Integer || valor instanceof Double || valor instanceof Float || valor instanceof Number) {
            return TipoDato.NUMERICO;
        } else if (valor instanceof Boolean) {
            return TipoDato.BOOLEANO;
        } else if (valor instanceof String) {
            return TipoDato.CADENA;
        } else {
            return TipoDato.DESCONOCIDO;
        }
    }
    public String obtenerTipoDato() {
        return tipoDato.name();
    }
}

