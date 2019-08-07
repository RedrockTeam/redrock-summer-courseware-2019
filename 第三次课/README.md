# ES6+异步编程精讲

> 2019 年 8 月 7 日——牛奥林

ES6 之前，我们解决异步问题采用的方案是回调函数，但传统的以回调函数解决异步问题的方案有两大缺点

1. 不符合人的思考方式
2. 控制反转失去可信任性

如果你理解不了上面说的两点,你可以先思考下面两个问题

1. 在对自己的行为做出规划时，你的大脑是怎么想的，在实际执行这些规划时，你的大脑又是怎么运作的？
2. 你的回调函数是由谁来执行的？

我们从一些代码开始

```javascript
function ajax(url, cb) {
  const xhr = new XMLHttpRequest();
  xhr.open("GET", url);
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const data = JSON.parse(xhr.responseText);
      cb(data);
    }
  };
  xhr.send();
}

ajax("url", function(data) {
  console.log(data);
});
```

这段代码我们都不陌生，每个初学前端的人肯定都接触过，因为只有简单的一次回调，所以我们觉得相当简单，假如给他加点东西，可能变得稍微复杂

```javascript
function ajax(url, cb) {
  const xhr = new XMLHttpRequest();
  xhr.open("GET", url);
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const data = JSON.parse(xhr.responseText);
      cb(data);
    }
  };
  xhr.send();
}

ajax("url1", function(data) {
  const url2 = data.url;
  ajax(url2, function(data) {
    const url3 = data.url;
    ajax(url3, function(data) {
      const url4 = data.url;
      ajax(url4, function(data) {
        const url5 = data.url;
        ajax(url5, function(data) {
          const url6 = data.url;
          ajax(url6, function(data) {
            //...
          });
        });
      });
    });
  });
});
```

所以就有了传说中的回调地狱（毁灭金字塔），你以为只是因为形式难看吗，其实它完全可以改成这样

```javascript
function ajax(url, cb) {
  const xhr = new XMLHttpRequest();
  xhr.open("GET", url);
  xhr.onreadystatechange = function() {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const data = JSON.parse(xhr.responseText)
      cb(data)
    }
  };
  xhr.send();
}

ajax('url1', ajax2)

function ajax2(data) {
    cosnt url2 = data.url
    ajax('url2', ajax3)
}

function ajax3(data) {
    cosnt url3 = data.url
    ajax('url3', ajax4)
}


function ajax4(data) {
    cosnt url4 = data.url
    ajax('url4', ajax5)
}

function ajax5(data) {
    cosnt url5 = data.url
    ajax('url5', ajax6)
}

function ajax6(data) {
    //...
}
```

很明显，这种形式一样复杂。其中的信任问题也显而易见，回调函数的使用控制权依旧不在自己手里。

而这两个缺点被 ES6 中的生成器和迭代器（1）以及 Promise（2）完美解决。

### 生成器 Generator 和迭代器 Iterator

#### 打破完整运行

在 ES6 之前的时代，函数执行具有原子性，要么不执行，要么全部执行，但 ES6 的 Generator 函数则打破了这个定律，各函数之间以协作式的方式来实现函数中断（阮一峰老师的书里把它叫做“半协程”）。举个栗子

```javascript
function* foo(x) {
  let y = x * (yield 3);
  return y;
}

const it = foo(6);

const firstRes = it.next();

console.log(firstRes);

const res = it.next(7);

console.log(res);
console.log(res.value);
```

#### 双向消息传递

生成器函数 foo 执行后返回一个迭代器 it，迭代器通过调用它的 next 方法不断迭代这个生成器，生成器函数内的每个 yield 就是一个断点，next 方法和 yield 执行形成双向消息传递。

每次 next 就相当于提出一个问题，生成器给我的下一个值是什么？

第一个 next 的问题由第一个 yield 解答，yield 后面跟的东西就是它的答案。

与此同时，yield 也提出自己的问题，这里我应该插入什么值？

第一个 yield 的问题由第二个 next 解答，next 的参数就是它的答案。

#### 测试

懂了这个过程之后，来一个测试

```javascript
function* foo() {
  let x = yield 2;
  z++;
  let y = yield x * z;
  console.log(x, y, z);
}

var z = 1;

var it1 = foo();
var it2 = foo();

var val1 = it1.next().value;
var val2 = it2.next().value;

val1 = it1.next(val2 * 10).value;
val2 = it2.next(val1 * 5).value;

it1.next(val2 / 2);
it2.next(val1 / 4);
```

#### 异步迭代生成器

