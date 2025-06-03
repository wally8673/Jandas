package jandas;

import jandas.Etiquetas.Etiqueta;

import java.util.ArrayList;
import java.util.List;

public class Dataframe {

    private List<Columna<?>> columnas;
    private List<Etiqueta> etiquetasFilas;
    private List<Etiqueta> etiquetaColumnas;


    public Dataframe(List<Columna<?>> columnas, List<Etiqueta> etiquetasFilas){
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetaColumnas = new ArrayList<>();
    }

    public int cantColumnas(){
        return columnas.size();
    }
    public int cantFilas(){
        return columnas.get(0).getCeldas().size();
    }
    public void etiquetasColum(){
        for(Columna<?> col: columnas){
            System.out.println(col.getNombre().getValor());
        }
    }
    public void etiquetasFila(){
        for(Etiqueta<?> etq : etiquetasFilas){
            System.out.println(etq.getValor());
        }
    }
    
}
