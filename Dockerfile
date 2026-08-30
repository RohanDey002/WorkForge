# ==========================================
# BUILD STAGE
# ==========================================
FROM eclipse-temurin:26-jdk AS build

WORKDIR /app

# Copy the pom.xml, Maven wrapper, and .mvn directory
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Make the wrapper executable (crucial when building from Windows)
RUN chmod +x mvnw

# Copy the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# ==========================================
# RUNTIME STAGE
# ==========================================
FROM eclipse-temurin:26-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]