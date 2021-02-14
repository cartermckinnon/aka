FROM adoptopenjdk:15-jdk AS builder
RUN apt update && apt install maven -y
WORKDIR /app/
ADD lombok.config lombok.config
ADD pom.xml pom.xml
RUN mvn verify
ADD src/ src/
RUN mvn package -DskipTests

FROM adoptopenjdk:15-jre
WORKDIR /app/
COPY --from=builder /app/target/lib/ lib/
COPY --from=builder /app/target/aka-*.jar aka.jar
# configuration file is placed in its own directory so it can be easily replaced with a volume mount
ADD configuration.yaml configuration/configuration.yaml
CMD ["java", "-jar", "aka.jar", "server", "configuration/configuration.yaml"]