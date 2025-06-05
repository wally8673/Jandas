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

    @Override
    public void visualizar(int maxFilas, int maxColumnas) {
        if (columnas.isEmpty()) {
            System.out.println("DataFrame vacío");
            return;
        }

        maxFilas = Math.min(maxFilas, cantFilas());
        maxColumnas = Math.min(maxColumnas, cantColumnas());

        System.out.println("Cantidad de filas: " + cantFilas());
        System.out.println("Cantidad de columnas: " + cantColumnas());

        imprimirHeaders(etiquetasColumnas.subList(0, maxColumnas));

        for (Etiqueta etiqueta : etiquetasFilas.subList(0, maxFilas)) {
            System.out.println(filaToString(getFila(etiqueta), maxFilas, maxColumnas));
        }
    }

    private void imprimirHeaders(List<Etiqueta> etiquetas) {
        StringBuilder separador = new StringBuilder(" | ");

        for (Etiqueta etiqueta : etiquetas) {
            separador.append(etiqueta);
            separador.append(" | ");
        }

        System.out.println(separador);
    }

    private void imprimirFila(List<FilaGenerica> filas, int maxFilas) {
        if (!filas.isEmpty()) {

            imprimirHeaders(filas.get(0).getEtiquetasColumnas());

            for (int i = 0; i < Math.min(maxFilas, filas.size()); i++) {
                System.out.println(filas.get(i));
            }
        }
    }