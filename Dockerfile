FROM maven:3.8.8-openjdk-8 AS builder

WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:8-jre-slim

LABEL maintainer="xuesong.lei <228389787@qq.com>"

ENV TZ=Asia/Shanghai

RUN apt-get update && apt-get install -y tzdata \
    && ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /aegis

COPY --from=builder /build/target/aegis-1.0.0.jar /aegis/aegis-1.0.0.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "/aegis/aegis-1.0.0.jar", "--spring.profiles.active=prod"]
