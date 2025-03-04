# Utiliza una imagen base con OpenJDK
FROM openjdk:11-jre-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR compilado desde tu proyecto al contenedor
COPY target/tienda-textil-0.0.1-SNAPSHOT.jar /app/tienda-textil.jar

# Exponer el puerto donde Spring Boot correrá (por defecto, 8080)
EXPOSE 8080

# Comando para ejecutar el archivo JAR
ENTRYPOINT ["java", "-jar", "/app/tienda-textil.jar"]