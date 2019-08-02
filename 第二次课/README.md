# HTML5 与 CSS3

> 2019 年 8 月 2 日——牛奥林

## 了解标准

结构、表现、行为是 Web 应用的三大核心，业界为这三大核心制定了一系列标准，分为结构标准、表现标准和行为标准，前端开发的三大支柱技术 HTML、CSS、JavaScript 由此而来。

其中结构标准语言、表现标准语言和行为标准中的 DOM 规范均由 Web 技术领域最具权威和影响力的国际中立性技术标准机构[W3C](https://www.w3.org/)（万维网联盟，World Wide Web Consortium，简称 W3C）制定，行为标准中的 ECMAScript 由 Ecma 国际（前身为欧洲计算机制造商协会，European Computer Manufacturers Association）的第 39 号技术专家委员会（Technical Committee 39，简称 TC39）制定。

前端从业者需要了解标准，但是并不是要以官方标准为学习素材，W3C 的标准是给浏览器厂商看的，好让他们去实现，我们平常需要什么 API 直接去[MDN](https://developer.cdn.mozilla.net/en-US/)查，MDN 的文档本身也是根据标准来写的，完全能够满足我们对标准的了解需求。如果实在有很困惑的地方再去标准制定者官方查标准不迟。

关于某个 API 的兼容性[Can I use](https://caniuse.com/)能提供最清晰的解答，也不必查看标准。

另推荐一个查 API 的快速途径[DevDocs](https://devdocs.io/)，用 Electron 包装一下之后相当方便。

## HTML5

首先普及一下概念，[互联网行业常说的 H5 和 HTML5 有什么关系？](https://blog.hypers.io/2017/08/22/what-is-h5/)，自己了解下，以下开始 HTML5 的新特性。

### 语义化标签

- header：具有引用或导航作用的解构元素，通常包括网站标志、主导航、全站链接以及搜索框。 也适合对页面内部一组介绍性或导航性内容进行标记。
- nav：元素代表页面的导航链接区域。用于定义页面的主要导航部分。譬如：侧边栏上目录，面包屑导航，搜索样式，或者下一篇上一篇文章。用在整个页面主要导航部分上，不合适就不要用 nav 元素。
- article：当前页面或文章的附属信息，可以包含于当前页面或主要内容相关的引用、侧边栏、广告、导航条。
  article 可以嵌套 article，只要里面的 article 与外面的是部分与整体的关系。
- section：具有相似主题的一组内容，比如网站的主页可以分成介绍、新闻条目、联系信息等条块。元素代表文档中的“节”或“段”，“段”可以是指一篇文章里按照主题的分段；“节”可以是指一个页面里的分组。
- aside：指定附注栏，包括引述、侧栏、指向文章的一组链接、广告、友情链接、相关产品列表等。
- footer： 作为其上层父级内容区块或根区块的脚注。通常包含脚注信息，如作者、相关链接或版权。

代码结构从这样

```
<body>
    <div id="header">...</div>
    <div id="nav">...</div>
    <div class="artical">
        <div class="section"></div>
    </div>
    <div id="side-bar">...</div>
    <div id="footer">...</div>
</body>
```

变成这样

```
<body>
  <header>...</header>
  <nav>...</nav>
  <article>
     <section>
       ...
    </section>
  </article>
  <aside>...</aside>
  <footer>...</footer>
</body>
```

### 表单

#### 新的 input 类型

- email
- url
- number
- range
- Date pickers (date, month, week, time, datetime, datetime-local)
- search
- color

#### 新的表单元素

- datalist
- keygen
- output

#### 新的 form 属性

- autocomplete
- novalidate

#### 新的 input 属性

- autocomplete
- autofocus
- form
- form overrides (formaction, formenctype, formmethod, formnovalidate, formtarget)
- height 和 width
- list
- min, max 和 step
- multiple
- pattern (regexp)
- placeholder
- required

### canvas

canvas 允许用户通过 js 在浏览器绘制图像，个人认为，它是 HTML5 提供的最强大 API，如果你有丰富的图形学知识，完全在浏览器上完成任何计算机可视化领域的工作。

canvas 的内容足以写一整本书，所以我们今天不具体涉及，只给出一个简单 demo 作参考。

### requestAnimationFrame

rAF 是 setTimeout 的增强版，使用时，只需传入一个回调函数，它会自动在浏览器下一次重绘或回流之前执行，与 setTimeout 相比，它的执行更精确，因为我们曾经讲过浏览器定时器的运行机制，定时器中的任务被放到任务队列之后并不一定会在你指定的时间执行。

requestAnimationFrame 采用系统时间间隔，保持最佳绘制效率，不会因为间隔时间过短，造成过度绘制，增加开销；也不会因为间隔时间太长，使用动画卡顿不流畅，让各种网页动画效果能够有一个统一的刷新机制，而且在页面处于非激活状态下，动画会自动暂停，从而节省系统资源，提高系统性能，改善视觉效果。

和上面提到的 canvas 结合，是大部分 HTML5 小游戏的根本运行机制。

### 多媒体支持

- audio 音频
- video 视频

HTML5 的原生 audio 与 video 标签提供了相当多的 API 供操作媒体文件，几乎可满足所有日常需求，用到的时候查就好了。

### 贴近 native 的交互

#### 全屏

element.requestFullscreen();
element.exitFullscreen();

#### 可拖拽

- 标签属性 draggable
- 被拖拽元素事件：

  1. dragstart：开始拖拽时触发此事件。

  2. drag：dragstart 事件触发后，随后触发，元素被拖动期间此事件会持续触发，类似 mousemove 事件。

  3. dragend：当放开被拖拽元素也就是拖拽结束后触发该事件。

- 目标元素事件：

  1. dragenter:：当被拖拽元素进入时触发此事件。

  2. dragover：此事件会紧接着 dragenter 事件触发，并且在拖动期间持续触发。

  3. dragleave：当被拖拽元素离开目的地元素的时候触发。

  4. drop：当结束拖拽时触发此事件。

#### 可编辑

标签属性 contenteditable，此功能常用来做富文本编辑功能

#### 获取地理位置

```javascript
function getPosition() {
  const success = position => console.log(position);
  const fail = err => console.log(err);

  const options = {
    timeout: 5000,
    maximumAge: 3000
  };

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(success, fail, options);
  }
}
```

### Web API盘点

#### WebSocket

WebSocket是HTML5一种新的协议，实现了浏览器与服务器的双向通讯。在 WebSocket API 中，浏览器和服务器只需要要做一个握手的动作，然后，浏览器和服务器之间就形成了一条快速通道，两者之间就直接可以数据互相传送。

#### Web Storage

以前在本地存储一些简单的数据（比如用户名或者密码等）大多使用cookie。但cookie能够存储数据的太少，只有4K左右，难以满足当前的需求，所以HTML5推出Web Storage。

Web Storage有更大的本地存储，大约在4M左右（不同浏览器有所不同），而且不会被浏览器主动发送到服务端。

Web Storage分为localStorage和sessionStorage，它们的区别和联系在于

- localStorage生命周期是永久，这意味着除非用户显示在浏览器提供的UI上清除localStorage信息，否则这些信息将永远存在。
- sessionStorage生命周期为当前窗口或标签页，一旦窗口或标签页被永久关闭了，那么所有通过sessionStorage存储的数据也就被清空了。
- localStorage和sessionStorage一样都是用来存储客户端临时信息的对象。他们均只能存储字符串类型的对象（虽然规范中可以存储其他原生类型的对象，但是目前为止没有浏览器对其进行实现）。
- 不同浏览器无法共享localStorage或sessionStorage中的信息。相同浏览器的不同页面间可以共享相同的localStorage（页面属于相同域名和端口），但是不同页面或标签页间无法共享sessionStorage的信息。

#### Web Workers

HTML5 Web Workers可以让Web 应用程序具备后台处理能力，它对多线程的支持非常好，因此，使用了HTML5的Javascript应用程序可以充分利用多核CPU带来的优势。将耗时长的任务分配给HTML5 Web Workers执行，可以加快响应速度。但Web Workers不能直接访问 Web 页面和DOM API。

#### FileReader

FileReader 对象允许Web应用程序异步读取存储在用户计算机上的文件（或原始数据缓冲区）的内容，使用 File 或 Blob 对象指定要读取的文件或数据。

其中File对象可以是来自用户在一个input元素上选择文件后返回的FileList对象,也可以来自拖放操作生成的 DataTransfer对象,还可以是来自在一个HTMLCanvasElement上执行mozGetAsFile()方法后返回结果。

以上特性并非HTML5的全部内容，只是列举了其中的一些，想了解更多HTML5的内容，推荐《HTML5权威指南》这本书。

## CSS3

### 新特性一览

- 过渡transition
- 动画animation
- 转换transform
- 选择器
- 阴影box-shadow
- 边框
  - 边框图片border-image
  - 边框圆角border-radius

- 背景
  - 绘制区域background-clip
  - 背景位置background-position
  - 背景相对于background-origin
  - 背景尺寸background-size
  - 多张背景图

- 反射box-reflect
- 文字阴影text-shadow
- 颜色表示rgba/hsla
- 渐变
  - 线性渐变linear-gradient
  - 径向渐变radial-gradient
  - 圆锥渐变conic-gradient

- 滤镜filter
- 布局
  - 弹性布局flex
  - 网格布局grid
  - 多列布局

- 盒模型box-sizing
- 媒体查询@media

### 动画

#### transition
- 基本使用
transition: margin-right 4s;
- 添加延时
transition: margin-right 4s 1s;
- 速度曲线
transition-timing-function: linear | ease-in | ease-out | cubic-bezier;

#### animation
- 基本使用
```
@keyframes rainbow {
  0% { background: #c00; }
  50% { background: orange; }
  100% { background: yellowgreen; }
}
```

- 结束状态
animation-fill-mode

- 动画方向
animation-direction

- 播放状态
animation-play-state

### scss

sass是一门css预处理语言，可以让你像写脚本语言一样写css。

scss是sass兼容css3的版本，其实和sass没什么差别,用的时候都一样。

日常结合框架使用时，脚手架里是配置好了的，所以无须过度关心如何安装使用，项目初始化后开箱即用。

在官方文档上有详尽的中文教程，常用的功能有变量、函数、属性嵌套、混合、继承，其余功能不用太关心。
