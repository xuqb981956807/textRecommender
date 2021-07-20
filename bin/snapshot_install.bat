@echo off
echo [INFO] install snapshot module to local repository.

cd %~dp0
cd ..
call mvn clean install -Pinstall.snapshot -Dmaven.test.skip=true
cd bin
pause