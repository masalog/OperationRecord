# 例外処理一本化に伴うテスト方針

## 目的
- 例外処理の変更に合わせてテストも整備する
- 例外ハンドリングの振る舞いを明文化し、HTTP レスポンスを保証する
- 既存の正しい動作テストはそのまま残す

## 1. カバレッジ対象

### 1.1 `GlobalExceptionHandler` の振る舞い
- `BadRequestException` / `IllegalArgumentException` が発生した場合に HTTP 400 を返す
- `ResourceNotFoundException` が発生した場合に HTTP 404 を返す
- 想定外の `Exception` は HTTP 500 になる
- 返却 JSON に `timestamp`, `status`, `error`, `message`, `path` が含まれる

### 1.2 コントローラの例外伝播
- コントローラが例外を捕まえずに上位へ伝播させる設計であることを確認する
- `MockMvc` でコントローラ + `GlobalExceptionHandler` を組み合わせ、例外時のステータスとボディを検証する

### 1.3 ドメイン/サービスの例外発生点
- `OperationRecord#updateEndInfo` は不正状態で `BadRequestException` を投げる
- `OperationRecordServiceImpl#findById` は存在しない場合に `ResourceNotFoundException` を投げる
- `OperationRecordFormMapper` の入力検証は `BadRequestException`（または `IllegalArgumentException`）になる

## 2. 具体的なテスト追加方針

### 2.1 例外レスポンス確認テスト
- `OperationRecordControllerTest` に例外ケースを追加する
- `MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();` で例外ハンドラを適用する
- 例:
  - `service.findById(1L)` が `ResourceNotFoundException` を投げたときに `status().isNotFound()`
  - 400 系例外のときに `status().isBadRequest()`
  - レスポンス JSON に `$.status`, `$.error`, `$.message`, `$.path` が存在することを確認する

### 2.2 例外型ごとの分離
- `BadRequestException` は入力/ドメイン検証エラーを表すことをテストする
- `ResourceNotFoundException` は検索失敗を表すことをテストする
- 可能なら `IllegalArgumentException` も `400` にマップされることを確認する

### 2.3 既存テストの維持
- 既存の正常系 `POST/GET/DELETE` テストはそのまま残す
- 変更後も正常系の `status().isOk()` が通ることを確認する

### 2.4 ドメイン/マッパー単体テストの追加・修正
- `OperationRecordTest` の `BadRequestException` 発生テストはそのままで有効
- `OperationRecordFormMapper` を追加または修正している場合は、
  - フィールドが null/空文字のときに `BadRequestException` になる
  - 形式不正のときに `BadRequestException` になる
  - 数値以外や範囲外のときに `BadRequestException` になる

## 3. テスト設計上の注意点

- `BadRequestException` を使う設計なら、テストでも `BadRequestException` を直接期待できるようにする
- `IllegalArgumentException` を `400` で扱う場合は、どちらの例外を投げるかを明確にする
- `GlobalExceptionHandler` のテストは REST 例外レスポンスの振る舞いを確認するものにする
- 内部例外のメッセージをそのまま返す場合、テストで期待メッセージを固定しすぎると保守性が下がる
  - 必要でなければ `status` と `error` の確認に留める

## 4. まとめ
- まずは `GlobalExceptionHandler` の基本レスポンスをテストする
- 次に `Service` / `ドメイン` で例外が正しく投げられることをテストする
- 最後に `Controller` からのエラーレスポンスを統合的に検証する
- 既存の正常系テストは維持しつつ、例外ケースを追加する
