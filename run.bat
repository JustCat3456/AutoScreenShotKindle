@echo off
echo Kindle Captureアプリケーションを起動します...
echo.

:: Java実行可能ファイルのパスを確認
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo エラー: Javaが見つかりません。Java 17以上をインストールしてください。
    pause
    exit /b 1
)

:: Javaバージョンを確認
echo Javaバージョンを確認中...
java -version

echo.
echo アプリケーションを起動中...
echo 注意: このアプリケーションはWindows環境でのみ動作します。

:: JavaFXモジュールパスを設定してアプリケーションを実行
java --module-path "lib" ^
     --add-modules javafx.controls,javafx.fxml ^
     --add-opens java.desktop/java.awt=ALL-UNNAMED ^
     --add-opens java.desktop/sun.awt.windows=ALL-UNNAMED ^
     -cp "build/libs/*" ^
     com.kindlecapture.KindleCaptureApp

if %errorlevel% neq 0 (
    echo.
    echo アプリケーションの実行中にエラーが発生しました。
    echo build/libs/kindle-capture-1.0-SNAPSHOT.jar が存在することを確認してください。
    pause
)

echo.
echo アプリケーションが終了しました。
pause
