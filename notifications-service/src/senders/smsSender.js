const fetch = require('node-fetch')

async function send({ phone, text }) {
    const url = process.env.SMS_API_URL
    if (!url) throw new Error('SMS_API_URL not configured')
    const res = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ to: phone, message: text, apiKey: process.env.SMS_API_KEY }),
    })
    if (!res.ok) throw new Error('sms failed ' + res.status)
    return res.json()
}

module.exports = { send }