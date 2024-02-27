FROM amazoncorretto:21-alpine AS BUILDER
# Edit jar-file name if it's needed
ENV JAR_NAME=springboot-fullstack-1.0.0.jar
COPY build/libs/${JAR_NAME} app.jar

    # Extracting jar-file
RUN java -Djarmode=layertools -jar ./app.jar extract && \
    # Analyzing all dependencies
    jdeps --multi-release 21 --class-path 'dependencies/BOOT-INF/lib/*' --ignore-missing-deps --recursive  \
    --print-module-deps ./app.jar > jre-deps.info && \
    # Required by jlink for --strip-debug to work \
    apk add --no-cache binutils && \
    # Creating custom JRE and adding `jdk.crypto.ec` module manually
    jlink --strip-debug --no-man-pages --compress=zip-0 --no-header-files --add-modules jdk.crypto.ec,$(cat jre-deps.info) --output /custom-jre

# Main app image
FROM alpine
ENV JAVA_HOME=/custom-jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy custom JRE from the base image
COPY --from=BUILDER /custom-jre $JAVA_HOME

# Copy jar-file from the base image
COPY --from=BUILDER /app.jar /app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
