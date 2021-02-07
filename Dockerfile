FROM adoptopenjdk:15-jre

WORKDIR /app/
ADD target/lib/ lib/
ADD target/url-alias-service-*.jar url-alias-service.jar

# configuration file is placed in its own directory so it can be easily replaced with a volume mount
ADD configuration.yaml configuration/configuration.yaml

CMD ["java", "-jar", "url-alias-service.jar", "server", "configuration/configuration.yaml"]