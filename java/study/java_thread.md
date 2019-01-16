# 线程

### 1. 线程概念

在java中，每个任务都是Runnable接口的一个实例，也称为**可运行对象**(runnable object)。线程本质上讲就是便于任务执行的对象。

### 2. 创建任务和线程

1. 实现Runnable接口
2. new Thread(r).start();

### 3. Thread类

```
start()
isAlive()
setPriority()
join()
sleep()
yield()
interrput() 中断线程,该标志位，后续如何处理由程序控制
isInterrupted() 判断线程被中断
static interrupted() 判断线程是否被中断，并且将中断状态改为false
```

### 4. 线程状态

- New 新创建
- Runnable 可运行
- Blocked 被阻塞
- Waiting 等待
- Timed waiting 计时等待
- Terminated 被终止

### 5. 线程同步

如果一个类的对象在多线程程序中没有导致竞争状态，则称这样的类为线程安全的。

1. synchronized
2. synchronized(expr)同步块
3. 锁同步:
   - ReentrantLock是Lock的一个具体实现，用于创建互相排斥的锁。
   - lock(),unlock(),newCondition()

### 6. 线程间协作

1. await(),signal(),aignalAll()
2. 监视器:wait(),notify(),notifyAll()
3. 生产者/消费者:缓冲区非空+缓冲区未满
4. 阻塞队列：ArrayBlockingQueue,LinkedBlockingQueue,PriorityBlockingQueue
5. 信号量
6. 避免死锁:资源排序

### 7. 线程池

newFixedThreadPool

newCachedThreadPool

### 8. 同步集合

### 9. 并行编程

1. fork/join

