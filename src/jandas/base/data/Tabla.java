package jandas.base.data;

import jandas.operaciones.filtros.*;
import jandas.visualizacion.*;
import jandas.base.etiquetas.EtiquetaString;
import jandas.excepciones.JandasException;
import jandas.base.etiquetas.Etiqueta;
import jandas.base.etiquetas.EtiquetaInt;
import jandas.operaciones.Muestreable;
import jandas.operaciones.concatenacion.ConcatenarTabla;
import jandas.operaciones.muestreo.MuestreadorTabla;
import jandas.operaciones.Ordenable;
import jandas.operaciones.ordenamiento.OrdenadorTabla;
import jandas.operaciones.ordenamiento.CriterioOrden;
import jandas.operaciones.ordenamiento.Orden;
import jandas.operaciones.Concatenable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tabla implements
        Ordenable,
        Muestreable,
        Concatenable {

    private List<Columna<?>> columnas;
    private List<Etiqueta> etiquetasFilas;
    private List<Etiqueta> etiquetasColumnas;

    // Constructor vacío
    public Tabla() {
        this.columnas = new ArrayList<>();
        this.etiquetasFilas = new ArrayList<>();
        this.etiquetasColumnas = new ArrayList<>();
    }

    // Constructor de tablas en base a una lista de columnas y etiquetas
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
     *
     * @param datos       Lista de strings con los datos en secuencia lineal
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

    // Procesamiento de datos

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
                Object valorCrudo = datos[i][j];
                if (valorCrudo == null) {
                    valoresColumna.add(null);
                } else if (valorCrudo instanceof String) {
                    valoresColumna.add(parsearValor((String) valorCrudo));
                } else {
                    valoresColumna.add(valorCrudo); // mantener tipo original
                }



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
    public Class<?> inferirTipoColumna(List<Object> valores) {
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
    public void crearYAgregarColumna(Etiqueta etiqueta, Class<?> tipo, List<Object> valores) {
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

    // Métodos para agregar columnas o filas

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

    // Manipulacion de columnas y filas

    public <T> void duplicarColumna(String colunueva, String coluorigen) {
        EtiquetaString etiquetaColumnaOrigen = new EtiquetaString(coluorigen);
        EtiquetaString nuevaEtiqueta = new EtiquetaString(colunueva);
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

    public <T> void insertarColumnaDesdeSecuencia(String valorColumna, Class<T> tipo, List<T> secuencia) {
        EtiquetaString etiquetaColumna = new EtiquetaString(valorColumna);
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

    public void eliminarColumna(String valor) {
        EtiquetaString etiqueta = new EtiquetaString(valor);
        // Verificar que la tabla no esté vacía
        if (columnas.isEmpty()) {
            throw new JandasException("No se puede eliminar columna: la tabla está vacía");
        }

        // Buscar el índice de la columna a eliminar
        int indiceColumna = -1;
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            if (etiquetasColumnas.get(i).getValor().equals(etiqueta.getValor())) {
                indiceColumna = i;
                break;
            }
        }

        // Verificar que la columna existe
        if (indiceColumna == -1) {
            throw new JandasException("Columna no encontrada: " + etiqueta.getValor());
        }

        // Eliminar la columna y su etiqueta
        columnas.remove(indiceColumna);
        etiquetasColumnas.remove(indiceColumna);

        // Si eliminamos todas las columnas, también limpiamos las etiquetas de filas
        if (columnas.isEmpty()) {
            etiquetasFilas.clear();
        }
    }

    public void eliminarFila(int valor) {
        EtiquetaInt etiqueta = new EtiquetaInt(valor);
        // Verificar que la tabla no esté vacía
        if (columnas.isEmpty() || etiquetasFilas.isEmpty()) {
            throw new JandasException("No se puede eliminar fila: la tabla está vacía");
        }

        // Buscar el índice de la fila a eliminar
        int indiceFila = -1;
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            if (etiquetasFilas.get(i).getValor().equals(etiqueta.getValor())) {
                indiceFila = i;
                break;
            }
        }

        // Verificar que la fila existe
        if (indiceFila == -1) {
            throw new JandasException("Fila no encontrada: " + etiqueta.getValor());
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

    public Object getCeldaColYFila(String nombreColumna, int indiceFila) {
        EtiquetaString etiquetaColumna = new EtiquetaString(nombreColumna);
        EtiquetaInt etiquetaFila = new EtiquetaInt(indiceFila);

        // Buscar el índice de la columna
        int indiceColumna = -1;
        for (int i = 0; i < etiquetasColumnas.size(); i++) {
            if (etiquetasColumnas.get(i).getValor().equals(etiquetaColumna.getValor())) {
                indiceColumna = i;
                break;
            }
        }

        // Verificar que la columna existe
        if (indiceColumna == -1) {
            throw new JandasException("Columna no encontrada: " + nombreColumna);
        }

        // Buscar el índice de la fila
        int indiceFilaReal = -1;
        for (int i = 0; i < etiquetasFilas.size(); i++) {
            if (etiquetasFilas.get(i).getValor().equals(etiquetaFila.getValor())) {
                indiceFilaReal = i;
                break;
            }
        }

        // Verificar que la fila existe
        if (indiceFilaReal == -1) {
            throw new JandasException("Fila no encontrada: " + indiceFila);
        }

        // Obtener la celda
        Columna<?> columna = columnas.get(indiceColumna);
        Celda<?> celda = columna.getCeldas().get(indiceFilaReal);

        return celda.getValor();
    }

    public void setValoresCelda(int valorfila, String valorcolumna, Object valor) {
        EtiquetaInt fila = new EtiquetaInt(valorfila);
        EtiquetaString columna = new EtiquetaString(valorcolumna);

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

    // operaciones

    // // Ordenar

    @Override
    public Tabla ordenar(String nombreColumna, Orden direccion) {
        Columna<?> columna = getColumna(nombreColumna);
        return OrdenadorTabla.ordenar(this, nombreColumna, direccion);
    }

    @Override
    public Tabla ordenar(String... criterios) {
        List<CriterioOrden> lista = new ArrayList<>();
        for (String criterio : criterios) {
            String[] partes = criterio.trim().split("\\s+"); // Ej: "Ciudad DESC"
            String nombre = partes[0];
            Orden orden = (partes.length > 1 && partes[1].equalsIgnoreCase("DESC"))
                    ? Orden.DESCENDENTE
                    : Orden.ASCENDENTE;
            lista.add(new CriterioOrden(new EtiquetaString(nombre), orden));
        }
        return this.ordenarPorCriterios(lista);
    }

    @Override
    public Tabla ordenarPorCriterios(List<CriterioOrden> criterios) {
        return OrdenadorTabla.ordenarPorCriterios(this, criterios);
    }
    // // Muestrear

    @Override
    public Tabla muestrear(int porcentaje) {
        return MuestreadorTabla.muestrear(this, porcentaje);
    }

    @Override
    public Tabla muestrear(int cantidadFilas, boolean exacto) {
        return MuestreadorTabla.muestrear(this, cantidadFilas, exacto);
    }

    @Override
    public Tabla muestrearEstratificado(String nombreColumna, int porcentaje) {
        return MuestreadorTabla.muestrearEstratificado(this, nombreColumna, porcentaje);
    }
    // // Concatenacion

    @Override
    public Tabla concatenacion(Tabla otra) {
        return ConcatenarTabla.concatenacion(this, otra);
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

    /**
     * RELLENA LOS NA CON EL TIPO DE DATO QUE LE PASEMOS Y MANTIENE EL TIPO DE DATO DE LA COLUMNA
     **/
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






