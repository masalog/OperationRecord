# OperationRecord

## 概要

`OperationRecord` は Spring Boot アプリケーションで、運行記録を管理する REST API と、AWS SQS と LINE ボットを組み合わせたメッセージ駆動の入力処理を提供します。

## 技術スタック

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- MySQL
- AWS SDK for Java v2 (SQS)
- LINE Bot SDK
- Lombok
- Maven

## 主要機能

- `POST /operation-records`
  - 運行記録を登録
- `GET /operation-records/{id}`
  - ID で運行記録を取得
- `GET /operation-records`
  - 全件取得
- `DELETE /operation-records/{id}`
  - 運行記録を削除

- AWS SQS からのポーリング受信
- LINE ボットへの通知／返信
- MySQL を利用した永続化

## プロジェクト構成

- `src/main/java/com/example/OperationRecord`
  - `controller/` REST API コントローラー
  - `service/` ドメインサービス
  - `repository/` JPA リポジトリ
  - `dto/` 入出力 DTO
  - `domain/` ドメインモデル
  - `listener/` SQS メッセージリスナー
  - `config/` AWS / LINE クライアント設定

- `storage/`
  - `schema.sql` MySQL テーブル定義
  - `data.sql` 初期データ
  - `docker-compose.yaml` MySQL とアプリの Docker Compose 設定
  - `.env` 環境変数定義（機密情報が含まれるため取り扱い注意）

## セットアップ

1. Java 21 がインストールされていることを確認
2. リポジトリのルートで Maven を実行できる状態にする

### Maven でビルド

Windows:
```powershell
mvnw.cmd clean package
```

Unix/macOS:
```bash
./mvnw clean package
```

### アプリケーション起動

Windows:
```powershell
mvnw.cmd spring-boot:run
```

Unix/macOS:
```bash
./mvnw spring-boot:run
```

## Docker Compose による起動

`storage/docker-compose.yaml` を使うと、MySQL とアプリをコンテナで起動できます。

1. `storage/.env` に必要な環境変数を設定
2. `storage/docker-compose.yaml` を利用してコンテナを起動

```bash
cd storage
docker compose up -d
```

## 環境変数

`storage/.env` には以下のような設定が必要です。

- `SPRING_PROFILES_ACTIVE`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `AWS_REGION`
- `SQS_QUEUE_URL`
- `LINE_CHANNEL_ACCESS_TOKEN`
- `MYSQL_DATABASE`
- `MYSQL_USER`
- `MYSQL_PASSWORD`
- `MYSQL_ROOT_PASSWORD`
- `MYSQL_HOST_PORT`
- `APP_IMAGE_NAME`
- `APP_CONTAINER_NAME`
- `APP_PORT`

`aws.sqs.enabled=true` の場合、SQS と LINE の連携が有効になります。

## データベース

`storage/schema.sql` に `operation_records` テーブル定義が含まれています。
`storage/data.sql` にサンプルデータを用意しています。

## テスト

Maven でテストを実行できます。

```bash
mvnw.cmd test
```

## 注意事項

- `storage/.env` には機密情報（LINE アクセストークン、DB パスワードなど）が含まれています。公開リポジトリに含めないよう注意してください。
- `aws.sqs.enabled` が `true` の場合、AWS の認証情報が必要です。
- Base URL はデフォルトで `http://localhost:8080` です。
