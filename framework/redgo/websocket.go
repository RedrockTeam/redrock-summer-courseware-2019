// Time : 2019/8/15 下午3:09 
// Author : MashiroC

// redgo something
package redgo

import (
	"bufio"
	"crypto/sha1"
	"encoding/base64"
	"net"
	"net/http"
)

type WebSocketConfig struct {
	OnOpen    func(*WebSocketContext)
	OnClose   func(*WebSocketContext)
	OnMessage func(*WebSocketContext, string)
	OnError   func(*WebSocketContext)
}

type WebSocketContext struct {
	Conn net.Conn
	buf  *bufio.ReadWriter
}

func (c *WebSocketContext) Send(str string) {

}

func (app *App) WebSocket(uri string, config WebSocketConfig) {
	app.r["GET:"+uri] = enter
	app.wsMap[uri] = config
}

func enter(params Params) string {
	r := (params["request"]).(*http.Request)
	w := (params["response"]).(http.ResponseWriter)
	config := (params["config"]).(WebSocketConfig)

	key := r.Header.Get("Sec-WebSocket-Key")

	s := sha1.New()
	s.Write(QuickStringToBytes(key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"))
	b := s.Sum(nil)
	accept := base64.StdEncoding.EncodeToString(b)

	hijack := w.(http.Hijacker)
	con, buf, _ := hijack.Hijack()

	context := &WebSocketContext{
		Conn: con,
		buf:  buf,
	}

	upgrade := "HTTP/1.1 101 Switching Protocols\r\n" +
		"Upgrade: websocket\r\n" +
		"Connection: Upgrade\r\n" +
		"Sec-WebSocket-Accept: " + accept + "\r\n\r\n"
	buf.Write(QuickStringToBytes(upgrade))
	buf.Flush()

	config.OnOpen(context)

	go func() {
		for true {
			data := make([]byte, 2)
			_, err := buf.Read(data)
			if err != nil {
				wsClose(con, config.OnClose, context)
				break
			}

			bin1 := parseIntToBin(int(data[0]))
			bin2 := parseIntToBin(int(data[1]))

			// bin1
			// 0    1    2     3   4 5 6 7
			// FIN RSV1 RSV2 RSV3 opcode(4)

			// bin2
			// 8     9 10 11 12 13 14 15
			// MASK      PayloadLen

			// RSV
			if bin1[1] || bin1[2] || bin1[3] {
				wsClose(con, config.OnClose, context)
				break
			}

			if !bin2[0] {
				wsClose(con, config.OnClose, context)
				break
			}

			opcode := parseBinToInt(bin1[4:])

			payloadLen := parseBinToInt(bin2[1:])

			switch opcode {
			case 1:
				maskingKey := make([]byte, 4)
				buf.Read(maskingKey)

				payload := make([]byte, payloadLen)
				buf.Read(payload)

				data := make([]byte, payloadLen)
				for i := 0; i < payloadLen; i++ {
					data[i] = payload[i] ^ maskingKey[i%4]
				}

				config.OnMessage(context, QuickBytesToString(data))

			default:
				wsClose(con, config.OnClose, context)
				break
			}
		}
	}()
	return ""
}

func wsClose(conn net.Conn, onclose func(ctx *WebSocketContext), ctx *WebSocketContext) {
	onclose(ctx)
	conn.Close()
}
