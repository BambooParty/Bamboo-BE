FROM eclipse-temurin:21-jdk-alpine

# Step 2: JAR 파일 복사
ARG JAR_FILE=build/lib/BambooClub-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# Step 3: 포트 노출 (필요시 변경)
EXPOSE 8080

# Step 4: 실행 명령어 설정
ENTRYPOINT ["java","-jar","/app.jar"]
