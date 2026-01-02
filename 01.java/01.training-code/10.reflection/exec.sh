#!/bin/bash

javac -d target *.java
java -cp target ReflectionExample$1
