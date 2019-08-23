## WebGL 入门

### 基本的3D原理

#### 🚩 坐标系
![Coordinate system](C:\Users\skywalker512\Documents\WebGL 入门.assets\mdn-games-3d-coordinate-system.png)
WebGL 使用右手坐标系统 — `x` 轴向右, `y` 轴向上 `z` 轴指向屏幕外, 在上图可以看到。

并且默认屏幕是在 **`XOY` 面**上的**（也就意味着我们的视角是从`Z` 轴向下看）**

#### 📦顶点、物体

- **位置**: 在 3D 空间用来确定位置 (`x`, `y`, `z`)
- **颜色**: 包含RGBA (R, G 和 B 分别是红, 绿, 蓝和 Alpha 通道, Alpha 通道控制透明度 — 所有通道值的范围都是 `0.0` 到 `1.0`)
- **法线:** 描述顶点朝向（比如一个平面，在背对法线方向就看不到该平面）
- **纹理**: 顶点用来装饰模型表面的一张 2D 图片, 它是代替简单颜色的选择之一

#### 🎥 渲染流程

- **术语**

    - **原始数据**: 渲染流程中的输入 — 就是顶点坐标
    
    - **片段**: 一个**像素**的 3D 投射, 有着和像素一样的属性  （意思就是这个将会通过一系列的工作转换成像素）
    
    - **像素**: 屏幕上的 2D 网格中的点布置的点, 包含 RGBA
    
- **顶点处理**
  
  ![Vertex processing](C:\Users\skywalker512\Documents\WebGL 入门.assets\mdn-games-3d-vertex-processing.png)
  
  从图中可以看到顶点的位置发生了改变，可以说这一操作中发生了很多事情，我们只提取出其中一部分来讲
  
  - **矩阵变换**
  
    我们是通过矩阵来达到坐标变换的，当然除了这个比较简单的位移变换，还有其他的变换，比如旋转，缩放之类的。
  
  ![1566438908698](C:\Users\skywalker512\Documents\WebGL 入门.assets\1566438908698.png)
  
  - **视图转换(view transformation)**
  
    这一步只关心位置和 3D 空间中摄像机的朝向设置
  
    摄像机有三个参数 **(位置, 方向和朝向)** 
  
    ![Camera](C:\Users\skywalker512\Documents\WebGL 入门.assets\mdn-games-3d-camera.png)
  
    你肯定想到了，设定摄像机也是通过矩阵变换得到的。事实上我们设定摄像机参数生成的矩阵并不是作用于摄像机上，而是求出其逆矩阵作用于顶点上，同样可以达到摄像机移动的效果。
  
  - **透视转换(perspective transformation) **
  
    ![Camera settings](C:\Users\skywalker512\Documents\WebGL 入门.assets\mdn-games-3d-camera-settings-1566452798733.png)
  
    这个同样也是经过相应的矩阵变换，输入一定的参数（视野, 宽高比例和近裁剪、远裁剪等等），得到矩阵，然后来确定顶点在 3D 空间中的具体表现
  
- **栅格化**

    我们现在得到的只是顶点，然后中间还是 “中空” 的，所以就有了栅格化。

    栅格化将**原始数据**（从**顶点**信息**转换**过来的）**转换**为一系列的片段（通俗来讲就是补充中间空缺的部分）

    >  **片段**: 一个**像素**的 3D 投射, 有着和像素一样的属性  （意思就是这个将会通过一系列的工作转换成像素）

    ![Rasterization](C:\Users\skywalker512\Documents\WebGL 入门.assets\mdn-games-3d-rasterization.png)

- **片段合成**

    现在我们的片段还只是一些点（片段），没有颜色，没有光照的效果，而这一步就是要将颜色之类的东西定义到我们的片段上去。（今天我们只使用纯色，所以不细讲）

    ![Fragment processing](C:\Users\skywalker512\Documents\WebGL 入门.assets\mdn-games-3d-fragment-processing.png)

- **输出合成**

    之前我们说过片段是像素在 3D 中的投射，而这一步就是将来自3D空间的原始数据的片段转换到2D像素网格中,  然后打印到屏幕像素上。

    ![Output merging](C:\Users\skywalker512\Documents\WebGL 入门.assets\mdn-games-3d-output-merging.png)

- **总结**

    ![Rendering pipeline](C:\Users\skywalker512\Documents\WebGL 入门.assets\mdn-games-3d-rendering-pipeline-1566452855785.png)

    WebGL 为我们做好了**栅格化(rasterization)**和**输出合成(output merging)**，我们只需要关注如何定义顶点位置，以及片段合成就可以了。

### WebGL 基础概念

#### 🕵️‍♂️ What? 

WebGL: 是一个光栅化引擎，它可以根据你的 **代码 绘制** 出点，线和三角形。

