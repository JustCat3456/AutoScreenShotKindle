#!/bin/bash

# Kindle Capture リリース作成スクリプト
# 使用方法: ./create-release.sh 1.0.0

set -e

if [ $# -eq 0 ]; then
    echo "エラー: バージョン番号を指定してください"
    echo "使用方法: $0 <version>"
    echo "例: $0 1.0.0"
    exit 1
fi

VERSION=$1
TAG_NAME="v${VERSION}"

echo "=== Kindle Capture リリース作成 ==="
echo "バージョン: ${VERSION}"
echo "タグ名: ${TAG_NAME}"
echo ""

# 現在の状態を確認
echo "[1/6] Git状態の確認..."
git status --porcelain
if [ -n "$(git status --porcelain)" ]; then
    echo "警告: コミットされていない変更があります"
    echo "続行しますか? (y/N)"
    read -r answer
    if [ "$answer" != "y" ] && [ "$answer" != "Y" ]; then
        echo "中止しました"
        exit 1
    fi
fi

# build.gradleのバージョンを更新
echo "[2/6] build.gradleのバージョンを更新..."
sed -i "s/version = '.*'/version = '${VERSION}'/" build.gradle
echo "バージョンを ${VERSION} に更新しました"

# ローカルビルドテスト
echo "[3/6] ローカルビルドテスト..."
./gradlew clean build shadowJar
echo "ビルドテスト完了"

# 変更をコミット
echo "[4/6] 変更をコミット..."
git add build.gradle
git commit -m "Release version ${VERSION}"

# タグを作成
echo "[5/6] リリースタグを作成..."
git tag -a "${TAG_NAME}" -m "Release ${VERSION}

## 新機能
- Kindle Desktop自動検出機能
- 自動スクリーンショット撮影
- PDF作成機能
- JavaFX GUI

## システム要件
- Windows 10/11 (64bit)
- Kindle for PC

## 注意事項
- 個人使用に限定
- 著作権法を遵守してください"

echo "タグ ${TAG_NAME} を作成しました"

# プッシュ
echo "[6/6] GitHubにプッシュ..."
echo "プッシュしますか? (y/N)"
read -r answer
if [ "$answer" = "y" ] || [ "$answer" = "Y" ]; then
    git push origin main
    git push origin "${TAG_NAME}"
    echo ""
    echo "🎉 リリース作成完了!"
    echo ""
    echo "GitHub Actionsが自動的に以下を作成します:"
    echo "  - Windows MSIインストーラー"
    echo "  - Windows ポータブル版"
    echo "  - Java JARファイル"
    echo ""
    echo "リリース状況: https://github.com/your-username/kindle-capture/actions"
    echo "リリースページ: https://github.com/your-username/kindle-capture/releases"
else
    echo "プッシュをキャンセルしました"
    echo "手動でプッシュする場合:"
    echo "  git push origin main"
    echo "  git push origin ${TAG_NAME}"
fi
