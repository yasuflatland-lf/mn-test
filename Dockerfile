FROM adoptopenjdk/openjdk13-openj9:jdk-13.0.2_8_openj9-0.18.0-alpine-slim
COPY build/libs/mn-test-*-all.jar mn-test.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx256m", "-XX:+IdleTuningGcOnIdle", "-Xtune:virtualized", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "-jar", "mn-test.jar"]
