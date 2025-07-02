@echo off
echo ================================
echo Kindle Capture Debug Launcher
echo ================================
echo.

echo Java Version Check:
java -version
echo.

echo JavaFX Module Check:
java --list-modules | findstr javafx
echo.

echo Starting Kindle Capture with detailed logging...
echo.

REM JARファイルから実行（デバッグ用）
if exist "build\libs\kindle-capture-1.0-SNAPSHOT.jar" (
    echo Running from JAR file...
    java --add-modules=javafx.controls,javafx.fxml --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED -jar "build\libs\kindle-capture-1.0-SNAPSHOT.jar"
) else (
    echo JAR file not found in build\libs\
    echo Building application first...
    call gradlew.bat shadowJar
    if exist "build\libs\kindle-capture-1.0-SNAPSHOT.jar" (
        echo Now running from JAR file...
        java --add-modules=javafx.controls,javafx.fxml --add-opens=java.desktop/java.awt=ALL-UNNAMED --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED -jar "build\libs\kindle-capture-1.0-SNAPSHOT.jar"
    ) else (
        echo ERROR: Failed to build JAR file
    )
)

echo.
echo Press any key to exit...
pause > nul
