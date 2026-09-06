# コーディング規約

このリポジトリでは、PlantUML を設計の source of truth とし、コードはその設計を具体化する実装として扱います。

規約は次の優先順位で判断します。

1. `docs/plantuml/` 配下の PlantUML
2. `AGENTS.md`
3. formatter、linter、type checker、test
4. `docs/design/` 配下の Markdown design document
5. 周辺コードの既存パターン

PlantUML、Java、Markdown が矛盾している場合は PlantUML を baseline とし、矛盾を修正または報告します。

## 自動化とレビューの分担

コードレビューでは、formatter、linter、type checker、test で検出できる内容に時間を使いません。レビューでは、設計意図、domain rule、読みやすさ、変更範囲、テスト観点など、人の判断が必要な内容を確認します。

### 自動化で確認すること

次の項目は、原則としてレビュー前にツールで確認します。

| 対象            | ツール                        | 確認内容                                                                      |
| --------------- | ----------------------------- | ----------------------------------------------------------------------------- |
| Java format     | Spotless / google-java-format | indentation、改行、空白、annotation format、import order、unused import       |
| Java lint       | Checkstyle                    | tab 禁止、行長、star import 禁止、braces、命名、method size、複雑度など       |
| Java test       | JUnit / Gradle / ArchUnit     | domain unit test、architecture test、integration test、Spring wiring          |
| Frontend format | oxfmt                         | TypeScript、TSX、JSON、Markdown などの formatting と import order             |
| Frontend lint   | oxlint                        | `eqeqeq`、unused variables、unused disable directive、React hooks、`any` など |
| Comment lint    | custom script                 | `TODO`、`FIXME` の形式                                                        |
| Frontend type   | TypeScript / tsc              | 型エラー、API 型との不整合                                                    |
| API contract    | OpenAPI generation / git diff | generated API type の drift                                                   |
| Secret scan     | gitleaks                      | secret、token、credential の混入                                              |
| Build           | Gradle / Vite                 | build failure、生成物作成、compile error                                      |

自動化で確認できる規約は、レビューコメントではなく設定やコマンドで直します。必要なら規約文より先に formatter、linter、type checker、test の設定を強化します。

### レビューで確認すること

次の項目はツールだけでは判断できないため、レビューで確認します。

- PlantUML の設計と実装が一致しているか。
- aggregate boundary を越えた不適切な変更がないか。
- domain invariant が domain layer で守られているか。
- Spring、JPA、HTTP、database dependency が domain layer に漏れていないか。
- application service が workflow 調整に留まり、business rule を抱え込んでいないか。
- controller が HTTP 境界の責務に留まっているか。
- コメントが処理説明ではなく、意図、制約、背景を説明しているか。
- 名前が domain language と use case を表しているか。
- method の抽象度が揃っていて、人が主要な流れを追えるか。
- 変更範囲に対して test coverage が足りているか。
- public repository に出せない情報が含まれていないか。

### 自動化へ移管済み

次の規約は自動化済みです。レビューでは、原則として個別指摘せず、失敗した check を直します。

| 規約                                                                                          | 方法                                | レビューから外せる観点                     |
| --------------------------------------------------------------------------------------------- | ----------------------------------- | ------------------------------------------ |
| domain layer に Spring、JPA、HTTP、persistence dependency を入れない                          | ArchUnit test                       | domain の framework independence           |
| domain layer が application、presentation、infrastructure に依存しない                        | ArchUnit test                       | domain から外側への依存                    |
| application layer が presentation、infrastructure に依存しない                                | ArchUnit test                       | application から adapter への依存          |
| infrastructure layer が presentation に依存しない                                             | ArchUnit test                       | adapter から HTTP 境界への依存             |
| class、method、field、parameter、local variable の基本命名                                    | Checkstyle                          | Java の機械的な命名崩れ                    |
| method length、parameter count、nesting depth、cyclomatic complexity の上限                   | Checkstyle                          | 長すぎる method、深い nest、複雑すぎる分岐 |
| Java/TypeScript/Markdown の formatting                                                        | Spotless、google-java-format、oxfmt | 空白、改行、import order                   |
| TypeScript の unused variables、`eqeqeq`、unused disable directive、`any`、non-null assertion | oxlint                              | frontend の基本 lint                       |
| React hooks、JSX key、unknown property                                                        | oxlint                              | React の典型的な事故                       |
| TypeScript の型整合性                                                                         | tsc                                 | API 型や component props の型エラー        |
| `TODO`、`FIXME`、一時対応コメントの形式                                                       | custom script                       | 放置されやすい一時コメント                 |
| generated file の手編集防止                                                                   | OpenAPI generation / git diff       | API 型生成物の drift                       |
| secret や credential の混入                                                                   | gitleaks                            | 公開できない情報の混入                     |

### 自動化へ移管できる次候補

