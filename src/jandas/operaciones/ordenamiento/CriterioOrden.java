package jandas.operaciones.ordenamiento;

import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaString;

public class CriterioOrden {
    private Etiqueta etiqueta;
    private Orden direccion;

    public CriterioOrden(Etiqueta etiqueta, Orden direccion) {
        this.etiqueta = etiqueta;
        this.direccion = direccion;
    }

    public CriterioOrden(String nombreColumna, Orden direccion) {
        this.etiqueta = new EtiquetaString(nombreColumna);
        this.direccion = direccion;
    }

    public CriterioOrden(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
        this.direccion = Orden.ASCENDENTE;
    }

    public Etiqueta getEtiqueta() { return etiqueta; }

    public Orden getTipoOrden() { return direccion;}
}