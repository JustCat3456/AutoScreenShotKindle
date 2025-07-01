# Kindle Capture - Kindle Desktop スクリーンショット & PDF作成ツール

Kindle Desktopのページを自動でスクリーンショット撮影し、PDFファイルを作成するJavaアプリケーションです。

## 機能

- **Kindle Desktop自動検出**: Kindle for PCアプリケーションを自動で検出
- **スクリーンショット撮影**: 指定間隔でKindleウィンドウのスクリーンショットを自動撮影
- **自動ページ送り**: 撮影後に自動で次のページに進行
- **画像プレビュー**: 撮影した画像をリストとプレビューで確認
- **PDF作成**: 撮影した画像から高品質なPDFファイルを作成
- **直感的なGUI**: JavaFXによる美しく使いやすいユーザーインターフェース

## システム要件

- **OS**: Windows 10/11 (64bit)
- **Java**: Java 17以上
- **メモリ**: 最低512MB以上の空きメモリ
- **その他**: Kindle for PC アプリケーション

## インストール

### 1. GitHub Releasesからダウンロード（推奨）

1. [Releases](https://github.com/your-repo/kindle-capture/releases)から最新バージョンをダウンロード
2. **MSIインストーラー** - `KindleCapture-*.msi`（推奨）
   - 管理者権限で実行
   - スタートメニューに追加される
3. **ポータブル版** - `KindleCapture-Windows-App.zip`
   - 展開後、`bin/KindleCapture.exe`を実行
4. **JAR版** - `kindle-capture-*.jar`
   - Java 17+が必要: `java -jar kindle-capture-*.jar`

### 2. ソースコードからのビルド

```bash
# プロジェクトをクローン
git clone https://github.com/your-repo/kindle-capture.git
cd kindle-capture

# Gradleでビルド（Windows）
gradlew.bat build

# またはUnix/Linux/Mac
./gradlew build

# アプリケーションを実行
gradlew.bat run

# Windows用EXEファイルを作成
gradlew.bat createWindowsExe
```

## 使用方法

### 1. 初期設定

1. Kindle for PCアプリケーションを起動し、読みたい本を開く
2. Kindle Captureアプリケーションを起動
3. 「Kindle Desktopを検出」ボタンをクリック
4. 「保存フォルダを選択」で画像保存先を指定

### 2. スクリーンショット撮影

1. 撮影間隔をスライダーで調整（1-10秒）
2. 必要に応じて「自動ページ送り」をチェック
3. 「撮影開始」ボタンをクリック
4. 撮影を止めたい場合は「撮影停止」ボタンをクリック

### 3. PDF作成

1. 撮影完了後、「PDFを作成」ボタンをクリック
2. 保存先フォルダにPDFファイルが作成されます

## 設定オプション

- **撮影間隔**: 1秒～10秒の間で調整可能
- **自動ページ送り**: 撮影後に自動で次のページに進む
- **保存フォルダ**: 画像とPDFの保存先を指定

## トラブルシューティング

### Kindle Desktopが検出されない

- Kindle for PCが起動していることを確認
- Kindleのウィンドウが最小化されていないことを確認
- アプリケーションを管理者権限で実行

### スクリーンショットが真っ黒になる

- Kindleウィンドウが他のウィンドウで隠れていないことを確認
- グラフィックドライバを最新版に更新
- ハードウェアアクセラレーションを無効にする

### PDFが作成できない

- 保存フォルダに書き込み権限があることを確認
- ディスク容量が十分にあることを確認
- 撮影した画像ファイルが破損していないことを確認

### Java関連のエラー

- Java 17以上がインストールされていることを確認
- 環境変数JAVA_HOMEが正しく設定されていることを確認

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。詳細は[LICENSE](LICENSE)ファイルをご覧ください。

## 貢献

バグ報告や機能リクエストは[Issues](https://github.com/your-repo/kindle-capture/issues)にお願いします。

プルリクエストも歓迎します！

## 開発情報

### 使用技術

- **Java 17+**
- **JavaFX 20** - GUI フレームワーク
- **Apache PDFBox 3.0** - PDF作成
- **JNA (Java Native Access)** - Windows API アクセス
- **Gradle 8.5** - ビルドツール

### プロジェクト構造

```
src/
├── main/
│   ├── java/
│   │   └── com/kindlecapture/
│   │       ├── KindleCaptureApp.java          # メインアプリケーション
│   │       ├── controller/
│   │       │   └── MainController.java       # UIコントローラー
│   │       ├── service/
│   │       │   ├── KindleWindowService.java  # Windowsウィンドウ操作
│   │       │   ├── ScreenshotService.java    # スクリーンショット撮影
│   │       │   └── PdfService.java           # PDF作成
│   │       └── util/
│   │           ├── FileUtils.java            # ファイル操作
│   │           └── UIUtils.java              # UI関連ユーティリティ
│   └── resources/
│       ├── fxml/
│       │   └── main-view.fxml                # メインUI定義
│       └── css/
│           └── style.css                     # スタイルシート
```

### ビルドコマンド

```bash
# 依存関係をダウンロード
./gradlew dependencies

# コンパイル
./gradlew compileJava

# テスト実行
./gradlew test

# JARファイル作成
./gradlew jar

# 実行可能JARファイル作成
./gradlew shadowJar

# Windows用EXEファイル作成
./gradlew createWindowsExe
```

## 更新履歴

### v1.0.0 (2024-12-XX)
- 初回リリース
- Kindle Desktop自動検出機能
- スクリーンショット自動撮影機能
- PDF作成機能
- JavaFX GUI実装

---

**注意**: このアプリケーションは個人使用目的で作成されています。商用利用の際は著作権法を遵守してください。
