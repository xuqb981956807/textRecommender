@echo off
echo [INFO] deploy snapshot module to nexus repository.

cd %~dp0
cd ..
call mvn clean deploy -Pdeploy.snapshot -Dmaven.test.skip=true
cd bin
pause