package jandas.io.csv;

import jandas.base.data.Tabla;
import jandas.io.LeerArchivo;

public interface LectorCsv extends LeerArchivo {

    Tabla leer(String rutaArchivo);
    Tabla leer(String rutaArchivo, CsvConfig config);
    Tabla leer(String rutaArchivo, boolean encabezado);
    Tabla leer(String rutaArchivo, boolean encabezado, String separador);
}
