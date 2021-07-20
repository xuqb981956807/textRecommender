@echo off
echo [INFO] deploy release module to nexus repository.

cd %~dp0
cd ..
call mvn release:rollback -Pdeploy.prepare 
cd bin
pause