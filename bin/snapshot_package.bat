@echo off
echo [INFO] package snapshot module to target directory.

cd %~dp0
cd ..
call mvn clean package -Dmaven.test.skip=true
cd bin
pause