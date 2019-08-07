**Kotlin**

**1、主要特征**

 kotlin的目标平台是服务器端、Android以及任何Java运行的地方。Kotlin最常见的应用

函数式和面向对象编程



函数式编程的核心概念

1)、头等函数--将函数（一小段行为）保存为值使用

2)、不可变性--使用不可变对象（保证状态再创建后不改变）

3)、无副作用--无副作用

函数结构

（此处应有插图

```kotlin
fun main(args:Array<String>){     println("Hello, World!") } 
```

 

表达式函数体

语句和表达式的区别在于，表达式是有值的，并且能作为另一个表达式的一部分使用。而for、while这种就没有自己的值，像if控制语句就是表达式，所以for可以在函数中如此使用

```kotlin
fun max(a: Int, b: Int): Int = if(a > b) a else b  //返回值可省略 
```

**一些特殊运算符**

？：

Elvis运算符，（可空运算符）





if else代替了三目运算符



**2、函数和变量**

1、声明变量的关键字有两个，val（不可变引用）类似java中的final。考虑变量时应该尽量使用val。val自身不可变，但是val变量指向的对象可变

val languages = arrayListOf("Java") languages.add("Kotlin") 

var，可变引用，允许变量改变自己的值，但其指向的对象不可改变

```kotlin
//该代码为错误例子 var answer = 42 answer = "Kotlin" //该类错误编译器能够指出 
```

变量是有根据上下文的类型推导的，以下两种写法相同

```kotlin
//第一种 val android = "Kotlin" //第二种 val android :String = "Kotlin" 
```

2、字符串模板

```kotlin
//第一种，在打印语句时，直接使用“$”符号加变量进行引用 
val name = "Irving" println("Hello, $name!") 
//第二种，还可以引用更复杂的表达式在打印语句中
println("Hello, ${if(a > b) "Java" else "Kotlin"}") 


```

3、调用属性是通过都是getter和setter。可自定义

1）val定义的变量，生成一个字段和一个简单的getter

2）var定义的变量，生成一个字段，一个getter和一个setter

3）当在声明变量的同时就已经声明了默认的访问器，但是在有需要的同时也可以自定义默认的访问器

```kotlin
class Person{     
    var name: String         
    get(){                          
        //自定义的程序         
    }         
    set(value){               
        //val的变量就没有set语句            
        //TODO         
    }
} 
```



4、when结构中能够使用任意对象（最后可以添加else）。可以使用不带参数的when

见代码

5、is智能转换

```kotlin
fun getView() : View{     //TODO } 
```



```kotlin
val view = getView()  
when (view){     
    is EditText -> view.text = "Hello, Kotlin"     i
    s ImageView -> view.setImageUrl = "..."     
    //TODO     
    //...... 
} 
```





6、try、when、if均可作表达式

如果其主体包含多个表达式，那么整个表达式的值就是最后一个表达式的值



**3、函数的定义与调用**

1、函数默认参数值，避免代码大面积重复

 见代码

2、顶层函数与顶层属性

顶层函数：为了解决Java中某些类，写到最后就成了一个静态函数的容器。在Kotlin中，这些方法可以直接放在代码文件的顶层。

顶层属性：

以下两种写法效果是一样的

```kotlin
/*Kotlin*/ 
const val REQUEST_CODE = 2 
/*Java*/
public static final int REQUEST_CODE = 2   
```

类似于全局变量，但独立于类本身存在（个人理解

3、扩展函数和属性

1）！：扩展函数不能访问私有或是受保护的成员（防止类的密封性被打破

2）在Java中调用Kotlin文件中的扩展方法，只需要使用文件名称加上方法名即可

```kotlin
//Java 
//lastChar为StringUtil.Kt文件中的一个方法 
char c = StringUtilKt.lastChar("java") 
```

3）不可被重写的扩展函数

重写的成员函数，结果与Java相同（见例一）

扩展函数是无法被重写的。具体执行哪个扩展函数是由变量的静态类型所决定的，而并不是变量的运行时类型（见例二）

