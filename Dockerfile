FROM gradle:8.8-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean war --no-daemon

FROM tomcat:10.1-jdk21-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
RUN sed -i 's/port="8080"/port="10000"/' /usr/local/tomcat/conf/server.xml
COPY --from=build /app/build/libs/lau-tam-gioi.war /usr/local/tomcat/webapps/lau-tam-gioi.war
EXPOSE 10000
