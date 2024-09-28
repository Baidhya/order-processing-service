FROM openjdk:17-alpine
VOLUME /tmp
COPY target/*.jar order-processor-service.jar
ENTRYPOINT ["java","-jar","/order-processor-service.jar"]
