# Kindle Capture - インストール・ビルドガイド

## システム要件

### 開発環境（WSL/Linux）
- **OS**: WSL2/Ubuntu または Linux
- **Java**: Java 17以上 (SDKMAN推奨)
- **ツール**: Gradle, jpackage
- **制限**: MSIインストーラー作成にはWindows環境が必要

### 実行環境（Windows）
- **OS**: Windows 10/11 (64bit)
- **必要なソフトウェア**: Kindle for PC
- **Java**: 不要（実行ファイルに含まれる）

## 開発・ビルド手順

### 1. 開発環境の準備（WSL/Linux）

#### SDKMANでJava環境をセットアップ
```bash
# SDKMANのインストール（未インストールの場合）
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Java 21（LTS）をインストール
sdk install java 21.0.7-tem
sdk use java 21.0.7-tem

# 確認
java -version
```

#### プロジェクトのビルド
```bash
# プロジェクトディレクトリに移動
cd /path/to/Kindle_java

# 実行権限を付与
chmod +x gradlew build-exe.sh

# Windows用実行可能アプリケーション（app-image）をビルド
./gradlew createWindowsExe

# または
./build-exe.sh
```

### 2. 配布物の作成

WSL環境では以下のファイルが作成されます：

#### 成功例：
- `build/distributions/KindleCapture/` - Windows用アプリケーションフォルダ
  - `bin/KindleCapture` - Windows実行ファイル
  - `lib/` - 必要なライブラリ群
- `build/libs/kindle-capture-1.0-SNAPSHOT.jar` - 予備用JARファイル

#### MSIインストーラー（制限事項）
WSL環境では Windows MSI インストーラーは作成できません。  
MSIインストーラーが必要な場合は、実際のWindows環境で以下を実行：
```cmd
gradlew.bat createWindowsInstaller
```

### 3. Windows環境での配布方法

以下の方法でWindows環境にアプリケーションを配布できます：

**方法1: アプリケーションフォルダ配布（推奨）**
1. `build/distributions/KindleCapture/` フォルダ全体をzip圧縮
2. Windows環境で展開
3. `bin/KindleCapture` を実行

**方法2: JARファイル配布**
1. `build/libs/kindle-capture-1.0-SNAPSHOT.jar` をコピー
2. Windows環境でJava 17+がインストール済みの場合のみ実行可能
```cmd
java -jar kindle-capture-1.0-SNAPSHOT.jar
```

## GitHub Actionsでの自動ビルド・リリース

### 自動ビルドシステム

このプロジェクトはGitHub Actionsを使用して自動的にWindows用MSIインストーラーを作成します。

#### リリース手順

1. **開発完了後、リリースタグを作成**
```bash
# リリーススクリプトを使用（推奨）
./create-release.sh 1.0.0

# または手動でタグ作成
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin v1.0.0
```

2. **GitHub Actionsが自動実行**
   - Windows環境でビルド
   - MSIインストーラー作成
   - ポータブル版ZIP作成
   - JAR ファイル作成
   - GitHub Releasesに自動公開

#### 作成される配布物

- 🔧 **KindleCapture-1.0.0.msi** - MSIインストーラー
- 📁 **KindleCapture-Windows-App.zip** - ポータブル版
- ☕ **kindle-capture-1.0.0.jar** - Java JAR

#### ビルド状況の確認

- **Actions**: https://github.com/your-username/kindle-capture/actions
- **Releases**: https://github.com/your-username/kindle-capture/releases

### ローカル開発者向け

開発者がローカルでMSIを作成したい場合：

```cmd
# Windows環境で実行
gradlew.bat createWindowsInstaller
```

**注意**: WSL環境ではMSIインストーラーは作成できません。

## Windows環境での使用方法

### 1. インストール

1. `KindleCapture-1.0.exe` をダウンロード
2. 実行してインストール（管理者権限推奨）
3. スタートメニューまたはデスクトップからアプリを起動

### 2. 初回設定

1. Kindle for PCアプリケーションを起動
2. 読みたい本を開く
3. Kindle Captureを起動
4. 「Kindle Desktopを検出」ボタンをクリック
5. 「保存フォルダを選択」で画像保存先を指定

## 使用方法

### 基本的な流れ

1. **Kindle Desktop準備**
   - Kindle for PCを起動
   - 読みたい本の最初のページを表示

2. **設定調整**
   - 撮影間隔を調整（1-10秒）
   - 自動ページ送りの有効/無効を選択

3. **撮影開始**
   - 「撮影開始」ボタンをクリック
   - 必要な枚数を撮影後「撮影停止」をクリック

4. **PDF作成**
   - 「PDFを作成」ボタンをクリック
   - 指定フォルダにPDFファイルが保存される

### 推奨設定

- **撮影間隔**: 3-5秒（ページ読み込み時間を考慮）
- **自動ページ送り**: 有効（手動でページを送る必要がない）
- **保存フォルダ**: ドキュメントフォルダ内に専用フォルダを作成

## トラブルシューティング

### よくある問題と解決方法

#### 1. Kindle Desktopが検出されない

**原因**: 
- Kindle for PCが起動していない
- ウィンドウが最小化されている
- 管理者権限が必要

**解決方法**:
- Kindle for PCを起動し、ウィンドウを表示状態にする
- アプリケーションを管理者として実行

#### 2. スクリーンショットが真っ黒

**原因**: 
- Kindleウィンドウが他のウィンドウに隠れている
- グラフィックドライバの問題

**解決方法**:
- Kindleウィンドウを最前面に表示
- グラフィックドライバを最新版に更新

#### 3. PDFが作成できない

**原因**: 
- 保存フォルダへの書き込み権限がない
- ディスク容量不足

**解決方法**:
- 保存フォルダの権限を確認
- ディスク容量を確認

#### 4. Java関連のエラー

**原因**: 
- Java 17未満のバージョンを使用
- 環境変数の設定が不正

**解決方法**:
- Java 17以上をインストール
- 環境変数JAVA_HOMEを正しく設定

### エラーメッセージ別対処法

#### "Javaが見つかりません"
```
where java
```
コマンドでJavaがインストールされているか確認

#### "module not found"
Java 17以上を使用しているか確認

#### "access denied"
アプリケーションを管理者として実行

## 注意事項

### 法的な注意

- **個人使用に限定**: このアプリケーションは個人での読書用途に限定して使用してください
- **著作権の遵守**: 撮影した画像やPDFの配布は行わないでください
- **利用規約の確認**: Kindle for PCの利用規約を確認してください

### 技術的な制限

- **Windows環境のみ**: WindowsのAPIを使用しているため、Mac/Linuxでは動作しません
- **Kindle for PC専用**: Webブラウザ版Kindleには対応していません
- **DRM保護**: DRM保護されたコンテンツは正常に撮影できない場合があります

## サポート

問題が解決しない場合は、以下の情報を含めてお問い合わせください：

- 使用しているOSのバージョン
- Javaのバージョン（`java -version`の出力）
- エラーメッセージの全文
- 再現手順

## 更新情報

最新バージョンは以下で確認できます：
- GitHub: [プロジェクトページ]
- リリースノート: [更新履歴]

---

**このアプリケーションは教育目的で作成されています。商用利用や著作権侵害にあたる使用は禁止されています。**
