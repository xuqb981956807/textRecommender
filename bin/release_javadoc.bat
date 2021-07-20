@echo off
echo [INFO] java doc to target directory.

cd %~dp0
cd ..
call mvn clean javadoc:javadoc -Pjavadoc
cd bin
pause