次の規約は、追加設定や CI script を入れればさらにレビューから外せます。導入時は、誤検知が少なく既存コードへの修正量が小さいものから採用します。

| 優先度 | 移管する規約                            | 方法                                                       | レビューから外せる観点        |
| ------ | --------------------------------------- | ---------------------------------------------------------- | ----------------------------- |
| 中     | TypeScript の type-aware unsafe pattern | `oxlint --type-aware` または ESLint 追加                   | TypeScript の unsafe な値操作 |
| 低     | PlantUML と Markdown の更新漏れ         | 変更パスに応じた CI script で docs の同時更新を検査する    | 設計資料の更新忘れ            |
| 低     | コメントが処理説明になっていないか      | 完全自動化は困難。禁止語句や TODO 形式だけ補助的に検査する | 明らかなコメント運用違反      |

最初に移管するなら、次の順が費用対効果が高いです。

1. `oxlint --type-aware` または ESLint で TypeScript の unsafe pattern を追加する。
2. PlantUML と Markdown の更新漏れを補助する CI check を追加する。

### 自動化へ移管しないもの

次の規約は、人の判断を残します。ツールで無理に判定すると、誤検知や形だけの回避が増えます。

- 名前が domain language と use case を正しく表しているか。
- コメントが本当に必要な意図や背景を説明しているか。
- domain invariant の置き場所が model として自然か。
- aggregate boundary の設計判断そのものが妥当か。
- test case が仕様上重要な失敗モードを押さえているか。
- PlantUML の設計が今後の変更に対して十分か。

## 全体方針

- 設計変更が必要な実装では、先に関連する PlantUML を更新します。
- Java 実装後は、必要な unit test と integration test を追加または更新します。
- 実装の最後に、関連する Markdown design document を更新します。
- public repository として安全な状態を保ちます。個人情報、実データ、試験結果、password、API key、token、`.env`、クラウド認証情報はコミットしません。
- 既存の formatter と linter によって機械的に決まる内容は、手作業の好みで上書きしません。

## 読みやすさ

このリポジトリは DDD と AI 支援開発のサンプルでもあるため、人が読んで設計意図を追えるコードを優先します。

### 命名

- class、method、variable の名前は、実装都合ではなく domain language と use case を表す名前にします。
- 省略語は広く定着しているものだけ使います。短すぎる名前や一時的な名前を公開 API、domain model、test name に残しません。
- boolean は `isActive`、`hasExamPlan`、`canComplete` のように true/false の意味が読める名前にします。
- collection は `certifications`、`attempts` のように複数形を使います。
- mapper、adapter、request、response、command などの suffix は、レイヤ上の役割が明確な場合だけ使います。
- test method name は、期待する振る舞いが読める名前にします。

### コメント

- コメントは「何をしているか」ではなく、「なぜそうしているか」「どの制約を守っているか」を説明するために使います。
- コードを読めば分かる処理説明コメントは追加しません。
- domain invariant、aggregate boundary、外部仕様、互換性維持、workaround など、背景を知らないと誤って変更しやすい箇所にはコメントを残します。
- 一時的な対応には、理由と削除条件が分かるコメントを付けます。
- コメントとコードが矛盾した場合はコードだけを直さず、コメントも同じ変更で更新します。
- public repository 前提のため、コメントにも個人情報、credential、実データ、非公開事情を書きません。

### 構造

- method は 1 つの意図を表す単位にします。複数の判断、変換、保存を長く並べる場合は、名前の付いた private method へ分けます。
- deep nesting は避け、guard clause や小さな method で主要な流れを読みやすくします。
- 同じ抽象度の処理を同じ method に並べます。HTTP、application workflow、domain rule、persistence mapping を 1 つの method に混ぜません。
- null を扱う境界を狭くします。domain model では必須値を `Objects.requireNonNull` や value object で早めに検証します。
- 例外 message は、呼び出し側や test failure から原因を特定できる具体性を持たせます。
- 意図が読み取れない条件式や値変換は、名前の付いた変数または method に切り出します。

## Backend

Backend は Java 21、Spring Boot、Gradle、PostgreSQL、Spring Data JPA、Flyway、Bean Validation、JUnit 5、Testcontainers、OpenAPI を使います。

### Format と Lint

- Java は Spotless の `google-java-format` で整形します。
- Java import は Spotless の `importOrder()` と `removeUnusedImports()` に任せます。
- Checkstyle の既存設定を守ります。
- tab は使いません。
- star import は使いません。
- Java の行長は原則 140 文字以内にします。
- 1 行に複数 statement を置きません。
- `if`、`for`、`while` などでは braces を省略しません。

### Package 構成

bounded context ごとに top-level package を分けます。

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

新しい bounded context を追加する場合も、同じレイヤ構成を基本にします。

### Domain Layer

