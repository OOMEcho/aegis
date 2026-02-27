FROM openjdk:8

LABEL maintainer="xuesong.lei <228389787@qq.com>"

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /aegis

ADD ./target/aegis-1.0.0.jar aegis-1.0.0.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "aegis-1.0.0.jar", "--spring.profiles.active=prod"]
