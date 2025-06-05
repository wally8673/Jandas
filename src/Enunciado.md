Enunciado del problema
Se desea construir una librería que permita manipular y analizar datos en forma tabular (2
dimensiones) para el lenguaje Java. Deberá ofrecer estructuras de datos y operaciones que
soporten la funcionalidad solicitada, como así también contemple posibles extensiones
futuras, minimizando el impacto ante alguna modificación.
En principio no es necesario hacer foco en la eficiencia de las operaciones, pero sería
deseable disponer también de algún mecanismo que nos permita cuantificar, al menos en
tiempo, el costo de su ejecución.
Es posible apoyarse en estructuras existentes de forma nativa en el lenguaje Java para
construir las estructuras de datos, pero no se debe depender de librerías externas. En caso
de necesitar consumir alguna librería externa, se debe consultar previamente si es viable su
aplicación.
Funcionalidad principal de la librería
Información básica
Se debe poder conocer la siguiente información sobre una instancia dada:
● Cantidad de filas
● Cantidad de columnas
● Etiquetas de filas
● Etiquetas de columnas
● Tipos de datos de columnas
De esto se desprende que cada columna tiene un tipo de dato asociado, el cual debe
validarse al momento de generación o modificación. Por el momento, los tipos de datos
soportados para una columna son:
● Numérico (entero, real, etc)
● Booleano
● Cadena
Nota: Debe existir un valor especial que hace referencia a valor faltante (NA), el cual puede
asignarse a cualquier celda sin importar el tipo de dato de la columna.
Acceso indexado
Proveer acceso indexado a nivel de fila y columna. Esto permitirá que podamos acceder a:
● una fila completa si se selecciona la etiqueta de la fila
● una columna completa si se selecciona la etiqueta de la columna
● una celda si se especifican etiquetas de fila y columna simultáneamente
Una etiqueta (label) puede ser en formato numérico entero o cadena. Si no se especifican
etiquetas en una estructura, se inicializan como secuencia de números enteros iniciada en
0.
8
Formatos de carga/descarga
Soportar la lectura y escritura entre estructuras en memoria y el formato CSV en disco. Se
debe poder establecer el carácter delimitador de columnas y si se usa encabezado
(etiquetas de las columnas).
Visualización
Presentar de forma simple y sencilla en formato texto la información en forma de tabla. Se
pueden definir tamaños máximos configurables para cortar la salida en caso de estructuras
grandes. Por ejemplo, mostrar un máximo de columnas o filas, y mostrar un máximo de
caracteres por cada celda.
Generación y modificación
Una estructura tabular puede generarse de la siguiente forma:
a) Desde la carga de un formato compatible en disco (CSV)
b) A través de copia profunda de otra estructura del mismo tipo
c) Desde una estructura de dos dimensiones nativa de Java
d) Desde una secuencia lineal nativa de Java
Una estructura tabular puede modificarse de la siguiente forma:
a) Accediendo directo a una celda y asignando un nuevo valor
b) Insertando una columna nueva a partir de otra columna (con misma cantidad de
elementos que filas)
c) Insertando una columna nueva a partir de una secuencia lineal nativa de Java (con
misma cantidad de elementos que filas)
d) Eliminando una columna
e) Eliminando una fila
Selección
Permitir la selección parcial de la estructura tabular a través de una lista de etiquetas de
cada índice. Esta operación no genera una nueva estructura, sino una vista reducida de la
original (slicing).  
Por ejemplo, se deberían poder elegir ciertas filas y columnas para establecer una selección
o vista de la estructura original.
Casos especiales:
● head(x): Devolver las primeras x filas
● tail(x): Devolver las últimas x filas
Filtrado
Permitir la selección parcial de la estructura tabular a través de un filtro aplicado a valores
de las celdas (query). Este filtro se puede componer de uno o más comparadores sobre
cierta columna (<, >, =) que se combinan con operadores lógicos (and, or, not). Así es
posible generar filtros del estilo “columna1 > 3 and columna2 = Verdadero”, filtrando así
aquellas filas donde las celdas cumplen aquella condición.
9
Esta operación devuelve una selección o vista de la estructura original.
Copia independiente
Permitir la copia profunda de los elementos de la estructura para generar una nueva con
mismos valores, pero independiente de la estructura original en memoria.
Concatenación
Permitir generar una nueva estructura tabular a partir de la concatenación de dos
estructuras existentes, creando así una nueva combinando las filas de la primera y luego las
filas de la segunda. Esta operación es válida si las columnas de ambas estructuras
coinciden, tanto en cantidad de columnas como también orden, tipo de datos y etiquetas
asociadas. Las etiquetas de las filas son generadas automáticamente.
Esta operación debe utilizar la copia de las estructuras originales.
Ordenamiento
Permitir ordenar las filas de la estructura según un criterio (ascendente o descendente)
sobre una o más columnas. Si se opta por ordenar por más de una columna, se toma el
orden ingresado de las columnas como precedencia para ordenar las filas.
Imputación
Incorporar la posibilidad de modificar (rellenar) las celdas con valores faltantes (celdas con
NA) con cierto valor literal indicado.
Muestreo
Ofrecer una selección aleatoria de filas según un porcentaje del total de la estructura.
10
Funcionalidad opcional de la librería
A continuación se presentan características deseables de la librería que no son obligatorias
en esta entrega pero sería interesante disponerlas.
Agregación
Incorporar la posibilidad de dividir las filas en diferentes grupos (agrupamiento o groupby)
según una o más columnas. Luego aplicar a estos grupos una operación de sumarización
estadística y devolver una nueva estructura donde:
● la etiqueta de las filas sea el nombre del grupo (si se dividió por más de una
columna, se pueden separar por comas)
● las columnas sean sólo aquellas que no son parte del grupo y con tipo numérico
● las celdas tengan el valor de la operación de sumarización aplicado al grupo
Por el momento, las operaciones disponibles de sumarización son:
● suma
● máximo
● mínimo
● cuenta
● media
● varianza
● desvío estándar