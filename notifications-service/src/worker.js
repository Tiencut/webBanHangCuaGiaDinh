require('dotenv').config()
const { Worker } = require('bullmq')
const IORedis = require('ioredis')
const pino = require('pino')
const path = require('path')
const Handlebars = require('handlebars')
const fs = require('fs')
const emailSender = require('./senders/emailSender')
const smsSender = require('./senders/smsSender')
const client = require('prom-client')
const http = require('http')

const logger = pino()
const connection = new IORedis(process.env.REDIS_URL || 'redis://127.0.0.1:6379')

const templatesDir = path.join(__dirname, '..', 'templates')

function compileTemplate(name) {
    const p = path.join(templatesDir, `${name}.hbs`)
    if (!fs.existsSync(p)) return null
    const src = fs.readFileSync(p, 'utf8')
    return Handlebars.compile(src)
}

const tmplCache = {}

// Prometheus metrics
const register = new client.Registry()
const jobsProcessed = new client.Counter({ name: 'notifications_jobs_processed_total', help: 'Total processed jobs' })
const jobsFailed = new client.Counter({ name: 'notifications_jobs_failed_total', help: 'Total failed jobs' })
register.registerMetric(jobsProcessed)
register.registerMetric(jobsFailed)

const worker = new Worker('notifications', async job => {
    const { name } = job
    const payload = job.data.data || job.data
    // Handle email jobs
    if (name === 'order-confirmation' || name === 'stock-low' || name === 'order-confirmed' || name === 'order-shipped' || name === 'order-delivered' || name === 'payment-failure') {
        const tpl = tmplCache[name] || (tmplCache[name] = compileTemplate(name) || compileTemplate('order-confirmation'))
        if (!tpl) throw new Error('template not found: ' + name)
        const html = tpl(payload)
        // email payload must include to and subject
        await emailSender.send({ to: payload.to, subject: payload.subject || 'Thông báo', html })
        return { ok: true }
    }

    if (name === 'sms') {
        // payload should have phone and text
        await smsSender.send({ phone: payload.phone, text: payload.text })
        return { ok: true }
    }

    // unknown job
    throw new Error('unknown job type: ' + name)
}, { connection })

worker.on('completed', job => {
    logger.info({ jobId: job.id }, 'job completed')
    jobsProcessed.inc()
})
worker.on('failed', (job, err) => {
    logger.error({ jobId: job.id, err: err.message }, 'job failed')
    jobsFailed.inc()
})

// expose metrics
const metricsPort = parseInt(process.env.METRICS_PORT || '9400', 10)
http.createServer(async (req, res) => {
    if (req.url === '/metrics') {
        res.setHeader('Content-Type', register.contentType)
        res.end(await register.metrics())
    } else {
        res.statusCode = 404
        res.end('Not found')
    }
}).listen(metricsPort)
logger.info(`metrics server listening on ${metricsPort}`)