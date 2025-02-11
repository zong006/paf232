
# ---------------------------- STAGE 1 ----------------------------
    FROM maven:3.9.9-eclipse-temurin-23 AS compiler

    ARG COMPIILE_DIR=/code_folder
    
    WORKDIR ${COMPIILE_DIR}
    
    COPY bedandbreakfastapp/pom.xml .
    COPY bedandbreakfastapp/mvnw .
    COPY bedandbreakfastapp/mvnw.cmd .
    COPY bedandbreakfastapp/src src
    COPY bedandbreakfastapp/.mvn .mvn 
    
    RUN mvn package -Dmaven.test.skip=true
    
    # ---------------------------- STAGE 1 ----------------------------
    
    # ---------------------------- STAGE 2 ----------------------------
    
    FROM maven:3.9.9-eclipse-temurin-23
    
    ARG DEPLOY_DIR=/app
    
    WORKDIR ${DEPLOY_DIR}
    COPY --from=compiler /code_folder/target/assessment-0.0.1-SNAPSHOT.jar target/assessment.jar
    
    ENV SERVER_PORT=3000
    EXPOSE ${SERVER_PORT}
    
    # HEALTHCHECK --interval=10s --timeout=5s --start-period=3s --retries=3 \
    #    CMD curl http://localhost:${SERVER_PORT}/health || exit 1
    
    ENTRYPOINT java -jar target/assessment.jar
    
    # ---------------------------- STAGE 2 ----------------------------