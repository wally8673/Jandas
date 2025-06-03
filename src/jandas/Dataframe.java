package jandas;

import jandas.Etiquetas.Etiqueta;

import java.util.ArrayList;
import java.util.List;
import java.util.SequencedCollection;

public class Dataframe implements Visualizable {

    private List<Columna<?>> columnas;
    private List<Etiqueta> etiquetasFilas;
    private List<Etiqueta> etiquetasColumnas;


    public Dataframe(){
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
    }

    public int cantColumnas(){
        return columnas.size();
    }
    public int cantFilas(){
        return columnas.get(0).getCeldas().size();
    }
    public void etiquetasColum(){
        for(Columna<?> col: columnas){
            System.out.println(col.getEtiqueta().getValor());
        }
    }
    public void etiquetasFila(){
        for(Etiqueta etq : etiquetasFilas){
            System.out.println(etq.getValor());
        }
    }

    public Fila getFila(Etiqueta etiquetaFila) {
        int indexFila = getIndex(etiquetaFila, etiquetasFilas);
        List<Celda<?>> celdasFilas = new ArrayList<>();
        for (Columna<?> col : columnas) {
            celdasFilas.add(col.getCeldas().get(indexFila));
        }
        return new Fila(etiquetaFila, celdasFilas, etiquetasColumnas);
    }

    public void visualizar(int filas, int columnas) {

        System.out.println("Cantidad de filas" + cantFilas());
        System.out.println("Cantidad de columnas" + cantColumnas());

        imprimirHeaders(etiquetasColumnas.subList(0, columnas));

        for (Etiqueta etiqueta: etiquetasFilas.sublist(0, filas)) {
            System.out.println(imprimirFila(getFila(etiqueta), filas));
        }
    }

    public void imprimirHeaders(List<Etiqueta> etiquetas){
        String separador = " | ";

        for (int i = 0; i < etiquetas.size(); i++) {
            separador += etiquetas.get(i);
            separador += " | ";
        }

        System.out.println(separador);
    }

    public void imprimirFila(List<Etiqueta> etiquetas, List<List<Celda<?>>> filas){
        if (!filas.isEmpty()) {

            imprimirHeaders(filas.get(0).getEtiquetasColumnas());

            for (Fila fila : filas) {
                System.out.println(fila);
            }
        }
    }

    public List<Etiqueta> getEtiquetasColumnas(){return etiquetasColumnas;}

}
