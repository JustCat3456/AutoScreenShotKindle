#!/bin/bash

echo "=== Kindle Capture Windows EXE ビルドスクリプト ==="
echo "開発環境: WSL/Linux"
echo "ターゲット: Windows 10/11"
echo ""

# エラー時に停止
set -e

# Java環境の確認
echo "[1/6] Java環境の確認..."
java -version
echo ""

# 依存関係のダウンロード
echo "[2/6] 依存関係をダウンロード中..."
./gradlew dependencies
echo ""

# コンパイル
echo "[3/6] Javaソースをコンパイル中..."
./gradlew compileJava
echo ""

# ShadowJAR作成（全依存関係を含む）
echo "[4/6] Fat JARファイルを作成中..."
./gradlew shadowJar
echo ""

# JARファイルの確認
echo "[5/6] 作成されたJARファイルを確認..."
ls -la build/libs/
echo ""

# Windows EXEファイル作成
echo "[6/6] Windows用EXEファイルを作成中..."
echo "注意: jpackageを使用してWindows実行ファイルを作成します"

if command -v jpackage &> /dev/null; then
    ./gradlew createWindowsExe
    
    echo ""
    echo "=== ビルド完了 ==="
    echo ""
    echo "作成されたファイル:"
    echo "📦 JAR: build/libs/kindle-capture-1.0-SNAPSHOT.jar"
    
    if [ -f "build/distributions/KindleCapture-1.0.exe" ]; then
        echo "💻 EXE: build/distributions/KindleCapture-1.0.exe"
        echo ""
        echo "Windows環境で KindleCapture-1.0.exe を実行してください。"
    else
        echo "⚠️  EXE作成に失敗しました。"
        echo "   JARファイルは正常に作成されました。"
    fi
    
    echo ""
    echo "📁 配布用ファイル:"
    find build/distributions -name "*.exe" -o -name "*.msi" 2>/dev/null || echo "   EXEファイルが見つかりません"
    
else
    echo "❌ エラー: jpackageコマンドが見つかりません"
    echo "Java 14以上をインストールしてください"
    exit 1
fi

echo ""
echo "🎉 ビルドプロセス完了!"
