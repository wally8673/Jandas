package jandas.io.csv;

/**
 * Clase de configuración para archivos CSV.
 * <p>
 * Permite definir parámetros como el separador de columnas,
 * si el archivo contiene encabezado, y el valor que representa un dato nulo.
 */
public class CsvConfig {

    /** Separador utilizado para delimitar columnas (por defecto: ","). */
    private String separador;

    /** Indica si el archivo CSV tiene una fila de encabezado (por defecto: true). */
    private boolean tieneEncabezado;

    /** Valor que representa datos nulos en el CSV (por defecto: "NA"). */
    private String valorNulo;

    /**
     * Constructor por defecto.
     * Establece el separador en coma (","), habilita encabezado y valor nulo "NA".
     */
    public CsvConfig() {
        this.separador = ",";
        this.tieneEncabezado = true;
        this.valorNulo = "NA";
    }

    /**
     * Constructor con separador y flag de encabezado.
     * El valor nulo se establece en "NA" por defecto.
     *
     * @param separador Separador de columnas (ejemplo: ",", ";").
     * @param tieneEncabezado Indica si el archivo tiene encabezado.
     */
    public CsvConfig(String separador, boolean tieneEncabezado) {
        this.separador = separador;
        this.tieneEncabezado = tieneEncabezado;
        this.valorNulo = "NA";
    }

    /**
     * Constructor con todos los parámetros configurables.
     *
     * @param separador Separador de columnas.
     * @param tieneEncabezado Indica si el archivo tiene encabezado.
     * @param valorNulo Valor que representa datos nulos en el CSV.
     */
    public CsvConfig(String separador, boolean tieneEncabezado, String valorNulo) {
        this.separador = separador;
        this.tieneEncabezado = tieneEncabezado;
        this.valorNulo = valorNulo;
    }

    /**
     * Obtiene el separador de columnas.
     *
     * @return El separador de columnas.
     */
    public String getSeparador() { return separador; }

    /**
     * Establece el separador de columnas.
     *
     * @param separador Nuevo separador de columnas.
     */
    public void setSeparador(String separador) { this.separador = separador; }

    /**
     * Indica si el archivo CSV tiene una fila de encabezado.
     *
     * @return true si tiene encabezado, false en caso contrario.
     */
    public boolean isTieneEncabezado() { return tieneEncabezado; }

    /**
     * Define si el archivo CSV tiene encabezado.
     *
     * @param tieneEncabezado true si tiene encabezado, false en caso contrario.
     */
    public void setTieneEncabezado(boolean tieneEncabezado) { this.tieneEncabezado = tieneEncabezado; }

    /**
     * Obtiene el valor que representa datos nulos en el CSV.
     *
     * @return El valor que representa datos nulos.
     */
    public String getValorNulo() { return valorNulo; }

    /**
     * Establece el valor que representa datos nulos en el CSV.
     *
     * @param valorNulo Nuevo valor para representar datos nulos.
     */
    public void setValorNulo(String valorNulo) { this.valorNulo = valorNulo; }

}
