<h1 align="center">GitHub+ BOT for Discord</h1>

<p align="center">
  <a href="#overview">Overview</a> |
  <a href="#usage">Usage</a> |
  <a href="#expectation">Expectation</a> |
  <a href="#reference">Reference</a>
</p>

<p align="center">
  <img alt="Screenshot" src="GitHubPlus_Overview.png">
</p>

# Overview
- 💬 コミットメッセージと更新があったファイル名を表示します。
- 🤐 プライベートレポジトリ対応。
- 🤖 Webhookのメッセージから更新を読み取ります。

# Usage
1. BOTを適用したいレポジトリ -> Settings -> WebHooks からWebHookを登録
2. BOTを起動
3. 生成されたConfigファイルの中身に従って入力し、設定を保存してもう一度起動
4. ファイルを更新しメッセージが送信されることを確認
- ※WebHookのメッセージを受け取るチャンネルと当BOTのメッセージを受け取るチャンネルは別にすることを推奨します

# Expectation
- WebHookを登録せず当BOT単体で動くようにする

# Reference
- https://stackoverflow.com/questions/45726013/how-can-i-get-last-commit-from-github-api
- https://living-sun.com/ja/java/464414-execute-curl-from-java-with-processbuilder-java-json-curl-processbuilder-django-rest-framework.html
