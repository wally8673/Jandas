# Jandas - Librería de Manipulación de Datos Tabulares para Java

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-En%20Desarrollo-yellow.svg)]()

Una librería Java para manipulación y análisis de datos en formato tabular, inspirada en pandas pero diseñada específicamente para el ecosistema Java.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Instalación](#instalación)
- [Uso Rápido](#uso-rápido)
- [Documentación](#documentación)
- [Contribuir](#contribuir)
- [Licencia](#licencia)

## 🚀 Características

- **Manipulación de datos tabulares** en 2 dimensiones
- **Soporte para múltiples tipos de datos**: numérico, booleano, cadena
- **Importación/Exportación CSV** con configuración flexible
- **Operaciones avanzadas**: filtrado, ordenamiento, concatenación
- **Acceso indexado** por filas, columnas y celdas
- **Gestión de valores faltantes** (NA)
- **Visualización** en formato texto
- **Sin dependencias externas** - Solo Java puro

## 📦 Instalación

```bash
# Clonar el repositorio
git clone https://github.com/tu-usuario/jandas.git

# Compilar el proyecto
cd jandas
javac -d bin src/**/*.java
```

## 🔧 Uso Rápido

```java
// Crear una estructura tabular
DataFrame df = new DataFrame();

// Cargar desde CSV
DataFrame df = DataFrame.fromCSV("datos.csv");

// Acceso a datos
df.getRow("fila1");          // Obtener fila completa
df.getColumn("columna1");    // Obtener columna completa
df.getCell("fila1", "col1"); // Obtener celda específica

// Filtrado
DataFrame filtrado = df.query("columna1 > 10 and columna2 = 'valor'");

// Ordenamiento
DataFrame ordenado = df.sort("columna1", true); // ascendente
```

---

## 📊 Análisis de Requerimientos

### Objetivo y Alcance

### 🎯 Objetivo
Desarrollar una librería para el lenguaje Java que permita manipular y analizar datos en forma tabular (2 dimensiones), ofreciendo estructuras de datos y operaciones robustas que soporten la funcionalidad requerida y contemplen posibles extensiones futuras, minimizando el impacto ante modificaciones.

### 🔍 Alcance

#### ✅ Funcionalidades Incluidas
- **Gestión de Estructuras Tabulares**: Creación, modificación y manipulación de datos en formato tabla
- **Operaciones de Acceso y Consulta**: Acceso indexado por filas, columnas y celdas individuales
- **Procesamiento de Datos**: Filtrado, ordenamiento, selección y concatenación de estructuras
- **Gestión de Archivos**: Importación y exportación en formato CSV
- **Visualización**: Presentación de datos en formato texto tabular
- **Operaciones Avanzadas**: Imputación de valores faltantes y muestreo aleatorio

#### ❌ Funcionalidades Excluidas
- Dependencias de librerías externas (salvo autorización previa)
- Optimización de rendimiento como prioridad principal
- Integración con bases de datos
- Soporte para formatos de archivo distintos a CSV en esta fase

## 🏗️ Descripción de Alto Nivel del Sistema

La librería Jandas estará compuesta por un núcleo de gestión de datos tabulares que permitirá la creación y manipulación de estructuras bidimensionales similares a DataFrames. El sistema incluirá un módulo de validación de tipos de datos que garantizará la integridad de la información almacenada, soportando tipos numéricos, booleanos y cadenas de texto, además de un valor especial NA para datos faltantes.

La arquitectura incluirá un sistema de etiquetado flexible que permitirá el uso de identificadores numéricos o de cadena para filas y columnas, con generación automática de etiquetas numéricas cuando no se especifiquen. Un módulo de acceso indexado proporcionará interfaces para la consulta eficiente de filas completas, columnas completas o celdas individuales.

El sistema incorporará capacidades de importación/exportación CSV con configuración personalizable de delimitadores y encabezados, así como un motor de visualización que presentará los datos en formato texto tabular con opciones de truncamiento configurables para estructuras grandes.

Las operaciones de manipulación incluirán funcionalidades de filtrado mediante consultas condicionales, ordenamiento multi-columna, selección parcial (slicing), concatenación de estructuras compatibles, y operaciones especializadas como imputación de valores faltantes y muestreo estadístico. El sistema garantizará la independencia de memoria mediante operaciones de copia profunda cuando sea necesario.

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

## 📋 Requerimientos No Funcionales

### 👤 Usabilidad
- **RNF-U1**: Los usuarios deben poder crear una estructura tabular básica en menos de 5 minutos de entrenamiento
- **RNF-U2**: El sistema debe proporcionar mensajes de error claros y descriptivos para operaciones inválidas
- **RNF-U3**: La documentación debe incluir ejemplos de código para cada operación principal

### 🌐 Portabilidad
- **RNF-P1**: El sistema debe ser compatible con Java 8 o versiones superiores
- **RNF-P2**: El 100% de las funcionalidades deben ejecutarse sin dependencias de librerías externas
- **RNF-P3**: El sistema debe funcionar en Windows, macOS y Linux

### 🛡️ Robustez
- **RNF-R1**: El sistema debe validar tipos de datos con una precisión del 100% para prevenir corrupción
- **RNF-R2**: Las operaciones de copia profunda deben garantizar independencia total de memoria
- **RNF-R3**: El sistema debe manejar archivos CSV malformados sin terminar abruptamente

### 🔧 Mantenibilidad
- **RNF-M1**: La arquitectura debe permitir agregar nuevos tipos de datos sin modificar código existente
- **RNF-M2**: Las operaciones de filtrado deben ser extensibles para nuevos operadores de comparación
- **RNF-M3**: El código debe mantener un nivel de cobertura de pruebas mínimo del 80%

### ⚡ Performance
- **RNF-PE1**: Las operaciones de acceso indexado deben completarse en menos de 100 milisegundos para estructuras de hasta 10,000 filas
- **RNF-PE2**: La importación de archivos CSV debe procesar al menos 1,000 filas por segundo
- **RNF-PE3**: Las operaciones de filtrado deben ejecutarse en tiempo lineal O(n) respecto al número de filas

### 🎯 Funcionalidad
- **RNF-F1**: El sistema debe incluir mecanismos de medición de tiempo de ejecución para operaciones principales
- **RNF-F2**: Las estructuras deben soportar un mínimo de 100,000 filas y 1,000 columnas
- **RNF-F3**: El sistema debe preservar la precisión numérica para operaciones con números decimales

---

## 👥 Autores

- **Agustin Rebechi** - [AgustinRebechi](https://github.com/AgustinRebechi) 
- **Walter Villalba** - [wally8673](https://github.com/wally8673)
- **Ignacio Figuera** - [nachongo](https://github.com/nachongo)