如果上面的答案你全都做对了，证明你已经对生成器函数有所了解，我们现在看一下怎么把它应用到异步编程中（极简版）。

依然使用上面的 Ajax

```javascript
const url = "xxx";
function foo(url) {
  ajax(url, function(data) {
    it.next(data);
  });
}

function* main() {
  const data = yield foo(url);
  console.log(data);
}
```

通过 Generator 的黑魔法，你能用同步的代码写异步的逻辑

### Promise

#### 控制反转再反转

上面已经提到，基于回调的控制反转会让程序失去可信任性，因为你把你的回调函数交给了第三方，你希望当某种条件达成之后，第三方会正确地调用你的函数，但你不能保证这个信任是可靠的。所以我们希望掌握主动权，能把控制反转再反转回来，我们让第三方提供给我们了解条件何时满足的能力，然后由我们自己的代码来决定下一步做什么。

这就是 Promise 的作用。

#### 美好的承诺

Promise 到底是什么，就和它的语义一样，它代表承诺。

今天是七夕，你和你的女神/男神相约晚上一起去看电影，这是你们的一个约定，也是女神/男神给你的承诺（promise），拥有这个承诺（promise），你虽然现在还在这里听着无聊的课程，但是你心里依然很高兴，因为你知道晚上会很幸福。等啊等，终于到了晚上，女神/男神兑现了对你的承诺（resolve），（then）之后你们愉快地渡过了一个浪漫的晚上。然而还有另外一种可能，你的女神/男神临时有事，把你给鸽了（reject），（catch）你万分伤心，最后自己一个人回到寝室写代码。

以上假设包含五个重要名词，Promise、resolve、reject、then、catch，他们构成了 Promise 的基本逻辑模式。

#### Promise 改写回调

```javascript
function request(url) {
  return new Promise(function(resolve, reject) {
    try {
      const xhr = new XMLHttpRequest();
      xhr.open("GET", url);
      xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
          const data = JSON.parse(xhr.responseText);
          resolve(data);
        }
      };
      xhr.send();
    } catch (err) {
      reject(err);
    }
  });
}

request(url)
.then(data => {
  console.log(data);
});
.catch(err => {
    console.error(err)
})
```

then 函数有两个函数作为参数，第一个是成功后的函数，第二个是错误处理函数，如果第二个函数没有给出，默认将错误返回，在整个 Promise 链中，直到显式指定了错误处理函数，否则错误会在链中一直传递，所以一般会在 Promise 末尾加 catch 处理这个错误。

注意到改写的 ajax 函数中把得到的数据通过 resolve 函数返回，然后在 then 函数中捕获进行后续处理，完成了控制反转再反转，掌握了主动权。

Promise 是一个完全可信任系统。

### 生成器 + Promise

#### 手动结合执行
生成器解决顺序问题，Promise 解决信任问题，理所当然地，我们用生成器 + Promise 作为新一代异步编程方案。

联系到上面两次对 Ajax 回调模式的改写，其实很容易写出以下代码

```javascript
//首先由Promise写出Ajax函数
function request(url) {
  return new Promise(function(resolve, reject) {
    try {
      const xhr = new XMLHttpRequest();
      xhr.open("GET", url);
      xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
          const data = JSON.parse(xhr.responseText);
          resolve(data);
        }
      };
      xhr.send();
    } catch (err) {
      reject(err);
    }
  });
}

//包裹下Ajax
function foo(url) {
  return request(url);
}

//生成器函数
function* main() {
    try {
        const data = yield foo(url)
        console.log(data)
    } catch (err) {
        console.error(err)
    }
}

//在Promise中调用next，推动生成器向下进行
var it = main()
var p = it.next().value
p
.then(data => {
    it.next(data)
})
.catch(err => {
    it.throw(err)
})
```

#### co模块

> co 模块是著名程序员 TJ Holowaychuk 于 2013 年 6 月发布的一个小工具，用于 Generator 函数的自动执行。

上述代码中，生成器函数需要我们手动进行启动和迭代，如果生成器中有大量甚至未知数目的网络请求，显然通过手动调用是不现实的，所以我们需要生成器函数自动执行。在我们的代码中，代码向下执行的核心在于，异步操作有了结果时通过Promise交回执行权。

co模块就把这个过程封装，让next函数始终调用自身，使其自动向下迭代，它接受一个生成器函数作为参数，返回一个Promise对象。

### async/await

ES2017标准中引入async函数，它实质上是就是我们上面说过的一系列内容的封装，如果以上讲的东西你都能理解，那么它的作用显然不用再说。

demo中给出一个使用示例，可以自己看看。
