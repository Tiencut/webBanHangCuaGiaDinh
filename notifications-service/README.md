# Notifications Service

Small microservice to enqueue notification jobs (email / sms) into a Redis-backed BullMQ queue and a worker to process them.

Requirements
- Node.js 18+
- Redis

Environment
Copy `.env.example` to `.env` and edit values.

Run

Install deps:
```bash
cd notifications-service
npm install
```

Start API server (enqueue endpoint):
```bash
npm run start
```

Start worker (process jobs):
```bash
npm run worker
```

API
- `POST /enqueue` body: `{ "name": "order-confirmation", "data": { ... } }` returns `{ ok: true, jobId }`.

Templates
- Templates are in `src/templates/*.hbs`.
