# ai-ddd-devtrack

技術学習の目標、学習記録、資格試験の進捗を管理するための、AI coding agent と DDD のサンプルプロジェクトです。

このリポジトリには、小さな Spring Boot backend と、シンプルな React frontend が含まれています。

## 設計の Source Of Truth

`docs/plantuml/` 配下の PlantUML ファイルを source of truth とします。

Backend、database、API、frontend のコードを実装する前に、まず関連する PlantUML ファイルを確認してください。

## 現在のスコープ

実装済み:

- 初期 DDD 設計ドキュメントと PlantUML 図
- Learning Goals、Study Sessions、Certifications、Dashboard の Spring Boot REST API
- Flyway で管理する PostgreSQL schema
- pure Java の domain model から分離した JPA adapter
- 焦点を絞った domain unit test
- API を操作するためのシンプルな React/Vite UI

未実装:

- Testcontainers を使った十分な integration test coverage
- 認証または multi-user support

## 要件

- Java 21 以降
- Docker
- aqua 2.62.3 以降
- `aqua/aqua.yaml` で管理する Bun と Node.js

aqua 管理の tool をインストールします。

```bash
aqua install
bun install
```

`aqua/aqua-policy.yaml` に関する警告が aqua から出た場合は、内容を確認したうえで local に許可してください。

```bash
aqua policy allow aqua/aqua-policy.yaml
```

## Diff Prompt の生成

Git リポジトリ全体の差分から、commit message、PR title、branch name、PR 本文を生成するための prompt を作ります。Commit message と branch name は英語/ASCII の形式を保ち、PR 本文は日本語で生成するように指示します。

```bash
bun run diff2prompt
```

出力は `generated-prompt.txt` に書き込まれます。このコマンドは、build artifact と local tool output を除外し、デフォルトで staged、unstaged、untracked の各ファイルを含めます。

## 品質チェック

リポジトリ root から、設定済みの formatter、linter、type check を実行します。

```bash
bun run check
```

対応している format と autofix を適用します。

```bash
bun run check:fix
```

TypeScript と frontend files は `oxfmt` と `oxlint` で確認します。Java files は google-java-format を使う Spotless で整形し、Checkstyle で lint します。Backend の layer dependency は ArchUnit test で確認します。レビューでは、自動チェックで検出できる内容ではなく、設計、責務、命名、コメント、テスト観点を中心に確認します。

コーディング規約は [`docs/development/coding-standards.md`](docs/development/coding-standards.md) を参照してください。

Backend test は別コマンドで実行できます。

```bash
bun run test
```

## Frontend API 型の生成

PostgreSQL と backend を起動したうえで、Springdoc OpenAPI document から TypeScript API types を生成します。

```bash
bun run generate:api
```

生成されたファイルは `frontend/src/api/generated/schema.ts` に書き込まれます。

生成用の依存関係は `tools/openapi-codegen` workspace に分離しています。この workspace では `openapi-typescript` 用に TypeScript 5 を固定し、frontend 側は TypeScript 7 で typecheck します。

## GitHub Actions

設定済み workflow:

- `CI`: Ubuntu と Windows で frontend の Bun checks と backend の Gradle build/test を実行する
- `autofix.ci`: pull request に対して対応可能な formatter/autofix 変更を適用する
- `GHA static checks`: GitHub Actions に対して actionlint、zizmor、ghalint を実行する
- `Label PRs`: `.github/labeler.yml` に基づいて label を付与する
- `Auto Approve`: repository owner が作成した non-draft pull request を approve する
- `Renovate`: aqua 管理 tool を更新する

## 依存関係の更新

Dependabot は `.github/dependabot.yml` で次を対象に設定しています。

- frontend workspace を含む Bun workspace dependencies
- Backend Gradle dependencies と Gradle wrapper
- Docker Compose images
- GitHub Actions

Dependabot は GitHub-native service として動作し、public dependency のために repository secret は不要です。

Renovate は `.github/renovate.json` で aqua 管理 tool のみを対象に設定しています。これは `aqua/aqua.yaml` にある既存の aqua comment と対応しています。

## ローカル実行

PostgreSQL を起動します。

```bash
docker compose up -d
```

Backend を起動します。

```bash
cd backend
./gradlew bootRun
```

Windows PowerShell の場合:

```powershell
cd backend
.\gradlew.bat bootRun
```

Frontend を起動します。

```bash
cd frontend
bun run dev
```

開く URL:

- Frontend: http://localhost:5173
- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Swagger UI: http://localhost:8080/swagger-ui.html

## Backend Package

現在のサンプル package:

```text
io.github.example.devtrack
```

公開前に必要であれば、`example` を対象の GitHub user または organization に置き換えます。
