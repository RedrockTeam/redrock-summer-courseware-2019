# Golang 基础

先来个HelloWorld吧！

```go
package main

import "fmt"

func main() {
    fmt.Println("Hello, World!")
}
```

## 基本语法

- 变量

```go
var name type

var name1,name2 type

name := value
name := fun()


var (
	name1	type1
    name2	type2
)
```

- 常量

```go
const name [type] = value

const (
	name1	type1
    name2	type2
)

// iota
const (
	a = iota
    b
    c
)
```

- 数组与切片

```go
// 定义数组
var arr1 [10] int
var arr2 = [5]int{1, 2, 3, 4, 5}
arr3 := [5]int{1, 2, 3, 4, 5}

// 定义切片
var slice1 []int
var slice2 = []int{1, 2, 3}
slice3 := []int{1, 2, 3}
slice4 := make([]int, 10)

// 截取切片
slice5 :=slice4[2:3]

// len函数 求长度 很多场景都可以用
size := len(slice4) // 0

// cap函数 求容量
c := cap(slice4) //10
```

- 条件语句(if)和循环(for)语句

```go
	name := "redrock"

	if name == "redrock" {
		fmt.Println("NB!")
	} else {
		fmt.Println("?")
	}

	for i := 0; i < len(name); i++ {
		fmt.Print(string(name[i]), " ")
	}
	fmt.Print("\n")

	slice := []int{1, 2, 3}
	for i, v := range slice {
		fmt.Println(i, v)
	}

	for name != "?" {
		fmt.Println("?")
	}

```

- 函数

```go
func main() {
	callback(fun0)
}

func fun0(){
	fmt.Println("this is a function.")
}

func fun1(a int) string {
	fmt.Println("param:", a)
	return "ok"
}

func fun2(a int) (res int) {
	res = a + 1
	return
}

func callback(a func()) {
	a()
}
```

- map

```go
 	var countryCapitalMap map[string]string /*创建集合 */
    countryCapitalMap = make(map[string]string)

    /* map插入key - value对,各个国家对应的首都 */
    countryCapitalMap [ "France" ] = "巴黎"
    countryCapitalMap [ "Italy" ] = "罗马"
    countryCapitalMap [ "Japan" ] = "东京"
    countryCapitalMap [ "India " ] = "新德里"

    /*使用键输出地图值 */ 
    for country := range countryCapitalMap {
        fmt.Println(country, "首都是", countryCapitalMap [country])
    }

    /*查看元素在集合中是否存在 */
    capital, ok := countryCapitalMap [ "American" ] /*如果确定是真实的,则存在,否则不存在 */
    /*fmt.Println(capital) */
    /*fmt.Println(ok) */
    if (ok) {
        fmt.Println("American 的首都是", capital)
    } else {
        fmt.Println("American 的首都不存在")
    }

	/*删除元素*/ 
	delete(countryCapitalMap, "France")
```

## 结构体与指针

- 定义 & 声明 & 访问

```go
// 定义
type Person struct {
    Age int
    Name string
}

// 声明
kjl := Person{20,"kjl"}
nal := &Person{
    Age:20,
    Name:"nal",
}

// 访问
fmt.Println(kjl.Name)
fmt.Println(nal.Name)
```

- 为结构体赋予函数

```go
type Person struct {
	Age  int
	Name string
}

func (p *Person) Say(str string) {
	fmt.Println(p.Name, "say", str)
}

func (p Person) AddAge(age int) {
	p.Age += age
}

func main() {
	kjl := Person{20, "kjl"}
	nal := &Person{
		Age:  20,
		Name: "nal",
	}

	nal.AddAge(1)

	// 访问
	fmt.Println(kjl.Age)
	fmt.Println(nal.Age)
}
```

- 接口

```go
type Person struct {
	Age  int
	Name string
}

type Humen interface {
	Say(string)
	AddAge(int)
}

func (p *Person) Say(str string) {
	fmt.Println(p.Name, "say", str)
}

func (p *Person) AddAge(age int) {
	p.Age += age
}

func SomeoneSay(h Humen, str string) {
	h.Say(str)
}

func main() {
	nal := &Person{
		Age:  20,
		Name: "nal",
	}
	
	SomeoneSay(nal,"hello")
}

```

- 权限控制
  - 首字母大写的变量、函数、类型，对于所有包都是可见的
  - 首字母小写的只有当前包可见

## 类型转换

- 普通类型

```go
	var sum int = 17
   var count int = 5
   var mean float32
   
   mean = float32(sum)/float32(count)
   fmt.Printf("mean 的值为: %f\n",mean)
```

- 接口与结构体

```go
func NewPerson(age int, name string) Humen {
	res := &Person{
		Age:  age,
		Name: name,
	}
	return res
}

func main() {
	h := NewPerson(20, "nal")
	p := h.(*Person)
	_, ok := h.(*Person)

	if ok {
		fmt.Println("is person")
	}

	p.Say("?")
}
```

## 错误处理

```go
// 定义一个 DivideError 结构
type DivideError struct {
    dividee int
    divider int
}

// 实现 `error` 接口
func (de *DivideError) Error() string {
    strFormat := `
    Cannot proceed, the divider is zero.
    dividee: %d
    divider: 0
`
    return fmt.Sprintf(strFormat, de.dividee)
}

// 定义 `int` 类型除法运算的函数
func Divide(varDividee int, varDivider int) (result int, errorMsg string) {
    if varDivider == 0 {
            dData := DivideError{
                    dividee: varDividee,
                    divider: varDivider,
            }
            errorMsg = dData.Error()
            return
    } else {
            return varDividee / varDivider, ""
    }

}

func main() {

    // 正常情况
    if result, errorMsg := Divide(100, 10); errorMsg == "" {
            fmt.Println("100/10 = ", result)
    }
    // 当被除数为零的时候会返回错误信息
    if _, errorMsg := Divide(100, 0); errorMsg != "" {
            fmt.Println("errorMsg is: ", errorMsg)
    }

}
```

## 字符串处理

详见 [golang strings 官方文档](https://golang.org/pkg/strings/)

