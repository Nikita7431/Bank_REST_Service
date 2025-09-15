FROM eclipse-temurin:23-jdk AS build

WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:23-jre

WORKDIR /app

COPY --from=build /app/target/EfMobileBankService-0.0.1-SNAPSHOT.jar BankRestApplication.jar
EXPOSE 8084

ENTRYPOINT ["java","-jar","BankRestApplication.jar"]
