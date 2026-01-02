#!/bin/bash

# 인자가 숫자인지 확인
if ! [[ $1 =~ ^[0-9]+$ ]]; then
    echo "오류: 숫자를 입력해야 합니다."
    exit 1
fi

# 파일이 존재하는지 확인
if [ ! -f "skalatest$1.java" ]; then
    echo "오류: skalatest$1.java 파일이 존재하지 않습니다."
    exit 1
fi

# 모든 검증을 통과하면 원래 명령 실행
javac -d bin skalatest$1.java
java -cp bin skalajava.skalatest$1
