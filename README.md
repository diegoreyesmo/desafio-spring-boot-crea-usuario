# Desafío java spring boot

Para ejecutar el programa debe tener `Docker` instalado y ejecutar el siguiente comando para construir una imagen:

`$ sudo docker build -t my-spring-boot-app .`

luego ejecutar un contenedor a partir de la imagen anterior:

`$ sudo docker run -d -p 8080:8080 --name my-running-app my-spring-boot-app`

Para probar los servicios puede utilizar el `postman_collection` adjunto.

# Instrucciones para compilar y ejecutar la app directo desde una host (sin docker):

requisitos: tener Java 17 y Gradle 8.2 instalados en la máquina.

### Paso 1: Compilar y generar el JAR ejecutable

desde Linux:

```bash
./gradlew bootJar
```

o desde Windows:

```
gradlew.bat bootJar
```

Este comando crea un archivo `JAR` ejecutable dentro de la carpeta `build/libs` del proyecto.


### Paso 2: Ejecutar el archivo JAR


```bash
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

