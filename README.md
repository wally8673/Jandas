# Jandas - Librería de Manipulación de Datos Tabulares para Java

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)

Una librería Java para manipulación y análisis de datos en formato tabular, inspirada en pandas pero diseñada específicamente para el ecosistema Java.

## 📊 Análisis de Requerimientos

### Objetivo y Alcance

### 🎯 Objetivo
Desarrollar una librería para el lenguaje Java que permita manipular y analizar datos en forma tabular (2 dimensiones), ofreciendo estructuras de datos y operaciones robustas que soporten la funcionalidad requerida y contemplen posibles extensiones futuras, minimizando el impacto ante modificaciones.

### 🔍 Alcance

- **Gestión de Estructuras Tabulares**: Creación, modificación y manipulación de datos en formato tabla
- **Operaciones de Acceso y Consulta**: Acceso indexado por filas, columnas y celdas individuales
- **Procesamiento de Datos**: Filtrado, ordenamiento, selección y concatenación de estructuras
- **Gestión de Archivos**: Importación y exportación en formato CSV
- **Visualización**: Presentación de datos en formato texto tabular por consola
- **Operaciones Avanzadas**: Imputación de valores faltantes, muestreo aleatorio, agrupamiento por una o más columnas y operaciones estadísticas.

## 🏗️ Descripción de Alto Nivel del Sistema

La librería Jandas estará compuesta por un núcleo de gestión de datos tabulares que permitirá la creación y manipulación de estructuras bidimensionales similares a DataFrames. El sistema incluirá un módulo de validación de tipos de datos que garantizará la integridad de la información almacenada, soportando tipos numéricos, booleanos y cadenas de texto, además de un valor especial NA para datos faltantes.

La arquitectura incluirá un sistema de etiquetado flexible que permitirá el uso de identificadores numéricos o de cadena para filas y columnas. Un módulo de acceso indexado proporcionará interfaces para la consulta eficiente de filas completas, columnas completas o celdas individuales.

El sistema incorporará capacidades de importación/exportación CSV con configuración personalizable de delimitadores y encabezados, así como un motor de visualización que presentará los datos en formato texto tabular con opciones de acotamiento configurables para estructuras grandes.

Las operaciones de manipulación incluirán funcionalidades de filtrado mediante consultas condicionales, ordenamiento multi-columna, selección parcial (slicing), concatenación de estructuras compatibles, y operaciones especializadas como imputación de valores faltantes, muestreo y agrupamiento para alicar operaciones estadísticas. El sistema garantizará la independencia de memoria mediante operaciones de copia profunda cuando sea necesario.

## ⚙️ Requerimientos Funcionales Más Relevantes

### 📊 Macro-requerimiento 1: Gestión de Información Básica
- **RF 1.1**: El sistema debe proporcionar información sobre cantidad de filas y columnas
- **RF 1.2**: El sistema debe gestionar etiquetas de filas y columnas (numéricas o de cadena)
- **RF 1.3**: El sistema debe validar y mantener tipos de datos por columna (numérico, booleano, cadena)
- **RF 1.4**: El sistema debe soportar valores faltantes (NA) en cualquier tipo de columna

### 🔍 Macro-requerimiento 2: Acceso y Manipulación de Datos
- **RF 2.1**: El sistema debe permitir acceso indexado a filas completas por etiqueta
- **RF 2.2**: El sistema debe permitir acceso indexado a columnas completas por etiqueta
- **RF 2.3**: El sistema debe permitir acceso a celdas individuales mediante etiquetas de fila y columna
- **RF 2.4**: El sistema debe permitir modificación directa de celdas con validación de tipo

### 🏗️ Macro-requerimiento 3: Operaciones de Estructura
- **RF 3.1**: El sistema debe permitir inserción de nuevas columnas desde otras columnas o secuencias
- **RF 3.2**: El sistema debe permitir eliminación de filas y columnas
- **RF 3.3**: El sistema debe implementar operaciones de selección parcial (head, tail, slicing)
- **RF 3.4**: El sistema debe soportar concatenación de estructuras compatibles

### 📁 Macro-requerimiento 4: Procesamiento de Archivos
- **RF 4.1**: El sistema debe importar datos desde archivos CSV con delimitadores configurables
- **RF 4.2**: El sistema debe exportar estructuras a formato CSV
- **RF 4.3**: El sistema debe manejar encabezados opcionales en archivos CSV

### 🧮 Macro-requerimiento 5: Operaciones Avanzadas
- **RF 5.1**: El sistema debe implementar filtrado mediante consultas condicionales con operadores lógicos
- **RF 5.2**: El sistema debe permitir ordenamiento multi-columna con criterios ascendente/descendente
- **RF 5.3**: El sistema debe soportar imputación de valores faltantes (NA)
- **RF 5.4**: El sistema debe implementar muestreo aleatorio por porcentaje
- **RF 5.5**: El sistema debe permitir agrupar y aplicar operaciones estadísticas

## 📋 Requerimientos No Funcionales

### 👤 Usabilidad
- **RNF-U1**: Los usuarios deben poder crear una estructura tabular básica en menos de 5 minutos de entrenamiento
- **RNF-U2**: El sistema debe proporcionar mensajes de error claros y descriptivos para operaciones inválidas

### 🌐 Portabilidad
- **RNF-P1**: El sistema debe ser compatible con Java 8 o versiones superiores
- **RNF-P2**: El sistema debe funcionar en Windows y Linux

### 🛡️ Robustez
- **RNF-R1**: Las operaciones de copia profunda deben garantizar independencia total de memoria
- **RNF-R2**: El sistema debe manejar archivos CSV malformados sin terminar abruptamente

### 🔧 Mantenibilidad
- **RNF-M1**: Las operaciones de filtrado deben ser extensibles para nuevos operadores de comparación

### ⚡ Performance
- **RNF-PE1**: La importación de archivos CSV debe procesar al menos 1,000 filas por segundo

### 🎯 Funcionalidad
- **RNF-F1**: El sistema debe incluir mecanismos de medición de tiempo de ejecución a la hora de leer archivos externos

---

## 👥 Autores

- **Walter Villalba** - [wally8673](https://github.com/wally8673)
- **Ignacio Figuera** - [nachongo](https://github.com/nachongo)
- **Agustin Rebechi** - [AgustinRebechi](https://github.com/AgustinRebechi) 