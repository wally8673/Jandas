package jandas;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReader {
    public static void main(String[] args) {
            String nombreArchivo = "C:\\Users\\wvill\\Downloads\\IRIS.csv";
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nombreArchivo))) {
                String linea;
                while ((linea = bufferedReader.readLine()) != null) {
                    Columnas matriz= linea;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}

