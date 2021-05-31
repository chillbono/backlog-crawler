## アプリ概要
Backlogの課題監視を行い、事前に設定したアクションの自動実行を行うオンプレミス型のアプリケーションです。  
プロジェクト名は汎用性を考慮して「Backlog Crawler」としました。

## 環境
- Docker
  - CentOSコンテナ：Java11 / Gradle / MySQL
  - MySQLコンテナ：MySQL
- Backlog API
- Spring Boot
- O/R mapper: MyBatis

## できること
課題の監視に際して、下記2点の機能を持たせてます。  

### 課題の監視

#### 期限日超過の通知機能  
- ステータスが「完了」以外、かつ 期限日から一定日数超過したチケットが監視対象  
- 監視対象になったチケットに対してコメントを投稿。通知対象は、作成者・担当者・課題参加ユーザに設定。
- コメント投稿後、実行結果をDBに登録する。
  
#### 期限日当日の通知機能
- ステータスが「完了」以外、かつ システム日付が期限日と一致するチケットが監視対象  
- 監視対象になったチケットに対してコメントを投稿。通知対象は、作成者・担当者・課題参加ユーザに設定。
- コメント投稿後、実行結果をDBに登録する。

## なぜ作ろうと思ったか
Backlogでは、日次実行で期限日当日の課題や期限日超過の課題をメール通知で知らせてくれます。  
ただ、ユーザーの中には、そもそもメールをあまり見なかったり、大量のメールの中に重要なメールが埋もれてしまうような人もいると思います。  
そのようなユーザーに対して何か別のアプローチはないかと考え、課題にコメントを投稿する形式で関係者に通知を送ることができる本システムの開発に至りました。  

## ローカル環境の立ち上げ
Docker環境は下記の記事を参考に構築しています。 
 
・DockerでSpringBoot-gradle-mysqlの開発環境を作成する  
https://qiita.com/ktkt11122334/items/74489e28a7fa350e0f82

・Spring Boot with Docker  
https://github.com/ktkt11122334/springbootwithdocker

### 各種設定
Backlogの接続先設定や、SpringBoot標準のスケジューラ設定はプロパティファイルに記述し、機能毎の設定値はDB管理（scheduler_config）としています。

#### application.properties

##### スケジューラの起動タイミング
課題スケジューラの起動タイミングを設定します。  
初期値は日次実行となっており、毎朝8時に監視タスクが起動します。
```
# 課題監視（日次実行：8:00:00）  
scheduler.cron.issue=0 0 8 * * *
```

##### Backlogの接続先
ホスト名とAPIキーを設定します。  
ここで設定したAPIキーの発行ユーザが、通知コメントの投稿者となります。
```
# ホスト名（ https://xxx.backlog.com ）  
backlog.api.hostname=  
# APIキー  
backlog.api.key=
```

#### scheduler_configテーブル
初期値では、期限日超過通知設定と期限日通知設定の2レコードを用意しています。  
※ 期限日超過通知設定： scheduler_config.comparison_type = past のレコード  
※ 期限日通知設定　　： scheduler_config.comparison_type = present のレコード
  
1機能につき1レコードとしており、将来的に機能拡充を行う際はこのテーブルにレコードを追加します。  
`scheduler_config.comment`、`scheduler_config.exceeded_days`、`scheduler_config.activated` の3カラムのみ変更可能。  

##### scheduler_config.comment
課題に投稿するコメントのテンプレートを格納しています。  
コメント内容に変更がある場合は変更します。

##### scheduler_config.exceeded_days
超過日数を格納しています。初期値では`1`（基準日より1日超過で通知対象）としています。

##### scheduler_config.activated
機能の有効化/無効化を行います。（`0`: 無効、`1`: 有効）

### 起動方法
```
cd ./＊docker-compose.ymlのカレントディレクトリへ移動

# コンテナをバックグランド起動
docker-compose up -d
  Creating boot-mysql ... done
  Creating boot-container ... done

# gradleでプロジェクトをビルド
docker exec -w /gradle-project/crawler exam-boot-container gradle build
 * BUILD SUCCESSFUL が表示されれば成功

# jarファイルが作成されていることを確認
docker exec -w /gradle-project/crawler/build/libs exam-boot-container ls
  crawler-0.0.1-SNAPSHOT.jar

# boot起動
docker exec -w /gradle-project/crawler/build/libs exam-boot-container java -jar crawler-0.0.1-SNAPSHOT.jar --spring.profiles.active=docker

# コンテナダウン
docker-compose down
```

### トラブルシューティング

#### ビルド時にJAVAの環境変数の設定でエラーになる
下記が出力される場合
```
ERROR: JAVA_HOME is set to an invalid directory
```
##### 原因
DockerfileのJAVA_HOMEに指定したJDKと、コンテナにインストールしたJDKのバージョンが異なっているのが原因。

##### 対処法  
Dockerfileではセキュリティの都合上、JAVA_HOMEに指定するJDKのバージョンを動的に生成できない。  
そのため、コンテナにインストールしたJDKのバージョンにDockerfileの指定を合わせることで対処する。

```
# CentOSコンテナのbashプロセス起動
docker exec -it  exam-boot-container /bin/bash

# JDK配置先の確認
ls /usr/lib/jvm
  java  java-11  java-11-openjdk  java-11-openjdk-11.0.11.0.9-1.el7_9.x86_64  java-openjdk  jre  jre-11  jre-11-openjdk  jre-11-openjdk-11.0.11.0.9-1.el7_9.x86_64  jre-openjdk

# Dockerfileの記述を修正
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-11.0.11.0.9-1.el7_9.x86_64
 *JDKをインストールするタイミングにより、バージョンが異なる

# Docker再構築
docker-compose build
 *Dockerfileの設定を反映する
```

### 注意事項

- BacklogAPIに負荷をかけないような使い方をしてください。  
実行タイミングを短いスパンにすることも可能ですが、その分Backlogに負荷がかかります。  
日次実行にする等、無理のない範囲でご利用ください。

