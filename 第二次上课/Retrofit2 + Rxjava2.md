#                       Retrofit2 + **Rxjava2**            

  这两个第三方库常常一起用来网络请求

 由于我能力有限，只能知道怎么用的，知道一些常用的api,内部实现的原理和源码暂时不懂，见谅



##  Retrofit2

现在最流行的网络请求框架是okhttp3,而这个Retrofit2就是基于okhttp3的，但是更加方便和强大，而且用注解的方式来拼接url。

依赖：**implementation 'com.squareup.retrofit2:retrofit:2.3.0'**

**用法**：

第一步：定义一个网络请求接口

```java
interface ApiService{

    @GET("v2/feed?")//使用注解
    fun getHomeData(@Query("num") num:Int): Call<HomeBean>
 
    }
```

在这个接口定义中，用注解@GET声明了url路径，用注解@Query 声明了请求参数。关于注解这里还有很多东西，后面说

 最重要的是，用Call<HomeBean>声明了返回值是一个Retrofit的Call对象，并且声明了这个对象处理的数据类型为HomeBean，HomeBean是我们自定义的数据模型,也就是实例类

第二步：

依次获得**Retrofit对象、接口实例对象、网络工作对象**

```kotlin
         // 获取retrofit的实例
         val retrofit  =  Retrofit.Builder()
                .baseUrl("http://baobab.kaiyanapp.com/api/")  //自己配置baseurl
                .addConverterFactory(GsonConverterFactory.create())//这里添加的Gson对应的是类是api请求里面返回的那个Call对象的泛型类,就是HomeBean
                .build()
```

Gson的Converter，当然这里的Conver可以自己配置，但是我不会

如果不加入Gson支持的话返回的就是responsebody ,就只是 josn数据，加了后就可以转化为HomeBean实体类

```kotlin
    val service= retrofit.create(ApiService::class.java)//接口实例对象 
    call: Call<HomeBean> = service.getHomeData(1);//网络工作对象，通过调用接口里面的那个函数获得

  call.enqueue(object :Callback<HomeBean>{//看到enqueue就很眼熟了吧，这里就和okhttp3一样了。
     override fun onFailure(call: Call<HomeBean>, t: Throwable) {
    /////////
     }

    override fun onResponse(call: Call<HomeBean>, response: Response<HomeBean>) {
    ///////
    }

})
```

  

---------------------
**我们从上面的应用场景可以看出，Retrofit并不做网络请求，只是生成一个能做网络请求的对象。**
**Retrofit的作用是按照接口去定制Call网络工作对象**

整个流程就是这样，但是如果仅仅像上面这样用的话，就大材小用了，Retrofit2还可以做到很多Okhttp3做不到的东西

### 注解的使用

注解是Retrofit2的特色，也使Retrofit2非常灵活

注解是为url服务的，所以先来看看url

