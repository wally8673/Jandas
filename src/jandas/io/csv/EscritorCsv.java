package jandas.io.csv;

import jandas.base.data.Tabla;
import jandas.io.EscribirArchivo;

public interface EscritorCsv extends EscribirArchivo {

    void escribir(Tabla tabla, String rutaArchivo);
    void escribir(Tabla tabla, String rutaArchivo, String separador);
    void escribir(Tabla tabla, String rutaArchivo, String separador, String valorNulo);
}
