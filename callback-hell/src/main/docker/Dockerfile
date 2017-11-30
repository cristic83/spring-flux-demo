FROM openjdk:9.0.1-11-jre
COPY output/libs/* libs/
COPY build/libs/callback-hell-1.0-SNAPSHOT.jar callback-hell-1.0-SNAPSHOT.jar
EXPOSE 80
CMD java -cp "callback-hell-1.0-SNAPSHOT.jar:libs/*" com.cristi.springflux.annotations.Server