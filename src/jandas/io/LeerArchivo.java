package jandas.io;

import jandas.base.data.Tabla;
import jandas.excepciones.JandasException;

/**
 * LeerArchivo.java
 */
public interface LeerArchivo {

	Tabla leer(String rutaArchivo) throws JandasException;

}
