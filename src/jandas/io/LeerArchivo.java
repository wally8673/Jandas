package jandas.io;

import jandas.base.data.Tabla;
import jandas.excepciones.JandasException;

/**
 * Interfaz que define el contrato para leer datos tabulares desde un archivo.
 * <p>
 * Las implementaciones de esta interfaz deben proporcionar la lógica para cargar
 * una instancia de {@link Tabla} desde un archivo ubicado en la ruta indicada.
 */
public interface LeerArchivo {

	/**
	 * Lee una tabla desde un archivo.
	 *
	 * @param rutaArchivo La ruta del archivo desde donde se leerán los datos.
	 * @return Una instancia de {@link Tabla} con los datos cargados desde el archivo.
	 * @throws JandasException Si ocurre algún error durante la lectura, como archivo no encontrado
	 * o formato inválido.
	 */
	Tabla leer(String rutaArchivo) throws JandasException;

}

