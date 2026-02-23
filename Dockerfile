# 1. 빌드 스테이지: JDK 설치 및 빌드 환경 구성
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar -x test

# 2. 실행 스테이지
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENV TZ=Asia/Seoul

# 실행 시 'prod' 프로파일 활성화
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]