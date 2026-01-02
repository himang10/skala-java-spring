# windows에서 ps1 파일의 실행 권한을 주려면
Unblock-File -Path .\build.ps1
Unblock-File -Path .\run.ps1

# linux/macbook에서 .sh 실행 권한을 주려면
chmod  +x build.sh
chmod  +x run.sh