OpenGL: 工业级的图形渲染 API。

OpenGL ES: OpenGL 的嵌入式版本，是 OpenGL 的子集去除了 OpenGL 中消耗性能的部分。

![1566306020197](C:\Users\skywalker512\Documents\WebGL 入门.assets\1566306020197.png)

#### 📕How?

WebGL 在电脑的 **GPU** [^2]中运行。因此你需要使用能够在 **GPU 上运行的代码**

- **顶点着色器**：计算顶点的位置 **(对应之前讲的顶点处理部分)**
- **片断着色器**：计算出当前绘制图元中每个像素的颜色值 **(片段合成)**

这两个着色器使用的语言叫做 **GL着色语言** （一种类似C或C++的强类型的语言）

每一对组合起来称作一个 **Program**（着色程序）

### WebGL 着色器和 GLSL

- **GLSL**

  顶点着色器需要的**数据**，可以通过以下三种方式获得。（相当于 GLSL 中的 let const var）

  - **属性 （Attributes）**

    相当于变量

  - **全局变量（Uniforms）**

    全局变量在着色程序运行前赋值，在运行过程中全局有效。

  - **可变量（Varyings）**

    可以从顶点着色器传递到片段着色器

  数据类型 （我只讲今天要涉及的一个 其他的不讲 因为比较多）

  - **vec4**

    它的默认值是 (0, 0, 0, 1)，也就是说你传递给他 （1, 0.5）最后生成 （1, 0.5, 0, 1）

- **顶点着色器** （VertexShader）

  有这这样的模板

  ```c
  attribute vec4 a_position;
  void main() {
     gl_Position = a_position // 做一些事情生成顶点坐标，就是之前讲的坐标变换
  }
  ```

  每次调用都需要设置一个特殊的全局变量 `gl_Position`， 该变量的值就是**裁剪空间**[^1]坐标值。

- **片段着色器** （FragmentShader）

  ```c
  // 片断着色器没有默认精度，所以我们需要设置一个精度
  // mediump是一个不错的默认值，代表“medium precision”（中等精度）
  precision mediump float;
   
  void main() {
    // gl_FragColor是一个片断着色器主要设置的变量
    gl_FragColor = vec4(1, 0, 0.5, 1); // 返回“一种紫色”
  }  
  ```

### 渲染管线

然后就万事具备只欠调 API 了，但是在调 API 之前我们再来了解一下渲染管线，同时也来复习一下上面所讲的部分。

![1566443693288](C:\Users\skywalker512\Documents\WebGL 入门.assets\1566443693288.png)

这张图可以说清晰的展现了 `WebGL` 的渲染流程，`Uniforms` 怎样作为全局变量存在，为什么 `Varyings` 可以从顶点着色器传递数据到片段着色器，这两个着色器所处的位置。

其中可以注意到 Attributes 得到的方式比较奇怪，他是通过一个**“指针”**指向显存中的一个位置，我单独拿出来讲可见他的复杂程度。

### Attributes 的赋值

前言：`gl = canvas.getContext("webgl");` 

- **创建一个缓存区**

  ```js
    // 创建缓冲区，GLSL 中的数据都是从缓存区中获取
    const positionBuffer = gl.createBuffer();
  ```

- **绑定缓冲区**

  ```js
    // 将 positionBuffer 与 gl.ARRAY_BUFFER 绑定起来，好在后面传递数据的时候能够传输到创建的缓存中
    // 可以看作 ARRAY_BUFFER(显存中) == positionBuffer(js内存中)
    // 相当于我们创建了一个缓冲区数据与 js 之间的映射关系
    gl.bindBuffer(gl.ARRAY_BUFFER, positionBuffer);
  ```

- **向缓冲区中放入数据**

  ```js
    // 顶点的数据
    const position = [
        0, 0, 
        0, 0.5, 
        0.7, 0
    ];
    // 将数据传递到 绑定点 gl.ARRAY_BUFFER 上
    // WebGL 需要强类型数据，所以 new Float32Array(positions) 创建了32位浮点型数据序列
    // gl.STATIC_DRAW 提示 WebGL 我们不会经常改变这些数据
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(position), gl.STATIC_DRAW);
  ```

  我们现在已经把数据放到了缓冲中，但是还没有让缓冲中的数据与 GLSL 中的变量产生联系，上面我提到了**“指针”**你肯定会想到怎样去获取 `Attributes ` 对应的“内存”地址

- **获取 Attributes 在显存中的地址**

  ```js
    // 获取 vertexShader 中 a_position 的“内存”地址
    const positionAttributeLocation = gl.getAttribLocation(program, "a_position");
    // 让 WebGL 知道我们要向 GLSL 中的那个变量传递值
    gl.enableVertexAttribArray(positionAttributeLocation);
  ```

