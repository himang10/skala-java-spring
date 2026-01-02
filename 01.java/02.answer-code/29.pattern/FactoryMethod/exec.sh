#!/bin/bash

# Factory Method Pattern 실행 스크립트

echo "Compiling Factory Method Pattern example..."
javac *.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "Running PizzaTestDrive..."
    echo "================================"
    java PizzaTestDrive
else
    echo "Compilation failed!"
    exit 1
fi
