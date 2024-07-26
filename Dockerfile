FROM amazoncorretto:17-alpine-jdk
MAINTAINER wandaymo
COPY target/caju-authorizer-0.0.1-SNAPSHOT.jar caju-authorizer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/caju-authorizer-0.0.1-SNAPSHOT.jar"]