- **怎样去取数据**

  你可能已经注意到了我们的顶点数据和只有两位, 例如 (0, 0.5) 可是我们是在三维世界中的顶点是有`xyz`的坐标, 并且我们定义的`a_position` 是 `vec4` 类型的变量需要有4个参数，这样我们就需要规定 WebGL 如何去取用我们放入缓冲区的数据。

  ```js
    // 告诉 WebGL 怎么从 positionBuffer (ARRAY_BUFFER) 中读取数据
    gl.vertexAttribPointer(
      positionAttributeLocation,     // 要放到那里去
      2, 							   // 每次迭代运行提取两个单位数据
      gl.FLOAT,                      // 每个单位的数据类型是32位浮点型
      false,                         // 不需要归一化数据  255 -> 1
      0,                             // 0 = 移动单位数量 * 每个单位占用内存（sizeof(type) 每次迭代运行运动多少内存到下一个数据开始点 (不需要了解)
      0                              // 从缓冲起始位置开始读取
    );
  ```

### 调用 API

请看代码

### 接下来

我们通过上面的了解，可以看到如果我们使用原生写 3D 应用将是一个灾难，我们主要的工作将会耗费在矩阵变换，WebGL API 的调用上，这将比我们操作 `DOM` 更耗费时间，所以我们可以使用一些库来帮助我们的工作

- **传统3D**

  - [Threejs](https://threejs.org/)  - 这个应该不用说了 (54,053 star) [(1,139 contributors)](https://github.com/BabylonJS/Babylon.js/graphs/contributors) [(39 BUG)](https://github.com/BabylonJS/Babylon.js/issues?q=is%3Aissue+is%3Aopen+label%3Abug)
  - [Babylon.js](https://www.babylonjs.com/) - 微软~~巨硬~~搞的 (TypeScript) (9,900 star) [(240 contributors)](https://github.com/BabylonJS/Babylon.js/graphs/contributors) [(1 BUG)](https://github.com/BabylonJS/Babylon.js/issues?q=is%3Aissue+is%3Aopen+label%3Abug) 
  - [playcanvas](https://github.com/playcanvas) - 更具有商业性，拥有一个比较强大的编辑器 (4,713 star) [53 contributors](https://github.com/playcanvas/engine/graphs/contributors) [(33 BUG](https://github.com/playcanvas/engine/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

- **VR**

  - [A-Frame](https://github.com/aframevr/aframe/) - (10,336 star) [ (294 contributors)](https://github.com/aframevr/aframe/graphs/contributors)

  - [React 360](https://github.com/facebook/react-360) - (7,432 star) [(38 contributors)](https://github.com/facebook/react-360/graphs/contributors)

    > 如果您的应用程序是由用户交互驱动的，并且有许多二维或三维UI元素，那么React 360将提供您需要的工具。如果您的应用程序由许多3D对象组成，或者依赖于复杂的效果（如需要使用 Shader），您将从 A-Frame 获得更好的支持。 -- React 360 官方文档

- **国内**（2D）

  - [Egret](https://www.egret.com/) 使用 TypeScirpt 开发，**官方有 Webassembly 版本**（C++），暴露 TS API，拥有自己的编辑器，但是其比较偏向于 Cocos2dx（Flash 流派，意思就是主要 2D，并且使用以前 Flash 开发的流程）
  
  - Cocos2dx 使用 C++/Lua/JS 开发，然后浏览器环境跑在 **JS 引擎**上，并且 BUG 是让开发团队修，早年有些版本连跑 demo 都跑不起来，但是国内有很多公司用，但是从2016年就开始唱衰他了。
  
    （游戏开发的话其实使用 Unity 比较有利，支持 3D 很好 并且整个生态较好）

### 参考

1. [解释基本的3D原理-MDN](https://developer.mozilla.org/zh-CN/docs/Games/Techniques/3D_on_the_web/Basic_theory)
2. [WebGL 基础概念](https://webglfundamentals.org/webgl/lessons/zh_cn/webgl-fundamentals.html)
3. [WebGL图形管线](https://www.yiibai.com/webgl/webgl_graphics_pipeline.html)
4. [WebGL 着色器和GLSL](https://webglfundamentals.org/webgl/lessons/zh_cn/webgl-shaders-and-glsl.html)

[^1]: 裁剪空间的目标是能够方便的对渲染图元进行裁剪：完全位于这块空间内部的图元将会被保留，完全位于这块空间外部的图元将会被剔除，而与这块空间边界相交的图元将会被裁剪。![1566441837160](C:\Users\skywalker512\Documents\WebGL 入门.assets\1566441837160.png)
[^2]: GPU ![1566441616036](C:\Users\skywalker512\Documents\WebGL 入门.assets\1566441616036.png)

