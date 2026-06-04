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

## セットアップ

1. Java 21 がインストールされていることを確認
2. リポジトリのルートで Maven を実行できる状態にする

