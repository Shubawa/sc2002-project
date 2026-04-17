#!/bin/bash
javac -sourcepath src -d out $(find src -name "*.java")
java -cp out Main
