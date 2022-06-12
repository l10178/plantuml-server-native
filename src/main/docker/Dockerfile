FROM openjdk:17-slim-buster
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
        fonts-noto-cjk \
        graphviz \
        && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /deployments
COPY ./build/libs/plantuml-server-native.jar plantuml-server-native.jar
EXPOSE 8080
CMD ["java","-jar","/deployments/plantuml-server-native.jar"]
