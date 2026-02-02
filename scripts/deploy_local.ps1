param(
    [string]$RemoteHost,
    [string]$RemoteUser = "$env:USERNAME",
    [string]$RemoteDir = "/home/$env:USERNAME/deploy",
    [string]$KeyPath = "$env:USERPROFILE\.ssh\id_ed25519",
    [string]$ComposeFile = "docker-compose.prod.yml",
    [string]$EnvFile = ".env.prod",
    [switch]$RunRemote
)

if (-not (Test-Path $ComposeFile)) {
    Write-Error "Compose file '$ComposeFile' not found in current directory"
    exit 1
}

if ($EnvFile -and -not (Test-Path $EnvFile)) {
    Write-Warning "Env file '$EnvFile' not found; continuing without it"
}

if (-not $RemoteHost) {
    Write-Error "RemoteHost is required. Example: .\deploy_local.ps1 -RemoteHost 1.2.3.4 -RemoteUser ubuntu -RemoteDir /home/ubuntu/deploy -KeyPath C:\Users\you\.ssh\deploy_key -RunRemote"
    exit 1
}

$scpKeyPart = if (Test-Path $KeyPath) { "-i `"$KeyPath`"" } else { "" }

Write-Output "Creating remote directory $RemoteDir on $RemoteUser@$RemoteHost"
ssh $scpKeyPart $RemoteUser@$RemoteHost "mkdir -p '$RemoteDir' && chmod 700 '$RemoteDir'"

Write-Output "Uploading compose file..."
scp $scpKeyPart $ComposeFile $RemoteUser@$RemoteHost:"$RemoteDir/$(Split-Path $ComposeFile -Leaf)"

if (Test-Path $EnvFile) {
    Write-Output "Uploading env file..."
    scp $scpKeyPart $EnvFile $RemoteUser@$RemoteHost:"$RemoteDir/$(Split-Path $EnvFile -Leaf)"
}

Write-Output "Uploading remote_deploy.sh"
scp $scpKeyPart scripts/remote_deploy.sh $RemoteUser@$RemoteHost:"$RemoteDir/remote_deploy.sh"

if ($RunRemote) {
    Write-Output "Running remote deploy script..."
    ssh $scpKeyPart $RemoteUser@$RemoteHost "bash -lc 'cd \"$RemoteDir\" && chmod +x remote_deploy.sh && ./remote_deploy.sh'"
}

Write-Output "Done. If you didn't run remote deploy, SSH to the server and run:"
Write-Output "  cd $RemoteDir && chmod +x remote_deploy.sh && ./remote_deploy.sh"
