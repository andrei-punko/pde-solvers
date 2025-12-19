@echo off
rem Start Spring Boot backend server
echo Starting backend server...
call gradlew.bat :web-ui:backend:bootRun
