# STAGE 1: Build - Compilar la aplicación con Maven
FROM maven:3.9-eclipse-temurin-21 AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar solo el pom.xml para aprovechar el cache de capas de Docker
COPY ./pom.xml .

# Copiar e instalar la dependencia local 'libreria-1.0.0.jar'
COPY ./libreria-1.0.0.jar .

RUN mvn install:install-file \
    -Dfile=libreria-1.0.0.jar \
    -DgroupId=org.DDYCC \
    -DartifactId=libreria \
    -Dversion=1.0.0 \
    -Dpackaging=jar

# Descargar todas las dependencias del proyecto principal
RUN mvn dependency:go-offline

# Copiar el resto del código fuente del microservicio
COPY ./src ./src

# Compilar el proyecto y generar el .war
# Se omiten los tests para acelerar el proceso de build
RUN mvn clean package -DskipTests

# STAGE 2: Runtime - Preparar la imagen final con Tomcat
FROM tomcat:10.1-jdk21-temurin

# Eliminar la aplicación por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el .war generado en la etapa de build al directorio webapps de Tomcat
# Se renombra a user-service.war para asegurar un context path predecible
COPY --from=build /app/target/notification-service-1.0.war /usr/local/tomcat/webapps/notification-service.war

# Exponer el puerto 8080 que usa Tomcat
EXPOSE 8080

# Comando por defecto para iniciar el servidor Tomcat
CMD ["catalina.sh", "run"]
