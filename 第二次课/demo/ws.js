const ws = require('ws')

const WebSocketServer = ws.Server
const wss = new WebSocketServer({ port: 8000 })

wss.on('connection', ws => {
    console.log('client connected')
    ws.on('message', function (message) {
        console.log(message)
        ws.send(message)
    })
})

console.log('web socket is running')
