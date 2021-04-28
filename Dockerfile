FROM  adoptopenjdk/openjdk11-openj9
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Djdk.tls.client.protocols=TLSv1.2","-jar","/app.jar"]
