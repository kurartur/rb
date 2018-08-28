FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD build/libs/rb-consumer*.jar target/app.jar
RUN sh -c 'touch target/app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar target/app.jar" ]
