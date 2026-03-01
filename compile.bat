@echo off
echo ============================================================
echo  VideoClub Manager — Compilation
echo ============================================================
mkdir out 2>nul
mkdir lib 2>nul
if not exist lib\mysql-connector-j-8.x.x.jar (
    echo ERREUR : Placez mysql-connector-j-*.jar dans le dossier lib\
    pause & exit /b 1
)
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -cp "lib\*" -d out @sources.txt
if %ERRORLEVEL% EQU 0 (
    echo [OK] Compilation réussie — lancez run.bat
) else (
    echo [ERREUR] Echec compilation
)
del sources.txt
pause
