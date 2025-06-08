package jandas.io;

import jandas.base.data.Tabla;

public interface LeerArchivo<O extends DataConfig> {

    Tabla leer(String rutaArchivo);

    Tabla leer(O config);
}
