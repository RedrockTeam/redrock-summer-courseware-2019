# Java/Android复习课

## Java

### 类型
- 基本类型：`int`/`short`/`long`/`byte`/`float`/`double`/`boolean`/`char`
- 引用类型：`String`/`List`/...
- 基本类型包装类：`Number`(`Integer`/`Short`/`Long`/`Byte`/`Float`/`Double`)/`Boolean`/`Character`
> 注意自动装箱与自动拆箱。 
该部分是由编译器实现的，而不是虚拟机。
**注意，基本类型与包装器类型不同，不要因为有自动装箱与自动拆箱而误以为两者相同（表现在使用`==`进行等值检测的时候会出现错误）**

### 位运算
- 移位：`<<`/`>>`/`>>>`
- 逻辑运算：`&`/`|`/`~`
- 应用：`MeasureSpec`/标志位的相关运算
> 注意有符号移位和无符号移位。
同时需注意在利用位运算进行相关Flag的存储与更新，需要先设计好对应位防止冲突。

### 字符串
- 字符串相等检测
- 空串与Null串
- `StringBuffer`/`StringBuilder`
- 字符串不可变/字符串常量池
- 字符串拼接(`+`)底层实现
    关于这部分有以下结论：
    - 在同一个变量中多个静态字符串相加的时候会被优化为一个完整的字符串
    例如：`String str = "you" + "me" => String str = "youme"`
    - 连续的字符串变量相加会使用`StringBuilder`进行优化
    - `+`的底层实现是`StringBuilder`，间断的进行字符串变量相加的时候会新建一个`StringBuilder`的对象出来
    > 因此在字符串变量相拼接的时候（**尤其是在循环时**），使用加号效率远低于`StringBuilder`的`append`方法
    

### 数组、迭代

#### 数组常用方法
- `Array.toString`
    打印数组，二维数组使用：`Array.deepToString`
- `Array.sort`
    优化的快排进行排序，适用于基本类型（除char）支持的数组
- `Array.binarySearch`
    二分查找，适用于基本类型（除boolean）支持的数组

#### 迭代
- `for`
- `foreach`
    注意集合类的迭代删除

### 类
- 访问权限：`private`/`public`/`protect`/`(default)`
- 重载：通过签名（方法名+参数类型）来确定方法是否相同
    **签名与方法的返回类型无关**
- 重写与重载
- 多态：对象变量可以指示多种实际类型的方法（置换法则）
- `final`：阻止继承或者重写
- `static`：不依赖于对象访问
    对于对象与代码块，static修饰的只加载一次
- 参数可变：`Type... name`
- `Object`类方法：
    - `equal()`
    - `hashcode()`
    - `toString()`
    - `clone()`

> 注意多态与数组
编译无误，运行会出错
> ```Java
>   String[] strings = new String[10];
>   Object[] objects = strings;
>   object[0] = new Object();
>   System.out.println(string[0]);
> ```


### 泛型
- 能够使用一套代码支持到多种类型
- 类型变量限定：`extends`（子类）/`super`（父类）/`?`（通配符）
- 泛型擦除：有指明限定类型的擦除为**限定类型**，无限定类型擦除为**Object类**
- 桥方法：解决多态与泛型擦除的问题
- 泛型与重写
- 泛型继承规则
    `List<String>`是`List<Object>`的子类吗？
- 约束与局限
    - 不能用基本类型实例化类型参数
    - 运行时只能够查询原始类型
    - 不能够创建参数化类型的数组
    - 不能实例化类型变量
    - 不能构造泛型数组
    - 静态上下文中泛型变量无效
    - 不能够跑出或者捕获泛型类的实例

### 接口
- 回调：eg.`OnClickListener`
- 函数式接口 & lambda表达式
- 默认方法 & 静态方法
- 细讲lambda
    - 方法引用
        `System.out::println`->`x->System.out.println(x)`
        即使用::分割方法名与对象/类名
    - 构造器引用


### 异常
- 分类：`Error`/`Exception`
- 非受查异常：`Error`/`RuntimeException`
- 受查异常：`IOException`
- 捕获

### 集合
- `List`/`Map`/`Set`
- 遍历
- `HashMap` & `SparseArray`

### 并发
- 线程 & 进程
- 线程状态
    - New（新创建）
    - Runnable（可运行）**可运行不等于正在运行**
    - Blocked（被阻塞）
    - Waiting（等待）
    - Timed waiting（计时等待）
    - Terminated（被终止）
- 同步
    - 竞争：出现在共享数据的情况
    - 锁
        - `synchronized`关键字
        - 锁对象
    - 原子操作：不会被线程调度所打断
- 线程调度
    - 优先级
    - 协作式调度
    - 抢占式调度
- ...

## Android

- 四大组件
- 事件分发
- 消息机制
- 自定义View

## Photoshop & PxCook
- 切图 & 其他