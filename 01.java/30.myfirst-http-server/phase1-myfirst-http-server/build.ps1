# target 디렉토리 없으면 생성
if (!(Test-Path target)) {
    New-Item -ItemType Directory -Path target | Out-Null
}

# src 폴더 내 모든 java 파일 찾기
$files = Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { $_.FullName }

# javac 실행
javac -cp "lib/*" -d target $files

Write-Output "컴파일 완료!"