- domain layer は pure Java にします。
- domain layer に Spring、JPA、HTTP、database-specific dependency を入れません。
- aggregate root、entity、value object、domain repository interface を domain layer に置きます。
- domain invariant は domain model の constructor、factory method、behavior method で守ります。
- controller、application service、JPA entity だけで domain invariant を検証しません。
- aggregate の内部状態は aggregate root の behavior method 経由で変更します。
- aggregate 間の参照は object reference ではなく identity で行います。
- value object は入力値の正当性を自身で検証します。
- domain service は、特定の aggregate に自然に置けない domain rule が出た場合だけ追加します。

### Application Layer

- application service は use case workflow を調整します。
- application service は repository から aggregate を取得し、domain behavior を呼び出し、保存します。
- application service に business rule を集めません。
- use case ごとに機械的に service class を増やさず、まとまりのある workflow 単位で構成します。
- command object は application layer に置き、presentation request と domain object の間の入力境界として使います。
- transaction boundary は application service に置きます。

### Presentation Layer

- controller は HTTP request/response と validation の境界に集中します。
- controller に business logic を置きません。
- request DTO には Bean Validation を使い、domain invariant は domain 側でも必ず守ります。
- response DTO は domain model をそのまま公開せず、API 用の形に変換します。
- API path は resource-oriented にし、workflow action は必要な場合だけ sub-resource または action endpoint として表現します。

### Infrastructure Layer

- Spring Data repository、JPA entity、persistence adapter、mapper は infrastructure layer に置きます。
- JPA entity と domain model は分離します。
- mapper が JPA entity と domain model の相互変換を担当します。
- infrastructure layer に business rule を置きません。
- Flyway migration が database schema を所有します。
- Hibernate による schema 自動更新を前提にしません。

### Test

- domain invariant と aggregate behavior は domain unit test で確認します。
- repository adapter、database migration、Spring wiring、REST API の結合は integration test で確認します。
- bug fix では、失敗を再現する test を先に追加することを基本にします。
- private method を直接 test せず、公開された behavior から確認します。
- test data は現実の個人情報や実試験履歴を使いません。

## Frontend

Frontend は React、TypeScript、Vite、React Router、TanStack Query を使います。

### Format と Lint

- TypeScript、TSX、JSON、Markdown などは `oxfmt` で整形します。
- JavaScript/TypeScript lint は `oxlint` を使います。
- indent は 2 spaces にします。
- string quote は double quote にします。
- semicolon を付けます。
- trailing comma を使います。
- import order は `.oxfmtrc.json` に従います。
- `==` / `!=` は使わず、`===` / `!==` を使います。
- 未使用変数は残しません。意図的に未使用の引数は `_` prefix を使います。

### TypeScript

- API 型は OpenAPI から生成された `frontend/src/api/generated/schema.ts` を基準にします。
- generated file は手編集しません。
- 型の重複定義を避け、必要な alias は `frontend/src/api/types.ts` など境界に近い場所で定義します。
- `any` は原則使いません。外部入力などで必要な場合は、境界で検証または型を絞ります。
- domain business rule を frontend に重複実装しません。frontend validation は入力補助と早期 feedback に留めます。

### React

- server state は TanStack Query で扱います。
- API call は `frontend/src/api/` 配下の client 経由に寄せます。
- feature-specific UI は `frontend/src/features/<feature>/` に置きます。
- component state は UI 状態に限定し、server state と混ぜません。
- mutation 後は、影響する query key を invalidate します。
- 表示文言や form field は API contract と domain language に合わせます。

## API と Schema

- REST API は backend の controller と Springdoc OpenAPI から定義します。
- frontend API types は OpenAPI document から生成します。
- API contract を変える場合は、backend、generated frontend type、frontend usage を同じ変更単位で揃えます。
- breaking change では、README または design document に影響を記録します。

## Documentation

- `README.md` は開発者向けの入口として保ちます。
- `docs/design/` は設計判断と背景を説明します。
- `docs/plantuml/` は構造と振る舞いの正本として保ちます。
- 実装と設計判断が変わった場合は、最後に Markdown document を更新します。
- Mermaid、画像、文章だけで PlantUML の内容を上書きしません。

## Verification

レビュー前に、変更内容に応じて次のコマンドを実行します。

```bash
bun run check
bun run test
bun run build
```

自動修正が必要な場合は次を使います。

```bash
bun run check:fix
```

Backend だけの変更では次を優先します。

```bash
bun run --cwd backend lint
bun run --cwd backend test
bun run --cwd backend build
```

Frontend だけの変更では次を優先します。

```bash
bun run format:oxfmt:check
bun run lint:oxlint
bun run typecheck
bun run --cwd frontend build
```

これらのコマンドで失敗する内容は、レビュー指摘ではなく実装側で修正します。レビューでは、コマンドが通った前提で設計、責務、命名、コメント、テスト観点を確認します。

検証コマンドを実行できない場合、または unrelated な既存問題で失敗した場合は、実行したコマンド、結果、理由を報告します。
