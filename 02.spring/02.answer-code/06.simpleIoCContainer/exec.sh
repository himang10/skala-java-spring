#!/bin/bash

javac -d target src/*.java
java -cp target SimpleIoCContainer
