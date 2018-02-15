@echo off
cd scripts
call remove_previous.py
cd ..
call gradlew.bat publishToMavenLocal > build-maven.log
cd scripts
call upload_api.py
call temp_batchfile.bat
call remove_previous.py
cd ..
call gradlew.bat build > build.log
echo Jars build. You can upload now
pause