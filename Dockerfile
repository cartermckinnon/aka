FROM adoptopenjdk:15-jre

WORKDIR /app/
ADD target/lib/ lib/
ADD target/url-alias-service-*.jar url-alias-service.jar
ADD configuration.yaml configuration.yaml

CMD ["java", "-cp", "lib/*:url-alias-service.jar", "mck.service.urlalias.UrlAliasServiceApplication", "server", "configuration.yaml"]