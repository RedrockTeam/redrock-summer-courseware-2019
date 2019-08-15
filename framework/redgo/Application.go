// Time : 2019/8/15 下午2:45 
// Author : MashiroC

// redgo something
package redgo

import (
	"log"
	"math"
	"net/http"
	"strings"
	"unsafe"
)

type App struct {
	r     Router
	wsMap map[string]WebSocketConfig
}

type Handle = func(Params) string

type Router = map[string]Handle

type Params = map[string]interface{}

func Default() (app *App) {
	m := make(Router, 10)
	ws := make(map[string]WebSocketConfig, 1)
	return &App{r: m, wsMap: ws}
}

func (app *App) GET(uri string, h Handle) {
	app.r["GET:"+uri] = h
}

func (app *App) Start(addr string) {
	http.Handle("/", app)
	if err := http.ListenAndServe(addr, nil); err != nil {
		log.Fatal("what's your problem?")
	}
}

func (app *App) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	uri := r.RequestURI
	arr := strings.Split(uri, "?")
	method := r.Method
	h, ok := app.r[method+":"+arr[0]]

	if !ok {
		w.Write(QuickStringToBytes("404 not found"))
		return
	}

	upgrade := r.Header.Get("Upgrade")
	if upgrade == "websocket" {
		c, ok := app.wsMap[arr[0]]
		if !ok {
			w.Write(QuickStringToBytes("404 not found"))
			return
		}
		param := make(Params, 3)
		param["request"] = r
		param["response"] = w
		param["config"] = c
		h(param)
		return
	}
	param := parseParam(arr[1])

	res := h(param)

	w.Write(QuickStringToBytes(res))

}

func parseBinToInt(b []bool) (res int) {
	pos := 0
	for i := len(b) - 1; i >= 0; i-- {
		if b[i] {
			res += int(math.Pow(float64(2), float64(pos)))
		}
		pos++
	}
	return
}

func parseIntToBin(i int) (b []bool) {
	b = make([]bool, 8)
	pos := 7
	for i != 0 {
		add := false
		tmp := i % 2
		i = i / 2
		if tmp == 1 {
			add = true
		}
		b[pos] = add
		pos--
	}
	return
}

func parseParam(uri string) Params {
	param := make(Params, strings.Count(uri, "="))
	kv := strings.Split(uri, "&")
	for _, str := range kv {
		tmp := strings.Split(str, "=")
		key := tmp[0]
		value := tmp[1]
		param[key] = value
	}
	return param
}

func QuickBytesToString(b []byte) (s string) {
	return *(*string)(unsafe.Pointer(&b))
}

func QuickStringToBytes(s string) (b []byte) {
	return *(*[]byte)(unsafe.Pointer(&s))
}
