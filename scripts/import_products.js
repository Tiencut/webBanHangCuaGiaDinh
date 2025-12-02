#!/usr/bin/env node
/*
 Simple import script (Node.js) for one-time large CSV imports.

 Usage:
   npm install csv-parse minimist
   node scripts/import_products.js <path/to/file.csv> --url=http://localhost:8080 --batch=500 --concurrency=5

 Behavior:
 - Parses CSV (first row headers)
 - Maps CSV columns to CreateProductRequest fields by header name (case-insensitive)
 - Sends POST /api/products/create for each row with concurrency control
 - Prints progress and saves errors to import-errors.json
*/

const fs = require('fs')
const path = require('path')
const { parse } = require('csv-parse/sync')

const argv = require('minimist')(process.argv.slice(2))
const filePath = argv._[0]
if (!filePath) {
  console.error('Usage: node scripts/import_products.js <file.csv> --url=http://localhost:8080 --batch=500 --concurrency=5')
  process.exit(1)
}

const baseUrl = argv.url || 'http://localhost:8080'
const batch = parseInt(argv.batch || '500', 10)
const concurrency = parseInt(argv.concurrency || '5', 10)

function sleep(ms) { return new Promise(r => setTimeout(r, ms)) }

async function postJson(url, body) {
  const full = url.startsWith('http') ? url : `${baseUrl}${url}`
  if (typeof fetch === 'function') {
    const res = await fetch(full, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    })
    const text = await res.text()
    return { ok: res.ok, status: res.status, text }
  }

  return new Promise((resolve, reject) => {
    const data = JSON.stringify(body)
    const u = new URL(full)
    const opts = {
      hostname: u.hostname,
      port: u.port || (u.protocol === 'https:' ? 443 : 80),
      path: u.pathname + (u.search || ''),
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Content-Length': Buffer.byteLength(data)
      }
    }
    const lib = u.protocol === 'https:' ? require('https') : require('http')
    const req = lib.request(opts, (res) => {
      let chunks = []
      res.on('data', c => chunks.push(c))
      res.on('end', () => {
        const text = Buffer.concat(chunks).toString('utf8')
        resolve({ ok: res.statusCode >= 200 && res.statusCode < 300, status: res.statusCode, text })
      })
    })
    req.on('error', reject)
    req.write(data)
    req.end()
  })
}

async function main() {
  const csvText = fs.readFileSync(path.resolve(filePath), 'utf8')
  const records = parse(csvText, { columns: true, skip_empty_lines: true })
  console.log(`Parsed ${records.length} rows from ${filePath}`)

  const errors = []
  let done = 0

  // simple concurrency queue
  const queue = [...records]
  async function worker(id) {
    while (queue.length > 0) {
      const row = queue.shift()
      if (!row) break
      const payload = mapRowToCreateRequest(row)
      try {
        const res = await postJson('/api/products/create', payload)
        if (!res.ok) {
          errors.push({ row: payload, status: res.status, text: res.text })
        }
      } catch (err) {
        errors.push({ row: payload, error: String(err) })
      }
      done++
      if (done % 50 === 0) console.log(`Progress: ${done}/${records.length}`)
      await sleep(10)
    }
  }

  const workers = []
  for (let i = 0; i < concurrency; i++) workers.push(worker(i))
  await Promise.all(workers)

  console.log(`Import finished. Success: ${records.length - errors.length}, Errors: ${errors.length}`)
  if (errors.length > 0) fs.writeFileSync('import-errors.json', JSON.stringify(errors, null, 2))
}

function mapRowToCreateRequest(row) {
  const get = (k) => {
    const key = Object.keys(row).find(x => x.toLowerCase() === k.toLowerCase())
    return key ? row[key] : undefined
  }
  const parseNum = (v) => {
    if (v == null || v === '') return null
    const n = Number(String(v).replace(/[,\s]/g, ''))
    return Number.isNaN(n) ? null : n
  }

  const req = {
    name: get('name') || get('ten') || get('product_name'),
    code: get('code') || get('ma') || get('product_code'),
    description: get('description') || get('mo_ta'),
    categoryId: get('categoryId') ? Number(get('categoryId')) : undefined,
    basePrice: get('basePrice') ? parseNum(get('basePrice')) : undefined,
    sellingPrice: get('sellingPrice') ? parseNum(get('sellingPrice')) : undefined,
    costPrice: get('costPrice') ? parseNum(get('costPrice')) : undefined,
    weight: parseNum(get('weight')),
    dimensions: get('dimensions'),
    brand: get('brand'),
    model: get('model'),
    vehicleType: get('vehicleType') || get('vehicle_type'),
    partNumber: get('partNumber') || get('part_number'),
    oemNumber: get('oemNumber') || get('oem_number'),
    warrantyPeriod: get('warrantyPeriod') ? Number(get('warrantyPeriod')) : undefined,
    status: (get('status') || 'ACTIVE'),
    minStockLevel: get('minStockLevel') ? Number(get('minStockLevel')) : undefined,
    reorderPoint: get('reorderPoint') ? Number(get('reorderPoint')) : undefined,
    isCombo: (String(get('isCombo') || '').toLowerCase() === 'true') || undefined,
    isSubstitutable: (String(get('isSubstitutable') || '').toLowerCase() === 'true') || undefined,
    imageUrls: get('imageUrls') ? String(get('imageUrls')).split(';').map(s=>s.trim()).filter(Boolean) : undefined,
    tags: get('tags') ? String(get('tags')).split(';').map(s=>s.trim()).filter(Boolean) : undefined,
    compatibleVehicleIds: get('compatibleVehicleIds') ? String(get('compatibleVehicleIds')).split(';').map(s=>Number(s.trim())).filter(Boolean) : undefined
  }
  Object.keys(req).forEach(k => { if (req[k] === undefined || req[k] === null || req[k] === '') delete req[k] })
  return req
}

main().catch(err => { console.error(err); process.exit(2) })
