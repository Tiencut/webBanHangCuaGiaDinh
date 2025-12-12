param(
    [string]$OutDir = 'D:\TienCut\code3\.ssh',
    [string]$KeyName = 'deploy_key'
)

Write-Host "Creating SSH key in: $OutDir" -ForegroundColor Cyan
if (!(Test-Path $OutDir)) {
    New-Item -ItemType Directory -Path $OutDir | Out-Null
    Write-Host "Created directory: $OutDir"
}

$privateKeyPath = Join-Path $OutDir $KeyName
$publicKeyPath  = Join-Path $OutDir ($KeyName + '.pub')

# Check ssh-keygen availability
try {
    & ssh -V 2>$null | Out-Null
} catch {
    Write-Host "ssh client (ssh-keygen) not found in PATH. On Windows 10/11 enable OpenSSH Client feature or install Git for Windows." -ForegroundColor Yellow
    return
}

if (Test-Path $privateKeyPath) {
    Write-Host "Private key already exists at $privateKeyPath" -ForegroundColor Yellow
    Write-Host "If you want to overwrite, remove the file first or choose a different KeyName." -ForegroundColor Yellow
    exit 0
}

# Generate key (ed25519, no passphrase)
Write-Host "Generating ed25519 key pair..." -ForegroundColor Green

# Locate ssh-keygen executable
try {
    $sshCmd = (Get-Command ssh-keygen -ErrorAction Stop).Source
} catch {
    Write-Host "ssh-keygen not found in PATH. Please install OpenSSH client." -ForegroundColor Red
    return
}

# Build argument list (pass empty string for -N to set no passphrase)
# Build arguments and invoke ssh-keygen directly (call operator) to avoid Start-Process quoting issues
$args = @('-t', 'ed25519', '-C', "deploy@$env:COMPUTERNAME", '-f', $privateKeyPath, '-N', '""')
try {
    & $sshCmd @args
} catch {
    Write-Host "ssh-keygen execution failed: $_" -ForegroundColor Red
}

if ((Test-Path $privateKeyPath) -and (Test-Path $publicKeyPath)) {
    Write-Host "Key pair created successfully:" -ForegroundColor Green
    Write-Host "  Private: $privateKeyPath"
    Write-Host "  Public : $publicKeyPath"

    # Show public key and copy to clipboard
    $pub = Get-Content $publicKeyPath -Raw
    Write-Host "\n----- PUBLIC KEY -----" -ForegroundColor Cyan
    Write-Host $pub
    Write-Host "----------------------\n" -ForegroundColor Cyan

    try {
        Set-Clipboard -Value $pub
        Write-Host "Public key copied to clipboard. You can now paste it into the server's ~/.ssh/authorized_keys or send it to your admin." -ForegroundColor Green
    } catch {
        Write-Host "Could not copy to clipboard automatically. Open the file and copy manually:" -ForegroundColor Yellow
        Write-Host $publicKeyPath
    }

    Write-Host "Next steps:" -ForegroundColor White
    Write-Host " 1) Add the public key to the server's ~/.ssh/authorized_keys for the deploy user." -ForegroundColor White
    Write-Host " 2) Add the private key content as GitHub secret named SSH_PRIVATE_KEY (paste entire file including BEGIN/END)." -ForegroundColor White
    Write-Host "    PowerShell to print private key: Get-Content $privateKeyPath -Raw" -ForegroundColor Gray
    Write-Host " 3) Upload docker-compose.yml and .env to the REMOTE_COMPOSE_DIR on the server." -ForegroundColor White
    Write-Host " 4) Add repository secrets: SSH_HOST, SSH_USER, SSH_PORT (optional), SSH_PRIVATE_KEY, REMOTE_COMPOSE_DIR." -ForegroundColor White
}
else {
    Write-Host "Key generation failed or files not found." -ForegroundColor Red
    exit 1
}
