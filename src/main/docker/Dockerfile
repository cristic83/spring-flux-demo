FROM openjdk:9.0.1-11-jre
COPY output/libs/* libs/
COPY build/libs/web-function-sample-1.0-SNAPSHOT.jar web-function-sample-1.0-SNAPSHOT.jar
EXPOSE 80
CMD java -cp "web-function-sample-1.0-SNAPSHOT.jar:libs/*" com.cristi.springflux.annotations.Server