4）扩展属性与扩展方法声明类似，不在赘述

4、中缀调用

1）基本调用

```kotlin
//中缀调用 
val map = mapOf(1 to "one", 7 to "seven", 9 to "nine") 
//以下两种调用方式是等价的 
//第一种 
1.to("one) 
//第二种 
 1 to "one" 
```

2）中缀调用可以与只有一个参数的函数一起使用，但是无论是扩展函数还是成员函数，必须使用infix标记才能使用中缀

```kotlin
infix fun Any.to(other: Any) = Pair(this, other) 
```

解构声明：

```kotlin
//也可以用来同时初始化两个变量 
val (number, name) = 1 to "one" 
```

5、局部函数

就是在函数内部的函数，用于检查自身的一些东西。比如我一个用户类，要定义一个方法来检测用户名还有地址是否为空

```kotlin
fun function(user：User){     
    if(user.name.isEmpty()){
        //TODO    
    }          
    if(user.address.isEmpty()){ 
        //TODO    
    }
    } 
```

这样写就会造成重复代码，在代码逻辑很大的情况下，就会很冗杂，使用内部函数就可以解决这类问题

```kotlin
fun function(user：User){
    fun innerFunction(value:String):Boolean{ 
        if(value.isEmpty()){
            //TODO        
        }         
        return ...    
    }     //在外部调用内部函数直接进行判断 } 
```

局部函数可以可以访问所在函数的所有参数和变量

**4、类、对象和接口**

1、object关键字



2、open、final、abstract关键字，默认都是final（禁止重写）

如果类没有提供子类应该怎么实现的明确规则，在重写子类时就可能出现预期之外的风险（如泛型没有限定符号）。所以在Kotlin中，类和方法默认都是final的（即不可被重写或者继承）

3、kotlin的访问修饰符和可见性修饰符

1）制继承的修饰符：

| 修饰符   | 相关成员               | 评注                                 |
| -------- | ---------------------- | ------------------------------------ |
| final    | 不可被重写             | 类中的成员默认使用                   |
| open     | 可以被重写             | 需要明确的标记出来                   |
| abstract | 必须被重写             | 只能在抽象类中使用抽象成员不能有实现 |
| override | 重写父类或接口中的成员 | 重写成员默认时开放的                 |

2）控制可见性的修饰符



| 修饰符    | 相关成员     |
| --------- | ------------ |
| public    | 所有地方可见 |
| internal  | 模块可见     |
| protected | 子类中可见   |
| private   | 类中可见     |

类的基础类型和类型参数列表中用到的所有类，或者函数的签名都与这个类或者函数本身相同的可见性。需要注意的是类的扩展函数不能访问它的private和protected成员

4、密封类

使用sealed关键字来来标记密封类

密封类中，所有的子类必须嵌套在父类中,这样可以对创建的子类做出严格的限制

```kotlin
sealed class Expr{
    class Num(val value:Int): Expr()
    class Sun(val left: Expr, val right: Expr): Expr()
} 
```

5、通过getter和setter访问支持字段（field）

可以使用标识符field来访问支持字段的值，在getter中，只能读值，在setter中，既能读取他也能修改他

访问属性的方式不依赖于它是否含有支持字段，如果你显示地引用或者使用默认的访问器实现，编译器会为属性生成访问字段，如果你提供了一个自定义的访问器并且没有使用field，支持字段就不会被呈现出来

可以修改访问器的可见性

```kotlin
class LengthCounter{ 
    val counter :Int = 0 
    private set    
    //这样，将不能在类外部修改这个属性值
} 
```

6、数据类

数据类会自动生成通用方法的实现（toString、equals、hashCode   ps:没有在主构造方法声明的属性不会加入到相等性检查和哈希值的计算中去，见代码）



7、类委托by关键字（略）



8、"Object"关键字：将声明一个类与创建一个实例结合起来

1）对象声明：一个对象声明可以包含除构造方法（主和从）以外，普通类的所有东西，在对象声明定义的时候就已经创建了实例（单例）

