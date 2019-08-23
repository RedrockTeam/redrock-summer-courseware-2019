## Three.js 入门

### 📣 前言

刚才我们使用 WebGL 在**三维空间**中创建了，一个**平面直角三角形**。相信大家没人会觉得简单。程序员应该是这个世界上最会偷懒的人，同时我们也想不要 996 这样的工作时间，Three.js 就帮我们解决了许多东西，甚至在大多数情况下我们不需要写 `GLSL` 语言，所以就让我们来了解一下 Three.js 吧。

前置的变量

```js
  const canvas = document.createElement('canvas')
  THREE // THREE 暴露出来的对象
```

###  📷  相机

![Camera settings](C:\Users\skywalker512\Documents\Three.js 入门.assets\mdn-games-3d-camera-settings-1566452798733.png)

```js
const fov = 75; // 视野的角度
const aspect = canvas.clientWidth / canvas.clientHeight;; // 长宽比例
const near = 0.1; // 近裁剪
const far = 5; // 远裁剪
// 设定一个有透视效果的相机
const camera = new THREE.PerspectiveCamera(fov, aspect, near, far);
```

通过 fov, aspect, near, far 就确定了一个区域，这个区域就叫做截椎体**（frustum）** 在这个区域外面的物体将不会显示。

### 🚩 坐标

![Coordinate system](C:\Users\skywalker512\Documents\Three.js 入门.assets\mdn-games-3d-coordinate-system.png)

又是一张熟悉的图，我们接下来用 Three.js 创建的物体**都会**位于坐标系**原点**。而我们创建的相机默认指向**Z轴负方向**，上方向朝向**Y轴正方向**，也就意味着我们的相机与我们创建的物体**重合了**，这肯定是不可行的，所以我们需要将相机从原点移走才能看到物体。

```js
camera.position.z = 2;
```
下面是我们想要达到的效果

![1566482441287](C:\Users\skywalker512\Documents\Three.js 入门.assets\1566482441287.png)

### 🎬 场景

现在我们已经有了一台摄像机，还需要一些演员来组成场景。

- **创建一个场景**

    ```js
    const scene = new THREE.Scene();
    ```

- **创建一个几何体**

    ```js
    const boxWidth = 1;
    const boxHeight = 1;
    const boxDepth = 1;
    const geometry = new THREE.BoxGeometry(boxWidth, boxHeight, boxDepth);
    ```

- **创建一个材质**

    现在我们有了几何体，但是我们还没有告诉他应该是属于什么，属于怎样的材质

    ```js
    const material = new THREE.MeshBasicMaterial({color: 0x44aa88});
    ```
    - `color`: 使用 16 进制颜色，可以将 `0x` 看成 `#`

    - `MeshBasicMaterial`: 是一种简单的材质，对光线没有反应，在没有光线的情况下我们也能看到。

- **创建一个网格**

  ```js
  const cube = new THREE.Mesh(geometry, material);
  ```

  然后创建一个Mesh。Mesh代表了 
  
  - Geometry: 物体的形状
  - Material: 怎么绘制物体, 光滑还是平整, 什么颜色, 什么贴图等等 的组合
  
- **添加到场景中并进行渲染**

    ```js
    scene.add(cube); // 添加到场景
    renderer.render(scene, camera); // 进行渲染
    ```


### 🏃 运动起来

我已经看到相较于 WebGL 来说，Three.js 创建一个物体的速度已经大幅度的提升。但我们平时在 3D 世界中看到的物体都不是静止他们都做着一定的运动

- **requestAnimationFrame**

  相信大家都见过这个东西

  - 该方法需要传入一个回调函数作为参数，该回调函数会在浏览器下一次重绘**之前**执行
  - 当`requestAnimationFrame()` 运行在后台标签页或者隐藏的 <iframe> 里时，`requestAnimationFrame()` 会被暂停调用以提升性能和电池寿命。
  - 回调函数会被传入 `DOMHighResTimeStamp` 参数，`DOMHighResTimeStamp` 指示当前被 `requestAnimationFrame()` 排序的回调函数被触发的时间 **（单位毫秒）**

- **怎样运动起来**

  如果我们还在 WebGL 的情况下我们需要在**顶点着色器**中使用相应的**变换矩阵**乘以顶点坐标，并**再**进行一次渲染。Three.js 帮我们自动编写了相应的**顶点着色器**我们只需要设定相应的参数就可以了。

  ```js
  function render(time) {
    time *= 0.001;  // 将毫秒转换成秒
   
    cube.rotation.x = time; // 单位弧度
    cube.rotation.y = time; // 单位弧度
   
    renderer.render(scene, camera); // 重新渲染
   
    requestAnimationFrame(render); // 保证再本次渲染完成之后再次被调用
  }
  requestAnimationFrame(render); // 页面开始加载时开始调用
  ```

### 💡 灯光

现在我们已经有了一个会动的物体，但是他和真实世界仍然还有差距，他缺少了灯光。现在我们就来创建一个灯光。

- **材质**

  我们之前使用的材质为 `MeshBasicMaterial` 他不受灯光的影响，现在我们需要换成一个可以受灯光影响的材质`MeshPhongMaterial`

  ```js
  -const material = new THREE.MeshBasicMaterial({color: 0x44aa88});  // 删除这一行
  +const material = new THREE.MeshPhongMaterial({color: 0x44aa88});  // 添加这一行
  ```

- **光源**

  ```js
  {
    const color = 0xffffff;
    const intensity = 1;
    const light = new THREE.DirectionalLight(color, intensity);
    light.position.set(-1, 2, 4); // 灯光的位置应该在我们视角（摄像机）的左上方
    scene.add(light);
  }
  ```

  **DirectionalLight**

  平行光是沿着特定方向发射的光。这种光的表现像是无限远, 从它发出的光线都是平行的。常常用平行光来模拟太阳光的效果。

  - color - (可选参数) 16进制表示光的颜色。 缺省值为 0xffffff (白色)
  - intensity - (可选参数) 光照的强度。缺省值为1

。。。大家都可以看到，Threejs 简化了很多操作，大部分都是调用 API 了，调用 API 的话官网文档比我讲的好，而且有些文字我也是直接 copy 下来的，没有涉及太多图形学的知识，所以就这样结束了吧。



### 参考

- [Threejs 文档](https://threejs.org/docs/)
- [Three.js Fundamentals](https://threejsfundamentals.org/)
- [解释基本的3D原理-MDN](https://developer.mozilla.org/zh-CN/docs/Games/Techniques/3D_on_the_web/Basic_theory)

