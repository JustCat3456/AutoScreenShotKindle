@echo off
echo Kindle Capture アプリケーションのビルドとパッケージングを開始します...
echo.

echo [1/4] 依存関係をダウンロード中...
call gradlew.bat dependencies
if %errorlevel% neq 0 (
    echo エラー: 依存関係のダウンロードに失敗しました
    pause
    exit /b 1
)

echo.
echo [2/4] アプリケーションをコンパイル中...
call gradlew.bat compileJava
if %errorlevel% neq 0 (
    echo エラー: コンパイルに失敗しました
    pause
    exit /b 1
)

echo.
echo [3/4] JARファイルを作成中...
call gradlew.bat jar
if %errorlevel% neq 0 (
    echo エラー: JARファイルの作成に失敗しました
    pause
    exit /b 1
)

echo.
echo [4/4] Windows用実行ファイルを作成中...
call gradlew.bat createWindowsExe
if %errorlevel% neq 0 (
    echo 警告: EXEファイルの作成に失敗しました（jpackageが必要です）
    echo JARファイルは正常に作成されました
)

echo.
echo ビルド完了！
echo.
echo 作成されたファイル:
echo - build/libs/kindle-capture-1.0-SNAPSHOT.jar
echo - build/distributions/KindleCapture-1.0.exe (作成された場合)
echo.
echo アプリケーションを実行するには:
echo   java -jar build/libs/kindle-capture-1.0-SNAPSHOT.jar
echo.
pause
