@echo off
dir /s /b src\*.java > sources.txt
javac -sourcepath src -d out @sources.txt
java -cp out Main
pause
