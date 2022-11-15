#
# Package stage
#
FROM openjdk:11-jre-slim
COPY /target/feesbatchreport-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]