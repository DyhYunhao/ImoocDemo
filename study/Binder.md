一。Binder
  1.除了service_manage，其他应用级进程或线程和Binder打交道的都是IPCThreadState.
  2.我们只关注进程间通信的核心流程 transaction
  3.传输发起者需要告诉Binder三个东西：handle(你找谁?), cmd（你要干啥?）和data(东西呢?)
  4.在客户端handle由BpBinder持有。不管什么样的服务在Binder眼里就是一串数字。记住这一点。
  5.在server端真正的服务是BBinder来完成的。
    error = reinterpret_cast<BBinder*>(tr.cookie)->transact(tr.code, buffer, &reply, tr.flags);
  6.显然Binder驱动帮我们完成了从本地的一串数字handle到远端实体服务BBinder的转换

  这里就引出了几个问题：
     1.本地handle是如何转换为远端BBinder的？
     2.这个handle是从哪里来的呢？

  这里先假设我们已经有了这个handle来说明第一个问题。
  Binder驱动传输流程:
     这个流程其实就只有三步：第一，客户端把数据写进来。第二，把数据从客户端传到服务端。第三，通知服务端把数据读走。
     这个流程我希望大家能紧盯着handle因为它是我们理解问题1的唯一线索。
     客户端把数据写进来: 在用户空间，函数IPCThreadState::writeTransactionData()负责写入驱动。
                       这个函数会构造一个binder_transaction_data的结构体tr。我们的handle会被写进去。
                       status_t IPCThreadState::writeTransactionData(...int32_t handle, ...)
                       {
                           binder_transaction_data tr;
                          ...
                          // handle在这里
                           tr.target.handle = handle;
                           tr.code = code;
                          ...
                       }

                       static int binder_thread_write(...) {
                       ...
                       //我们只关注BC_TRANSACTION
                       case BC_TRANSACTION:
                       case BC_REPLY: {
                                   struct binder_transaction_data tr;
                                   //tr到了这里
                                   if (copy_from_user(&tr, ptr, sizeof(tr)))
                                       return -EFAULT;
                                   ptr += sizeof(tr);
                                   //这个函数做传输工作
                                   binder_transaction(proc, thread, &tr,
                                              cmd == BC_REPLY, 0);
                                   break;
                               }
                        ...
                       }
      把数据从客户端传到服务端: 这事是由函数binder_transaction()来干的。
                              注意看一下那几个入参，proc代表客户端进程。thread代表客户端线程，tr呢就是最开始的那个，记住里面放着handle。
                              传输的前提是要找到目标的进程\线程。而我们唯一的线索就是handle。怎么通过handle来获得目标进程\线程呢？
                              static void binder_transaction(...) {
                              ...
                              if (tr->target.handle) {
                                          //通过handle查找目标引用
                                          target_ref = binder_get_ref(proc, tr->target.handle,
                                                       true);
                                          //通过目标引用得到目标节点。
                                          target_node = target_ref->node;
                                      }
                                      ...
                              }
                              //从node里拿到目标进程的信息
                              target_proc = target_node->proc;
                              ...
                              //然后搞了个binder_transaction的结构体
                              t = kzalloc(sizeof(*t), GFP_KERNEL);
                              ...
                              //设置目标进程/线程
                              t->to_proc = target_proc;
                              t->to_thread = target_thread;
                              ...
                              //设置目标节点
                              t->buffer->target_node = target_node;
                              ...
                              //要干的活是传输
                              t->work.type = BINDER_WORK_TRANSACTION;
                              ...
                              //把传输任务插入到目标进程/线程的todo队列。
                              binder_proc_transaction(t, target_proc, target_thread);
                              ...

                    从上述关键代码我们可以看到，要完成传输就需要拿到目标进程的信息。而我们只有handle
                    驱动会先拿这个handle在自身进程信息proc内查找节点的应用，然后由这个引用就能得到节点，
                    得到节点就能拿到目标进程的信息target_proc了。那么ref是啥？node又是啥呢？为什么他们能通过handle找的到呢？
                    node可以说是Binder驱动的核心数据结构。node会以红黑树的形式保存在服务端进程结构体内，客户端则保存的是它的引用ref。
                    而handle来自ref->desc。
                    node和ref是什么时候保存到Binder驱动中的呢？这个我们在后面解决handle从哪里来的问题再解释。
                    所以在传输阶段我们看到handle换成了node。在插入到目标进程todo队列的数据里只有node而没有handle。
                    handle已经完成了它的使命，接下来让我们关注node。

      通知服务端把数据读走:
                        static int binder_thread_read(...) {

                        ...
                        //有活干了
                        w = list_first_entry(...);
                        ...
                        //看看是什么活，我们只关心transaction
                        switch (w->type) {
                        case BINDER_WORK_TRANSACTION: {
                                    t = container_of(w, struct binder_transaction, work);
                                } break;
                        }
                        ...
                        if (t->buffer->target_node) {
                                    tr.target.ptr = target_node->ptr;
                                    tr.cookie =  target_node->cookie;
                                    ...
                                    cmd = BR_TRANSACTION;
                                }
                                ...
                                tr.code = t->code;
                        }

                        cmd设置成了BR_TRANSACTION；binder_thread_read()会拼一个binder_transaction_data。也就是tr出来。
                        它是要被送往用户空间的，就像之前从用户空间往binder驱动里写的也是一个tr一样。
                        这里注意驱动把node里的两个东西设置给了tr。ptr和cookie。记住它们。
      接下来就回到用户空间了：
                         status_t IPCThreadState::executeCommand(int32_t cmd) {

                         //我们只关心传输
                           case BR_TRANSACTION:
                                 {
                                     if (tr.target.ptr) {
                                         if (reinterpret_cast<RefBase::weakref_type*>(
                                                 tr.target.ptr)->attemptIncStrong(this)) {
                                             error = reinterpret_cast<BBinder*>(tr.cookie)->transact(tr.code, buffer,
                                                     &reply, tr.flags);
                                             reinterpret_cast<BBinder*>(tr.cookie)->decStrong(this);
                                         }
                                 }
                                 break;
                         }

       啊哈，这里我们就能看出来原来cookie的真身就是BBinder。也就是我们服务的真身了啊。

  所以捋一下Binder驱动的传输过程就是从客户端的handle在自身进程信息proc里查找ref。再通过ref就能得到node。
  而node则是存储在服务端进程信息里的。node就很关键了，从它身上能找到目标进程的信息和目标服务的真身BBinder实例。然后回到用户区直接调用就好了。

传输过程就介绍完了，接下来我们还有解决另外一个系列问题，handle从从哪里来？node又是什么时候给保存到内核里去的呢？这就涉及到services_manager了。
服务注册及获取:
          我们知道services_manager是个特殊的服务。有点像个DNS服务器，其他什么AMS，PMS等等都需要先到它那里注册以后，
          客户们才能在services_manager找到他们。既然services_manager这么特殊，那它在驱动中肯定会有个独一无二的node吧。
          所以services_manager启动的时候会给Binder驱动发这么个命令BINDER_SET_CONTEXT_MGR。
          Binder驱动照办：
                       static int binder_ioctl_set_ctx_mgr(struct file *filp)
                       {
                           ...
                           temp = binder_new_node(proc, 0, 0);
                           ...
                           context->binder_context_mgr_node = temp;
                           binder_put_node(temp);
                       ...
                       }
          函数binder_new_node()是用来新建node的:
                      static struct binder_node *binder_new_node(struct binder_proc *proc,
                                             binder_uintptr_t ptr,
                                             binder_uintptr_t cookie)
                      三个入参，proc，ptr和cookie。回想一下上面介绍传输过程的时候这三个参数是不是都用到过？
                      当然，对于services_manager来说只需要进程信息，其他两个入参都是0。
                      这个特殊的node被设置给context->binder_context_mgr_node。

         注册服务: 注册服务的要点是盯住服务实体Binder是如何转化为node的。
               我们都知道service_manager特殊，特殊就特殊在它的handle就是0。所以无论是注册服务还是获取服务，
               我们不需要去别处问，自己就可以搞出来个代表service_manager的BpBinder。注册的时候是这样的
               virtual status_t addService(const String16& name, const sp<IBinder>& service,
                                               bool allowIsolated, int dumpsysPriority) {
                       Parcel data, reply;
                       data.writeInterfaceToken(IServiceManager::getInterfaceDescriptor());
                       data.writeString16(name);
                       data.writeStrongBinder(service);
                       data.writeInt32(allowIsolated ? 1 : 0);
                       data.writeInt32(dumpsysPriority);
                       status_t err = remote()->transact(ADD_SERVICE_TRANSACTION, data, &reply);
                       return err == NO_ERROR ? reply.readExceptionCode() : err;
                   }
         注意这句data.writeStrongBinder(service);也就是说把服务自身给写到要传输的数据里面去了。 写成啥样了呢？
         status_t flatten_binder(){
         ...
         flat_binder_object obj;
         ...
          IBinder *local = binder->localBinder();
                 if (!local) {
                     BpBinder *proxy = binder->remoteBinder();
                     const int32_t handle = proxy ? proxy->handle() : 0;
                     obj.hdr.type = BINDER_TYPE_HANDLE;
                     obj.binder = 0;
                     obj.handle = handle;
                     obj.cookie = 0;
                 } else {
                     obj.hdr.type = BINDER_TYPE_BINDER;
                     obj.binder = reinterpret_cast<uintptr_t>(local->getWeakRefs());
                     obj.cookie = reinterpret_cast<uintptr_t>(local);
                 }
         ...
         }
         因为这里是注册服务，所以给的Binder是实体Binder，走的是else分支。类型为BINDER_TYPE_BINDER，cookie设置为service实体。
         注册服务是一次handle为0的Binder传输。回想我们之前讨论的传输过程，handle如果是0的话会在传输过程走不一样的分支：
           static void binder_transaction(...) {
           ...
           if (tr->target.handle) {
                       ...
                   } else {
                       target_node = context->binder_context_mgr_node;
                   }
           //从node里拿到目标进程的信息
           target_proc = target_node->proc;

           ...
                   switch (hdr->type) {
                   case BINDER_TYPE_BINDER:
                   case BINDER_TYPE_WEAK_BINDER: {
                       struct flat_binder_object *fp;
                       fp = to_flat_binder_object(hdr);
                       ret = binder_translate_binder(fp, t, thread);
                   } break;
                   case BINDER_TYPE_HANDLE:
                   case BINDER_TYPE_WEAK_HANDLE: {
                       struct flat_binder_object *fp;
                       fp = to_flat_binder_object(hdr);
                       ret = binder_translate_handle(fp, t, thread);
                   } break;

           ...

           //把传输任务插入到目标进程/线程的todo队列。
           binder_proc_transaction(t, target_proc, target_thread);
           ...

          对于注册服务的流程来说在传输的时候有两个地方需要注意，
               1. 由于handle是0，所以直接会找到binder_context_mgr_node。这个node也就是我们之前看到的service_manager初始化的时候创建的，
                  也就是说我们直接就扎到了service_manager。
               2. 还记得我们之前把服务实体放在传输数据里面了吗？这里驱动会检查传输的数据，如果发现有BINDER_TYPE_XXX类型的都要做转换，
                  为啥要转呢？因为我的实体到你那里就得是个handle啊, 否则都不在同一个进程，不转换的话你拿我的实体也没用啊。
                  所以，这里我们的服务实体要做一次转换了：
                       static int binder_translate_binder(struct flat_binder_object *fp,
                                          struct binder_transaction *t,
                                          struct binder_thread *thread)
                       {
                           struct binder_node *node;
                           struct binder_ref *ref;
                           struct binder_proc *proc = thread->proc;
                           struct binder_proc *target_proc = t->to_proc;

                           node = binder_get_node(proc, fp->binder);
                           if (!node) {
                               node = binder_new_node(proc, fp->binder, fp->cookie);
                           }

                           ref = binder_get_ref_for_node(target_proc, node, &thread->todo);

                           if (fp->hdr.type == BINDER_TYPE_BINDER)
                               fp->hdr.type = BINDER_TYPE_HANDLE;
                           else
                               fp->hdr.type = BINDER_TYPE_WEAK_HANDLE;
                           fp->binder = 0;
                           fp->handle = ref->desc;
                           fp->cookie = 0;
                       ...
                       }
                     注意看这里的转换，首先是在自己的node树里面找有没有实体服务Binder对应的node。
                     这里因为我们是要注册服务，所以node不存在，需要新建一个。你看至此我们的新服务已经有了自己的node了。
                     然后呢会在目标进程信息，也就是service_manager中找这个node的引用，没有的话会新建一个引用的，
                     这个过程会产生一个数放在ref->desc。对了，这个数就是我们的新服务在service_manager那边的handle了。
                     最后就是对原始数据做变身了。类型从BINDER变为HANDLE。清空binder和cookie。给handle字段赋值，
                     我们的实体Binder就这样华丽的变成自己的分身了。
                    千万不要搞混了，这里做转换的是我们要注册的服务，完全是处在事务数据中。而传输这些事务数据的是handle为0，
                    对应node是binder_context_mgr_node的事务。

        总体而言注册服务：
                    是一次对service_manager的传输，只不过需要在开始的时候把服务实体Binder放在要传输的数据里。
                    Binder驱动会在传输过程中做一次从实体服务Binder到对应handle的变换。
                    在这个变换的过程中会创建实体服务Binder对应的node。

        获取服务: 获取服务的过程分两段，一个是把服务名字传过去，另一个是把handle传回来。
                 去程没啥好说的，和上面的注册服务过程差不多，还少了中间做转换的步骤。回程就是不一样的操作了。
                 service_manager把查到的handle通过BC_REPLY给送回来的。别看handle只是个数，在传输的时候必须把它包装一下，变成这样的：
                     obj->hdr.type = BINDER_TYPE_HANDLE;
                     obj->handle = handle;
                     obj->cookie = 0;
                 类型是BINDER_TYPE_HANDLE。
                 这个数据结构会写入Binder驱动，命令BC_REPLY会走到和BC_TRANSACTION一样的函数，也就是binder_transaction()，
                 但是某些分支有有所不同。
                 binder_transaction(...){
                 if (reply) {
                 in_reply_to = thread->transaction_stack;
                 target_thread = in_reply_to->from;
                 target_proc = target_thread->proc;
                 }
                 ...
                 case BINDER_TYPE_HANDLE:
                 case BINDER_TYPE_WEAK_HANDLE: {
                             struct flat_binder_object *fp;
                             fp = to_flat_binder_object(hdr);
                             ret = binder_translate_handle(fp, t, thread);
                         } break;
                         ...
                 }
                 可见返回过程不需要再查找node因为调用端是谁是清楚的。直接把from变成target就行了。
                 因为返回数据块里面放着获取到的服务handle。同样的这里会给它做个变换。
                 看一下binder_translate_handle()如何做变换:
                        static int binder_translate_handle(struct flat_binder_object *fp,
                                           struct binder_transaction *t,
                                           struct binder_thread *thread)
                        {
                            struct binder_ref *ref;
                            struct binder_proc *proc = thread->proc;
                            struct binder_proc *target_proc = t->to_proc;

                            ref = binder_get_ref(proc, fp->handle,
                                         fp->hdr.type == BINDER_TYPE_HANDLE);

                            if (ref->node->proc == target_proc) {
                                if (fp->hdr.type == BINDER_TYPE_HANDLE)
                                    fp->hdr.type = BINDER_TYPE_BINDER;
                                else
                                    fp->hdr.type = BINDER_TYPE_WEAK_BINDER;
                                fp->binder = ref->node->ptr;
                                fp->cookie = ref->node->cookie;

                            } else {
                                struct binder_ref *new_ref;
                                new_ref = binder_get_ref_for_node(target_proc, ref->node, NULL);
                                fp->binder = 0;
                                fp->handle = new_ref->desc;
                                fp->cookie = 0;
                            }
                        }
                  这里的转换有两个分支，如果此服务进程和目标进程相同，则将handle转换成实体Binder。
                  也就是说你去service_manager那里获取到了和你同一个进程的服务，自然可以转换为实体Binder直接拿来用了。
                  如果不是同一个进程呢？则会到目标进程信息那里去获取一个新的handle。替换掉原来的handle返回给调用者。
                  为什么会这样呢？这就需要深入了解node和ref的关系。

                  node实体只保存在创建者进程所对应的proc中。
                  ref是保存在当前进程中对外部node实体的引用，不同的进程对同一个node的ref是不同的。
                  所以可以认为handle是进程私有的，不要想当然的认为从service_manager拿到的handle就是和自己进程拿到的一样。


      总结:
          Binder虽然结构复杂，难以全面掌握。但有时又是Android开发者在实际工作中不得不面对的问题。掌握Binder需要投入大量精力和努力。
          通过一些文章的指引，是可以在学习Binder的路途上减轻一些压力，避免一些误区的。
          本文就希望能帮助开发者打通一些在学习Binder时可能会遇到的阻塞点，特别是这些阻塞点基本上会存在于Binder驱动层。
          毕竟楼再高，盖起来还是要先从地基打起，Binder驱动就是Binder这座大厦的基础。打好这个基础，往上的就都是包裹和通道了，
          理解起来也就会比较轻松。希望能对大家有所帮助。









