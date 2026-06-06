# 例外処理の一本化方針

## 目的
- REST API の例外処理をアプリ全体で統一する
- コントローラは正常系の処理に集中し、例外応答は一箇所で管理する
- HTTP ステータスとエラーレスポンス形式を一貫化する

## 対象クラス

### 1. `OperationRecordFormMapper`
- 役割: リクエストフォームの文字列検証と変換
- 変更内容:
  - `IllegalArgumentException` を `BadRequestException` に置き換える
  - `null` / 空文字 / フォーマット不正 / 数値不正 を `BadRequestException` で表現する
- 期待効果:
  - 入力不正はすべて HTTP 400 で返せる
  - グローバルハンドラが例外をまとめて処理できる

### 2. `OperationRecord` (ドメイン)
- 役割: 業務ルールの検証
- 変更内容:
  - `updateEndInfo` のルール違反を `BadRequestException` に置き換える
- 期待効果:
  - 業務ルール違反も API レベルで `400 Bad Request` として統一できる

### 3. `OperationRecordServiceImpl`
- 役割: 永続化アクセスと存在チェック
- 変更内容:
  - `findById(Long id)` が見つからない場合に `RuntimeException` ではなく `ResourceNotFoundException` を投げる
- 期待効果:
  - リソース未発見を `404 Not Found` として明確に扱える

### 4. 追加: `exception` パッケージ
- 追加するクラス:
  - `BadRequestException`
  - `ResourceNotFoundException`
  - `ErrorResponse`
- 役割:
  - 例外の意味を表現するカスタム例外を用意する
  - API のエラーレスポンス形式を共通化する

### 5. 追加: `GlobalExceptionHandler`
- 追加するクラス:
  - `GlobalExceptionHandler` (`@RestControllerAdvice`)
- 例外マッピング例:
  - `BadRequestException` / `IllegalArgumentException` → HTTP 400
  - `ResourceNotFoundException` → HTTP 404
  - `Exception` → HTTP 500
- 返却形式:
  - `ErrorResponse` を返す
  - `timestamp`, `status`, `error`, `message`, `path` などを含める

### 6. `OperationRecordController`
- 役割: API エンドポイント処理
- 変更内容:
  - `try/catch` を極力削除し、例外をそのまま上位へ伝播させる
- 期待効果:
  - コントローラは正常系処理だけに集中できる
  - 例外応答は `GlobalExceptionHandler` が一元管理する

## 全体の流れ
1. `BadRequestException` は入力検証・業務検証の失敗を表す
2. `ResourceNotFoundException` はデータ未発見を表す
3. 例外処理の実装は `@RestControllerAdvice` で一箇所に集約する
4. クライアントへのレスポンス形式は `ErrorResponse` で統一する
5. コントローラは例外処理を記述せず、正常系ロジックに専念する

## 注意点
- 既存の `IllegalArgumentException` をすべて置き換える必要はないが、API の振る舞いを明確にするためには `BadRequestException` を優先する
- 内部的な `RuntimeException` や `NullPointerException` は最終ハンドラで `500` として扱う
- 入力検証と業務ルール検証のどちらも `400` で返す場合は、メッセージをわかりやすくする

## 期待効果
- 例外の振る舞いが明確になる
- エラー時のレスポンス構造が統一される
- テスト・デバッグがしやすくなる
- コントローラの責務がシンプルになる
