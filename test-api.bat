@echo off
echo Testing Backend API...
echo.
echo 1. Testing health endpoint:
curl -X GET http://localhost:8080/api/health
echo.
echo.
echo 2. Testing test endpoint:
curl -X GET http://localhost:8080/api/test
echo.
echo.
echo 3. Testing suppliers template endpoint:
curl -X GET http://localhost:8080/api/suppliers/template -o template_test.csv
echo Downloaded template to template_test.csv
echo.
pause
