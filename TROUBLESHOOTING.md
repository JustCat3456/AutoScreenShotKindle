# Kindle Capture - トラブルシューティングガイド

## 🚨 アプリが起動しない・UIが表示されない場合

### 1. MSIインストール版での問題

#### ✅ 基本確認
- **管理者権限**: MSIインストーラーを「管理者として実行」でインストールしたか
- **Windows版**: Windows 10/11 (64bit) を使用しているか
- **インストール場所**: `C:\Program Files\KindleCapture` にインストールされているか

#### 🔧 起動確認手順

1. **コマンドプロンプトから起動**
   ```cmd
   cd "C:\Program Files\KindleCapture"
   bin\KindleCapture.exe
   ```

2. **エラーメッセージを確認**
   - エラーが表示される場合は、その内容をメモ
   - 何も表示されない場合は次のステップへ

3. **JavaFXランタイム確認**
   ```cmd
   cd "C:\Program Files\KindleCapture"
   runtime\bin\java --list-modules | findstr javafx
   ```
   - JavaFX関連のモジュールが表示されるか確認

#### 🛠️ デバッグモード起動

1. **デバッグ用バッチファイルの作成**
   以下の内容でバッチファイル `debug.bat` を作成：
   ```batch
   @echo off
   cd /d "C:\Program Files\KindleCapture"
   echo Starting Kindle Capture in debug mode...
   runtime\bin\java --add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.graphics --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED --add-exports=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED -Djava.awt.headless=false -Dprism.order=sw -jar app\kindle-capture-1.0-SNAPSHOT.jar
   pause
   ```

2. **管理者として実行**
   - バッチファイルを右クリック → 「管理者として実行」

### 2. ZIP版（ポータブル版）での問題

#### ✅ 展開確認
- ZIPファイルを適切な場所に展開したか（デスクトップなど）
- フォルダ構造が正しいか：
  ```
  KindleCapture/
  ├── bin/
  │   └── KindleCapture.exe
  ├── app/
  │   └── kindle-capture-1.0-SNAPSHOT.jar
  ├── runtime/
  │   └── (Java実行環境)
  └── lib/
  ```

#### 🔧 ZIP版起動手順
1. `KindleCapture`フォルダを開く
2. `bin\KindleCapture.exe`をダブルクリック
3. ウイルス対策ソフトの警告が出た場合は「許可」を選択

### 3. JAR版での問題

#### ✅ Java環境確認
```cmd
java -version
```
- Java 17以上がインストールされているか確認

#### 🔧 JavaFX付きで起動
```cmd
java --add-modules=javafx.controls,javafx.fxml --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED -jar kindle-capture-1.0-SNAPSHOT.jar
```

### 4. よくあるエラーと対処法

#### 🚫 "Module javafx.controls not found"
**原因**: JavaFXランタイムが見つからない
**対処**: 
- MSI版: 再インストール（管理者権限で）
- JAR版: OpenJFX付きのJavaをインストール

#### 🚫 "Access Denied" / "アクセスが拒否されました"
**原因**: 権限不足
**対処**:
- 管理者として実行
- インストール先をC:\Program Files以外に変更

#### 🚫 画面が真っ黒 / ウィンドウが表示されない
**原因**: グラフィックドライバーまたはJavaFX描画の問題
**対処**:
```cmd
runtime\bin\java -Dprism.order=sw -Dprism.verbose=true -jar app\kindle-capture-1.0-SNAPSHOT.jar
```

#### 🚫 "Could not find or load main class"
**原因**: JARファイルの破損またはパス問題
**対処**:
- アプリケーションを再インストール
- フルパスで指定して実行

### 5. システム要件の再確認

#### 🖥️ 最小要件
- **OS**: Windows 10 (1903以降) または Windows 11
- **アーキテクチャ**: 64-bit
- **メモリ**: 4GB RAM
- **ディスク**: 200MB以上の空き容量

#### 📊 推奨要件
- **OS**: Windows 11
- **メモリ**: 8GB RAM
- **グラフィック**: DirectX 11対応

### 6. ログの確認方法

#### 📝 アプリケーションログ
1. コマンドプロンプトから起動
2. エラーメッセージをコピー
3. 以下の情報と合わせて報告：
   - Windows版（`winver`コマンドで確認）
   - Java版（`java -version`）
   - インストール方法（MSI/ZIP/JAR）

#### 📋 システム情報収集
```cmd
systeminfo | findstr /C:"OS Name" /C:"OS Version" /C:"System Type"
java -version
```

### 7. 最後の手段

#### 🔄 完全な再インストール
1. アプリケーションをアンインストール
2. `C:\Program Files\KindleCapture`フォルダを手動削除
3. MSIインストーラーを管理者として再実行

#### 💬 サポート連絡
上記で解決しない場合は、以下の情報と合わせてIssueを作成：
- Windowsバージョン
- エラーメッセージの全文
- 実行した手順
- `java -version`の出力

## 🎯 動作確認済み環境

- Windows 10 Pro (21H2) - ✅
- Windows 11 Home (22H2) - ✅
- Windows 10 Enterprise (LTSC 2021) - ✅

## 📞 追加サポート

- GitHub Issues: 技術的な問題
- Discord: リアルタイムサポート（コミュニティ運営時）
