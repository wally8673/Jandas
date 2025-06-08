package jandas.io.csv;

import jandas.io.EscribirArchivo;
import jandas.io.EscribirConfig;

public class EscribirCsvConfig extends EscribirArchivo<EscribirConfig> {

    private String separador;
    private boolean incluirEncabezados;
    private String rutaArchivo;

    public EscribirCsvConfig(String rutaArchivo, String separador, boolean incluirEncabezados) {
        this.rutaArchivo = rutaArchivo;
        this.separador = separador;
        this.incluirEncabezados = incluirEncabezados;
    }

    public String getSeparador() {
        return separador;
    }

    public boolean isIncluirEncabezados() {
        return incluirEncabezados;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

}
