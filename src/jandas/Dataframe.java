package jandas;

import jandas.Etiquetas.Etiqueta;
import jandas.Etiquetas.EtiquetaInt;

import java.util.ArrayList;
import java.util.List;

public class Dataframe implements Visualizable {

    private List<Columna<?>> columnas;
    private List<Etiqueta> etiquetasFilas;
    private List<Etiqueta> etiquetasColumnas;


    public Dataframe(){
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
    }

    // metodos para agregar columnas

    // metodo 1: especificando valores

    public <T> void agregarColumna(Etiqueta etiquetaColumna, Class<T> tipo, List<T> valores){

        if (!columnas.isEmpty() && valores.size() != cantFilas()) {
            throw Excepciones.dimensionMismatch(cantFilas(), valores.size());
        }

        Columna<T> nuevaColumna = new Columna<>(etiquetaColumna, tipo);
        for (T valor : valores) {
            nuevaColumna.agregarValor(valor);
        }

        columnas.add(nuevaColumna);
        etiquetasColumnas.add(etiquetaColumna);

        // Si es la primera columna, genera etiquetas de filas automaticamente

        if (etiquetasFilas.isEmpty()) {
            for (int i=0; i < valores.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }
    }

    //Metodo 2: Con celdas ya creadas

    public <T> void agregarColumnaCeldas(Etiqueta etiquetaColumna, Class<T> tipo, List<Celda<T>> celdas) {
        if (!columnas.isEmpty() && celdas.size() != cantFilas()) {
            throw Excepciones.dimensionMismatch(cantFilas(), celdas.size());
        }

        Columna<T> nuevaColumna = new Columna<>(etiquetaColumna, tipo);
        nuevaColumna.agregarCeldas(celdas);

        columnas.add(nuevaColumna);
        etiquetasColumnas.add(etiquetaColumna);

        // Si es la primera columna, generar etiquetas de filas
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < celdas.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }
    }

    //Metodo 3: Agregar columna con clase columna

    public <T> void agregarColumna(Columna<T> columna) {
        if (!columnas.isEmpty() && columna.size() != cantFilas()) {
            throw Excepciones.dimensionMismatch(cantFilas(), columna.size());
        }

        columnas.add(columna);
        etiquetasColumnas.add(columna.getEtiqueta());

        // Si es la primera columna, generar etiquetas de filas
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < columna.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }

    }

    // Agregar fila con celdas ya creadas
    public void agregarFila(Etiqueta etiquetaFila, List<Celda<?>> celdas) {

        if (celdas.size() != cantColumnas()) {
            throw Excepciones.dimensionMismatch(cantColumnas(), celdas.size());
        }

        for (int i = 0; i < columnas.size(); i++) {
            Columna columna = columnas.get(i);
            columna.agregarCelda(celdas.get(i));
        }

        etiquetasFilas.add(etiquetaFila);
    }

    public static void leerCsv(String url, boolean headers, String separador){}


    private int getIndex(Etiqueta etiqueta, List<Etiqueta> etiquetas) {
        for (int i = 0; i < etiquetas.size(); i++) {
            if (etiquetas.get(i).getValor().equals(etiqueta.getValor())) {
                return i;
            }
        }
        throw new IllegalArgumentException("Etiqueta no encontrada: " + etiqueta.getValor());
    }

    public Fila getFila(Etiqueta etiquetaFila) {
        int indexFila = getIndex(etiquetaFila, etiquetasFilas);
        List<Celda<?>> celdasFilas = new ArrayList<>();
        for (Columna<?> col : columnas) {
            celdasFilas.add(col.getCeldas().get(indexFila));
        }
        return new Fila(etiquetaFila, celdasFilas, etiquetasColumnas);
    }

    private String filaToString(Fila fila, int maxFilas, int maxColumnas) {
        String salida = "*" + fila.getEtiquetaFila().toString() + "*" + " | ";
        maxColumnas = Math.min(maxColumnas, fila.getCeldasFila().size());

        for (int nroColumna = 0; nroColumna < maxColumnas; nroColumna++) {
            Celda<?> celda = fila.getCeldasFila().get(nroColumna);
            String textoCelda = celda.toString();
            salida += textoCelda;
            salida += " | ";
        }
        return salida;
    }

    public void visualizar(int maxFilas, int maxColumnas) {

        System.out.println("Cantidad de filas" + cantFilas());
        System.out.println("Cantidad de columnas" + cantColumnas());

        imprimirHeaders(etiquetasColumnas.subList(0, maxColumnas));

        for (Etiqueta etiqueta: etiquetasFilas.subList(0, maxFilas)) {
            System.out.println(filaToString(getFila(etiqueta), maxFilas, maxColumnas));
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

    public void imprimirFila(List<Fila> filas, int maxFilas){
        if (!filas.isEmpty()) {

            imprimirHeaders(filas.get(0).getEtiquetasColumnas());

            for (Fila fila : filas) {
                System.out.println(fila);
            }
        }
    }

    public void setEtiquetasFilas(List<Etiqueta> nuevasEtiquetas) {
        if (nuevasEtiquetas.size() != cantFilas()) {
            throw new Excepciones("Debe proporcionar exactamente " + cantFilas() + " etiquetas");
        }
        this.etiquetasFilas = new ArrayList<>(nuevasEtiquetas);
    }


    public List<Columna<?>> getColumnas() {return new ArrayList<>(columnas);} //copia defensiva
    public Columna<?> getColumna(Etiqueta etiqueta) {
        int index = getIndex(etiqueta, etiquetasColumnas);
        return columnas.get(index);
    }
    public int cantColumnas(){return columnas.size();}
    public int cantFilas(){return etiquetasFilas.size();}
    public List<Etiqueta> getEtiquetasColumnas(){return etiquetasColumnas;}

    @Override
    public boolean equals(Object obj) {
        // Verificar si es el mismo objeto
        if (this == obj) {
            return true;
        }

        // Verificar si obj es null
        if (obj == null) {
            return false;
        }

        // Verificar si son de la misma clase
        if (getClass() != obj.getClass()) {
            return false;
        }

        // Casting seguro
        Dataframe other = (Dataframe) obj;

        // Comparar las listas de columnas
        if (columnas == null) {
            if (other.columnas != null) {
                return false;
            }
        } else if (!columnas.equals(other.columnas)) {
            return false;
        }

        // Comparar las etiquetas de filas
        if (etiquetasFilas == null) {
            if (other.etiquetasFilas != null) {
                return false;
            }
        } else if (!etiquetasFilas.equals(other.etiquetasFilas)) {
            return false;
        }

        // Comparar las etiquetas de columnas
        if (etiquetasColumnas == null) {
            if (other.etiquetasColumnas != null) {
                return false;
            }
        } else if (!etiquetasColumnas.equals(other.etiquetasColumnas)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((columnas == null) ? 0 : columnas.hashCode());
        result = prime * result + ((etiquetasFilas == null) ? 0 : etiquetasFilas.hashCode());
        result = prime * result + ((etiquetasColumnas == null) ? 0 : etiquetasColumnas.hashCode());

        return result;
    }

}
