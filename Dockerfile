FROM openjdk:11-jre-slim

# Put the jar file in /opt directory
CMD mkdir /opt
COPY target/hl7mp-1.0-jar-with-dependencies.jar /opt/

WORKDIR /home

# defaut config file = conf/config.yaml
# pass your own when running container
# docker run -ti --rm -v "$PWD":/home hl7mp conf/config-app02.yaml

ENTRYPOINT ["/usr/bin/java", "-jar", "/opt/hl7mp-1.0-jar-with-dependencies.jar", "org.opikanoba.hl7mp.App", "-cfg"]
CMD ["conf/config.yaml"]

