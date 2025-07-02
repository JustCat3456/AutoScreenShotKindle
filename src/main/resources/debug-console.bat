@echo off
REM Kindle Capture Debug Console Version
REM Shows all output in console window

title Kindle Capture Debug Console

echo =====================================
echo Kindle Capture - Debug Console Mode
echo =====================================
echo.

echo Environment Check:
echo Java Version:
java -version 2>&1
echo.

echo Java Home: %JAVA_HOME%
echo Current Directory: %CD%
echo.

echo Looking for application files...
if exist "app\*.jar" (
    echo Found JAR files in app directory:
    dir app\*.jar /b
    echo.
    
    for %%f in (app\*.jar) do (
        echo Starting application: %%f
        echo.
        echo === APPLICATION OUTPUT ===
        java --add-modules=javafx.controls,javafx.fxml,javafx.base,javafx.graphics ^
             --add-opens=java.desktop/java.awt=ALL-UNNAMED ^
             --add-opens=java.desktop/sun.awt.windows=ALL-UNNAMED ^
             --add-exports=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED ^
             -Djava.awt.headless=false ^
             -Dprism.order=sw ^
             -jar "%%f"
        echo === APPLICATION ENDED ===
        goto :end
    )
) else (
    echo ERROR: No JAR files found in app directory
    echo.
    echo Directory contents:
    if exist "app" (
        dir app /b
    ) else (
        echo app directory does not exist
    )
)

:end
echo.
echo Press any key to close this window...
pause > nul
