@echo off
cd web-ban-hang-gia-dinh
echo Starting backend...
call mvnw spring-boot:run
pause

netstat -ano | findstr :8080