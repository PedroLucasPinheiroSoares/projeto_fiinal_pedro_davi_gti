@echo off
chcp 65001 >nul
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin src\Main.java src\apresentacao\*.java src\negocio\*.java src\dominio\*.java src\util\*.java
if %errorlevel%==0 java -cp bin Main
pause
