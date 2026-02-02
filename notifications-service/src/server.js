require('dotenv').config()
const express = require('express')
const { Queue } = require('bullmq')
const IORedis = require('ioredis')
const pino = require('pino')

const logger = pino()
const app = express()
app.use(express.json())

const connection = new IORedis(process.env.REDIS_URL || 'redis://127.0.0.1:6379')
const queue = new Queue('notifications', { connection })

const RETRY_ATTEMPTS = parseInt(process.env.RETRY_ATTEMPTS || '3')

app.post('/enqueue', async(req, res) => {
    try {
        const { type, template, data } = req.body
        if (!type || !template || !data) return res.status(400).json({ error: 'type, template, data required' })

        const job = await queue.add(template, { type, template, data }, { attempts: RETRY_ATTEMPTS, backoff: { type: 'exponential', delay: 2000 } })
        return res.json({ jobId: job.id })
    } catch (e) {
        logger.error(e)
        return res.status(500).json({ error: e.message })
    }
})

app.get('/health', (req, res) => res.json({ ok: true }))

const port = process.env.PORT || 3001
app.listen(port, () => logger.info(`notifications-service listening on ${port}`))