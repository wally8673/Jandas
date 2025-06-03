package jandas;

import java.util.List;

public class Dataframe{
    private List<Columna<?>> columnas;
    private List<Etiqueta<?>> etiquetasFilas;


    public Dataframe(List<Columna<?>> columnas, List<Etiqueta<?>> etiquetasFilas){
        this.columnas = columnas;
        this.etiquetasFilas = etiquetasFilas;
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
    public
}
