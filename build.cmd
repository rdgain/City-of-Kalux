@echo off
setlocal
for %%I in ("%~dp0.") do set "REPO_ROOT=%%~fI"

:new_build_directory
set "BUILD_DIR=%TEMP%\kalux-build-%RANDOM%-%RANDOM%"
if exist "%BUILD_DIR%" goto :new_build_directory
mkdir "%BUILD_DIR%" >nul 2>nul
if not exist "%BUILD_DIR%" exit /b 1

set "SOURCE_LIST=%BUILD_DIR%\sources.txt"
set "JAR_FILE=%REPO_ROOT%\dist\City-of-Kalux.jar"
set "RESULT=0"
for /f "tokens=2" %%V in ('javac -version 2^>^&1') do set "JAVAC_VERSION=%%V"
set "COMPAT_OPTIONS=--release 8"
if "%JAVAC_VERSION:~0,2%"=="1." set "COMPAT_OPTIONS=-source 1.8 -target 1.8"

for /r "%REPO_ROOT%\src" %%F in (*.java) do call :add_source "%%F"
javac %COMPAT_OPTIONS% -encoding UTF-8 -d "%BUILD_DIR%" @"%SOURCE_LIST%"
if errorlevel 1 (
    set "RESULT=1"
    goto :cleanup
)
del "%SOURCE_LIST%" >nul 2>nul

xcopy "%REPO_ROOT%\images" "%BUILD_DIR%\images" /e /i /y >nul
if errorlevel 1 (
    set "RESULT=1"
    goto :cleanup
)
xcopy "%REPO_ROOT%\maps" "%BUILD_DIR%\maps" /e /i /y >nul
if errorlevel 1 (
    set "RESULT=1"
    goto :cleanup
)
xcopy "%REPO_ROOT%\notes" "%BUILD_DIR%\notes" /e /i /y >nul
if errorlevel 1 (
    set "RESULT=1"
    goto :cleanup
)
xcopy "%REPO_ROOT%\sounds" "%BUILD_DIR%\sounds" /e /i /y >nul
if errorlevel 1 (
    set "RESULT=1"
    goto :cleanup
)

if not exist "%REPO_ROOT%\dist" mkdir "%REPO_ROOT%\dist"
if exist "%JAR_FILE%" del "%JAR_FILE%"
jar cfe "%JAR_FILE%" game.KaluxGame -C "%BUILD_DIR%" .
if errorlevel 1 (
    set "RESULT=1"
    goto :cleanup
)
echo Created "%JAR_FILE%"

:cleanup
del "%SOURCE_LIST%" >nul 2>nul
rmdir /s /q "%BUILD_DIR%"
exit /b %RESULT%

:add_source
set "SOURCE=%~1"
set "SOURCE=%SOURCE:\=/%"
echo "%SOURCE%">>"%SOURCE_LIST%"
exit /b
