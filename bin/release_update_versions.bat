@echo off
echo [INFO] deploy release module to nexus repository.

cd %~dp0
cd ..
call mvn  --batch-mode release:update-versions -DautoVersionSubmodules=true -DdevelopmentVersion=1.0.1-SNAPSHOT
cd bin
pause