#
# Package stage
#
FROM openjdk:11-jre-slim
COPY /target/legalsystem-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]