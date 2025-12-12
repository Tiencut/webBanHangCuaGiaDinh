# webBanHangCuaGiaDinh

Quick notes:

- Run local dev stack (Postgres + backend + frontend):

```powershell
# from repository root
copy .env.example .env
docker compose --env-file .env compose up -d db
cd web-ban-hang-gia-dinh
.\mvnw spring-boot:run
```

- Run tests locally:

```powershell
cd web-ban-hang-gia-dinh
.\mvnw -B test
```

See `docker-compose.yml` and `.env.example` for configuration examples.

Deploying to a VPS (SSH + Docker Compose)
---------------------------------------

This repo includes a GitHub Actions workflow `deploy-to-vps.yml` that:

- builds the backend jar
- builds and pushes Docker images to GHCR
- SSHes to your VPS and runs `docker compose pull` + `docker compose up -d`

Required repository secrets (set in GitHub Settings -> Secrets):

- `SSH_HOST` - your server IP or hostname
- `SSH_USER` - SSH username
- `SSH_PORT` - SSH port (defaults to `22`)
- `SSH_PRIVATE_KEY` - private key (PEM) for SSH (no passphrase or use ssh-agent)
- `REMOTE_COMPOSE_DIR` - path on the server where `docker-compose.yml` is located (e.g. `/home/deploy/app`)

Notes:

- The workflow uses the `GITHUB_TOKEN` to push images to GHCR. Ensure `Settings -> Actions -> General` allows `GITHUB_TOKEN` to write packages, or replace with a personal access token stored in secrets with `write:packages` scope.
- On the VPS, install Docker and Docker Compose, place `docker-compose.yml` and `.env` in `REMOTE_COMPOSE_DIR`, and add the public SSH key from `SSH_PRIVATE_KEY` to `~/.ssh/authorized_keys` for the `SSH_USER`.

