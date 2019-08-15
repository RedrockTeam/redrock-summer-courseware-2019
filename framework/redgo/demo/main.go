// Time : 2019/8/15 下午2:44 
// Author : MashiroC

// demo something
package main

import (
	"fmt"
	"mashiroc.fun/redgo"
)

func main() {
	app := redgo.Default()
	app.GET("/hello", func(p redgo.Params) string {
		return "hello," + (p["name"]).(string)
	})
	app.WebSocket("/ws", redgo.WebSocketConfig{
		OnOpen: func(context *redgo.WebSocketContext) {
			fmt.Println("open!")
		},
		OnClose: func(context *redgo.WebSocketContext) {
			fmt.Println("close!")
		},
		OnMessage: func(context *redgo.WebSocketContext, s string) {
			fmt.Println(context.Conn.RemoteAddr(),"send message:", s)
		},
		OnError: nil,
	})
	app.Start(":1234")
}
