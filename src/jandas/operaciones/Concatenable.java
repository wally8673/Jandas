package jandas.operaciones;

import jandas.base.data.Tabla;

/**
 * Interfaz que define la operación de concatenación entre dos objetos de tipo Tabla.
 * <p>
 * Cualquier clase que implemente esta interfaz deberá proveer la implementación del método
 * {@code concatenacion} que permita unir dos tablas en una sola.
 * </p>
 */
public interface Concatenable {

   /**
    * Realiza la concatenación de la tabla actual con otra tabla especificada.
    * <p>
    * El resultado es una nueva tabla que contiene los datos de la tabla actual
    * y los datos de la tabla pasada como argumento.
    * </p>
    *
    * @param otra La tabla con la cual se desea concatenar la tabla actual.
    * @return Una nueva instancia de {@link Tabla} que contiene los datos concatenados.
    * @throws IllegalArgumentException si las tablas no son compatibles para concatenar
    * (por ejemplo, si tienen diferente número de columnas o etiquetas incompatibles).
    */
   Tabla concatenacion(Tabla otra);

}

