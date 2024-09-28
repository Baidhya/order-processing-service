FROM openjdk:17-alpine
VOLUME /tmp
COPY target/*.jar order-processing-service.jar
ENTRYPOINT ["java","-jar","/order-processing-service.jar"]
