#!/bin/sh
# Compila todos los .java a la carpeta out y ejecuta Main.
rm -rf out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out pe.edu.uni.menu.app.Main "$@"
