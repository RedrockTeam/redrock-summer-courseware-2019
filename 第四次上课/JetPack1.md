#JetPack

![](/Users/silas/Desktop/JetPack架构图.png)

**功能**

1. Activity和Fragment负责产品与用户的交互
2. ViewModel作为数据的存储和驱动
3. Resposity负责调度数据的获取
4. Room储存本地序列化的数据
5. Retrofit获取远程数据的数据

---

###数据绑定库（使用不当的话坑很多哦，到底是我也不会用）

---

* 使用入门：支持Android4.0(API level 14)及以上，Gradle 1.5.0以上。

  build.gradle添加配置:

  ```xml
  android {
      ...
      dataBinding {
          enabled = true
      }
  }
  ```

* 布局和绑定表达式:

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <layout xmlns:android="http://schemas.android.com/apk/res/android">
      <data>
          <variable name="user" type="com.xl.jetpackdemo.model.User"/>
      </data>
      <LinearLayout
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="match_parent">
          <TextView android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textSize="50sp"
                    android:layout_gravity="center_horizontal"
                    android:text="@{user.name}"/>
          <TextView android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_horizontal"
                    android:layout_marginTop="100dp"
                    android:textSize="100sp"
                    android:text="@{String.valueOf(user.age)}"/>
      </LinearLayout>
  </layout>
  ```

  这里的xml根节点变成了layout，里面包含了data节点和其他的布局。可以不给控件设置id，而使用@{}的方式，可以在括号里面引用data里面的对象的属性去赋值。

  对应的User类:

  ```kotlin
  class User(val name:String,val age:Int)
  ```

  Activity中:

  ```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      val binding: com.xl.jetpackdemo.databinding.ActivityMainBinding = DataBindingUtil.setContentView(
          this, R.layout.activity_main)
      binding.user = User("Test", 1)
  }
  ```
  
  这里的ActivityMainBinding是as生成的。

* Observable data objects

  DataBinding中有三种不同的数据，object, field, collection。

  * Observable Objects

    Observable是提供移除监听的一个java接口，Google为我们提供了一个BaseObservable类，可以直接继承。我们只要把Model类继承自它就获得了通知UI更新数据的能力了，然后再getter方法上添加Bindable注解，在setter方法中使用notifying提醒UI更新数据。

    ```kotlin
    class User : BaseObservable() {
    
        @get:Bindable
        var firstName: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.firstName)
            }
    
        @get:Bindable
        var lastName: String = ""
            set(value) {
                field = value
                notifyPropertyChanged(BR.lastName)
            }
    }
    ```

  * Observable Fields

    Google也推出ObservableField类，这样更简化Model类。

    ```kotlin
    class User {
        val firstName = ObservableField<String>()
        val lastName = ObservableField<String>()
        val age = ObservableInt()
    }
    ```

    然后通过get()，set()来得到属性的值

    ```kotlin
    user.firstName = "Google"
    val age = user.age
    ```

    ---

    ```java
    user.firstName.set("Google");
    int age = user.age.get();
    ```

    一些其他的Obserabl类 **ObservableBoolean, ObservableByte, ObservableChar, ObservableShort, ObservableInt, ObservableLong, ObservableFloat, ObservableDouble, and ObservableParcelable**

  * Observable Collections

    Google也为我们提供了一些通知类型的集合，有这三种：ObservableArrayList<**T**>、ObservableArrayMap<**K,V**>、ObservableMap<**K,V**>

    我们在layout中的<**data**>区域导入包后就可以直接用它了，当它内部的数据发生改变时就自动会通知UI界面更新

    ```kotlin
    ObservableArrayMap<String, Any>().apply {
        put("firstName", "Google")
        put("lastName", "Inc.")
        put("age", 17)
    }
    ```

    ```xml
    <data>
        <import type="android.databinding.ObservableMap"/>
        <variable name="user" type="ObservableMap<String, Object>"/>
    </data>
    …
    <TextView
        android:text="@{user.lastName}"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
    <TextView
        android:text="@{String.valueOf(1 + (Integer)user.age)}"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
    ```

* 绑定数据

  Android studio会根据layout文件自动生成一个默认的Binding类，类名是根据layout文件名生成的，并有"Binding"后缀结束。例如：activity_main.xml生成的Binding类为ActivityMainBinding

  binding 对象应该在 inflat 布局后立即创建，以确保 View 层次在绑定到布局中的表达式视图之前不被修改。

  创建绑定对象的几种方法:

  * 使用 inflate() 方法

    ```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        val binding: MyLayoutBinding = MyLayoutBinding.inflate(layoutInflater)
    }
    ```

    inflate()它接受一个`ViewGroup`对象

    ```kotlin
    val binding: MyLayoutBinding = MyLayoutBinding.inflate(getLayoutInflater(), viewGroup, false)
    ```

  * 绑定根布局

    ```kotlin
    val binding: MyLayoutBinding = MyLayoutBinding.bind(viewRoot)
    ```

  * 如果绑定的type提前不知道，可以使用DataBindingUtil创建

    ```kotlin
    val viewRoot = LayoutInflater.from(this).inflate(layoutId, parent, attachToParent)
    val binding: ViewDataBinding? = DataBindingUtil.bind(viewRoot)
    ```

  * 如果想在Fragment,ListView,RecyclerView里面绑定数据，可以用这种方式

    ```kotlin
    val listItemBinding = ListItemBinding.inflate(layoutInflater, viewGroup, false)
    // or
    val listItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false)
    ```

* Binding adapter

  例如，想通过databinding绑定一张网络请求的图片。

  ```kotlin
  @JvmStatic
  @BindingAdapter("imageUrl", "error")
  fun loadImage(view: ImageView, url: String, error: Drawable) {
      Picasso.get().load(url).error(error).into(view)
  }
  ```

  xml：

  ```xml
  <ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
  ```

  支持多参数

  方法为公共静态类

* 双向绑定

  例子

  ```xml
  <RelativeLayout ...>
      <EditText android:text="@={user.name}" .../>
    </RelativeLayout>
  ```

  这样，在用户输入text的时候，user.name也发生了变化
  
* 源码浅谈：

  1. Databinding在编译时会生成代码，利用的技术是Apt(annotation-processing-tool)。在我们按照规则编写完xml之后，就会生成相应的java文件，文件名为xml文件名加上Binding后缀，按照上述实例会生成ActivityMainBinding.java文件，（我的demo）其生成的路径为DataBindingDemo/app/build/generated/data_binding_base_class_source_out/debug/dataBindingGenBaseClassesDebug/out/com/xl/databindingdemo/databinding/ActivityMainBinding.java持有含id的view引用，持有绑定的类的引用
  
  2. ActivityMainBindingImpl，对应路径DataBindingDemo/app/build/generated/source/apt/debug/android/databinding/DataBinderMapperImpl.java，继承ActivityMainBinding持有没有id的view的引用，具体实现了绑定
  
  3. BR:对应路径DataBindingDemo/app/build/generated/source/apt/debug/com/xl/databindingdemo/BR.java存储了VM的id，类似R文件
  
  4. DataBinderMapperImp，对应路径：DataBindingDemo/app/build/generated/source/apt/debug/com/xl/databindingdemo/DataBinderMapperImpl.java,这个类主要是提供了从布局文件layoutId到ViewDataBinding类对象的映射，主要是用于在加载Layout返回对应的ViewDataBinding对象。
  
  5. setContentView，发现也是调用了Activity的setContentView
  
     然后传入到bindToAddedViews()方法，判断是否有多个子布局，调用不同参数的bind方法
  
     最后调用DataBinderMapperImpl的getDataBinder方法，返回对应的Layout的ViewDataBinding实例
  
     ![](/Users/silas/Desktop/view遍历流程.png)
  
     获得view之后，会通过invalidataAll()请求数据更新，还是回到了ActivityMainBinding的executeBindings()，在这个方法里将更新后的model数据，onclick等重新设置到Textview，Button上，完成了model->view的单向绑定。
  
     ![](/Users/silas/Desktop/数据绑定流程.png)
  
     （真看不懂了，剩下的可以去了解）

***

###Lifecycles

* 引入：Android系统中定义的大多数组件都是有生命周期的。这些组件的生命周期是由系统管理的，比如广播注册和解绑，Eventbus。作为一个开发者必须遵守这些生命周期的规则，否则就会出现内存泄漏或者应用崩溃的情况。lifecycle提供的类和接口就可以帮助构建能够感知生命周期的类。

* Google官方的一个例子：比如现在需要对Activity的一个位置进行监听。

  ```kotlin
  internal class MyLocationListener(
          private val context: Context,
          private val callback: (Location) -> Unit
  ) {
  
      fun start() {
          // connect to system location service
      }
  
      fun stop() {
          // disconnect from system location service
      }
  }
  
  class MyActivity : AppCompatActivity() {
      private lateinit var myLocationListener: MyLocationListener
  
      override fun onCreate(...) {
          myLocationListener = MyLocationListener(this) { location ->
              // update UI
          }
      }
  
      public override fun onStart() {
          super.onStart()
          myLocationListener.start()
          // manage other components that need to respond
          // to the activity lifecycle
      }
  
      public override fun onStop() {
          super.onStop()
          myLocationListener.stop()
          // manage other components that need to respond
          // to the activity lifecycle
      }
  }
  ```

  看似没有问题，那当在activity的每个生命周期中，比如onStart()，onStop()处理的事务多了，就很难管理生命周期。

  例如，现在需要在监听前，检查一些设置

  ```kotlin
  class MyActivity : AppCompatActivity() {
      private lateinit var myLocationListener: MyLocationListener
  
      override fun onCreate(...) {
          myLocationListener = MyLocationListener(this) { location ->
              // update UI
          }
      }
  
      public override fun onStart() {
          super.onStart()
          Util.checkUserStatus { result ->
              // what if this callback is invoked AFTER activity is stopped?
              if (result) {
                  myLocationListener.start()
              }
          }
      }
  
      public override fun onStop() {
          super.onStop()
          myLocationListener.stop()
      }
  
  }
  ```

  假如，在onStart()里面的检查时间太长，然后就执行了onStop()，那么，监听还没有开始，就结束，是没有逻辑的。而且，如果onStop()后，监听又开始start，而且一直没有调用stop，那就会一直监听。

* 使用Lifecycle后

  实现LifecycleObserver接口，用@OnLifecycleEvent注解

  ```kotlin
  class MyLocationListener(
      private val context: Context,
      private val lifecycle: Lifecycle,
      private val callback: (Location) -> Unit
  ) : LifecycleObserver{
  
      private var enabled = false
  
      @OnLifecycleEvent(Lifecycle.Event.ON_START)
      fun start() {
          if (enabled) {
              // connect
          }
      }
  
      fun enable() {
          enabled = true
          if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
              // connect if not connected
          }
      }
  
      @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
      fun stop() {
          // disconnect if connected
      }
  }
  ```

  Activity中

  ```kotlin
  lifecycle.addObserver(myLocationListener)
  ```

  或者，实现GenericLifecycleObserver接口，实现onStateChanged方法

* 核心类和结构

  订阅者模式

  * LifecycleObserver：安卓生命周期观察者，空接口
* Lifecycle: 类，定义了安卓生命周期对象。添加观察者，移除观察者，获取当前状态
  * LifecycleOwner: Android生命周期拥有者
  * ![结构图](/Users/silas/Desktop/lifecycle逻辑图.png)

* 源码浅谈

  * **LifecycleRegistryOwner/LifecycleOwner**

    在Activity等组件生命周期发生变化的时候，发出相应的Event给LifecycleRegistry
  
  * **LifecycleRegistry**
  
    控制state的转换、接受分发Event事件。
  
  * **LifecycleObserver**
  * 通过注解绑定Event和自定义的函数，实现对生命周期的监听并处理。
  
  (进入as)
  
  进入lifecycle:
  
  ```java
  public class LifecycleRegistry extends Lifecycle
  ```
  
  进入addObserver()：
  
  ```java
  ObserverWithState statefulObserver = new ObserverWithState(observer, initialState);
  ```
  
  把带着状态的observer封装成ObserverWithState
  
  ![states](/Users/silas/Desktop/states.png)
  
  进入ObserverWithState():
  
  ```java
  ObserverWithState(LifecycleObserver observer, State initialState) {
      mLifecycleObserver = Lifecycling.getCallback(observer);
      mState = initialState;
  }
  ```
  
  通过不同的observer返回不同的mLifecycleObserver
  
  进入getCallback()：
  
  ```java
  @NonNull
  static GenericLifecycleObserver getCallback(Object object) {
    ...
  }
  ```
  
  返回一个GenericLifecycleObserver或者它的实现类
  
  所以以上就是对我们传入的对象和state进行包装成ObserverWithState，然后再添加到mObserverMap，mObserverMap是一个Map结构，以观察者为key存储观察者状态。
  
  进入ReflectiveGenericLifecycleObserver():
  
  ```java
  class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver {
      private final Object mWrapped;
      private final CallbackInfo mInfo;
  
      ReflectiveGenericLifecycleObserver(Object wrapped) {
          mWrapped = wrapped;
          mInfo = ClassesInfoCache.sInstance.getInfo(mWrapped.getClass());
      }
  
      @Override
      public void onStateChanged(LifecycleOwner source, Event event) {
          mInfo.invokeCallbacks(source, event, mWrapped); //这里：通过反射实现事件分发
      }
  }
  ```
  
  在这里，我们看见了onStateChanged()
  
  但是我们不知道在哪里调用的。
  
  回到SupportActivity.class
  
  ```java
  protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      ReportFragment.injectIfNeededIn(this);
  }
  ```
  
  这里，把activity传到了ReportFragment
  
  进入ReportFragment
  
  ```java
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      dispatchCreate(mProcessListener);
      dispatch(Lifecycle.Event.ON_CREATE);
  }
  
  @Override
  public void onStart() {
      super.onStart();
      dispatchStart(mProcessListener);
      dispatch(Lifecycle.Event.ON_START);
  }
  
  @Override
  public void onResume() {
      super.onResume();
      dispatchResume(mProcessListener);
      dispatch(Lifecycle.Event.ON_RESUME);
  }
  
  @Override
  public void onPause() {
      super.onPause();
      dispatch(Lifecycle.Event.ON_PAUSE);
  }
  
  @Override
  public void onStop() {
      super.onStop();
      dispatch(Lifecycle.Event.ON_STOP);
  }
  
  @Override
  public void onDestroy() {
      super.onDestroy();
      dispatch(Lifecycle.Event.ON_DESTROY);
      // just want to be sure that we won't leak reference to an activity
      mProcessListener = null;
  }
  ```
  
  查看dispatch()这个方法:
  
  再进入handleLifecycleEvent(event):
  
  moveToState(State next)
  
  看到sync()来同步的方法
  
  里面又调用了backwardPass(LifecycleOwner lifecycleOwner)，forwarPass(LifecycleOwner lifecycleOwner)方法
  
  这两个方法都又调用了observer.dispatchEvent(lifecycleOwner, event)，又回到static class ObserverWithState{}里面
  
  ![<源码逻辑图>](/Users/silas/Desktop/屏幕快照 2019-08-06 上午11.47.55.png)

***

###LiveData&&ViewModel

---

**mvvm**

关键   MVVM 把 View 和 Model 的同步逻辑自动化了。以前 Presenter 负责的 View 和 Model 同步不再手动地进行操作，而是交由框架所提供的 Binder 进行负责。

* LiveData是一种持有可被观察数据的类。和其他可被观察的类不同的是，LiveData是有生命周期感知能力的，这意味着它可以在activities, fragments, 或者 services生命周期是活跃状态时更新这些组件。配合LifecycleOwner的对象使用，当对应的生命周期对象Ondestory后时，能及时移除观察者，有效的避免了内存泄漏的问题。

* ViewMode用于存放和处理根ui相关的数据，并且不受配置变化影响，比如屏幕旋转。mvvm中，提供数据。

* LiveData和ViewModel的搭配

  LiveDataDemo

* LiveData优点：

  * UI和实时数据保持一致 （观察者模式）
  * 不用考虑生命周期（lifecycle）
  * 数据共享（继承单例livedata）

* ViewModel优点：

  * 解耦，vm层管理数据
  
  * 共享fragment
  
  * Fragment/Activity, Fragment/Fragment之间通信
  
  * Activity在生命周期中可能会触发多次`onCreate()`，而ViewModel则只会在第一次`onCreate()`时创建，然后直到最后Activity销毁。
  
    ![生命周期对比](/Users/silas/Desktop/vm生命周期.png)

---

**LiveData源码浅谈**

* MutableLiveData: MutableLiveData是LiveData的子类，添加了公共方法setValue和postValue，方便开发者直接使用。setValue必须在主线程调用。postValue可以在后台线程中调用。

* observe方法

  ```java
  public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer)
  ```

  两个参数，LifecycleOwner和Observer。

  ```java
  LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
  ```

  将LifecycleBoundObserver传入LifecycleRegistry完成订阅

  ```java
  owner.getLifecycle().addObserver(wrapper);
  ```

  （再去看看lifecycle。。。。）

  调用setValue方法后会调用dispatchingValue方法循环通知订阅者响应数据变化。

  把所有的观察者，都执行considerNotify()，回调更新数据

  ```java
  observer.mObserver.onChanged((T) mData);
  ```

  注意livedata的活跃状态，onStart()之后，才能接收到更新，pause, stop,destory之后都不会接收更新

---

**ViewModel源码浅谈**

* 先看ViewModelProviders，？全是静态方法，那就相当于一个工具类了

  疯狂return。。。

  ```java
  @NonNull
  @MainThread
  public static ViewModelProvider of(@NonNull FragmentActivity activity,
          @Nullable Factory factory) {
      Application application = checkApplication(activity);
      if (factory == null) {
          factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application);
      }
      return new ViewModelProvider(ViewModelStores.of(activity), factory);
  }
  ```

  到了这个方法，返回的是一个ViewModelProvider类的对象，构造方法的参数是(ViewModelStores.of(activity)，那先看看这个ViewModelStore

  进ViewModelStores.of方法，看返回什么，这是一种情况：

  ``` java
  return holderFragmentFor(activity).getViewModelStore()
  ```

  …又是一个新的。看看 holderFragmentFor(activity)，HoldFragmet

  从这个类中可以发现获取ViewModelStore是通过构建一个HoldFragment，而这个HoldFragment的作用就是持有ViewModelStore的引用，然后将这个Fragment添加到Activity中，而且这个Fragmet有一个重要的特点，在实例化的时候会调用setRetainInstance(true)方法，这个方法的作用是让Fragment不受Activity销毁重建影响，这样一来就能保证ViewModel不会由于Activity的销毁重建导致数据丢失，这是ViewModel的一个重要特性。(网上抄的。。)

  ```java
  public HolderFragment() {
      setRetainInstance(true);
  }
  ```

  还真有。里面就看不懂了。。

  那就回去吧，回到最开始的ViewModelProvider类

  成员变量：

  ```java
  private final Factory mFactory;
  private final ViewModelStore mViewModelStore;
  ```

  工厂模式？Factory接口，里面一个craete方法，两个实现类AndroidViewModelFactory、NewInstanceFactory都是ViewModelProvider中的静态内部类。同时AndroidViewModelFactory又继承于NewInstanceFactory。

  ViewModelStore中维护了一个HashMap，存储ViewModel

  ```java
  private final HashMap<String, ViewModel> mMap = new HashMap<>();
  ```

  