Este proyecto utiliza Maven para la gestión de librerías, por lo que antes de ejecutarlo es necesario comprobar que todas estén instaladas.

Importante: para utilizar los datasets proporcionados por el profesorado (no se han incluido en la entrega por su tamaño), es necesario añadirlos dentro de la carpeta "datasets/extra", de forma que se encuentren en "datasets/extra/small", "datasets/extra/medium" y "datasets/extra/large".

En nuestro caso, ya que hemos implementado la clase TreePrinter, tal como hablamos en clase, las opciones de exportar estado de las estructuras y de visualizar el estado de las estructuras lo que hacen es exportar imágenes del estado de la estructura seleccionada a la carpeta "out". Si aparece algún error al generar la imagen se recomienda comprobar que exista la carpeta en la que se está intentando generar la imagen (la misma recomendación aplica al hecho de exportar el dataset en formato json).

Si al exportar alguna imagen, ésta no tiene suficiente resolución como para poder leer el texto que hay dentro de los bloques, se puede cambiar la resolución de la imagen generada dentro de la clase TreePrinter.

Nota:
Para exportar la tabla de hash a imagen es necesario cambiar la dimensión del array (es una constante dentro de la clase HashTable):
Por defecto está en 20749 pero para generar la imagen se recomienda 20, de lo contrario el proceso de generar la imagen puede fallar porque no soporta generar imagen con muchas casillas.

IDE utilizado y versión: IntelliJ IDEA 2019.1.1
La configuración para compilar es: Application -> Main
JRE: 1.8
JDK 1.8.0_144s