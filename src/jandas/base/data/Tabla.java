package jandas.base.data;

import jandas.operaciones.filtros.*;
import jandas.visualizacion.*;
import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tabla{
    private VConsola visualizador;
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
        this.visualizador = new VConsola();

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
    //metodo para agregar filas
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


    //Metodo head()
    public Tabla head(int n) {
        if (n <= 0) {
            throw new JandasException("El número de filas debe ser positivo");
        }

        // Ajustar n si es mayor que el número total de filas
        n = Math.min(n, cantFilas());

        // Crear nuevas columnas con las primeras n filas
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        for (Columna<?> columnaOriginal : getColumnas()) {
            @SuppressWarnings("unchecked")
            Columna<Object> nuevaColumna = new Columna<>(
                    columnaOriginal.getEtiqueta(),
                    (Class<Object>) columnaOriginal.getTipoDato()
            );

            List<? extends Celda<?>> celdasOriginales = columnaOriginal.getCeldas();
            for (int i = 0; i < Math.min(n, celdasOriginales.size()); i++) {
                Celda<?> celdaOriginal = celdasOriginales.get(i);
                @SuppressWarnings("unchecked")
                Celda<Object> nuevaCelda = new Celda<>(celdaOriginal.getValor());
                nuevaColumna.agregarCelda(nuevaCelda);
            }
            nuevasColumnas.add(nuevaColumna);
        }

        // Crear nueva tabla con las etiquetas de columnas y las nuevas columnas
        Tabla resultado = new Tabla(getEtiquetasColumnas(), nuevasColumnas);

        // Establecer las etiquetas de filas correspondientes
        List<Etiqueta> nuevasEtiquetasFilas = getEtiquetasFilas().subList(0, n);
        resultado.setEtiquetasFilas(nuevasEtiquetasFilas);

        return resultado;
    }

    //Metodo tail()
    public Tabla tail(int n) {
        if (n <= 0) {
            throw new JandasException("El número de filas debe ser positivo");
        }

        // Ajustar n si es mayor que el número total de filas
        n = Math.min(n, cantFilas());

        // Calcular el índice de inicio
        int inicio = cantFilas() - n;

        // Crear nuevas columnas con las últimas n filas
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        for (Columna<?> columnaOriginal : getColumnas()) {
            @SuppressWarnings("unchecked")
            Columna<Object> nuevaColumna = new Columna<>(
                    columnaOriginal.getEtiqueta(),
                    (Class<Object>) columnaOriginal.getTipoDato()
            );

            List<? extends Celda<?>> celdasOriginales = columnaOriginal.getCeldas();
            for (int i = inicio; i < cantFilas(); i++) {
                Celda<?> celdaOriginal = celdasOriginales.get(i);
                @SuppressWarnings("unchecked")
                Celda<Object> nuevaCelda = new Celda<>(celdaOriginal.getValor());
                nuevaColumna.agregarCelda(nuevaCelda);
            }
            nuevasColumnas.add(nuevaColumna);
        }

        // Crear nueva tabla con las etiquetas de columnas y las nuevas columnas
        Tabla resultado = new Tabla(getEtiquetasColumnas(), nuevasColumnas);

        // Establecer las etiquetas de filas correspondientes
        List<Etiqueta> nuevasEtiquetasFilas = getEtiquetasFilas().subList(inicio, cantFilas());
        resultado.setEtiquetasFilas(nuevasEtiquetasFilas);

        return resultado;
    }

    //FILTRADO
    public Tabla filtrar(Condicion condicion) {
        List<Columna<?>> nuevasColumnas = new ArrayList<>();
        List<Etiqueta> nuevasEtiquetasFilas = new ArrayList<>();

        // Crear nuevas columnas vacías con sus tipos correctos
        for (Columna<?> columnaOriginal : columnas) {
            Columna<?> nuevaColumna = new Columna<>(
                    columnaOriginal.getEtiqueta(),
                    columnaOriginal.getTipoDato()
            );
            nuevasColumnas.add(nuevaColumna);
        }

        // Evaluar cada fila y agregar las que cumplen la condición
        for (int i = 0; i < cantFilas(); i++) {
            Fila fila = getFila(etiquetasFilas.get(i));
            if (condicion.evaluar(fila)) {
                // Agregar la fila a las nuevas columnas
                for (int j = 0; j < cantColumnas(); j++) {
                    Celda<?> celdaOriginal = fila.getCeldasFila().get(j);
                    Columna columna = nuevasColumnas.get(j);
                    columna.agregarCelda(new Celda<>(celdaOriginal.getValor()));
                }
                nuevasEtiquetasFilas.add(etiquetasFilas.get(i));
            }
        }

        Tabla resultado = new Tabla(etiquetasColumnas, nuevasColumnas);
        resultado.setEtiquetasFilas(nuevasEtiquetasFilas);
        return resultado;
    }


    //METODO COPIA PROFUNDA
    public Tabla copiar() {
        // Crear nuevas listas para la copia
        List<Columna<?>> columnasCopiadas = new ArrayList<>();
        List<Etiqueta> etiquetasColumnasCopiadas = new ArrayList<>();
        List<Etiqueta> etiquetasFilasCopiadas = new ArrayList<>();

        // Copiar etiquetas de columnas
        for (Etiqueta etiqueta : etiquetasColumnas) {
            if (etiqueta instanceof EtiquetaString) {
                etiquetasColumnasCopiadas.add(new EtiquetaString(etiqueta.getValor().toString()));
            } else if (etiqueta instanceof EtiquetaInt) {
                etiquetasColumnasCopiadas.add(new EtiquetaInt((Integer) etiqueta.getValor()));
            }
        }

        // Copiar etiquetas de filas
        for (Etiqueta etiqueta : etiquetasFilas) {
            if (etiqueta instanceof EtiquetaString) {
                etiquetasFilasCopiadas.add(new EtiquetaString(etiqueta.getValor().toString()));
            } else if (etiqueta instanceof EtiquetaInt) {
                etiquetasFilasCopiadas.add(new EtiquetaInt((Integer) etiqueta.getValor()));
            }
        }

        // Copiar cada columna
        for (Columna<?> columnaOriginal : columnas) {
            columnasCopiadas.add(copiarColumna(columnaOriginal));
        }

        // Crear nueva tabla con las copias
        Tabla tablaCopia = new Tabla(etiquetasColumnasCopiadas, columnasCopiadas);
        tablaCopia.setEtiquetasFilas(etiquetasFilasCopiadas);

        return tablaCopia;
    }

    private <T> Columna<T> copiarColumna(Columna<T> columnaOriginal) {
        Columna<T> columnaCopia = new Columna<>(
                copiarEtiqueta(columnaOriginal.getEtiqueta()),
                columnaOriginal.getTipoDato()
        );

        // Copiar cada celda de la columna
        for (Celda<T> celdaOriginal : columnaOriginal.getCeldas()) {
            columnaCopia.agregarCelda(copiarCelda(celdaOriginal));
        }

        return columnaCopia;
    }

    private <T> Celda<T> copiarCelda(Celda<T> celdaOriginal) {
        // Crear nueva celda con el valor de la original
        return new Celda<>(celdaOriginal.getValor());
    }

    private Etiqueta copiarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta instanceof EtiquetaString) {
            return new EtiquetaString(etiqueta.getValor().toString());
        } else if (etiqueta instanceof EtiquetaInt) {
            return new EtiquetaInt((Integer) etiqueta.getValor());
        }
        throw new JandasException("Tipo de etiqueta no soportado");
    }


    //IMPUTAR
    public <T> void imputarColumna(Etiqueta etiquetaColumna, T valorImputacion) {
        Columna<?> columna = getColumna(etiquetaColumna);

        if (valorImputacion != null &&
                !columna.getTipoDato().isAssignableFrom(valorImputacion.getClass())) {
            throw new JandasException(String.format(
                    "Tipo incompatible. La columna es de tipo %s pero el valor de imputación es %s",
                    columna.getTipoDato().getSimpleName(),
                    valorImputacion.getClass().getSimpleName()));
        }

        @SuppressWarnings("unchecked")
        Columna<T> columnaTyped = (Columna<T>) columna;

        for (int i = 0; i < columnaTyped.size(); i++) {
            Celda<T> celda = columnaTyped.getCelda(i);
            if (celda.esNA()) {
                celda.setValor(valorImputacion);
            }
        }
    }

    /** RELLENA LOS NA CON EL TIPO DE DATO QUE LE PASEMOS Y MANTIENE EL TIPO DE DATO DE LA COLUMNA**/
    public void imputarDefault() {
        for (Columna<?> columna : columnas) {
            Class<?> tipo = columna.getTipoDato();

            if (tipo == Integer.class) {
                imputarColumna(columna.getEtiqueta(), 0);
            } else if (tipo == Double.class) {
                imputarColumna(columna.getEtiqueta(), 0.0);
            } else if (tipo == String.class) {
                imputarColumna(columna.getEtiqueta(), "");
            } else if (tipo == Boolean.class) {
                imputarColumna(columna.getEtiqueta(), false);
            }
        }
    }

}




