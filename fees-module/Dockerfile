#
# Package stage
#
FROM openjdk:11-jre-slim
COPY /target/feesmodule-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8087
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]