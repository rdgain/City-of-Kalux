@echo off
setlocal
for %%I in ("%~dp0..") do set "REPO_ROOT=%%~fI"

:new_build_directory
set "BUILD_DIR=%TEMP%\kalux-headless-tests-%RANDOM%-%RANDOM%"
if exist "%BUILD_DIR%" goto :new_build_directory
mkdir "%BUILD_DIR%" >nul 2>nul
if not exist "%BUILD_DIR%" exit /b 1

set "SOURCE_LIST=%BUILD_DIR%\sources.txt"
set "RESULT=0"
for /r "%REPO_ROOT%\src" %%F in (*.java) do call :add_source "%%F"
for /r "%REPO_ROOT%\test\game" %%F in (*.java) do call :add_source "%%F"

javac -source 8 -target 8 -d "%BUILD_DIR%" @"%SOURCE_LIST%"
if errorlevel 1 (
    set "RESULT=1"
    goto :cleanup
)

java "-Djava.awt.headless=true" -cp "%BUILD_DIR%" game.GameRulesTests
set "RESULT=%ERRORLEVEL%"

:cleanup
del "%SOURCE_LIST%" >nul 2>nul
rmdir /s /q "%BUILD_DIR%"
exit /b %RESULT%

:add_source
set "SOURCE=%~1"
set "SOURCE=%SOURCE:\=/%"
echo "%SOURCE%">>"%SOURCE_LIST%"
exit /b
