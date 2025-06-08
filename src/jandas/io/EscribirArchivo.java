package jandas.io;

import jandas.base.data.Tabla;
import jandas.excepciones.JandasException;

public interface EscribirArchivo  < O extends EscribirConfig> {

    void escribir(Tabla tabla, String rutaArchivo);

    void escribir(Tabla tabla, O config, String rutaArchivo);

}
