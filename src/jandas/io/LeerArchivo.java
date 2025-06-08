package jandas.io;

import jandas.base.data.Tabla;
import jandas.excepciones.JandasException;

/**
 * Generic interface for reading data from various sources.
 * @param <O> Configuration type that extends DataConfig
 */
public interface LeerArchivo<O extends LeerConfig> {

	/**
	 * Reads data from a file path using default configuration.
	 */
	Tabla leer(String rutaArchivo) throws JandasException;

	/**
	 * Reads data using specific configuration.
	 */
	Tabla leer(O config) throws JandasException;

	/**
	 * Reads data from a file path with specific configuration.
	 */
	default Tabla leer(String rutaArchivo, O config) throws JandasException {
		return leer(rutaArchivo);
	}
}
