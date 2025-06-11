package jandas.io;

import jandas.base.data.Tabla;

public interface EscribirArchivo  < O> {

    void escribir(Tabla tabla, String rutaArchivo);
}