![img](https://upload-images.jianshu.io/upload_images/1724103-95c263da5671d6fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/725/format/webp)

前面两协议和主机我们基本用不到，基本上在用的时候不会变化，一般变化的是后面两个，Path和Query

注解有很多种

![img](https://upload-images.jianshu.io/upload_images/1724103-db95c51539b62c96.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/527/format/webp)

![img](https://upload-images.jianshu.io/upload_images/1724103-4d09b5595bfb3291.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/671/format/webp)



![img](https://upload-images.jianshu.io/upload_images/1724103-073abf80aacf492e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/741/format/webp)

但是我只可以也只能说最常用的几个

就是path,query,和Url

**先来说path**

![1564387754526](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\1564387754526.png)

可以看到api中data包括data之前的都是不变的，只有后面三个参数在变，而且没有“ ？”这个符号，这就是path

(有“ ？”这个符号)代表参数query,后面说

前面的url表示了存放web资源的老窝的地方，那么Path就可以表示这个WEB资源在WEB服务器中的具体位置。

在注解中就是这样写



```kotlin
 interface NewsApi {
@GET("api/data/{category}/{count}/{page}")
Call<NewsEntity> getNews(@Path("category") String category, @Path("count") int count, @Path("page") int page);
 }
```
@GET表示使用GET请求数据，**括号里面的字符串和构建Retrofit对象的baseUrl组合起来形成完整的Url.**

可以看到后面的三个都是用{} 来围起来的，这个{category}就相当于一个占位符，当调用getNews方法传入的参数就会替代它。@Path("category") String category  在参数的注解中@Path("category")就代表用这个参数代替catagory.

后面两个也是一样的。



**然后是query**

先来一个url

```
http://baobab.kaiyanapp.com/api/v4/video/related?id=xxx
```

可以看到最后面有一个？，这就代表是参数，和path不一样，path只能传固定的几个，但是query可以是很多的值

在访问网络资源时，有时候我们可能不太确信访问的资源是否存在，我们可以通过查询WEB资源的方式去访问网络资源。

在注解中就可以这样写

```kotlin
 interface NewsApi {
@GET("v4/video/related")
fun getRelatedData(@Query("id") id: Long): Call<HomeBean>
 }
```

@Query("id")就表示id是一个参数，传入值来拼接成一个完成的url



**然后是@url**

直接传入一个url, 不加任何参数

```
@GET
fun getIssueData(@Url url: String): Call<HomeBean>
```

@url注解会将构建Retrofit的baseurl覆盖





## @Body

**POST请求体的方式向服务器传入json字符串@Body**
大家都清楚，我们app很多时候跟服务器通信，会选择直接使用POST方式将json字符串作为请求体发送到服务器，那么我们看看这个需求使用retrofit该如何实现。

再次添加一个方法：

```java
public interface IUserBiz
{
 @POST("add")
 Call<List<User>> addUser(@Body User user);
}
```

```java
 Call<List<User>> call = userBiz.addUser(new User(1001, "jj", "123,", "jj123", "jj@qq.com"));
```





## **Rxjava2**

依赖：

```
implementation 'io.reactivex.rxjava2:rxjava:2.1.9'
implementation 'io.reactivex.rxjava2:rxandroid:2.0.2'
```

简介：RxJava是一个在Java VM上使用可观测的序列来组成异步的、基于事件的程序的库。

虽然，在Android中，我们可以使用AsyncTask来完成异步任务操作，但是当任务的梳理比较多的时候，我们要为每个任务定义一个AsyncTask就变得非常繁琐。
 RxJava能帮助我们在实现异步执行的前提下保持代码的清晰。
 它的原理就是创建一个`Observable`来完成异步任务，组合使用各种不同的链式操作，来实现各种复杂的操作，最终将任务的执行结果发射给`Observer`进行处理。

**其实就是4个字：异步简洁**

使用：

1.创建observable 被观察者

2.创建observer观察者

 3.订阅   observable.subscribe(observer);  

```java
      //创建一个上游 Observable：
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });
        //创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }
     @Override
        public void onNext(Integer value) {
            Log.d(TAG, "" + value);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "error");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "complete");
        }
    };
    //建立连接
    observable.subscribe(observer);  
```


**只有当上游和下游建立连接之后, 上游才会开始发送事件. 也就是调用了`subscribe()` 方法之后才开始发送事件**.

ObservableEmitter： Emitter是发射器的意思，那就很好猜了，这个就是用来发出事件的，它可以发出三种类型的事件，通过调用emitter的`onNext(T value)`、`onComplete()`和`onError(Throwable error)`就可以分别发出next事件、complete事件和error事件。

但是，请注意，并不意味着你可以随意乱七八糟发射事件，需要满足一定的规则：

- 上游可以发送无限个onNext, 下游也可以接收无限个onNext.
- 当上游发送了一个onComplete后, 上游onComplete之后的事件将会`继续`发送, 而下游收到onComplete事件之后将`不再继续`接收事件.
- 当上游发送了一个onError后,  上游onError之后的事件将`继续`发送, 而下游收到onError事件之后将`不再继续`接收事件.
- 上游可以不发送onComplete或onError.
- 最为关键的是onComplete和onError必须唯一并且互斥, 即不能发多个onComplete, 也不能发多个onError,  也不能先发一个onComplete, 然后再发一个onError, 反之亦然

Disposable, 这个单词的字面意思是一次性用品,用完即可丢弃的,调用它的`dispose()`方法时, 它就把这个订阅事件切断, 从而导致下游收不到事件.

subscribe()有多个重载的方法:

```java
    public final Disposable subscribe() {}
    public final Disposable subscribe(Consumer<? super T> onNext) {}
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {} 
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
    public final void subscribe(Observer<? super T> observer) {}
```

可以看到它们都是返回一个Disposable，这个就一般用来在外部终止订阅事件

- 不带任何参数的`subscribe()` 表示下游不关心任何事件,你上游尽管发你的数据。
- 带有一个`**Consumer`参数的方法表示下游只关心onNext事件, 其他的事件我假装没看见, 因此我们如果只需要onNext事件可以这么写:

其他的方法也是一样的，实现哪些方法就可以处理哪些事件，没有就处理不了。

来看看参数最多的一个重载

```java
public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) 
```

这里的<? super T>表示传入泛型类只能被消费，不能被产出

这个consumer又是什么呢？

![1564555659176](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\1564555659176.png)

可以看到这时一个接口，里面只有一个方法。

就表示下游的一个消耗的事件

还有Action也是一个单方法接口，只不过没有参数而已，因为在onComplete的时候不需要什么参数

![1564556518863](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\1564556518863.png)

从参数名称就可以看出onNext，onError，onComplete，onSubscribe分别对应上面的四个方法。



## 线程控制

当我们在主线程中去创建一个上游Observable来发送事件, 则这个上游默认就在主线程发送事件.

当我们在主线程去创建一个下游Observer来接收事件, 则这个下游默认就在主线程中接收事件,

这样肯定是满足不了我们的需求的。


```java
Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {   
    @Override                                                                               
    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {            
                
        Log.d(TAG, "emit 1");                                                               
        emitter.onNext(1);                                                                  
    }                                                                                       
});                                                                                     
Consumer<Integer> consumer = new Consumer<Integer>() {                                      
    @Override                                                                               
    public void accept(Integer integer) throws Exception {                                               
        Log.d(TAG, "onNext: " + integer);                                                   
    }                                                                                       
};                                                                                                                                                                      
observable.subscribeOn(Schedulers.newThread())//线程切换                                              
        .observeOn(AndroidSchedulers.mainThread())//线程切换                                           
        .subscribe(consumer);                                                               }
```


记住，subscribeOn是发送的线程，oberveOn是接受的线程

多次指定上游的线程只有第一次指定的有效, 也就是说多次调用`subscribeOn()` 只有第一次的有效, 其余的会被忽略.

多次指定下游的线程是可以的, 也就是说每调用一次`observeOn()` , 下游的线程就会切换一次.

在RxJava中, 已经内置了很多线程选项供我们选择, 例如有

- Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作

- Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作

- Schedulers.newThread() 代表一个常规的新线程

- AndroidSchedulers.mainThread()  代表Android的主线程


## 操作符

操作符可以把Observable发送的事件进行转换

先来看看最简单的变换操作符**map**

```java
 Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
               emitter.onNext(1);//发送的是Integer类型的
               emitter.onNext(2);
                emitter.onNext(3);
           }
       }).map(new Function<Integer, String>() {//这个函数就是把Integer类型转换为String类型 
           @Override
            public String apply(Integer integer) throws Exception {
               return "This is result " + integer;
           }
        }).subscribe(new Consumer<String>() {//接受的却是String类型
           @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
           }
        });


 D/TAG: This is result 1 
 D/TAG: This is result 2 
 D/TAG: This is result 3 

```

**通过Map, 可以将上游发来的事件转换为任意的类型, 可以是一个Object, 也可以是一个集合**



### FlatMap

`FlatMap`将一个发送事件的上游Observable变换为多个发送事件的Observables，然后将它们发射的事件合并后放进一个单独的Observable里.

看一个场景，如果map将待处理数据类型T转换为一个集合类型List<R> ，那么会出现什么情况？ 

```java
一个学生有多节课
Observable.from( new  List(student1,student2,student3))
           .map(new Func1<Student, ArrayList<Course>>() {
               @Override
                public ArrayList<Course> call(Student student) {
                    return student.courses;
               }
           }).subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<ArrayList<Course>>() {
               @Override
                public void call(ArrayList<Course> courses) {
                    for (int i = 0; i < courses.size(); i++) {
                        Logger.d("观察者:" + courses.get(i).name);
                   }
                }
            });


```

用map，把一个对象转化成发送一个集合，注意下游接受也是把一个集合作为一个整体来接处理的，所以的话还要用for循环来取出来。因为map只能将发送的数据类型转换。但是flatmap就可以很简单

```java
 Observable.from(student1,student2,student3)
                .flatMap(new Func1<Student, Observable<Course>>() {
                   @Override
                    public Observable<Course> call(Student student) {
                      return Observable.from(student.courses);//上游Observable每发送一个事件经由flatMap后有了一个新的Observable来发送另外一系列的事件。
                    }
                }).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Course>() {
                   @Override
                    public void call(Course course) {
                       Logger.d("观察者:" + course.name);
                    }
 });
 最开始的Observable发送了三个onNext事件，经过flatmap,每一个事件又形成了一个新的事件流。
 

```

##   zip

`Zip`通过一个函数将多个Observable发送的事件结合到一起，然后发送这些组合到一起的事件. 它按照严格的顺序应用这个函数。它只发射与发射数据项最少的那个Observable一样多的数据



**还有很多的操作符**



### **创建操作符**

| 名称                                            | 解析                                                         |
| ----------------------------------------------- | ------------------------------------------------------------ |
| **just()**                                      | **将一个或多个对象转换成发射这个或这些对象的一个Observable** |
| **flatMap() , concatMap() , flatMapIterable()** | **将Observable发射的数据集合变换为Observables集合，然后将这些Observable发射的数据平坦化的放进一个单独的Observable** |
| **map()**                                       | **对序列的每一项都应用一个函数来变换Observable发射的数据序列** |
| **range()**                                     | **创建一个发射指定范围的整数序列的Observable**               |
| **filter()**                                    | **过滤数据**                                                 |
| **zip()**                                       | **打包，使用一个指定的函数将多个Observable发射的数据组合在一起，然后将这个函数的结果作为单项数据发射** |
| **observeOn()**                                 | **指定观察者观察Observable的调度器**                         |
| **subscribeOn()**                               | **指定Observable执行任务的调度器**                           |
|                                                 |                                                              |
|                                                 |                                                              |
|                                                 |                                                              |
|                                                 |                                                              |

#### 



#### **条件操作符**

根据条件发射或变换Observables



#### 背压（backpressure）

当上下游在不同的线程中，通过Observable发射，处理，响应数据流时，如果上游发射数据的速度快于下游接收处理数据的速度，这样对于那些没来得及处理的数据就会造成积压，这些数据既不会丢失，也不会被垃圾回收机制回收，而是存放在一个异步缓存池中，如果缓存池中的数据一直得不到处理，越积越多，最后就会造成内存溢出，这便是响应式编程中的背压（backpressure）问题。

```java
   public void demo1() {
Observable.create(new ObservableOnSubscribe<Integer>() {
		@Override
	public void subscribe(ObservableEmitter<Integer> e) throws Exception {
		int i = 0;
			while (true) {
			i++;
		e.onNext(i);//不停的发送事件
	}
	}
	})
	.subscribeOn(Schedulers.newThread())
	.observeOn(Schedulers.newThread())
	.subscribe(new Consumer<Integer>() {
	@Override
	public void accept(Integer integer) throws Exception {
	Thread.sleep(5000);
	System.out.println(integer);
	}
	});	
	}
```

由于上游通过Observable发射数据的速度大于下游通过Consumer接收处理数据的速度，而且上下游分别运行在不同的线程中，下游对数据的接收处理不会堵塞上游对数据的发射，造成上游数据积压，内存不断增加，最后便会导致内存溢出。

Rxjava2相对于Rxjava1最大的更新就是把对背压问题的处理逻辑从Observable中抽取出来产生了新的可观察对象Flowable。

在Rxjava2中，Flowable可以看做是为了解决背压问题，在Observable的基础上优化后的产物，与Observable不处在同一组观察者模式下，Observable是ObservableSource/Observer这一组观察者模式中ObservableSource的典型实现，而Flowable是Publisher与Subscriber这一组观察者模式中Publisher的典型实现。

**所以在使用Flowable的时候，可观察对象不再是Observable,而是Flowable;观察者不再是Observer，而是Subscriber。Flowable与Subscriber之间依然通过subscribe()进行关联。**

由于基于Flowable发射的数据流，以及对数据加工处理的各操作符都添加了背压支持，附加了额外的逻辑，其运行效率要比Observable慢得多。

只有在需要处理背压问题时，才需要使用Flowable。

1、上下游运行在同一个线程中，
2、上下游工作在不同的线程中，但是下游处理数据的速度不慢于上游发射数据的速度，
3、上下游工作在不同的线程中，但是数据流中只有一条数据
则不会产生背压问题，就没有必要使用Flowable，以免影响性能。



```java
Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR); //背压模式  
Subscriber<Integer> downstream = new Subscriber<Integer>() {
        @Override
        public void onSubscribe(Subscription s) {
            Log.d(TAG, "onSubscribe");
            s.request(Long.MAX_VALUE);  
        }
        @Override
        public void onNext(Integer integer) {
            Log.d(TAG, "onNext: " + integer);

        }
        @Override
        public void onError(Throwable t) {
             Log.w(TAG, "onError: ", t);
        }
        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete");
        }
    };
    upstream.subscribe(downstream);
```
**`BackpressureStrategy.ERROR`这种方式, 这种方式会在出现上下游流速不均衡的时候直接抛出一个异常,这个异常就是`MissingBackpressureException`.** 

在下游的`onSubscribe`方法中传给我们的**不再是`Disposable`了**, 而是`Subscription`, 它俩有什么区别呢,  首先它们都是上下游中间的一个开关, 之前我们说调用`Disposable.dispose()`方法可以切断水管, **同样的调用`Subscription.cancel()`**也可以切断水管, 不同的地方在于`Subscription`增加了一个`void request(long n)`方法, 这个方法有什么用呢, 在上面的代码中也有这么一句代码:

 s.request(Long.MAX_VALUE); 

request当做是一种能力, 当成`下游处理事件`的能力, 下游能处理几个就告诉上游我要几个, 这样只要上游根据下游的处理能力来决定发送多少事件, 就不会造成一窝蜂的发出一堆事件来, 从而导致OOM。

`Flowable`里默认有一个**`大小为128`的事件存储空间**, 当上下游工作在不同的线程中时, 上游就会先把事件发送到这个空间中,  因此, 下游虽然没有调用request，但是上游在空间中保存着这些事件, 只有当下游调用request时, 才从空间里取出事件发给下游.

### 使用 ERROR 的策略

- 当上游和下游位于同一个线程时，如果上游发送的事件超过了下游声明的`request(n)`的值，那么会抛出`MissingBackpressureException`异常。
- 当上游和下游位于不同线程时，如果上游发送的事件超过了下游的声明，事件会被放在水缸当中，这个水缸默认的大小是`128`，只有当下游调用`request`时，才从水缸中取出事件发送给下游，如果水缸中事件的个数超过了`128`，那么也会抛出`MissingBackpressureException`异常。



其它的背压策略：

BackpressureStrategy.BUFFER   `Flowable`存储空间无限大

### BUFFER 策略

- 使用`BUFFER`策略时，相当于在上游放置了一个容量无限大的水缸，所有下游暂时无法处理的消息都放在水缸当中，这里不再像`ERROR`策略一样，区分上游和下游是否位于同一线程。
- 因此，如果下游一直没有处理消息，那么将会导致内存一直增长，从而引起`OOM`。

##     DROP 策略

BackpressureStrategy.DROP     

  Drop就是直接把存不下的事件丢弃

- 使用`DROP`策略时，会把水缸无法存放的事件丢弃掉，这里同样不会受到下游和下游是否处于同一个线程的限制。

### LATEST 策略

- 和`DROP`类似，当水缸无法容纳下消息时，会将它丢弃，但是除此之外，上游还会缓存最新的一条消息

BackpressureStrategy.LATEST    Latest就是只保留最新的事件



   回顾：

- `Observable`：上游。
-  `ObservableOnSubscribe`：上游的`create`方法所接收的参数。
-  `ObservableEmitter`：上游事件的发送者。
-  `Observer`：下游的接收者。
-  `Disposable`：用于维系上游、下游之间的联系。

对于整个模型，可以总结为以下几点：

-  `RxJava2`简单的来说，就是一个发送事件、接收事件的过程，我们可以将发送事件方类比作上游，而接收事件方类比作下游。
- 上游每产生一个事件，下游就能收到事件，上游对应`Observable`，而下游对应`Observer`。
- 只有当上游和下游建立连接之后，上游才会开始发送事件，这一关系的建立是通过`subscribe`方法。



## ObservableEmitter

用于 **发出事件**，它可以分别发出`onNext/onComplete/onError`事件：

- 上游可以发送无限个`onNext`，下游也可以接收无限个`onNext`。
- 当上游发送了一个`onComplete/onError`后，上游`onComplete/onError`后的事件将会继续发送，但是下游在收到`onComplete/onError`事件后不再继续接收事件。
- 上游可以不发送`onComplete`或者`onError`事件。
- 调用`onError`或者`onComplete`切断了上游和下游的联系，在联系切断后上游再发送`onError`事件就会报错，`onComplete`和`onError`的调用情况有以下几种：
   **(1)** `onComplete`可以发送多次，但是只会收到一次回调。
   **(2)** `onError`只可以发送一次，发送多次会报错。
   **(3)** `onComplete`之后不可以发送`onError`，否则会报错。
   **(4)** `onError`之后可以发送`onComplete`，但是只会收到`onError`事件。
-  `onError`的参数不允许为空。



## Disposable

理解成为 **水管的机关**，当调用它的`dispose`方法时，将会将上游和下游之间的管道切断，从而导致 **下游接收不到事件**。

- 在`Observer`的`onSubscribe`回调中，会传入一个`Disposable`对象，下游可以通过该对象的`dispose()`方法主动切断和上游的联系，在这之后上游的`observableEmitter.isDisposed()`方法将返回`true`。
- 当上游和下游的联系切断之后，下游收不到包括`onComplete/onError`在内的任何事件，若此时上游再调用`onError`方法发送事件，那么将会报错。



##    Retrofit2 + **Rxjava2**   一起用来网络请求

```kotlin
@GET("v2/feed?")
fun getFirstHomeData(@Query("num") num:Int): Observable<HomeBean>//从Call变为了Observable
```

```kotlin
 Retrofit.Builder()
        .baseUrl(BASE_URL)  //自己配置
      //  .client()    可以自己定义一个Okhttp的client来替换默认的，里面可以加入自己需要的一些东西
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        // 就是Observable<HomeBean>中的HomeBean
        .build()
```



`Converter`是对于`Call<T>`中`T`的转换，而**`CallAdapter`则可以对`Call`转换**，这样的话`Call<T>`中的`Call`也是可以被替换的，而返回值的类型就决定你后续的处理程序逻辑，同样Retrofit提供了多个`CallAdapter`，这里以`RxJava`的为例，用`Observable`代替`Call`：

```kotlin
service.getFirstHomeData（1）
  .subscribeOn(Schedulers.io())
  .subscribe(object :Observer<HomeBean>{
      override fun onComplete() {
      
   }
     override fun onSubscribe(d: Disposable) {
              
   }

        override fun onNext(t: HomeBean) {
                    //这里来对HomeBean操作
   }

   override fun onError(e: Throwable) {
                      
   }

     })
```



**这只是最简单的用法，由于rxjava的操作符和线程切换的能力，中间还可以做很多的事情和数据变换。**

应用很多，我也没弄过几个，现在只能说这么多了。



**此外一个比较重要的东西**：如何在UI被销毁的时候把订阅事件断开，如果不断开的话就会内存泄漏

上面说了,subscribe返回了disposable对象，通过这个对象可以取消订阅事件

CompositeDisposable() 是专门用来管理disposable对象的

所以每一个订阅事件产生的时候，就把它加入CompositeDisposable中，在ui被销毁的时候就是onDestory的方法里面用

```
compositeDisposable.clear()
```

来取消里面的所以disposable的订阅。

这样就可以防止内存泄漏







很实用的东西例子

我们的界面上有一个按钮`mTvDownload`，点击之后会发起一个耗时的任务，这里我们用`Thread.sleep`来模拟耗时的操作，每隔`500ms`我们会将当前的进度通知主线程，在`mTvDownloadResult`中显示当前处理的进度。

```java

    private void startDownload() {
        final Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 100; i++) {
                    if (i % 20 == 0) {
                        try {
                            Thread.sleep(500); //模拟下载的操作。
                        } catch (InterruptedException exception) {
                            if (!e.isDisposed()) {
                                e.onError(exception);
                            }
                        }
                        e.onNext(i);
                    }
                }
                e.onComplete();
            }

        });
        DisposableObserver<Integer> disposableObserver = new DisposableObserver<Integer>() {

            @Override
            public void onNext(Integer value) {
                Log.d("BackgroundActivity", "onNext=" + value);
                mTvDownloadResult.setText("Current Progress=" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("BackgroundActivity", "onError=" + e);
                mTvDownloadResult.setText("Download Error");
            }

            @Override
            public void onComplete() {
                Log.d("BackgroundActivity", "onComplete");
                mTvDownloadResult.setText("Download onComplete");
            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
        mCompositeDisposable.add(disposableObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
```















