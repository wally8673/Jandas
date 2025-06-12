package jandas.base.data;

import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.operaciones.Ordenable;
import jandas.operaciones.ordenamiento.OrdenadorTabla;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.TipoOrden;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tabla implements Ordenable  {

    private List<Columna<?>> columnas;
    private List<Etiqueta> etiquetasFilas;
    private List<Etiqueta> etiquetasColumnas;

    // Constructor vacío
    public Tabla() {
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
    }

    // Constructor tabla en base a una lista de columnas y etiquetas
    public Tabla(List<Etiqueta> etiquetasColumnas, List<Columna<?>> columnas) {
        this.columnas = new ArrayList<>(columnas);
        this.etiquetasColumnas = new ArrayList<>(etiquetasColumnas);
        this.etiquetasFilas = generarEtiquetaFilas();

    }

    // Constructor para matriz con encabezado
    public Tabla(Object[][] datos) {
        this.columnas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
        this.etiquetasFilas = generarEtiquetaFilas();

        validarDatos(datos);
        procesarMatriz(datos);
    }

    // Constrctor secuencia lineal
    /**
     * Constructor para crear tabla desde una secuencia lineal de strings
     * @param datos Lista de strings con los datos en secuencia lineal
     * @param numColumnas Número de columnas para organizar los datos
     */
    public Tabla(List<String> datos, int numColumnas) {
        this.columnas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
        this.etiquetasFilas = generarEtiquetaFilas();

        validarDatosLineales(datos, numColumnas);
        Object[][] matriz = construirMatriz(datos, numColumnas);
        procesarMatriz(matriz);
    }

    private void validarDatosLineales(List<String> datos, int numColumnas) {
        if (datos == null || datos.isEmpty()) {
            throw new JandasException("La lista de datos no puede ser null o vacía");
        }

        if (numColumnas <= 0) {
            throw new JandasException("El número de columnas debe ser mayor a 0");
        }

        if (datos.size() % numColumnas != 0) {
            throw new JandasException(String.format(
                    "La cantidad de datos (%d) no es divisible por el número de columnas (%d)",
                    datos.size(), numColumnas));
        }
    }

    private Object[][] construirMatriz(List<String> datos, int numColumnas) {
        int numFilas = datos.size() / numColumnas;
        Object[][] matriz = new Object[numFilas][numColumnas];

        for (int i = 0; i < datos.size(); i++) {
            int fila = i / numColumnas;
            int columna = i % numColumnas;
            matriz[fila][columna] = datos.get(i);
        }

        return matriz;
    }

    private void validarDatos(Object[][] datos) {
        if (datos == null || datos.length < 2) {
            throw new JandasException("Se necesita al menos una fila de encabezado y una de datos.");
        }

        int columnasEsperadas = datos[0].length;
        for (int i = 0; i < datos.length; i++) {
            if (datos[i].length != columnasEsperadas) {
                throw new JandasException("Todas las filas deben tener la misma cantidad de columnas.");
            }
        }
    }

    private void procesarMatriz(Object[][] datos) {
        int cantidadFilas = datos.length;
        int cantidadColumnas = datos[0].length;
        // Procesar datos por columna
        for (int j = 0; j < cantidadColumnas; j++) {
            // Crear etiqueta del encabezado
            Object encabezado = datos[0][j];
            Etiqueta etiqueta = crearEtiquetaDesdeObjeto(encabezado);

            // Recopilar valores de la columna (excluyendo encabezado)
            List<Object> valoresColumna = new ArrayList<>();
            for (int i = 1; i < cantidadFilas; i++) {
                valoresColumna.add(parsearValor(datos[i][j].toString()));
            }

            // Inferir el tipo de la columna y crearla directamente
            Class<?> tipoColumna = inferirTipoColumna(valoresColumna);
            crearYAgregarColumna(etiqueta, tipoColumna, valoresColumna);
        }

    }

    private Object parsearValor(String s) {
        if (s == null || s.trim().isEmpty()) return null;

        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e2) {
                if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) {
                    return Boolean.parseBoolean(s);
                }
            }
        }

        return s; // default: sigue siendo string
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

    // operaciones

    public Tabla ordenar(String nombreColumna) {
        return OrdenadorTabla.ordenar(this, nombreColumna);
    }

    public Tabla ordenar(String nombreColumna, TipoOrden direccion) {
        Columna<?> columna = getColumna(nombreColumna);
        return OrdenadorTabla.ordenar(this, nombreColumna, direccion);
    }

    public Tabla ordenarPorCriterios(List<CriterioOrden> criterios) {
        return OrdenadorTabla.ordenarPorCriterios(this, criterios);
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

    public Columna<?> getColumna(String nombreEtiqueta) {
        for (Columna<?> columna : columnas) {
            if (columna.getEtiqueta().getValor().equals(nombreEtiqueta)) {
                return columna;
            }
        }
        throw new JandasException("No se encontró una columna con la etiqueta: " + nombreEtiqueta);
    }

    public Columna<?> getColumna(int index) {
        if (index < 0 || index >= columnas.size()) {
            throw new JandasException("Índice de columna fuera de rango: " + index);
        }
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

}
