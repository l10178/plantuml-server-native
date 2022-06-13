FROM nxest/native-image:0.0.5 AS builder
WORKDIR /workspace
ADD ./src src/
ADD ./gradle gradle/
ADD ./build.gradle build.gradle
ADD ./settings.gradle settings.gradle
ADD ./gradlew gradlew
RUN apt-get update && apt-get install -y --no-install-recommends libfreetype-dev
RUN ./gradlew bootJar
#RUN native-image -jar build/libs/plantuml-server-native.jar

# runtime
FROM openjdk:17-slim-buster
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        fonts-noto-cjk \
        graphviz \
        && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /deployments
COPY --from=builder /workspace/build/libs/plantuml-server-native.jar plantuml-server-native.jar
EXPOSE 8080
CMD ["java","-jar","/deployments/plantuml-server-native.jar"]
