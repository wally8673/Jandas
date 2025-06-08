package jandas;
import java.io.*;
import java.util.*;

public class CsvReader implements LeerArchivos{
    private char delimitador;
    private boolean tieneEncabezado;

    public CsvReader(char delimitador, boolean tieneEncabezado) {
        this.delimitador = delimitador;
        this.tieneEncabezado = tieneEncabezado;
    }

    @Override
    public Dataframe leer(String rutaArchivo) throws IOException {
        List<Columna<?>> columnas = new ArrayList<>();
        List<Etiqueta<?>> etiquetasFilas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            List<String> nombresColumnas = new ArrayList<>();

            if (tieneEncabezado && (linea = br.readLine()) != null) {
                String[] partes = linea.split(Character.toString(delimitador));
                for (String nombre : partes) {
                    nombresColumnas.add(nombre);
                }
                for (String nombre : nombresColumnas) {
                    columnas.add(new Columna<>(new Etiqueta<>(nombre), new ArrayList<>()));
                }
            }

            int fila = 0;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(Character.toString(delimitador), -1);
                if (!tieneEncabezado && columnas.isEmpty()) {
                    for (int i = 0; i < partes.length; i++) {
                        columnas.add(new Columna<>(new Etiqueta<>("Col" + i), new ArrayList<>()));
                    }
                }
                etiquetasFilas.add(new Etiqueta<>(fila));
                for (int i = 0; i < partes.length; i++) {
                    String dato = partes[i].trim();
                    Celda<Object> celda;
                    if (dato.equals("") || dato.equalsIgnoreCase("NA")) {
                        celda = new Celda<>();
                    } else {
                        celda = new Celda<>(inferirTipo(dato));
                    }
                    ((Columna) columnas.get(i)).agregarCelda(celda);
                }
                fila++;
            }
        }

        return new Dataframe(new ArrayList<>(columnas), etiquetasFilas);
    }

    private static Object inferirTipo(String valor) {
        try {
            if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false")) {
                return Boolean.parseBoolean(valor);
            } else if (valor.contains(".")) {
                return Double.parseDouble(valor);
            } else {
                return Integer.parseInt(valor);
            }
        } catch (NumberFormatException e) {
            return valor;
        }
    }
}





