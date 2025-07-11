@echo off
REM Kindle Capture Launcher
REM This script ensures proper JavaFX module loading

setlocal enabledelayedexpansion

echo Starting Kindle Capture...

REM Get the directory where this script is located
set "APP_DIR=%~dp0"

REM Look for the JAR file in the app directory
for %%f in ("%APP_DIR%app\*.jar") do (
    set "JAR_FILE=%%f"
    goto :found_jar
)

echo ERROR: Could not find JAR file in %APP_DIR%app\
pause
exit /b 1

:found_jar
echo Found JAR: !JAR_FILE!

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or later from https://adoptium.net/
    pause
    exit /b 1
)

REM Launch the application - simplified approach since JavaFX is embedded in JAR
echo Launching application...
java -jar "!JAR_FILE!"

if errorlevel 1 (
    echo.
    echo ERROR: Application failed to start
    echo.
    echo Debug information:
    echo Java version:
    java -version
    echo.
    echo JAR file info:
    dir "!JAR_FILE!"
    echo.
    echo Trying with additional JavaFX settings...
    java -Dprism.order=sw -Djava.awt.headless=false -jar "!JAR_FILE!"
    echo.
    pause
)

endlocal
