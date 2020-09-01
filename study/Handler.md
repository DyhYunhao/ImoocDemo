前言：
    Handler可以说小伙伴们用的非常多了，可以说Handler是支撑整个Android系统运行的基础，本质上Android系统都是由事件驱动的。
    而处理事件的核心就在于Handler。接下来我们就从简单的使用，到源码分析让你彻彻底底明白Handler的本质。
    不会再让你发出为什么Looper.loop不会堵塞主线程，Handler是如何切换线程等这类疑惑。

简单使用： 一般是在主线程中实现一个Handler，然后在子线程中使用它
          class HandlerActivity: AppCompatActivity() {

              private val mHandler = MyHandler()

              override fun onCreate(savedInstanceState: Bundle?) {
                  super.onCreate(savedInstanceState)
                  // 在子线程中通过自定义的 Handler 发消息
                  thread {
                      mHandler.sendEmptyMessageDelayed(1, 1000)
                  }
              }

              // 自定义一个 Handler
              class MyHandler: Handler() {
                  override fun handleMessage(msg: Message) {
                      Log.i("HandlerActivity", "主线程：handleMessage: ${msg.what}")
                  }
              }
          }

        或者有时候需要在子线程中创建运行在主线程中的Handler：
         class HandlerActivity: AppCompatActivity() {
             private var mHandler: Handler? = null

             override fun onCreate(savedInstanceState: Bundle?) {
                 super.onCreate(savedInstanceState)
                 thread {
                     //获得main looper 运行在主线程
                     mHandler = MyHandler(Looper.getMainLooper())
                     mHandler!!.sendEmptyMessageDelayed(1, 1000)
                 }
             }
             // 自定义一个 Handler
             class MyHandler(): Handler() {
                 override fun handleMessage(msg: Message) {
                     Log.i("HandlerActivity", "子线程：handleMessage: ${msg.what}")
                 }
             }
         }
     这就是小伙伴们一般常用的两个用法。大家注意到了在第二个用法中出现了一个Looper.getMainLooper()，使用它作为参数，
     即使MyHandler是在子线程中定义的，但是它的handleMessage方法依然运行在主线程。我们看一下这个参数究竟是什么东东~
     public Handler(@NonNull Looper looper) {
            this(looper, null, false);
     }
    可以看到这个Looper就是我们上面传入的参数Looper.getMainLooper()，也就说明了handleMessage方法具体运行在哪个线程是和这个Looper息息相关的。
    那么这个Looper究竟是何方神圣，它是怎么做到线程切换的呢？

    这就是整个Handler在Java层的流程示意图。可以看到，
    在Handler调用sendMessage方法以后，Message对象会被添加到MessageQueue中去。
    而这个MessageQueue就是被包裹在了Looper中。那么Looper对象是干什么的呢？它和Handler是什么关系呢？
    我们来看一下他们具体的职责把~

    Handle 消息机制中作为一个对外暴露的工具，其内部包含了一个 Looper 。负责Message的发送及处理
       Handler.sendMessage() ：向消息队列发送各种消息事件
       Handler.handleMessage()：处理相应的消息事件
    Looper 作为消息循环的核心，其内部包含了一个消息队列 MessageQueue  ，用于记录所有待处理的消息；
       通过Looper.loop()不断地从MessageQueue中抽取Message，按分发机制将消息分发给目标处理者，
       可以看成是消息泵。注意，线程切换就是在这一步完成的。
    MessageQueue 则作为一个消息队列，则包含了一系列链接在一起的 Message ；
       不要被这个Queue的名字给迷惑了，就以为它是一个队列，但其实内部通过单链表的数据结构来维护消息列表，等待Looper的抽取。
    Message 则是消息体，内部又包含了一个目标处理器 target ，这个 target 正是最终处理它的 Handler

    从我们大家最熟悉的sendMessage方法说起。sendMessage方法见名思意，就是发送一个信息，可是要发送到哪里去呢，这是代码：
        public final boolean sendMessage(@NonNull Message msg) {
            return sendMessageDelayed(msg, 0);
        }
        调用了sendMessageDelayed方法：
        public final boolean sendMessageDelayed(@NonNull Message msg, long delayMillis) {
                if (delayMillis < 0) {
                    delayMillis = 0;
                }
                return sendMessageAtTime(msg, SystemClock.uptimeMillis() + delayMillis);
        }
       继而调用sendMessagAtTime方法
       public boolean sendMessageAtTime(@NonNull Message msg, long uptimeMillis) {
               MessageQueue queue = mQueue;
               if (queue == null) {
                   RuntimeException e = new RuntimeException(
                           this + " sendMessageAtTime() called with no mQueue");
                   Log.w("Looper", e.getMessage(), e);
                   return false;
               }
               return enqueueMessage(queue, msg, uptimeMillis);
       }
       眼尖的小伙伴就会发现，等等不对，这代码中出了一个叛徒，啊不对，出了一个奇怪的东西。
       没错，就是刚才流程图中出现的这个MessageQueue。你看，我没有胡说吧，这个MessageQueue是实打实存在的，
       并且被作为参数一起传给了enqueueMessage方法。其实无论你是如何使用Handler发送消息，结果都会走到enqueueMessage方法中。

       这个enqueueMessage方法具体做了什么事呢：
       private boolean enqueueMessage(@NonNull MessageQueue queue, @NonNull Message msg,
                   long uptimeMillis) {
               msg.target = this;
               msg.workSourceUid = ThreadLocalWorkSource.getUid();

               if (mAsynchronous) {
                   msg.setAsynchronous(true);
               }
               return queue.enqueueMessage(msg, uptimeMillis);
       }
       enqueueMessage一共做了两件事情，一个是给Message赋值，一个是调用传进来的这个MessageQueue的enqueueMessage方法。
       注意啊，最后这个enqueueMessage方法是在MessageQueue中的，已经不再是Handler的方法了，也就是说，调用走到了这里。
       事件的流向已经不归Handler管了。

      Handler::enqueueMessage方法中第一行msg.target = this;，这个this是什么呢？
      这个this在handler方法中自然是handler本身了，也就是说这一行代码将handler自身赋值给了Message对象的target字段。
      我们可以看以下这个target字段的定义：
         //简化后的代码
         public final class Message implements Parcelable{
             @UnsupportedAppUsage
             /*package*/ Handler target;
         }
      啊，这样明白了，也就是说每个发出去的Message都持有把它发出去的Handler的引用，对不对？
      没错事实就是这样，每个发出去的Message对象内部都会有个把它发出去的Handler对象的引用，也可以理解Message这么做的目的，
      毕竟Handler把它发射出去了，它不得知道是谁干的，好随后找它报仇么。
      那么我们继续下一步，msg.setAsynchronous(true)这一行代码是设置异步消息的，这里暂时先不管它。
      我们先看queue.enqueueMessage(msg, uptimeMillis)这行代码。也就是从这行代码，Message就可以和Handler说拜拜了您讷。

  MessageQueue
     Handler这个mQueue就是上文我们提到过的MessageQueue对象，在上面的介绍说也说了，这货就是个骗子，明明起名是Queue，却是单链表。
     你可能误会Google工程师了，名字也确实没什么错了，从机制上看确实很像队列。队列是什么特性啊，先进先出对吧。
     这个先后就是按时间来划分的，时间靠前的就在前面时间靠后的就在后面。而在这个单链表中也确实是这样实现的，按照时间的先后排序。
     这个就先不多讲了，一会讲如何实现的消息延时发送的时候会讲到这个。
     到这里你可能有疑惑了，这个MessageQueue是什么鬼，从哪里冒出来的。你可能还记得，
     在上面的sendMessageAtTime方法中有这么一行：
       MessageQueue queue = mQueue;
     那么这个mQueue是在哪里被赋值的呢？当然是在构造方法中啦~
     public Handler(@Nullable Callback callback, boolean async) {
         if (FIND_POTENTIAL_LEAKS) {
             final Class<? extends Handler> klass = getClass();
             if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                     (klass.getModifiers() & Modifier.STATIC) == 0) {
                 Log.w(TAG, "The following Handler class should be static or leaks might occur: " +
                     klass.getCanonicalName());
             }
         }

         mLooper = Looper.myLooper();
         if (mLooper == null) {
             throw new RuntimeException(
                 "Can't create handler inside thread " + Thread.currentThread()
                         + " that has not called Looper.prepare()");
         }
         mQueue = mLooper.mQueue;
         mCallback = callback;
         mAsynchronous = async;
     }
     不对啊， 你TM骗我，在最开始你继承的Handler可没有这几个参数。哎呀，小伙子别心急，你看这个无参构造方法不也调用的这个方法么。
     public Handler() {
             this(null, false);
     }
     在这个有参数的构造方法中呢，可以看到有这么两行
     mLooper = Looper.myLooper();
     if (mLooper == null) {
         throw new RuntimeException(
            "Can't create handler inside thread " + Thread.currentThread()
              + " that has not called Looper.prepare()");
     }
     mQueue = mLooper.mQueue;
     我们在Handler中使用的mQueue就是在这里赋值的。这里的赋值可不简单，
     它拿的是人家Looper的MessageQueue作为自己的MessageQueue，而且在上面的代码中有一个很关键的点，
     就是调用Looper.myLooper()方法中获取这个Looper对象，如果是空的话就要抛出异常。
     这一点非常关键，我们先做个记号，一会回过头来会看这一行代码。你就会明白它的作用了。
     现在先不研究Looper，我们继续看我们的MessageQueue。上面说到，
     最后发送消息都调用的是MessageQueue的queue.enqueueMessage(msg, uptimeMillis)方法。
     现在我们已经拿到了queue，进去看看这个方法它做了什么。
       // MessageQueue.java
       //省略部分代码
       boolean enqueueMessage(Message msg, long when) {

           synchronized (this) {
               if (mQuitting) {
                   IllegalStateException e = new IllegalStateException(
                           msg.target + " sending message to a Handler on a dead thread");
                   msg.recycle();
                   return false;
               }

               msg.markInUse();
               msg.when = when;

               //【1】拿到队列头部
               Message p = mMessages;
               boolean needWake;

               //【2】如果消息不需要延时，或者消息的执行时间比头部消息早，插到队列头部
               if (p == null || when == 0 || when < p.when) {
                   // New head, wake up the event queue if blocked.
                   msg.next = p;
                   mMessages = msg;
                   needWake = mBlocked;
               } else {
                   //【3】消息插到队列中间
                   needWake = mBlocked && p.target == null && msg.isAsynchronous();
                   Message prev;
                   for (;;) {
                       prev = p;
                       p = p.next;
                       if (p == null || when < p.when) {
                           break;
                       }
                       if (needWake && p.isAsynchronous()) {
                           needWake = false;
                       }
                   }
                   msg.next = p; // invariant: p == prev.next
                   prev.next = msg;
               }

               if (needWake) {
                   nativeWake(mPtr);
               }
           }
           return true;
       }
     1. mMessages 是队列的第一消息，获取到它
     2. 判断消息队列是不是空的，是则将当前的消息放到队列头部；如果当前消息不需要延时，或当前消息的执行时间比头部消息早，
        也是放到队列头部。
     3. 如果不是以上情况，说明当前队列不为空，并且队列的头部消息执行时间比当前消息早，需要将它插入到队列的中间位置。

     如何判断这个位置呢？依然是通过消息被执行的时间。
     通过遍历整个队列，当队列中的某个消息的执行时间比当前消息晚时，将消息插到这个消息的前面。
     可以看到，消息队列是一个根据消息【执行时间先后】连接起来的单向链表。
     想要获取可执行的消息，只需要遍历这个列表，对比当前时间与消息的执行时间，就知道消息是否需要执行了。