```kotlin
object payRoll{
    val shuxing = arrayListOf<String>() 
    fun function(){
        //TODO     
    } 
} 
//可以通过对象名加"."字符的方式来调用方法和属性 
payRoll.shuxing.add("hello") payRoll.function() 
```





2）伴生对象：可用于代替Java中的静态方法（另一种代替就是顶层函数），关键字companion和object搭配，就获得了直接通过容器名称来访问属性和方法的能力，基础语法如下。

```kotlin
clas A{ 
    companion object{
        fun function(){ 
            //TODO        
        }     
    } 
} 
```

对于工程方法，也能很简单地实现（略）



3）作为普通对象的半生对象：伴生对象是一个声明在类中的普通对象。和普通类差别不大,它也可以实现接口或者有扩展函数、属性

```kotlin
class A{
    companion object bansheng{ 
        fun function(){     
            //TODO       
        }  
    }
} 
```

半生对象扩展，即指，如果类c有一个伴生对象，并且c.Companion上定义了一个扩展函数function，可以通过c.function调用它

```kotlin
class C{    
    companion object{  
        //TODO   
    } 
}  
fun C.Companion.function(){
    
} 
//实际调用 val p = C.function() 


```

4)对象表达式，改变写法的匿名内部类

用object关键字来声明匿名内部类



```kotlin
button.setOnClickListener(object :View.OnClickListener{  
    override fun onClick(v: View?) {  
        //TODO    
    } 
}) 


```

**5、Lambda编程**

1、作为函数参数的代码块：把函数当作值来对待，可以直接传递函数

//这一行代码和上面的代码功能相同 button.setOnClickListener{//TODO} 



2、语法

```kotlin
val sum = {x:Int, y: Int -> x + y} println(sum(1, 2)) 
```

"->"号前面是参数，后面是函数体，整个 表达式始终处于{}中



```kotlin
val people = listOf(Person("A",20),Person("B",22)) 
println(people.maxBy(){it.age}) 
//下面这句与上面的等效果 
println(people.maxBy({p: Person -> p.age})) 
```

 	其中的第二行的it是用来引用集合中的元素作为实参，是自动生成的参数名称，但是不应滥用，特别是在嵌套lambda的情况下，很难搞清楚it到底引用的哪个值



```kotlin
var groupedStudentList = studentList.GroupBy(it.ClassId).OrderByDescending(it.Min (it->it.Age)) 
```

ps：Kotlin有一种语法约定，如果lambda表达式是函数调用的最后一个实参，它可以放到括号的外面（即以下两行语句的作用相同）

```kotlin
people.maxBy(){it.age} people.maxBy({it.age}) 
//当lambda是唯一实参时，还可以去掉括号 
people.maxBy{it.age} 


```

3、捕捉可变变量的实现细节

基本语法：

```kotlin
fun printSomething(messeages:Collection<String>, pre:String){ 
    messages.forEach{  
        println("$pre $it)    
                }
                } 


```

Kotlin允许在lambda内部访问非final变量，甚至修改它们。从lambda内部访问外部变量，我们称这些变量被lambda捕捉.

```kotlin
//模拟捕捉可变变量的类 
class Ref<T> (var value: T) 
val counter = Ref(0) 
val inc = {counter.value++} 
//实际代码中，可以不要这个包装器 
var counter =  val inc = {counter++} 
```

当捕捉一个val变量时，它的值被拷贝下来。而当捕捉var变量时，它的值作为Ref类的一个实例被保存，实际值储存在其字段中，并可以在lambda内部修改

4、对集合进行操作：filter和map

```kotlin
//filter val list = listOf(1, 2, 3, 4) 
println(list.filte{it % 2 == 0}) [2, 4] 
```

filter函数可以从集合中移除你不想要的元素，但它并不会改变这些元素



```kotlin
//map val lsit = listOf(1, 2, 3, 4) 
println(list.map{it * it}) [1, 4, 9, 16] 
```

map和filter可以合并处理集合中的元素（略）

5、flatMap和flatten处理嵌套集合中的元素

假设有一堆书用Book类表示

