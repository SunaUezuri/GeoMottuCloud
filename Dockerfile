
# Usamos uma imagem oficial do Maven com o JDK 21 para compilar o projeto.
FROM maven:3.9-eclipse-temurin-21 AS build

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# 1. Copia apenas o pom.xml primeiro.
COPY pom.xml .

# 2. Baixa todas as dependências do projeto.
RUN mvn dependency:go-offline

# 3. Copia o resto do código fonte do projeto.
COPY src ./src

# 4. Compila a aplicação e gera o JAR, pulando os testes.
RUN mvn package -DskipTests

# Imagem JRE mínima para este estágio.
FROM eclipse-temurin:21-jre-jammy AS unpack

WORKDIR /app

# Copia o JAR gerado no estágio anterior.
COPY --from=build /app/target/*.jar application.jar

# Usa a ferramenta do Spring Boot para extrair o JAR em camadas otimizadas.
RUN java -Djarmode=layertools -jar application.jar extract


# Imagem JRE limpa e mínima para manter a imagem final pequena e segura.
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Cria um usuário e grupo específicos para rodar a aplicação.
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Copia as camadas extraídas do estágio 'unpack', na ordem da que menos muda para a que mais muda.
COPY --from=unpack /app/dependencies/ ./
COPY --from=unpack /app/spring-boot-loader/ ./
COPY --from=unpack /app/snapshot-dependencies/ ./
COPY --from=unpack /app/application/ ./

# Define o usuário 'spring' como o dono do diretório da aplicação.
RUN chown -R spring:spring /app

# Muda para o usuário não-root.
USER spring

# Expõe a porta que a aplicação Spring Boot usa.
EXPOSE 8080

# Comando para iniciar a aplicação.
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
