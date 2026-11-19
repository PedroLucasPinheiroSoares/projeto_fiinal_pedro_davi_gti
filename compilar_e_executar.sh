#!/bin/bash
mkdir -p bin
javac -encoding UTF-8 -d bin src/Main.java src/*/*.java && java -cp bin Main
