FROM nxest/native-image:0.0.5 AS builder
WORKDIR /workspace
ADD ./build/libs/plantuml-server-native.jar plantuml-server-native.jar
RUN apt-get update && apt-get install -y --no-install-recommends libfreetype-dev build-essential
#RUN ./gradlew bootJar
#RUN ./gradlew nativeCompile -Djava.awt.headless=true
# github action only support 7g memory
RUN native-image -jar plantuml-server-native.jar -J-Xmx7g

# runtime
FROM ubuntu:jammy
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        openjdk-17-jdk \
        fonts-noto-cjk \
        graphviz \
        && \
    rm -rf /var/lib/apt/lists/*
ENV JAVA_HOME=/usr/lib/jvm/$(ls /usr/lib/jvm/ |grep java-17-openjdk)
WORKDIR /deployments
COPY --from=builder /workspace/plantuml-server-native plantuml-server-native
EXPOSE 8080
CMD ["/deployments/plantuml-server-native"]
