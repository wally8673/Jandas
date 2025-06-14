package jandas.visualizacion;

/**
 * Clase para configurar la visualización de objetos {@link jandas.base.data.Tabla}.
 * <p>
 * Permite definir límites de visualización como cantidad máxima de filas y columnas,
 * longitud máxima de las cadenas, formato de celdas, representación de valores faltantes (NA),
 * y otras opciones relacionadas al formato de impresión.
 * </p>
 */
public class VConfig {

    private int maxFilas;
    private int maxColumnas;
    private int maxLargoCadena;
    private String na;
    private String separadorCelda;
    private String separadorHeader;
    private boolean mostrarEtiquetaFila;

    /**
     * Crea una nueva configuración de visualización con valores predeterminados:
     * <ul>
     *     <li>máximo 10 filas</li>
     *     <li>máximo 100 columnas</li>
     *     <li>máximo 20 caracteres por celda</li>
     *     <li>valores faltantes representados como "NA"</li>
     *     <li>separador de celdas " | "</li>
     *     <li>separador de encabezado "-"</li>
     *     <li>etiquetas de filas no visibles</li>
     * </ul>
     */
    public VConfig() {
        this.maxFilas = 10;
        this.maxColumnas = 100;
        this.maxLargoCadena = 20;
        this.na = "NA";
        this.separadorCelda = " | ";
        this.separadorHeader = "-";
        this.mostrarEtiquetaFila = false;
    }

    /**
     * Crea una nueva configuración de visualización personalizada.
     *
     * @param maxFilas Número máximo de filas a mostrar.
     * @param maxColumnas Número máximo de columnas a mostrar.
     * @param maxLargoCadena Longitud máxima de los valores de cadena en las celdas.
     */
    public VConfig(int maxFilas, int maxColumnas, int maxLargoCadena) {
        this();
        this.maxFilas = maxFilas;
        this.maxColumnas = maxColumnas;
        this.maxLargoCadena = maxLargoCadena;
    }

    // Getters y setters

    /**
     * Devuelve el número máximo de filas a mostrar.
     *
     * @return número máximo de filas.
     */
    public int getMaxFilas() {
        return maxFilas;
    }

    /**
     * Establece el número máximo de filas a mostrar.
     *
     * @param maxFilas valor a establecer.
     */
    public void setMaxFilas(int maxFilas) {
        this.maxFilas = maxFilas;
    }

    /**
     * Devuelve el número máximo de columnas a mostrar.
     *
     * @return número máximo de columnas.
     */
    public int getMaxColumnas() {
        return maxColumnas;
    }

    /**
     * Establece el número máximo de columnas a mostrar.
     *
     * @param maxColumnas valor a establecer.
     */
    public void setMaxColumnas(int maxColumnas) {
        this.maxColumnas = maxColumnas;
    }

    /**
     * Devuelve la longitud máxima de caracteres por celda.
     *
     * @return longitud máxima de cadena.
     */
    public int getMaxLargoCadena() {
        return maxLargoCadena;
    }

    /**
     * Establece la longitud máxima de caracteres por celda.
     *
     * @param maxLargoCadena valor a establecer.
     */
    public void setMaxLargoCadena(int maxLargoCadena) {
        this.maxLargoCadena = maxLargoCadena;
    }

    /**
     * Devuelve la representación de valores faltantes (NA).
     *
     * @return cadena utilizada para representar valores faltantes.
     */
    public String getNa() {
        return na;
    }

    /**
     * Establece la cadena utilizada para representar valores faltantes (NA).
     *
     * @param na nueva representación de NA.
     */
    public void setNa(String na) {
        this.na = na;
    }

    /**
     * Devuelve el separador de celdas utilizado en la visualización.
     *
     * @return separador de celdas.
     */
    public String getSeparadorCelda() {
        return separadorCelda;
    }

    /**
     * Establece el separador de celdas a utilizar en la visualización.
     *
     * @param separadorCelda nuevo separador.
     */
    public void setSeparadorCelda(String separadorCelda) {
        this.separadorCelda = separadorCelda;
    }

    /**
     * Devuelve el carácter o cadena usado como separador del encabezado.
     *
     * @return separador del encabezado.
     */
    public String getSeparadorHeader() {
        return separadorHeader;
    }

    /**
     * Establece el carácter o cadena que se usará como separador del encabezado.
     *
     * @param separadorHeader nuevo valor del separador.
     */
    public void setSeparadorHeader(String separadorHeader) {
        this.separadorHeader = separadorHeader;
    }

    /**
     * Indica si deben mostrarse las etiquetas de las filas.
     *
     * @return {@code true} si se muestran, {@code false} en caso contrario.
     */
    public boolean isMostrarEtiquetaFila() {
        return mostrarEtiquetaFila;
    }

    /**
     * Establece si se deben mostrar las etiquetas de las filas.
     *
     * @param mostrarEtiquetaFila valor booleano a establecer.
     */
    public void setMostrarEtiquetaFila(boolean mostrarEtiquetaFila) {
        this.mostrarEtiquetaFila = mostrarEtiquetaFila;
    }
}
