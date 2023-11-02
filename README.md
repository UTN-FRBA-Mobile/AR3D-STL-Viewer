# AR3D-STL-Viewer
AR3D STL Viewer

Aplicación para visualizar objetos en realidad aumentada y ver como quedaría en un lugar determinado, antes de llevarlo a la impresora 3D.

### Para agregar un .obj a un mock http:

1. Descargar el [blender](https://www.blender.org/)

2. Importar el objeto de ejemplo de ARCORE de google

3. Importar el nuevo objeto y reducirle el tamaño al que tiene el objeto de ejemplo del paso 2.

4. Eliminar el objeto ejemplo y exportar el nuestro a .obj

5. copiar el contenido y guardar en el mock esa respuesta a un elemento del catálogo. Ej: GET /catalogo?name=Beer retorna el contenido que es el cuerpo del archivo .obj



### Para agregar un objeto al catálogo, seguir estos pasos:

1. Obtener la url de la imagen del objeto.

2. Agregar al mock /catalogo?page=N, el objeto a la página correspondiente con nombre del objeto y la url.
