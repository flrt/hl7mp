FROM openjdk:11

RUN apt update
RUN apt -y install maven

RUN mkdir /opt/hl7mp
RUN mkdir /home/hl7mp

WORKDIR /opt/hl7mp

RUN git clone https://github.com/flrt/hl7mp.git .
RUN mvn compile assembly:single

# defaut config file = conf/config.yaml
# pass your own when running container
# docker run -ti --rm -v "$PWD":/home/hl7mp hl7mp conf/config-app02.yaml

WORKDIR /home/hl7mp
ENTRYPOINT ["/usr/local/openjdk-11/bin/java", "-jar", "/opt/hl7mp/target/hl7mp-1.0-jar-with-dependencies.jar", "org.opikanoba.hl7mp.App", "-cfg"]
CMD ["conf/config.yaml"]

