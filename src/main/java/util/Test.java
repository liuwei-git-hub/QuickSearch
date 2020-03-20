package util;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
//    static volatile int COUNT;

    private static final AtomicInteger COUNT = new AtomicInteger();

    private static final CountDownLatch LATCH = new CountDownLatch(3);
    /*
    * 闭锁：计数器调用await的，只有一个线程堵塞
    * latch.countDown()计数器减一
    * latch.await()阻塞等待直到计数器为0继续执行
    * */
   private static final CyclicBarrier BARRIER
            = new CyclicBarrier(4);//这个参数需要看我们一共wait多少次
   /*
   * 栅栏：所有线程一起等待直达计数器为0
   * await（）代表所有线程都阻塞等待，计数器减一，直到计数器=0，所有线程执行
   * */
    private static final Semaphore SEMAPHORE=new Semaphore(0);

    /*
    * 信号量：
    * 每有一个线程就会有一个release()，  +1  也可以release(n)+n
    * 申请计数器一定数量的资源，申请不到就阻塞等待
    * 然后我们线程结束之后我，我们调用acquire() -1或者acquire(n)-n.
    * */
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        for (int i = 0; i <3 ; i++) {
            final  int j=i;
            new Thread(new Task( String.valueOf(j),SEMAPHORE)).start();
        }
        SEMAPHORE.acquire(3);
        System.out.println("执行成功");
    }
private static class Task implements Runnable{
    private CountDownLatch latch;
    private String message;
    private CyclicBarrier cyclicBarrier;
    private Semaphore semaphore;
    public Task(CountDownLatch latch, String message) {
        this.latch = latch;
        this.message = message;
    }

    public Task(String message, CyclicBarrier cyclicBarrier) {
        this.message = message;
        this.cyclicBarrier = cyclicBarrier;
    }

    public Task( String message, Semaphore semaphore) {
        this.message = message;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        System.out.println(message);
        //CountDown的方法计数器-1；
        //latch.countDown();
        //CyclicBarrier
/*        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }*/
        //Semaphore
        semaphore.release();
    }
}


    public static void main1(String[] args) throws InterruptedException {
        for(int i=0; i<10; i++){
//            synchronized (Test.class){
//                COUNT++;
//            }
            COUNT.incrementAndGet();
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(j+"====="+COUNT);
                    if(j == 5){
                        throw new RuntimeException();
                    }
                    if(COUNT.decrementAndGet() == 0){
                        //通知
                    }

                    // 逻辑
//                    synchronized (Test.class){
//                        COUNT--;
//                        if(COUNT==0)
//                            Test.class.notifyAll();
//                    }
                }
            }).start();
        }
//        synchronized (Test.class){
//            Test.class.wait();
//        }
        // 等待
        System.out.println("执行完毕："+COUNT);
    }

}
