# syntax=docker/dockerfile:1

############################
# 1) build stage
############################
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace

# 依存解決をキャッシュさせるために pom.xml を先にコピー
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# ソースをコピーしてビルド
COPY src ./src
RUN mvn -B -DskipTests package

############################
# 2) runtime stage
############################
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# セキュリティ：非rootユーザーで実行
RUN addgroup -S app && adduser -S app -G app
USER app

# build stage の成果物（jar）だけをコピー
COPY --from=build /workspace/target/*.jar app.jar

# Spring Boot のデフォルト(8080)を公開
EXPOSE 8080

# コンテナ向けのJVM設定（メモリはECS側で調整する前提）
ENTRYPOINT ["java","-XX:+UseContainerSupport","-jar","/app/app.jar"]