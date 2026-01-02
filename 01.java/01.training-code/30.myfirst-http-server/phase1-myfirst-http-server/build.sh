#!/bin/bash

# 컴파일
javac -cp "lib/*" -d target $(find src -name "*.java")
