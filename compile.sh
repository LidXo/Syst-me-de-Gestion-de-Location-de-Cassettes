#!/bin/bash
mkdir -p out lib
if ! ls lib/mysql-connector-j-*.jar 1>/dev/null 2>&1; then
    echo "ERREUR : Placez mysql-connector-j-*.jar dans lib/"
    exit 1
fi
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -cp "lib/*" -d out @sources.txt
[ $? -eq 0 ] && echo "[OK] Compilation réussie — lancez ./run.sh" || echo "[ERREUR]"
rm sources.txt
