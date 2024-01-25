FROM maven:3.8.2-jdk-11
COPY src home/apiframework/src
COPY extent-test-output home/apiframework/index.html
COPY pom.xml home/apiframework/pom.xml
COPY src/test/resources/testrunner/testng.xml home/apiframework/testng.xml
WORKDIR home/apiframework
ENTRYPOINT mvn clean test