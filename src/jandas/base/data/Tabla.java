package jandas.base.data;

import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tabla {

    private List<Columna<?>> columnas;
    private List<Etiqueta> etiquetasFilas;
    private List<Etiqueta> etiquetasColumnas;

    public Tabla() {
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
    }

    // Crea tabla en base a una lista de columnas y etiquetas

    public Tabla(List<Etiqueta> etiquetasColumnas, List<Columna<?>> columnas) {
        this.columnas = new ArrayList<>(columnas);
        this.etiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        this.etiquetasFilas = generarEtiquetaFilas();

    }

    // Constructor para matriz con encabezado
    public Tabla(Object[][] datosConEncabezado) {
        this.columnas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
        this.etiquetasFilas = generarEtiquetaFilas();

        if (datosConEncabezado == null || datosConEncabezado.length < 2) {
            throw new JandasException("Se necesita al menos una fila de encabezado y una de datos.");
        }

        int cantidadFilas = datosConEncabezado.length;
        int cantidadColumnas = datosConEncabezado[0].length;

        // Validar que todas las filas tengan la misma cantidad de columnas
        for (int i = 0; i < cantidadFilas; i++) {
            if (datosConEncabezado[i].length != cantidadColumnas) {
                throw new JandasException("Todas las filas deben tener la misma cantidad de columnas.");
            }
        }

        // Procesar datos por columna
        for (int j = 0; j < cantidadColumnas; j++) {
            // Crear etiqueta del encabezado
            Object encabezado = datosConEncabezado[0][j];
            Etiqueta etiqueta = crearEtiquetaDesdeObjeto(encabezado);

            // Recopilar valores de la columna (excluyendo encabezado)
            List<Object> valoresColumna = new ArrayList<>();
            for (int i = 1; i < cantidadFilas; i++) {
                valoresColumna.add(datosConEncabezado[i][j]);
            }

            // Inferir el tipo de la columna y crearla directamente
            Class<?> tipoColumna = inferirTipoColumna(valoresColumna);
            crearYAgregarColumna(etiqueta, tipoColumna, valoresColumna);
        }
    }

    // Método auxiliar para crear etiquetas desde objetos
    private Etiqueta crearEtiquetaDesdeObjeto(Object obj) {
        if (obj instanceof Integer) {
            return new EtiquetaInt((Integer) obj);
        } else {
            return new EtiquetaString(obj.toString());
        }
    }

    // Método auxiliar para inferir el tipo de una columna
    private Class<?> inferirTipoColumna(List<Object> valores) {
        if (valores.isEmpty()) {
            return Object.class;
        }

        // Contar tipos no nulos
        int contadorInteger = 0;
        int contadorDouble = 0;
        int contadorString = 0;
        int contadorBoolean = 0;
        int totalNoNulos = 0;

        for (Object valor : valores) {
            if (valor != null) {
                totalNoNulos++;
                if (valor instanceof Integer) {
                    contadorInteger++;
                } else if (valor instanceof Double || valor instanceof Float) {
                    contadorDouble++;
                } else if (valor instanceof Boolean) {
                    contadorBoolean++;
                } else {
                    contadorString++;
                }
            }
        }

        if (totalNoNulos == 0) {
            return Object.class;
        }

        // Determinar tipo predominante
        if (contadorInteger == totalNoNulos) {
            return Integer.class;
        } else if (contadorDouble == totalNoNulos) {
            return Double.class;
        } else if (contadorBoolean == totalNoNulos) {
            return Boolean.class;
        } else if (contadorInteger + contadorDouble == totalNoNulos) {
            // Mezcla de enteros y decimales -> usar Double
            return Double.class;
        } else {
            // Cualquier otra combinación -> String
            return String.class;
        }
    }

    // Método auxiliar para crear columna con tipo específico (SIN agregar a listas)
    @SuppressWarnings("unchecked")
    private void crearYAgregarColumna(Etiqueta etiqueta, Class<?> tipo, List<Object> valores) {
        if (tipo == Integer.class) {
            List<Integer> valoresInt = new ArrayList<>();
            for (Object valor : valores) {
                if (valor == null) {
                    valoresInt.add(null);
                } else if (valor instanceof Integer) {
                    valoresInt.add((Integer) valor);
                } else {
                    try {
                        valoresInt.add(Integer.valueOf(valor.toString()));
                    } catch (NumberFormatException e) {
                        valoresInt.add(null);
                    }
                }
            }
            agregarColumna(etiqueta, Integer.class, valoresInt);

        } else if (tipo == Double.class) {
            List<Double> valoresDouble = new ArrayList<>();
            for (Object valor : valores) {
                if (valor == null) {
                    valoresDouble.add(null);
                } else if (valor instanceof Double) {
                    valoresDouble.add((Double) valor);
                } else if (valor instanceof Float) {
                    valoresDouble.add(((Float) valor).doubleValue());
                } else if (valor instanceof Integer) {
                    valoresDouble.add(((Integer) valor).doubleValue());
                } else {
                    try {
                        valoresDouble.add(Double.valueOf(valor.toString()));
                    } catch (NumberFormatException e) {
                        valoresDouble.add(null);
                    }
                }
            }
            agregarColumna(etiqueta, Double.class, valoresDouble);

        } else if (tipo == Boolean.class) {
            List<Boolean> valoresBoolean = new ArrayList<>();
            for (Object valor : valores) {
                if (valor == null) {
                    valoresBoolean.add(null);
                } else if (valor instanceof Boolean) {
                    valoresBoolean.add((Boolean) valor);
                } else {
                    valoresBoolean.add(Boolean.valueOf(valor.toString()));
                }
            }
            agregarColumna(etiqueta, Boolean.class, valoresBoolean);

        } else {
            // String por defecto
            List<String> valoresString = new ArrayList<>();
            for (Object valor : valores) {
                valoresString.add(valor == null ? null : valor.toString());
            }
            Columna<String> nuevaColumna = new Columna<>(etiqueta, String.class);
            for (String valor : valoresString) {
                nuevaColumna.agregarCelda(new Celda<>(valor));
            }
            agregarColumna(etiqueta, String.class, valoresString);
        }

        // Generar etiquetas de filas solo la primera vez
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < valores.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }
    }

    // Métodos para agregar columnas

    public <T> void agregarColumna(Etiqueta etiquetaColumna, Class<T> tipo, List<T> valores) {
        if (!columnas.isEmpty() && valores.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Dimensiones no coinciden. Se esperaban %d filas, pero se recibieron %d",
                    cantFilas(), valores.size()));
        }

        Columna<T> nuevaColumna = new Columna<>(etiquetaColumna, tipo);
        for (T valor : valores) {
            nuevaColumna.agregarCelda(new Celda<>(valor));
        }

        columnas.add(nuevaColumna);
        etiquetasColumnas.add(etiquetaColumna);

        // Si es la primera columna, genera etiquetas de filas automáticamente
        if (etiquetasFilas.isEmpty()) {
            for (int i = 0; i < valores.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }
    }

    public <T> void agregarColumnaCeldas(Etiqueta etiquetaColumna, Class<T> tipo, List<Celda<T>> celdas) {
        if (!columnas.isEmpty() && celdas.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Dimensiones no coinciden. Se esperaban %d filas, pero se recibieron %d",
                    cantFilas(), celdas.size()));
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

    public <T> void agregarColumna(Columna<T> columna) {
        if (!columnas.isEmpty() && columna.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Dimensiones no coinciden. Se esperaban %d filas, pero se recibieron %d",
                    cantFilas(), columna.size()));
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

    public void agregarFila(Etiqueta etiquetaFila, List<Celda<?>> celdas) {
        if (celdas.size() != cantColumnas()) {
            throw new JandasException(String.format(
                    "Dimensiones no coinciden. Se esperaban %d columnas, pero se recibieron %d",
                    cantColumnas(), celdas.size()));
        }
        for (int i = 0; i < columnas.size(); i++) {
            Columna<Object> columna = (Columna<Object>) columnas.get(i);
            Celda<Object> celda = (Celda<Object>) celdas.get(i);
            columna.agregarCelda(celda);
        }
        etiquetasFilas.add(etiquetaFila);
    }

    private List<Etiqueta> generarEtiquetaFilas() {
        if (!columnas.isEmpty()) {
            int cantFilas = columnas.get(0).size();
            List<Etiqueta> etiquetas = new ArrayList<>();
            for (int i = 0; i < cantFilas; i++) {
                etiquetas.add(new EtiquetaInt(i));
            }
            return etiquetas;
        }
        return new ArrayList<>();
    }


    private int getIndex(Etiqueta etiqueta, List<Etiqueta> etiquetas) {
        for (int i = 0; i < etiquetas.size(); i++) {
            if (etiquetas.get(i).getValor().equals(etiqueta.getValor())) {
                return i;
            }
        }
        throw new JandasException("Etiqueta no encontrada: " + etiqueta.getValor());
    }

    public Fila getFila(Etiqueta etiquetaFila) {
        int indexFila = getIndex(etiquetaFila, etiquetasFilas);
        List<Celda<?>> celdasFilas = new ArrayList<>();
        for (Columna<?> col : columnas) {
            celdasFilas.add(col.getCeldas().get(indexFila));
        }
        return new Fila(etiquetaFila, celdasFilas, etiquetasColumnas);
    }

    public void setEtiquetasFilas(List<Etiqueta> nuevasEtiquetas) {
        if (nuevasEtiquetas.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Debe proporcionar exactamente %d etiquetas", cantFilas()));
        }
        this.etiquetasFilas = new ArrayList<>(nuevasEtiquetas);
    }

    public List<Columna<?>> getColumnas() {
        return new ArrayList<>(columnas); // copia defensiva
    }

    public Columna<?> getColumna(Etiqueta etiqueta) {
        int index = getIndex(etiqueta, etiquetasColumnas);
        return columnas.get(index);
    }

    public int cantColumnas() {
        return columnas.size();
    }

    public int cantFilas() {
        return columnas.isEmpty() ? 0 : columnas.get(0).size();
    }

    public List<Etiqueta> getEtiquetasColumnas() {
        return new ArrayList<>(etiquetasColumnas);
    }

    public List<Etiqueta> getEtiquetasFilas() {
        return new ArrayList<>(etiquetasFilas);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Tabla other = (Tabla) obj;
        return Objects.equals(columnas, other.columnas) &&
                Objects.equals(etiquetasFilas, other.etiquetasFilas) &&
                Objects.equals(etiquetasColumnas, other.etiquetasColumnas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnas, etiquetasFilas, etiquetasColumnas);
    }

    public void setValoresCelda(Etiqueta fila, Etiqueta columna, Object valor) {
        int idxfilas = etiquetasFilas.indexOf(fila);
        if (idxfilas == -1) {
            throw new JandasException("Etiqueta de fila no encontrada: " + fila.getValor());
        }

        for (Columna<?> col : columnas) {
            if (col.getEtiqueta().getValor().equals(columna.getValor())) {
                if (!col.getTipoDato().isInstance(valor)) {
                    throw new JandasException(String.format(
                            "Tipo incompatible. Se esperaba %s pero se recibió %s.",
                            col.getTipoDato().getSimpleName(),
                            valor == null ? "null" : valor.getClass().getSimpleName()
                    ));
                }
                Celda celda = col.getCeldas().get(idxfilas);
                celda.setValor(valor);  // Actualiza directamente la celda
                return;
            }
        }
        throw new JandasException("Etiqueta de columna no encontrada: " + columna.getValor());
    }

    public <T> void insertarColumnaDesdeColumna(Etiqueta nuevaEtiqueta, Etiqueta etiquetaColumnaOrigen) {

        Columna<?> columnaOrigen = getColumna(etiquetaColumnaOrigen);

        // Crear nueva columna con el mismo tipo que la columna origen
        Class<T> tipoColumna = (Class<T>) columnaOrigen.getTipoDato();
        Columna<T> nuevaColumna = new Columna<>(nuevaEtiqueta, tipoColumna);

        // Copiar todas las celdas de la columna origen
        for (Celda<?> celdaOrigen : columnaOrigen.getCeldas()) {
            T valor = (T) celdaOrigen.getValor();
            nuevaColumna.agregarCelda(new Celda<>(valor));
        }

        // Agregar la nueva columna a la tabla
        columnas.add(nuevaColumna);
        etiquetasColumnas.add(nuevaEtiqueta);
    }

    public <T> void insertarColumnaDesdeSecuencia(Etiqueta etiquetaColumna, Class<T> tipo, List<T> secuencia) {
        // Validar que la secuencia tenga la cantidad correcta de elementos
        if (!columnas.isEmpty() && secuencia.size() != cantFilas()) {
            throw new JandasException(String.format(
                    "Dimensiones no coinciden. Se esperaban %d elementos, pero se recibieron %d",
                    cantFilas(), secuencia.size()));
        }

        // Validar que el tipo sea permitido
        if (!tipo.equals(String.class) && !tipo.equals(Integer.class) && !tipo.equals(Double.class) && !tipo.equals(Boolean.class)) {
            throw new JandasException("Tipo no permitido. Solo se permiten: String, Integer, Double, Boolean");
        }

        // Si la tabla está vacía, crear etiquetas de filas automáticamente
        if (columnas.isEmpty()) {
            for (int i = 0; i < secuencia.size(); i++) {
                etiquetasFilas.add(new EtiquetaInt(i));
            }
        }

        // Crear nueva columna
        Columna<T> nuevaColumna = new Columna<>(etiquetaColumna, tipo);
        for (T valor : secuencia) {
            nuevaColumna.agregarCelda(new Celda<>(valor));
        }

        // Agregar columna a la tabla
        columnas.add(nuevaColumna);
        etiquetasColumnas.add(etiquetaColumna);
    }

    public void eliminarColumna(Etiqueta etiquetaColumna) {
        // Verificar que la tabla no esté vacía
        if (columnas.isEmpty()) {
            throw new JandasException("No se puede eliminar columna: la tabla está vacía");
        }

        // Buscar el índice de la columna a eliminar
        int indiceColumna = -1;
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            if (etiquetasColumnas.get(i).getValor().equals(etiquetaColumna.getValor())) {
                indiceColumna = i;
                break;
            }
        }

        // Verificar que la columna existe
        if (indiceColumna == -1) {
            throw new JandasException("Columna no encontrada: " + etiquetaColumna.getValor());
        }

        // Eliminar la columna y su etiqueta
        columnas.remove(indiceColumna);
        etiquetasColumnas.remove(indiceColumna);

        // Si eliminamos todas las columnas, también limpiamos las etiquetas de filas
        if (columnas.isEmpty()) {
            etiquetasFilas.clear();
        }
    }

    public void eliminarFila(Etiqueta etiquetaFila) {
        // Verificar que la tabla no esté vacía
        if (columnas.isEmpty() || etiquetasFilas.isEmpty()) {
            throw new JandasException("No se puede eliminar fila: la tabla está vacía");
        }

        // Buscar el índice de la fila a eliminar
        int indiceFila = -1;
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            if (etiquetasFilas.get(i).getValor().equals(etiquetaFila.getValor())) {
                indiceFila = i;
                break;
            }
        }

        // Verificar que la fila existe
        if (indiceFila == -1) {
            throw new JandasException("Fila no encontrada: " + etiquetaFila.getValor());
        }

        // Eliminar la celda correspondiente de cada columna
        for (Columna<?> columna : columnas) {
            columna.getCeldas().remove(indiceFila);
        }

        // Eliminar la etiqueta de la fila
        etiquetasFilas.remove(indiceFila);

        // Si eliminamos todas las filas, también limpiamos las etiquetas de filas
        if (etiquetasFilas.isEmpty()) {
            // Las columnas quedan vacías pero mantienen su estructura
            for (Columna<?> columna : columnas) {
                columna.getCeldas().clear();
            }
        }
    }
    /*public Tabla concatenacion(Tabla otra){
        if(cantColumnas() != otra.cantColumnas()){
            throw new JandasException("No se pueden concatenar ya que las columnas tienen distintos tamaños");
        }
        for(int i = 0; i < cantColumnas(); i++){
            if(etiquetasColumnas.get(i).getValor().equals(otra.etiquetasColumnas.get(i).getValor()) && columnas.get(i).getTipoDato().equals(otra.columnas.get(i).getTipoDato()){
                Fila fila = new Fila(otra.columnas.get(i).getCeldas().get(i));
            }
            throw new JandasException("No se puede realizar la concatenacion")
        }
        columnas.add(fila);
        Tabla tabla = new Tabla()
    }*/

}