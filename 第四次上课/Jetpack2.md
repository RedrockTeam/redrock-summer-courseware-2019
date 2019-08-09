# Jetpack第二部分： Navigation、Paging、Room、WorkManager

![bg_jetpack](/Users/huanglong/Documents/bg_jetpack.png)

## 主要内容

* Navigation 
* Paging 分页加载库
* Room 实现数据持久化
* WorkManager 处理后台工作  
## Navigation 导航

> 目前来说主要服务于单Activity和多个Fragment的架构，同时也支持Activity，今天主要探讨单Activity多Fragment的架构   


提供~~我用得上~~主要的功能：

* 自动管理Fragment(创建，销毁等)√

* 更方便的跳转、参数传递，提供可视化的界面√

* 跳转动画√

官方介绍的Benefit:

![Navigation Component Benefits](/Users/huanglong/Documents/Navigation Component Benefits.png)

* 简单的设置常见的导航
* 处理后台堆栈
* 自动Fragment跳转
* 安全的参数传递
* 基于导航的动画
* 深层链接
* 将以上所有数据收集在一个可视化的页面，即导航图

### 1.Navigation的简单使用

1. 在res目录下新加一个新的目录`navigation`，然后新建`Navigation resource file`  
![1](/Users/huanglong/Documents/1.png)  
系统会提示你添加依赖，如果没有成功添加请手动添加依赖  
```
dependencies{
//Kotlin版本，Java去掉 -ktx 即可
	implementation 'androidx.navigation:navigation-fragment-ktx:2.0.0'
	implementation 'androidx.navigation:navigation-ui-ktx:2.0.0'
}
```
2. 在Activity的xml布局文件中添加`NavHostFragment`  
```xml
<fragment
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/fragment_main"
        android:name="androidx.navigation.fragment.NavHostFragment"
        app:navGraph="@navigation/main_navigation"
        app:defaultNavHost="true" />
```
> id不写会报错，name指定错误也会报错  
3. 新建三个fragment，写好布局，添加几个用于跳转的按钮  
4. 配置导航图，增加每个fragment的 `<action/>`   
5. 在fragment里配置跳转  

### 2.Navigation的安全传参  
1. 添加插件  

```xml
//Project的build.gradle
dependencies {
        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0-beta02"
    }
//app的build.gradle 
    apply plugin: "androidx.navigation.safeargs.kotlin"
```
在导航图中发送数据的fragment中添加`<argument>`标签，可以为自定义类型的参数，但是需要支持序列化

```
<argument
            android:name="arg_1"
            app:argType="com.override0330.jetpackdemo.Data"
            android:defaultValue=""
            app:nullable="true"/>
```

在fragment中传递数据出去,其中`BlankFragmentArgs`为系统自动生成的类(和DataBinding类似) 这里使用的是自定义的类型

```Kotlin
btn_fragment_1_1.setOnClickListener {
            //参数传递
            val args = BlankFragmentArgs(Data("2019.8.7", "fragment1"))
            findNavController().navigate(R.id.action_1_to_2, args.toBundle())
        }
```

4. 接收参数，这里需要判空，因为不一定是由指定fragment传递的参数，也可能没有参数
```Kotlin
val args = arguments?.let { BlankFragmentArgs.fromBundle(it) }
        val data = args?.arg1
        Toast.makeText(this.context, "${data?.time}from${data?.from}",Toast.LENGTH_LONG).show()
```

### 3. Navigation DeepLink
* 实现通过uri来启动指定的`destination`
1. 在Navigation中想要跳转到的Fragment/Activity中添加`<deepLink>`标签，并定义你的uri
```
 <deepLink app:uri="www.Override0330.com/jetpack"/>
```
2. 在AndroidManifest.xml的activity中添加`<nav-graph>`标签,类似静态广播接收器的注册
```
<nav-graph android:value="@navigation/main_navigation"/>
```
3. 现在你可以在游览器搜索框中输入我们设定个uri来跳转到指定的`destination`  

## Paging 分页加载库
* 其实就是一个Recyclerview的adapter，用来实现无限滚动的加载效果，e.g. JD、知乎 (上拉下拉加载： 淘宝)
### 1. 简单使用
1. 首先添加依赖，Jetpack官网的依赖最新版本⤵️
```
dependencies {
  def paging_version = "2.1.0"

  implementation "androidx.paging:paging-runtime:$paging_version" // For Kotlin use paging-runtime-ktx

  // alternatively - without Android dependencies for testing
  testImplementation "androidx.paging:paging-common:$paging_version" // For Kotlin use paging-common-ktx

  // optional - RxJava support
  implementation "androidx.paging:paging-rxjava2:$paging_version" // For Kotlin use paging-rxjava2-ktx
}
```
2. 创建数据源，这里使用Room数据库作为数据源

   ```kotlin
   @Entity
   data class Student(@PrimaryKey(autoGenerate = true) val id: Int,
                      val name: String)
   ```

   

3. 创建Adapter，该Adapter继承自`PagedListAdapter`，该泛型接收两个泛型参数，一个是数据源的Entity类，也就是我们平时的item类，一个是ViewHold，同时需要传入一个判断item之间是否相同的callback

   ```kotlin
   class BasicStudentAdapter : PagedListAdapter<Student, StudentViewHolder>(diffCallback){
     //省略其他配置
     companion object {
           private val diffCallback = object : DiffUtil.ItemCallback<Student>() {
               override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean =
                       oldItem.id == newItem.id
   
               override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean =
                       oldItem == newItem
           }
       }
   }
   ```