```kotlin
class Book(val title:String, val authors: List<String>)  
//用flatMap便可以统计出所有作者的集合 
books. flatMap{it.authors}.toSet() 
```

flatMap()先是将作为实参给定的函数对集合中的每个元素做变换，然后合并多个列表。（映射与平铺，见代码）



6、序列

```kotlin
people.map(Person::name).filter{it.startsWith("A")} 
```

上述语会先经过map返回一个列表，filter再在这个列表进行操作，当数据量过大时，会占用非常多的资源，效率十分低下，但是如果将其转换成序列再进行操作，将会有非常高的效率

```kotlin
people.asSequence(   
    .map(Person::name)     
    .filter{it.startsWith("A")}   
    .toList() ) 
```



因为这种写法没有创建任何的中间元素集合

序列操作分为中间和末端操作，中间操作返回一个序列，末端操作返回一个结果。中间操作时惰性的，没有末端操作，中间操作不会被执行。

创建序列：1、在集合上调用asSequence（）；2、使用generateSequence（）函数。（generateSequence（）函数的使用见代码）



7、函数式接口与SAM构造方法

函数式接口：某些接口只有一个抽象方法（如OnClickListener,其只有一个onClick函数），这种接口被称为函数式接口、或者SAM接口。其基本语法如下：

```kotlin
button.setOnClickListener{view -> ...} 
```



当然也可以使用传统的匿名内部内的形式

```kotlin
button.setOnClickListener(object :View.OnClickListener{ 
    override fun onClick(v: View?) {    
        //TODO    
    } 
})  
```

但是显示地声明对象时，每次调用会创建一个新的实例，使用lambda则不同，如果没有访问任何来自定义它地函数的变量，相应的匿名类实例可以在多次调用之间重用

```kotlin
val click = View.OnClickListener{println("hello")} button.setOnClickListener(click) 
```





SAM构造方法是编译器生成的函数，让你执行从lambda到函数式接口实例的显示转换。。。通常可以用SAM构造方法把lambda包装起来(见代码)



SAM构造方法还可以用在需要把从lambda生成的函数式接口实例存储在一个变量中的情况

```kotlin
val listener = OnClicListener{view ->...} 
button1.setOnClicListener(listener) 
button2.setOnClicListener(listener) 
```



8、with和apply

/

```kotlin
/该方法返回字母表
fun function ： String(){   
    val strBuilder = StringBuilder() 
    return with(strBuilder){          
        for(letter in 'A'..'Z')  
        //可以通过显式的this来调用   
        this.append(letter)   
        //也可以省略掉this    
        append("\nfinished") 
        this.toString()    
    } } 


```

实际上的调用是with（strBuilder，{...}）。with函数把它的第一个参数转换成作为第二个参数传给它的lambda的接收者，可以显式的用this访问这个接收者，也可以省掉this，隐式地直接访问这个值地方法和属性



```kotlin
//该方法返回字母表
fun function = with(StringBuilder()){    
    for(letter in 'A'..'Z')      
    append(letter)      
    append("\nfinished")  
    this.toString()
} } 
```

可以创建一个一个新的StringBuilder实例直接当作参数传给这个函数



apply

在function3（）中，apply被声明成了扩展函数。他的接收者变成了作为实参的lambda的接收者。执行的结果是StringBuilder



apply还常用于创建一个对象实例并需要用正确的方式初始化它的一些属性的时候

```kotlin
fun function(context: Context) = 
TextView(context).apply{  
    text = "something"     
    textSize = 20.0     
    .... 
}  


```

补充（来自大姐头的友情提醒

lateinit关键字

在Kotlin中，如果在类型声明后面没有使用符号？，就表明该变量不会为null。但是这个时候会要求我们初始化一个值，但在某些时候，我们并不能初始化这个值

所以有了，延时加载，如果你给变量加上了lateinit关键字，他就认为你肯定会初始化，Kotlin的编译器就不会做这种检查

ps：因为kotlin会用null来对lateinit修饰的属性初始化，而基础类型没有null类型，所以该关键字无法修饰基本类型，并且无法修饰val变量