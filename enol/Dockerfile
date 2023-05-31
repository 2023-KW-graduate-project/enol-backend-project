#FROM mysql:latest
#COPY map_data_1.sql /docker-entrypoint-initdb.d/

FROM amazoncorretto:11
COPY build/libs/enol-0.0.1-SNAPSHOT.jar mnol.jar
CMD ["java", "-jar", "mnol.jar"]
