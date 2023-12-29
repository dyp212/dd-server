FROM seraphimalia/openjdk8-alpine-plus-fonts

ENV LC_ALL C.UTF-8
ENV LANG C.UTF-8
ENV TZ asia/Shanghai

VOLUME /rs
COPY target/*.jar app.jar

EXPOSE 60000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Duser.timezone=GMT+08","-Dfile.encoding=utf-8","/app.jar"]