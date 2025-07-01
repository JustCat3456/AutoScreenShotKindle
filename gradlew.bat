@echo off
rem Gradle Wrapper Bootstrap Script for Windows

setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
rem Setup Gradle classpath
if not exist "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" (
    echo Downloading Gradle Wrapper...
    powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.5.0/gradle/wrapper/gradle-wrapper.jar' -OutFile '%APP_HOME%\gradle\wrapper\gradle-wrapper.jar'}"
)

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar

rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

:end
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable GRADLE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd_ return code of the console.
if not "" == "%GRADLE_EXIT_CONSOLE%" exit /b %ERRORLEVEL%
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
