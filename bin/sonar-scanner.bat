@echo off
echo [INFO] package snapshot module to target directory.

cd %~dp0
cd ..
call mvn clean sonar:sonar -Psonar
cd bin
pause