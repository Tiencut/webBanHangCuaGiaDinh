require('dotenv').config()
const express = require('express')
const bodyParser = require('body-parser')
const { Queue } = require('bullmq')
const IORedis = require('ioredis')

const app = express()
app.use(bodyParser.json())

const connection = new IORedis(process.env.REDIS_URL || 'redis://127.0.0.1:6379')
const queue = new Queue('notifications', { connection })

app.post('/enqueue', async(req, res) => {
    const { name, data } = req.body
    if (!name || !data) return res.status(400).json({ error: 'name and data required' })
    const job = await queue.add(name, { data }, { attempts: parseInt(process.env.RETRY_ATTEMPTS || '3', 10) })
    res.json({ ok: true, jobId: job.id })
})

const port = process.env.PORT || 3100
app.listen(port, () => console.log('notifications service listening on', port))