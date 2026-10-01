@echo off
REM Compila todos los .java a la carpeta out y ejecuta Main.
if exist out rmdir /s /q out
dir /s /b src\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
del fuentes.txt
java -cp out pe.edu.uni.menu.app.Main %*
