package jandas.operaciones.ordenamiento;

import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaString;

public class CriterioOrden {
    private Etiqueta etiqueta;
    private TipoOrden direccion;

    public CriterioOrden(Etiqueta etiqueta, TipoOrden direccion) {
        this.etiqueta = etiqueta;
        this.direccion = direccion;
    }

    public CriterioOrden(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
        this.direccion = TipoOrden.ASCENDENTE;
    }

    public Etiqueta getEtiqueta() { return etiqueta; }

    public TipoOrden getTipoOrden() { return direccion;}
}