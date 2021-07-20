@echo off
echo [INFO] deploy release module to nexus repository.

cd %~dp0
cd ..
call mvn release:prepare -Pdeploy.prepare
call mvn release:perform -X -DuseReleaseProfile=false -Pdeploy.perform
cd bin
pause