# Test Backend API PowerShell Script

Write-Host "Testing Backend API..." -ForegroundColor Green

# Test 1: Health check
Write-Host "`n1. Testing health endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method Get
    Write-Host "Health check: OK" -ForegroundColor Green
    $response | ConvertTo-Json
} catch {
    Write-Host "Health endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Download template
Write-Host "`n2. Testing template download..." -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri "http://localhost:8080/api/suppliers/template" -OutFile "template_test.csv"
    if (Test-Path "template_test.csv") {
        Write-Host "Template downloaded successfully:" -ForegroundColor Green
        Get-Content "template_test.csv" | Select-Object -First 3
    }
} catch {
    Write-Host "Template download failed: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Create test CSV
Write-Host "`n3. Creating test CSV file..." -ForegroundColor Yellow
$csvContent = @"
Mã nhà cung cấp,Tên nhà cung cấp,Số điện thoại,Email,Địa chỉ,Thành phố,Quận/Huyện,Phường/Xã,Vùng miền,Nhóm nhà cung cấp,Tổng giá trị mua,Nợ hiện tại,Ngày giao dịch cuối,Ghi chú
TEST001,Nhà cung cấp Test,0123456789,test@email.com,123 Test Street,Hà Nội,Ba Đình,Phúc Xá,Miền Bắc,Nhóm A,1000000,0,01/01/2024,Test import
TEST002,Công ty Test 2,0987654321,test2@email.com,456 Test Ave,TP.HCM,Quận 1,Phường 1,Miền Nam,Nhóm B,2000000,500000,15/06/2024,Test import 2
"@

$csvContent | Out-File -FilePath "test_import.csv" -Encoding UTF8
Write-Host "Test CSV created with 2 records" -ForegroundColor Green

# Test 4: Import CSV
Write-Host "`n4. Testing CSV import..." -ForegroundColor Yellow
try {
    $boundary = [System.Guid]::NewGuid().ToString()
    $fileName = "test_import.csv"
    $fileContent = [System.IO.File]::ReadAllBytes($fileName)
    
    $body = @"
--$boundary
Content-Disposition: form-data; name="file"; filename="$fileName"
Content-Type: text/csv

$([System.Text.Encoding]::UTF8.GetString($fileContent))
--$boundary
Content-Disposition: form-data; name="updateExisting"

false
--$boundary--
"@

    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/suppliers/import" -Method Post -ContentType "multipart/form-data; boundary=$boundary" -Body $body
    Write-Host "Import successful:" -ForegroundColor Green
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Import failed: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $errorDetails = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($errorDetails)
        $errorContent = $reader.ReadToEnd()
        Write-Host "Error details: $errorContent" -ForegroundColor Red
    }
}

Write-Host "`nAPI Testing completed!" -ForegroundColor Green

# Cleanup
if (Test-Path "template_test.csv") { Remove-Item "template_test.csv" }
if (Test-Path "test_import.csv") { Remove-Item "test_import.csv" }
