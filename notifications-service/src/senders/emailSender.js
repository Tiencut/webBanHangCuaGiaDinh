const nodemailer = require('nodemailer')

const transporter = nodemailer.createTransport({
    host: process.env.SMTP_HOST || 'localhost',
    port: process.env.SMTP_PORT ? parseInt(process.env.SMTP_PORT) : 1025,
    secure: false,
    auth: process.env.SMTP_USER ? { user: process.env.SMTP_USER, pass: process.env.SMTP_PASS } : undefined,
})

async function send({ to, subject, html }) {
    const info = await transporter.sendMail({ from: process.env.FROM_EMAIL || 'no-reply@example.com', to, subject, html })
    return info
}

module.exports = { send }