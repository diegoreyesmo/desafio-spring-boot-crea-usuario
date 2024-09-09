# Desafío java spring boot

Para ejecutar el programa debe tener `Docker` instalado y ejecutar el siguiente comando para construir una imagen:

`$ sudo docker build -t my-spring-boot-app .`

luego ejecutar un contenedor a partir de la imagen anterior:

`$ sudo docker run -d -p 8080:8080 --name my-running-app my-spring-boot-app`

Para probar los servicios puede utilizar el `postman_collection` adjunto.