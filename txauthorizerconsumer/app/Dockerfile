# Use uma imagem base do OpenJDK
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho dentro do container
WORKDIR /app

# Copie o JAR compilado para o container
COPY txauthorizerconsumer-application/target/txauthorizerconsumer-application-0.0.1-SNAPSHOT.jar /app/app.jar

# Expõe a porta onde o aplicativo estará ouvindo
EXPOSE 8081

# Comando para rodar o aplicativo
CMD ["java", "-jar", "/app/app.jar"]
