FROM maven:3-amazoncorretto-21

WORKDIR /app

ENV SENTRY_DSN=https://6fd629738eab583bc35dc54eb8ca7000@o4511181130235904.ingest.de.sentry.io/4511181137248336

COPY pom.xml .
RUN mvn --batch-mode exec:java || exit 0

COPY . .
RUN mvn --batch-mode compiler:compile

CMD ["mvn", "--batch-mode", "--offline", "exec:java"]