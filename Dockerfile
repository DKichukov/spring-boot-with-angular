# Build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Use cache mount to speed up Maven dependency downloads
RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests

# Production stage
FROM pmpapplication/java21-alpine AS production

# Create a non-root user to run the application
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE=target/crud-spring-project-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Stage 1: Build
FROM node:16 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --output-path=./dist/out --configuration production

# Stage 2: Serve
FROM nginx:alpine
COPY --from=builder /app/dist/out /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]