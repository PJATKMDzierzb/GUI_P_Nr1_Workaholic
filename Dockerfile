FROM eclipse-temurin:26-jdk-jammy

COPY ./ /app

WORKDIR /app

ENTRYPOINT ["tail", "-f", "/dev/null"]
