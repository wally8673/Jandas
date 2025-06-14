package jandas.io.csv;

import jandas.base.data.Tabla;
import jandas.io.LeerArchivo;

/**
 * Interfaz para leer archivos CSV y convertirlos en objetos {@link Tabla}.
 * Define métodos para leer archivos CSV con diferentes configuraciones
 * como presencia de encabezado, separador personalizado y configuración completa.
 */
public interface LectorCsv extends LeerArchivo {

    /**
     * Lee un archivo CSV desde la ruta especificada usando la configuración por defecto.
     *
     * @param rutaArchivo ruta (path) del archivo CSV a leer
     * @return objeto {@link Tabla} con los datos leídos del archivo
     */
    Tabla leer(String rutaArchivo);

    /**
     * Lee un archivo CSV con configuración personalizada para separador,
     * encabezado y valor nulo según la configuración pasada.
     *
     * @param rutaArchivo ruta del archivo CSV a leer
     * @param config configuración específica para la lectura CSV
     * @return objeto {@link Tabla} con los datos leídos del archivo
     */
    Tabla leer(String rutaArchivo, CsvConfig config);

    /**
     * Lee un archivo CSV especificando si contiene o no encabezado,
     * usando separador por defecto y valor nulo predeterminado.
     *
     * @param rutaArchivo ruta del archivo CSV a leer
     * @param encabezado indica si el archivo CSV contiene una fila de encabezado
     * @return objeto {@link Tabla} con los datos leídos del archivo
     */
    Tabla leer(String rutaArchivo, boolean encabezado);

    /**
     * Lee un archivo CSV especificando si contiene encabezado y el separador a utilizar,
     * con valor nulo predeterminado.
     *
     * @param rutaArchivo ruta del archivo CSV a leer
     * @param encabezado indica si el archivo CSV contiene una fila de encabezado
     * @param separador cadena que separa los valores en el archivo CSV
     * @return objeto {@link Tabla} con los datos leídos del archivo
     */
    Tabla leer(String rutaArchivo, boolean encabezado, String separador);
}

