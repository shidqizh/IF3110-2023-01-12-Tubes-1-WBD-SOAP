FROM amazoncorretto:8

RUN yum update -y && yum install -y maven

WORKDIR /app

COPY . /app

RUN mvn clean package

EXPOSE 8001

ENTRYPOINT ["java","-jar","target/SOAP_API_WBD-1.0-SNAPSHOT-jar-with-dependencies.jar"]