4. 在ViewModel层拿到Room的数据包装成LiveData  
	```kotlin

   fun getRefreshLiveData(): LiveData<PagedList<Student>> =
               LivePagedListBuilder(dao.getAllStudent(), PagedList.Config.Builder()
                       .setPageSize(PAGE_SIZE)                         //配置分页加载的数量
                       .setEnablePlaceholders(ENABLE_PLACEHOLDERS)     //配置是否启动PlaceHolders
                       .setInitialLoadSizeHint(PAGE_SIZE)              //初始化加载的数量
                       .build()).build()
   //注:
    @Query("SELECT * FROM Student ORDER BY name COLLATE NOCASE ASC")
       fun getAllStudent(): DataSource.Factory<Int, Student>
   //返回的是一个DataSource.Factory
   //hint 通过这个工厂类获得DataSource,然后在转换成PageList，最后生成为LiveData
  ```
  
## Room 实现数据持久化-基于SQLite

数据持久化的重要性：

* 能够及时地保存信息
* 在网络不佳的情况下依然可以提供数据展示服务

### 1. Room的简单使用
**三大组件：**
* RoomDatabase 数据库对象
* Entitise 数据库中每一条记录对应的java对象
* DAOs 数据库操作接口  
依赖：
```
    implementation 'androidx.room:room-runtime:2.1.0'
    annotationProcessor 'androidx.room:room-compiler:2.1.0'
```
#### 1.1 建立数据库记录的类
这里以Student为例
​```kotlin
@Entity
class Student {
    @PrimaryKey
    var id: Int = 0

    var name: String = ""
    @ColumnInfo(name = "地址")
    var address: String = ""
    
    @Ignore
    var someThingNotNeed: String = ""
}
```
**需要给类添加`@Entity`注释**
* `@Entity(primaryKeys = {"id","name"})` 如果主键不是单个field，可以使用`primaryKeys`来设定由多个field组合而成的主键
* `@Entity(tableName = "StudentTable")` 默认使用类名作为数据库表的名称。可以使用`tableName`来设定自定义名称  
* `@Entity(indices = {@Index("id"),Index(value = {"id","name"})})` 添加单一索引和复合索引
* `@Entity(indices = {@Index(value = ...,unique = true)})`对特定的field设置唯一性，value内的field不能同时相同
下面是属性的相关注释用途⤵️
|  注解    |    用途    |
| ---- | ---- |
|@PrimaryKey|主键，每一条数据的唯一标识符|
|@Ignore|忽略，不想记录到数据库中的属性|
|@ColumnInfo    |   自定义列名   |

**关系型数据库**
关系型数据库可以在两个Entity之间建立关系，Room中使用**外键**来实现
假设有两个Entity,一个是Student，一个是Subject，Student应该持有Subject
​```Kotlin
@Entity(foreignKeys = [ForeignKey(
    entity = Student::class, //建立关系的类
    parentColumns = arrayOf("id"), //父（？）类的列名
    childColumns = arrayOf("user_id"), //本类中存放父（？）类的列名
    onDelete = CASCADE //（可选）父（？）类记录被删除时，本类记录自动删除
    onUpdate = SET_NULL //（可选）父（？）类记录更新时，本类记录全部设置为null
)]
)
class Computer {
    @PrimaryKey
    var computerId: Int = 0

    var name: String = ""
    @ColumnInfo(name = "user_id")
    var userId: String = ""
    
    @Ignore
    var someThingNotNeed: String = ""
}
```
**Entity对象嵌套**
使用`@Embedded`来在Entity类中加入一个类，此时数据库中会增加加入类的所有字段
​```Kotlin
class Address {
    var postCode = "000001"
    var city = "Beijing"
    ......
}

@Entity
class Student {
    @PrimaryKey
    var id: Int = 0

    var name: String = ""
    @Embedded
    var address = Address()
    
    @Ignore
    var someThingNotNeed: String = ""
}
```
#### 1.2 建立DAOs(Data Access Objects)

* 访问数据库的接口，可以是一个interface也可以是一个abstract class
不多bb上代码：
​```kotlin
@Dao
interface StudentDao {
    //增
    @Insert
    fun insertStdent(student: Student)

    @Insert
    fun insertStdent(studentList: List<Student>)

    //删
    @Delete
    fun deleteStudent(student: Student)

    //改
    @Update
    fun updateStudent(student: Student)

    //查
    @Query("SELECT * FROM student")
    fun getAllStdent(): List<Student>

    @Query("SELECT * FROM student WHERE id = :id")
    fun getById(id: Int): Student

    @Query("SELECT * FROM student WHERE id in (:id)")
    fun getInId(id: List<Int>): List<Student>
}
```
#### 1.3 建立数据库类(建议使用单例模式，这里不列出，demo中有)

```kotlin
@Database(entities = [Student::class], version = 1)
abstract class StudentDataBase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
}
```
#### 1.4 建表

​```Kotlin
val studentDb = Room.databaseBuilder(this,
            StudentDataBase::class.java,
            "studentRoom").build()//保存在本地的数据库
Room.inMemoryDatabaseBuilder()//保存在内存中的数据库，进程结束会丢失数据
```
#### 1.5使用数据库

![method](/Users/huanglong/Documents/method.png)

> 注意，在对数据库进行操作的时候应该在非UI线程，如果不进行设定的话是不允许在UI线程中对数据库进行操作的