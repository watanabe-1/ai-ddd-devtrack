# 概要

`ai-ddd-devtrack` は、技術学習と資格試験準備を管理するアプリケーションです。

このアプリケーションには、次の 2 つの目的があります。

- 個人の学習管理・資格試験準備トラッカーとして役に立つこと。
- DDD と Spring Boot を使った AI 支援開発の公開サンプルとして提示できること。

PlantUML を設計の source of truth とします。この Markdown ドキュメントは、人間がレビューしやすいように現在の設計判断を説明するものであり、PlantUML ファイルと整合している必要があります。

# 要件

初期スコープの機能要件は次のとおりです。

- active な Learning Goal を作成、更新、完了、照会できる。
- Study Session を記録、修正できる。
- 学習履歴を閲覧できる。
- 学習時間を集計できる。
- Certification を登録できる。
- 試験予定を設定できる。
- 試験結果を記録できる。
- Certification の状態を確認できる。

初期スコープの非機能要件とアーキテクチャ制約は次のとおりです。

- Backend は Java 21 以降、Spring Boot、Gradle、PostgreSQL、Spring Data JPA、Flyway、Bean Validation、JUnit 5、Testcontainers、OpenAPI を使う。
- Frontend は React、TypeScript、Vite、React Router、TanStack Query を使う。
- Domain model は pure Java のままにする。
- JPA entity は domain model から分離する。
- Repository interface は domain 側に置き、実装は infrastructure 側に置く。
- Controller に business logic を持たせない。
- Public repository として安全に公開できる状態を保つ。

# ユビキタス言語

| 用語                 | 意味                                                                                             |
| -------------------- | ------------------------------------------------------------------------------------------------ |
| Learning Goal        | Spring Boot、DDD、AWS、GitHub Actions などの技術学習目標。                                       |
| Goal Status          | Learning Goal のライフサイクル状態。planned、active、completed、archived のいずれか。            |
| Study Session        | Learning Goal に対して実際に行った学習活動の記録。                                               |
| Study Date           | Study Session を実施した日付。                                                                   |
| Study Duration       | Study Session で学習に使った時間。                                                               |
| Study Content        | Study Session で学習した具体的なトピックや作業内容。                                             |
| Certification        | 管理対象の資格または認定試験の目標。                                                             |
| Exam Plan            | Certification に対する受験予定日。                                                               |
| Exam Attempt         | Certification に対する 1 回の受験イベント。                                                      |
| Exam Result          | Exam Attempt の結果。passed、failed、absent など。                                               |
| Certification Status | Certification のライフサイクル状態。considering、preparing、scheduled、passed、failed、retired。 |

# 境界づけられたコンテキスト

初期設計では、次の 2 つの bounded context を使います。

- `learning`: Learning Goal と Study Session を管理する。
- `certification`: Certification、試験予定、受験、試験結果を管理する。

学習と資格はプロダクト体験上は近い関係にありますが、ライフサイクルのルールが異なります。Learning Goal は学習の進捗と完了を扱います。Certification は受験意思、試験予定、受験履歴、結果を扱います。これらを別コンテキストに分けることで、試験ライフサイクルのルールを learning model に押し込まないようにします。

# ドメインモデル

learning context は次の要素を含みます。

- Aggregate Root としての `LearningGoal`。
- 独立した Aggregate Root としての `StudySession`。
- `LearningGoalId`、`StudySessionId`、`GoalPeriod`、`StudyDuration`、`StudySessionContent` などの Value Object。

certification context は次の要素を含みます。

- Aggregate Root としての `Certification`。
- `Certification` 内の Entity としての `ExamAttempt`。
- `CertificationId`、`QualificationName`、`ExamPlan`、`ExamResult` などの Value Object。

# 集約

## LearningGoal

`LearningGoal` は、目標の identity、title、description、target period、lifecycle status を所有します。

`StudySession` の記録は所有しません。1 つの goal には時間とともに多数の session が紐づく可能性があり、goal を読み込んだり変更したりするために学習履歴全体を読み込むべきではありません。

Repository の保存単位:

- `LearningGoal`

## StudySession

`StudySession` は、`LearningGoalId` に紐づく独立した Aggregate Root です。

