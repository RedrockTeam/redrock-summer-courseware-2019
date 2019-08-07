## EventBus

#### 1 简介

EventBus是一种用于Android的事件发布-订阅总线，由GreenRobot开发，Gihub地址是：[EventBus](https://github.com/greenrobot/EventBus)。它可以用来简化活动，碎片，线程，服务等之间的通信。

#### 2 简单使用

- 定义消息事件

其实也就是创建事件类型，事件是就是一个简单的pojo类

```java
public class MessageEvent {
    
    public final String message;
    
    public MessageEvent(String message) {
        this.message = message;
    }
}
```

- 注册观察者并订阅事件

选择要订阅该事件的订阅者（subscriber），Activity即在onCreate()加入，调用EventBus的register方法，注册。

```java
EventBus.getDefault().register(this);
```

在不需要接收事件发生时可以取消注册

```java
EventBus.getDefault().unregister(this);
```

在订阅者里需要用注解关键字 `@Subscribe`来告诉EventBus使用什么方法处理event

```java
//根据不同的事件进行不同的处理
@Subscribe(threadMode = ThreadMode.POSTING)
public void onMessageEvent(MessageEvent event) {
    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
}

@Subscribe
public void handleSomethingElse(SomeOtherEvent event) {
    doSomethingWith(event);
}
```

注意方法只能被public修饰，在EventBus3.0之后该方法名字就可以自由的取了，之前要求只能是onEvent()

- 发送事件

通过EventBus的post方法，发出我们要传递的事件。

```java
EventBus.getDefault().post(new MessageEvent("HelloEveryone"));
```

这样选择的Activity就会接收到该事件，并且触发onMessageEvent方法

#### 3 三个角色

![](https://raw.githubusercontent.com/iRGboy/photoWarehouse/master/三个角色.png)

**Publisher**：事件的发布者，可以在任意线程里发布事件。一般情况下，使用`EventBus.getDefault()`就可以得到一个EventBus对象，然后再调用`post(Object)`方法即可。

**Event**：事件，它可以是任意类型，EventBus会根据事件类型进行全局的通知。

**Subscriber**：事件订阅者，在EventBus 3.0之前我们必须定义以onEvent开头的那几个方法，分别是onEvent、onEventMainThread、onEventBackgroundThread和onEventAsync，而在3.0之后事件处理的方法名可以随意取，不过需要加上注解@subscribe，并且指定线程模型，不指定的话默认是POSTING。

#### 4 四种线程模型

**POSTING**：默认的线程模型，表示事件处理函数的线程跟发布事件的线程在同一个线程(其实就是发布者和订阅者在同一个线程)，这个模式的开销最小，因为它完全避免了线程切换。

**MAIN**：表示事件处理函数的线程在主线程(UI)线程，因此在这里不能进行耗时操作，避免阻塞主线程。

**MAIN_ORDERED**:这个其实和MAIN这个线程模型没有什么区别 。使用MAIN_ORDERED，第一个事件处理程序将完成，然后第二个事件处理程序将在稍后的时间点调用（一旦主线程具有容量）。

**BACKGROUND**：表示事件处理函数的线程在后台线程，因此不能进行UI操作。如果发布事件的线程是主线程(UI线程)，那么事件处理函数将会开启一个后台线程，如果果发布事件的线程是在后台线程，那么事件处理函数就使用该线程。

**ASYNC**：表示无论事件发布的线程是哪一个，事件处理函数始终会新建一个子线程运行，同样不能进行UI操作。

- [x] 给订阅者设置线程模型的方法如下：

在Subscribe注解后面添加参数`threadMode=xxx`即可,这里的xxx是ThreadMode的枚举类型，默认为ThreadMode.POSTING

```java
@Subscribe(threadMode = ThreadMode.POSTING)
public void onMessageEvent(MessageEvent event) {
    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
}
```

#### 5 黏性事件

黏性事件就是在事件的发出早于订阅者的注册，但是在订阅者注册之后仍然能够接收到的事件

- [x] 如何使用黏性事件：

在Subscribe注解后面添加参数`sticky=xxx`即可，这里的xxx是布尔类型，默认为false

```java
@Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
public void onMessageEvent(MessageEvent event) {
    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
}
```

#### 6 优先级

顾名思义，就是设置订阅方法的优先级

- [x] 如何设置订阅方法的优先级：

在Subscribe注解后面添加参数`priority=xxx`即可，这里的xxx是整数类型，默认是0，值越大表示优先级越大。在某个事件被发布出来的时候，优先级较高的订阅方法会首先接受到事件。

```java
@Subscribe(threadMode = ThreadMode.POSTING, sticky = true, priority = 1)
public void onMessageEvent(MessageEvent event) {
    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
}
```

这里要注意的是只有当两个订阅方法使用相同的`ThreadMode`参数的时候，它们的优先级才会与`priority`指定的值一致

#### 7 2.x和3.0的区别

EventBus 2.x 是采用反射的方式对整个注册的类的所有方法进行扫描来完成注册，当然会有性能上的影响。EventBus 3.0中EventBus提供了EventBusAnnotationProcessor注解处理器来在编译期通过读取@Subscribe()注解并解析、处理其中所包含的信息，然后生成java类来保存所有订阅者关于订阅的信息，这样就比在运行时使用反射来获得这些订阅者的信息速度要快

#### 8 源码解析

了解了对于EventBus的基础使用，我们针对其基础使用的调用流程，来了解EventBus的实现流程和源码细节。

###### 注册观察者

![](https://raw.githubusercontent.com/iRGboy/photoWarehouse/master/注册观察者.png)

```java
EventBus.getDefault().register(this);
```

- getDefault()

EventBus.getDefault()得到的是一个单例，实现如下：

```java
public static EventBus getDefault() {  
   if (defaultInstance == null) {  
       synchronized (EventBus.class) {  
           if (defaultInstance == null) {  
               defaultInstance = new EventBus();  
           }  
       }  
   }  
   return defaultInstance;  
} 
```

这样保证了App单个进程中只会有一个EventBus实例。

- register(Object subscriber)

```java
public void register(Object subscriber) {
    //1 这里先获得订阅实例的类
    Class<?> subscriberClass = subscriber.getClass();
    //2 调用SubscriberMethodFinder实例的findSubscriberMethods方法来找到该类中订阅的相关方法
    List<SubscriberMethod> subscriberMethods = subscriberMethodFinder.findSubscriberMethods(subscriberClass);
    synchronized (this) {
        for (SubscriberMethod subscriberMethod : subscriberMethods) {
            //3 对这些方法调用订阅方法
            subscribe(subscriber, subscriberMethod);
        }
    }
}
```

注册的过程涉及到两个问题，一个是如何查找注册方法？另一个是如何将这些方法进行存储，方便后面的调用？

- SubscriberMethodFinder是如何从实例中查找到相关的注册方法的呢？

首先从缓存的方法中，通过Class作为Key进行查找，如何查找内容为空，则会调用findUsingReflection或者findUsingInfo来从相关类中查找，得到注册的方法列表之后，将其添加到缓存之中。

```java
List<SubscriberMethod> findSubscriberMethods(Class<?> subscriberClass) {
    
	//1 查找注册方法	
    if (ignoreGeneratedIndex) {
        subscriberMethods = findUsingReflection(subscriberClass);
    } else {
        subscriberMethods = findUsingInfo(subscriberClass);//查找注册内容为空时
    }
    
    //2 将得到的订阅方法加入到缓存中
    if (subscriberMethods.isEmpty()) {
        throw new EventBusException("Subscriber " + subscriberClass
                + " and its super classes have no public methods with the @Subscribe annotation");
    } else {
        METHOD_CACHE.put(subscriberClass, subscriberMethods);
        return subscriberMethods;
    }
}
```

- subscribe

subscribe方法的执行流程是先根据事件类型，判断该注册者是否已经进行过注册，如果未注册将其中的方法进行保存，以事件类型为键保存一份，然后以注册者实例为键保存一份。

```java
private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
	 //1 获取订阅方法要监听的事件类型
    Class<?> eventType = subscriberMethod.eventType;
    
    Subscription newSubscription = new Subscription(subscriber, subscriberMethod);
    
    //2 根据事件类型查找相应的订阅者
    CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
    
    //3 如果不存在该事件类型，则创建，如果已经包含该订阅者，抛出异常
    if (subscriptions == null) {
        subscriptions = new CopyOnWriteArrayList<>();
        subscriptionsByEventType.put(eventType, subscriptions);
    } else {
        if (subscriptions.contains(newSubscription)) {
            throw new EventBusException("Subscriber " + subscriber.getClass() + " already registered to event "
                    + eventType);
        }
    }

	//获得该事件类型的订阅者列表，根据其优先级确定当前插入者的位置...
    int size = subscriptions.size();
    for (int i = 0; i <= size; i++) {
        if (i == size || subscriberMethod.priority > subscriptions.get(i).subscriberMethod.priority) {
            subscriptions.add(i, newSubscription);
            break;
        }
    }

	//在该注册者中加入对应的监听事件类型...
    List<Class<?>> subscribedEvents = typesBySubscriber.get(subscriber);
    if (subscribedEvents == null) {
        subscribedEvents = new ArrayList<>();
        typesBySubscriber.put(subscriber, subscribedEvents);
    }
    subscribedEvents.add(eventType);

	//黏性事件处理...
    if (subscriberMethod.sticky) {
        if (eventInheritance) {
            Set<Map.Entry<Class<?>, Object>> entries = stickyEvents.entrySet();
            for (Map.Entry<Class<?>, Object> entry : entries) {
                Class<?> candidateEventType = entry.getKey();
                if (eventType.isAssignableFrom(candidateEventType)) {
                    Object stickyEvent = entry.getValue();
                    checkPostStickyEventToSubscription(newSubscription, stickyEvent);
                }
            }
        } else {
            Object stickyEvent = stickyEvents.get(eventType);
            checkPostStickyEventToSubscription(newSubscription, stickyEvent);
        }
    }
}
```

###### 发送事件

对于事件的发送，调用的是post函数

- post(Object event)

![](https://raw.githubusercontent.com/iRGboy/photoWarehouse/master/发送事件.png)

```java
public void post(Object event) {
	//获取当前线程的Event队列，并将其添加到队列中
    PostingThreadState postingState = currentPostingThreadState.get();//currentPostingThreadState是一个ThreadLocal类型的，里面存储了PostingThreadState。
    List<Object> eventQueue = postingState.eventQueue;
    eventQueue.add(event);
    //如果当前PostingThreadState不是在post中
    if (!postingState.isPosting) {
        postingState.isMainThread = isMainThread();
	    postingState.isPosting = true;
	    if (postingState.canceled) {
	        throw new EventBusException("Internal error. Abort state was not reset");
	    }
	    try {
	    	//遍历事件队列，调用postSingleEvent方法 抛出其中的事件
	        while (!eventQueue.isEmpty()) {
	            postSingleEvent(eventQueue.remove(0), postingState);
	        }
	    } finally {
	        postingState.isPosting = false;
	        postingState.isMainThread = false;
	    }  
     }
}
```

post方法中，首先从当前的PostingThreadState中获取当前的事件队列，然后将要post的事件添加到其中，之后判断当前的线程是否处在post中，如果不在，那么则会遍历事件队列，调用`postSingleEvent`将其中的事件抛出。

- postSingleEvent

postSingleEvent的具体实现如下。

```java
//...
private void postSingleEvent(Object event, PostingThreadState postingState) throws Error {
    Class<?> eventClass = event.getClass();
    boolean subscriptionFound = false;
    if (eventInheritance) {
        List<Class<?>> eventTypes = lookupAllEventTypes(eventClass);//通过该方法得到当前eventClass的Class，以及父类和接口的Class类型，而后逐个调用postSingleEventForEventType方法。
        int countTypes = eventTypes.size();
        for (int h = 0; h < countTypes; h++) {
            Class<?> clazz = eventTypes.get(h);
            subscriptionFound |= postSingleEventForEventType(event, postingState, clazz);
        }
    } else {
        subscriptionFound = postSingleEventForEventType(event, postingState, eventClass);//事件派发的核心方法在postSingleEventForEventType方法中。
    }
    if (!subscriptionFound) {
        if (logNoSubscriberMessages) {
            logger.log(Level.FINE, "No subscribers registered for event " + eventClass);
        }
        if (sendNoSubscriberEvent && eventClass != NoSubscriberEvent.class &&
                eventClass != SubscriberExceptionEvent.class) {
            post(new NoSubscriberEvent(this, event));
        }
    }
}
```

- postSingleEventForEventType

```java
private boolean postSingleEventForEventType(Object event, PostingThreadState postingState, Class<?> eventClass) {
    CopyOnWriteArrayList<Subscription> subscriptions;
    synchronized (this) {
        subscriptions = subscriptionsByEventType.get(eventClass);
    }
    if (subscriptions != null && !subscriptions.isEmpty()) {
        for (Subscription subscription : subscriptions) {//从subscriptionsByEventType中拿到订阅了eventClass的订阅者列表 ，遍历，调用postToSubscription方法，逐个将事件抛出。
            postingState.event = event;
            postingState.subscription = subscription;
            boolean aborted = false;
            try {
                postToSubscription(subscription, event, postingState.isMainThread);//
                aborted = postingState.canceled;
            } finally {
                postingState.event = null;
                postingState.subscription = null;
                postingState.canceled = false;
            }
            if (aborted) {
                break;
            }
        }
        return true;
    }
    return false;
}
```

- postToSubscription

```java
private void postToSubscription(Subscription subscription, Object event, boolean isMainThread) {
	//根据订阅者方法的线程模型进行不同的处理
    switch (subscription.subscriberMethod.threadMode) {
        case POSTING:
            invokeSubscriber(subscription, event);
            break;
        case MAIN:
            if (isMainThread) {
                invokeSubscriber(subscription, event);//这个方法通过反射的方式，直接调用订阅该事件方法
            } else {
                mainThreadPoster.enqueue(subscription, event);
            }
            break;
        case MAIN_ORDERED:
            if (mainThreadPoster != null) {
                mainThreadPoster.enqueue(subscription, event);
            } else {
                invokeSubscriber(subscription, event);
            }
            break;
        case BACKGROUND:
            if (isMainThread) {
                backgroundPoster.enqueue(subscription, event);//相比于asyncPoster，backgroundPoster可以保证添加进来的数据是顺序执行的，通过同步锁和信号量的方式来保证，只有一个线程是在活跃从事件队列中取事件，然后执行。
            } else {
                invokeSubscriber(subscription, event);
            }
            break;
        case ASYNC:
            asyncPoster.enqueue(subscription, event);
            break;
        default:
            throw new IllegalStateException("Unknown thread mode: " + subscription.subscriberMethod.threadMode);
    }
}
```

###### 函数扫描

在register方法中对于订阅方法的查找，调用的方法是SubscriberMethodFinder的findSubscriberMethods方法，对于其中方法的查找有两种方式，一个是`findUsingInfo`，一个是`findUsingReflection`。

```java
private List<SubscriberMethod> findUsingReflection(Class<?> subscriberClass) {
	//获取FindState实例
    FindState findState = prepareFindState();
    findState.initForSubscriber(subscriberClass);
    //从当前类中查找，然后跳到其父类，继续查找相应方法
    while (findState.clazz != null) {
        findUsingReflectionInSingleClass(findState);
        findState.moveToSuperclass();
    }
    return getMethodsAndRelease(findState);
}
```

首先，会获得一个FindState实例，其用来保存查找过程中的一些中间变量和最后结果，首先找当前类中的注册方法，然后跳到其父类之中，其父类会自动过滤掉Java，Android中的相应类，然后继续查找。

查找的核心实现在方法findUsingReflectionInSingleClass中。

```java
private void findUsingReflectionInSingleClass(FindState findState) {
    Method[] methods;
    try {
        // 获取该类中的所有方法，不包括继承的方法
        methods = findState.clazz.getDeclaredMethods();
    } catch (Throwable th) {
        methods = findState.clazz.getMethods();
        findState.skipSuperClasses = true;
    }
    //遍历获取的方法，判断添加规则为是否为public函数，其参数是否只有一个，获取其注解，然后调用checkAdd，
    //在加入到订阅方法之前
    for (Method method : methods) {
        int modifiers = method.getModifiers();
        if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1) {
                Subscribe subscribeAnnotation = method.getAnnotation(Subscribe.class);
                if (subscribeAnnotation != null) {
                    Class<?> eventType = parameterTypes[0];
                    if (findState.checkAdd(method, eventType)) {
                        ThreadMode threadMode = subscribeAnnotation.threadMode();
                        findState.subscriberMethods.add(new SubscriberMethod(method, eventType, threadMode,
                                subscribeAnnotation.priority(), subscribeAnnotation.sticky()));
                    }
                }
            } else if (strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
					//多于一个参数
            }
        } else if (strictMethodVerification && method.isAnnotationPresent(Subscribe.class)) {
            //非public，abstract，非静态的
        }
    }
}
```

按照如下扫描规则，对类中的函数进行扫描
扫描规则：1.函数非静态，抽象函数 2.函数为public；3.函数仅单个参数；4.函数拥有`@Subscribe`的注解;

在符合了以上规则之后，再对方法进行校验之后，然后将其加入到函数的队列之中。

###### 粘性事件处理

粘性事件的设计初衷是，在事件的发出早于观察者的注册，EventBus将粘性事件存储起来，在观察者注册后，将其发出。通过一个Map保存每个Event类型的最近一次post出的event

```java
public void postSticky(Object event) {
    synchronized (stickyEvents) {
        stickyEvents.put(event.getClass(), event);
    }
    // Should be posted after it is putted, in case the subscriber wants to remove immediately
    post(event);
}
```

将粘性事件保存在stickyEvents，而后post出，此时如果存在已经注册的观察者，则情况同普通事件情况相同；如尚无注册的观察者，在postSingleEvent函数中将时间转化为一个NoSubscriberEvent事件发出，可由EventBus消耗并处理。待观察者注册时，从stickyEvents中将事件取出，重新分发给注册的观察者。

```java
if (subscriberMethod.sticky) {
    if (eventInheritance) {
        Set<Map.Entry<Class<?>, Object>> entries = stickyEvents.entrySet();
        for (Map.Entry<Class<?>, Object> entry : entries) {
            Class<?> candidateEventType = entry.getKey();
            if (eventType.isAssignableFrom(candidateEventType)) {
                Object stickyEvent = entry.getValue();
                checkPostStickyEventToSubscription(newSubscription, stickyEvent);
            }
        }
    } else {
        Object stickyEvent = stickyEvents.get(eventType);
        checkPostStickyEventToSubscription(newSubscription, stickyEvent);
    }
}
```

## Gson

#### 1 简介

![](https://raw.githubusercontent.com/iRGboy/photoWarehouse/master/GSON基本作用.png)

Gson，就是帮助我们完成序列化和反序列化的工作的一个库,它的Github地址是[gson](https://github.com/google/gson)

#### 2 基础使用

```java
        UserInfo userInfo = getUserInfo();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(userInfo); // 序列化
        UserInfo user = gson.fromJson(jsonStr,UserInfo.class);  // 反序列化
```

#### 3 GsonFormat和JoinToKotlinClass

#### 4 TypeAdapter 

###### 主要功能

要实现反序列化流程，Gson大体会做以下这三件事：

- 反射创建该类型的对象
- 把json中对应的值赋给对象对应的属性
- 返回该对象。

TypeAdapter是Gson的核心，因为Json数据接口和Type的接口两者是无法兼容，因此TypeAdapter就是来实现兼容，把json数据读到Type中，把Type中的数据写入到json里

###### 和type的对应关系

Gson会为每一种类型创建一个TypeAdapter，同样的，每一个Type都对应唯一一个TypeAdapter

每一种基本类型都会创建一个TypeAdapter来适配它们，而所有的复合类型（即我们自己定义的各种JavaBean）都会由ReflectiveTypeAdapterFactory.Adapter来完成适配

###### 和Gson运行机制的关系

![](https://raw.githubusercontent.com/iRGboy/photoWarehouse/master/TypeAdapter和gson之间的关系.png)

上图，如果是基本类型，那么对应的TypeAdapter就可以直接读写Json串，如果是复合类型，ReflectiveTypeAdapterFactory.Adapter会反射创建该类型的对象，并逐个分析其内部的属性的类型，然后重复上述工作。直至所有的属性都是Gson认定的基本类型并完成读写工作。

```java
// 创建ReflectiveTypeAdapter
new Adapter<T>(constructor, getBoundFields(gson, type, raw));

...
...

public static final class Adapter<T> extends TypeAdapter<T> {
    // 该复合类型的构造器，用于反射创建对象
    private final ObjectConstructor<T> constructor;
    // 该类型内部的所有的Filed属性，都通过map存储起来
    private final Map<String, BoundField> boundFields;

    Adapter(ObjectConstructor<T> constructor, Map<String, BoundField> boundFields) {
      this.constructor = constructor;
      this.boundFields = boundFields;
    }

 //JsonReader是Gson封装的对Json相关的操作类，可以依次读取json数据
 // 类似的可以参考Android封装的对XML数据解析的操作类XmlPullParser
    @Override public T read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      }

      T instance = constructor.construct();

      try {
        in.beginObject();  // 从“{”开始读取
        while (in.hasNext()) {
          String name = in.nextName(); //开始逐个读取json串中的key
          BoundField field = boundFields.get(name); // 通过key寻找对应的属性
          if (field == null || !field.deserialized) {
            in.skipValue();
          } else {
            field.read(in, instance); // 将json串的读取委托给了各个属性
          }
        }
      } catch (IllegalStateException e) {
        throw new JsonSyntaxException(e);
      } catch (IllegalAccessException e) {
        throw new AssertionError(e);
      }
      in.endObject(); // 到对应的“}”结束
      return instance;
    }
    ...
    ...
  }
```

我们可以看到，ReflectiveTypeAdapterFactory.Adapter内部会首先创建该类型的对象，然后遍历该对象内部的所有属性,接着把json传的读去委托给了各个属性。

被委托的BoundField内部又是如何做的呢？BoundField这个类，是对Filed相关操作的封装，我们来看看BoundField是如何创建的，以及内部的工作原理。

```java
// 创建ReflectiveTypeAdapter getBoundFields获取该类型所有的属性
new Adapter<T>(constructor, getBoundFields(gson, type, raw));

...
...


private Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw) {
    // 创建一个Map结构，存放所有的BoundField
    Map<String, BoundField> result = new LinkedHashMap<String, BoundField>();
    if (raw.isInterface()) {
      return result;
    }

    Type declaredType = type.getType();
    while (raw != Object.class) { // 如果类型是Object则结束循环
      Field[] fields = raw.getDeclaredFields(); // 获取该类型的所有的内部属性
      for (Field field : fields) {
        boolean serialize = excludeField(field, true);
        boolean deserialize = excludeField(field, false);
        if (!serialize && !deserialize) {
          continue;
        }
        accessor.makeAccessible(field);
        Type fieldType = $Gson$Types.resolve(type.getType(), raw, field.getGenericType());
        List<String> fieldNames = getFieldNames(field); // 获取该Filed的名字（Gson通过注解可以给一个属性多个解析名）
        BoundField previous = null;
        for (int i = 0, size = fieldNames.size(); i < size; ++i) {
          String name = fieldNames.get(i);
          // 多个解析名，第一作为默认的序列化名称
          if (i != 0) serialize = false; // only serialize the default name
          // 创建BoundField
          BoundField boundField = createBoundField(context, field, name,
              TypeToken.get(fieldType), serialize, deserialize);
        // 将BoundField放入Map中，获取被替换掉的value(如果有的话)
          BoundField replaced = result.put(name, boundField);
          // 做好记录
          if (previous == null) previous = replaced;
        }
        if (previous != null) {
        // 如果previous != null证明出现了两个相同的Filed name，直接抛出错误
        // 注：Gson不允许定义两个相同的名称的属性（父类和子类之间可能出现）
          throw new IllegalArgumentException(declaredType
              + " declares multiple JSON fields named " + previous.name);
        }
      }
      type = TypeToken.get($Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
      raw = type.getRawType(); // 获取父类类型，最终会索引到Object.因为Object是所有对象的父类
    }
    return result;
  }
```

下面这段代码的主要工作就是，找到该类型内部的所有属性，并尝试逐一封装成BoundField。

```java
// 根据每个Filed创建BoundField（封装Filed读写操作）
  private ReflectiveTypeAdapterFactory.BoundField createBoundField(
      final Gson context, final Field field, final String name,
      final TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
      // 是否是原始数据类型 （int,boolean,float...）
    final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
    ...
    ...
    if (mapped == null){
        // Gson尝试获取该类型的TypeAdapter，这个方法我们后面也会继续提到。
        mapped = context.getAdapter(fieldType);
    }
    // final变量，便于内部类使用
    final TypeAdapter<?> typeAdapter = mapped;
    return new ReflectiveTypeAdapterFactory.BoundField(name, serialize, deserialize) {
      ...
      ...
      // ReflectiveTypeAdapterFactory.Adapter委托的Json读操作会调用到这里
      @Override void read(JsonReader reader, Object value)
          throws IOException, IllegalAccessException {
          // 通过该属性的类型对应的TypeAdapter尝试读取json串
          //如果是基础类型，则直接读取，
          //如果是复合类型则递归之前的流程
        Object fieldValue = typeAdapter.read(reader);
        if (fieldValue != null || !isPrimitive) {
          field.set(value, fieldValue); //更新filed值 
        }
      }
      @Override public boolean writeField(Object value) throws IOException, IllegalAccessException {
        if (!serialized) return false;
        Object fieldValue = field.get(value);
        return fieldValue != value; // avoid recursion for example for Throwable.cause
      }
    };
  }
```

