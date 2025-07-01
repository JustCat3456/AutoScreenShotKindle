# Kindle Capture - 配布ガイド

## 配布可能ファイル一覧

### 1. Windows配布用アプリケーション（推奨）
- **場所**: `build/distributions/KindleCapture/`
- **サイズ**: 約210MB
- **形式**: アプリケーションフォルダ
- **実行方法**: `KindleCapture/bin/KindleCapture` を実行
- **Java要件**: 不要（Java Runtime含まれる）

#### 配布手順:
1. `build/distributions/KindleCapture/` フォルダ全体をZIP圧縮
2. Windows環境で展開
3. `bin/KindleCapture` を実行

### 2. Java JARファイル
- **場所**: `build/libs/kindle-capture-1.0-SNAPSHOT.jar`
- **サイズ**: 約15.5MB
- **形式**: Fat JAR（依存関係込み）
- **実行方法**: `java -jar kindle-capture-1.0-SNAPSHOT.jar`
- **Java要件**: Java 17以上が必要

## WSL環境での制限事項

### 対応済み
- ✅ Java 17+環境でのビルド
- ✅ 依存関係の解決
- ✅ Fat JAR作成
- ✅ Windows用app-image作成
- ✅ クロスプラットフォーム対応ビルドスクリプト

### 制限事項
- ❌ MSIインストーラー作成（Windows環境が必要）
- ❌ Windows専用オプション（--win-menu, --win-shortcut）
- ❌ Windows Store対応パッケージ

## 実際のWindows環境での最終作業

Windows環境でMSIインストーラーを作成する場合：

```cmd
# Windows環境で実行
gradlew.bat createWindowsInstaller
```

## 配布方法の推奨順位

1. **Windows app-image** (build/distributions/KindleCapture/)
   - Java不要
   - 配布が簡単
   - ウイルススキャンを通りやすい

2. **Fat JAR** (build/libs/kindle-capture-1.0-SNAPSHOT.jar)
   - ファイルサイズが小さい
   - Java 17+が必要
   - コマンドライン実行

3. **MSIインストーラー** （Windows環境でのみ作成可能）
   - プロフェッショナルな配布方法
   - スタートメニュー統合
   - アンインストーラー付き

## 最終テスト項目

### WSL環境
- ✅ `./gradlew build` - ビルド成功
- ✅ `./gradlew shadowJar` - Fat JAR作成成功
- ✅ `./gradlew createWindowsExe` - app-image作成成功
- ✅ `./build-exe.sh` - ビルドスクリプト動作確認
- ❌ `./gradlew createWindowsInstaller` - WSL環境では制限

### Windows環境（実行予定）
- ⏳ `build/distributions/KindleCapture/bin/KindleCapture` - 実行テスト
- ⏳ Kindle for PC連携テスト
- ⏳ スクリーンショット機能テスト
- ⏳ PDF作成機能テスト
- ⏳ GUI操作テスト

## プロジェクトの状態

✅ **開発完了**  
✅ **WSL環境でのビルド体制確立**  
✅ **Windows配布物作成完了**  
⏳ **Windows環境での最終動作確認待ち**  

---

**次のステップ**: Windows環境で`build/distributions/KindleCapture/bin/KindleCapture`を実行し、実際の動作を確認してください。