実際に行った学習作業を記録します。goal のライフサイクルとは独立して作成・修正できます。一方で、正の duration や空でない content など、ローカルなルールは自身で守ります。

Repository の保存単位:

- `StudySession`

## Certification

`Certification` は、qualification name、certification lifecycle status、planned exam date、exam attempts を所有します。

Exam attempt は `Certification` の内側でモデル化します。attempt は特定の certification target の中でのみ意味を持ち、通常は小さな履歴に収まるためです。

Repository の保存単位:

- `ExamAttempt` 子要素を含む `Certification`

# Entity

| Entity        | Aggregate     | 役割                                 |
| ------------- | ------------- | ------------------------------------ |
| LearningGoal  | LearningGoal  | 学習目標のライフサイクルを追跡する。 |
| StudySession  | StudySession  | 1 回分の完了した学習活動を記録する。 |
| Certification | Certification | 資格目標と試験進捗を追跡する。       |
| ExamAttempt   | Certification | 1 回の受験イベントと結果を記録する。 |

# Value Object

| Value Object        | 目的                              |
| ------------------- | --------------------------------- |
| LearningGoalId      | LearningGoal の identity。        |
| StudySessionId      | StudySession の identity。        |
| CertificationId     | Certification の identity。       |
| ExamAttemptId       | ExamAttempt の identity。         |
| GoalTitle           | 空ではない Learning Goal title。  |
| GoalPeriod          | 開始日と任意の目標日。            |
| StudyDuration       | 正の学習時間。                    |
| StudySessionContent | 空ではない学習内容と任意の note。 |
| QualificationName   | 空ではない certification name。   |
| ExamPlan            | 任意の planned exam date。        |
| ExamResult          | 試験結果と結果日。                |

# 不変条件

- Learning Goal title は空にできない。
- Learning Goal target date は start date より前にできない。
- Completed な Learning Goal は、明示的な将来の設計変更なしに active へ戻せない。
- Study Session は `LearningGoalId` で Learning Goal を参照しなければならない。
- Study Session duration は正でなければならない。
- 該当する Study Session がない場合、算出された学習時間は 0 になり得る。
- Study Session date は必須。
- Study Session content は空にできない。
- Certification qualification name は空にできない。
- Certification が現在持てる planned exam date は最大 1 つ。
- Exam Attempt result date は、結果を記録するとき必須。
- Exam Attempt は必ず 1 つの Certification に属する。
- passed の Exam Attempt を記録すると、Certification status は passed になる。

# 集約間の関係

`StudySession` は `LearningGoal` を identity のみで参照します。これにより aggregate boundary を小さく保ち、学習履歴の編集時に goal を誤って変更することを避けます。

`Certification` は、資格が学習目標に支えられている場合に限り、任意で `LearningGoalId` を参照できます。この関係は意図的に疎にしています。すべての certification に専用の learning goal が必要なわけではなく、1 つの learning goal が複数の certification を支えることもあるためです。

# 主なユースケース

learning のユースケース:

- Learning Goal を作成する。
- Learning Goal を変更する。
- Learning Goal を完了する。
- active な Learning Goal を取得する。
- Study Session を記録する。
- Study Session を修正する。
- 学習履歴を閲覧する。
- 学習時間を計算する。

certification のユースケース:

- Certification を登録する。
- 試験予定を設定する。
- 試験結果を記録する。
- Certification status を確認する。

Application service は、ユースケースごとに機械的に 1 クラスを作るのではなく、まとまりのある workflow を基準に構成します。

# 永続化

Domain repository:

- `LearningGoalRepository`
- `StudySessionRepository`
- `CertificationRepository`

Repository interface は、domain が必要とする永続化要求を表すため domain package に置きます。Spring Data JPA adapter は infrastructure package に置きます。

Domain model と JPA entity は分離します。Infrastructure mapper が相互変換を担当します。

Flyway が database schema migration を所有します。Hibernate は table を作成・更新せず、起動時に migration 済み schema を検証します。

# Backend Package

現在のサンプル package:

```text
io.github.example.devtrack
```

公開前に必要であれば、`example` を対象の GitHub user または organization に置き換えます。

Package structure:

```text
io.github.example.devtrack
+-- learning
|   +-- domain
|   |   +-- model
|   |   +-- repository
|   +-- application
|   +-- presentation
|   +-- infrastructure
|       +-- persistence
+-- certification
    +-- domain
    |   +-- model
    |   +-- repository
    +-- application
    +-- presentation
    +-- infrastructure
        +-- persistence
```

# Repository Directory

```text
ai-ddd-devtrack/
+-- backend/
|   +-- src/
|   |   +-- main/
|   |   +-- test/
|   +-- build.gradle
|   +-- settings.gradle
+-- frontend/
|   +-- src/
|   +-- package.json
|   +-- vite.config.ts
+-- docs/
|   +-- plantuml/
|   |   +-- architecture/
|   |   +-- domain/
|   |   +-- sequence/
|   +-- design/
+-- .github/
|   +-- workflows/
+-- AGENTS.md
+-- README.md
+-- compose.yaml
+-- .gitignore
+-- .env.example
```

実装では backend と frontend を分離します。DDD backend は責務に集中させ、frontend は薄い API client として保ちます。

# PlantUML File Structure

```text
docs/plantuml/
+-- architecture/
|   +-- system-architecture.puml
+-- domain/
|   +-- learning-domain.puml
|   +-- certification-domain.puml
+-- sequence/
    +-- record-study-session.puml
    +-- record-exam-result.puml
```

# 設計判断

## LearningGoal と StudySession は同じ Aggregate ではない

`LearningGoal` と `StudySession` は同じ aggregate にしません。

理由:

- Learning Goal は変更頻度が低い。
- Study Session は頻繁に追加される。
- 学習履歴は自然に小さな上限へ収まるとは限らない。
- Learning Goal を読み込むために、すべての Study Session を読み込むべきではない。
- Study Session は Learning Goal を変更せずに自身の不変条件を守れる。

## StudySession は独立した Aggregate

`StudySession` は、自身の identity、lifecycle、修正ユースケース、永続化要求を持つため、独立した aggregate とします。

`StudySession` は `LearningGoal` を `LearningGoalId` で参照します。完了済み goal に対する session 記録を拒否するような集約をまたぐルールは、application service が goal を読み込んだあと、session を保存する前に扱います。

## Certification は別の Bounded Context

`Certification` は learning context ではなく certification context に属します。

理由:

- 試験予定と受験履歴は、日々の学習とは異なる言語とライフサイクルを持つ。
- Certification は Learning Goal なしでも存在できる。
- 1 つの Learning Goal が複数の Certification を支えることがある。
- 疎な参照にすることで、学習と試験の関心事を混ぜた大きな aggregate を避けられる。

## 学習時間は StudySession から算出する

初期設計では、総学習時間を `LearningGoal` に保存せず、`StudySession` の記録から算出します。

理由:

- Study Session は実際の学習に対する source event-like record である。
- 派生した合計値は一貫して再計算できる。
- session の修正と保存済み合計値の同期バグを避けられる。

将来 performance が問題になった場合は、core aggregate の外側に read model や cached summary を導入できます。

## ExamAttempt は Certification 内の Entity

`ExamAttempt` は別 aggregate ではなく、`Certification` 内の Entity とします。

理由:

- Exam attempt は 1 つの Certification の中でのみ意味を持つ。
- Attempt history は小さく収まる想定である。
- passed result の記録は、同じ整合性境界の中で Certification status を更新する必要がある。
- 現時点では、Certification から独立して attempt を変更するユースケースがない。

# 検討した代替案

## 総学習時間を LearningGoal に保存する

初期設計では採用しません。Study Session にすでに表現されている情報を重複して持つことになり、session 編集時の更新整合性に懸念があるためです。

## LearningGoal、StudySession、Certification を 1 つの context に入れる

採用しません。資格試験のライフサイクルルールが learning model を汚すためです。identity による疎な参照であれば、model 同士を結合しすぎずに十分な関連を表現できます。

## すべてのユースケースを個別の service にする

採用しません。機械的な application service が増えるためです。Application layer はまとまりのある workflow をグループ化し、business rule は domain model に委譲します。

## Domain Event を今すぐ追加する

現時点では見送ります。非同期の domain reaction を必要とする要件がまだないためです。通知生成や read model 更新など、具体的なユースケースが出た時点で追加できます。
