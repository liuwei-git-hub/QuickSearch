package task;
import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileScanTask {
    //线程池
    public  final ExecutorService POOL=Executors.newFixedThreadPool(4);
    //有了线程池后可以直接使用execute提交任务

    //定义一个可以记录线程个数的值
    //private static volatile int COUNT;
    private final   AtomicInteger COUNT=new AtomicInteger();
    private  final CountDownLatch LATCH=new CountDownLatch(1);
    /*
    * 做接口回调
    * */
    private  FileScanCallback fileScanCallback;
    public FileScanTask(FileScanCallback fileScanCallback){
        this.fileScanCallback=fileScanCallback;
    }



    //private static final CyclicBarrier BARRIER=new CyclicBarrier(2);
    /*
    * 启动根目录的扫描文件
    * */
    public  void startScan(File root) {
        //扫描此文件夹下的文件（包含文件夹）
        //将文件路径传给递归list（）
        //启动线程进行扫描文件
        /*synchronized (this) {
            COUNT++;
        }*/
        COUNT.incrementAndGet();
        POOL.execute(new Runnable() {
            @Override
            public void run() {
                list(root);
            }
        });
    }



/*
    //测试
    public static void main(String[] args) throws InterruptedException {
        FileScanTask task=new FileScanTask();//实例化一个类
        task.startScan(new File("D:\\ccc"));//启动扫描文件夹
        synchronized (task){
            task.wait();//等待task结束
        }
        System.out.println("执行完毕");
    }
*/


    /*
    * 等待所有的扫描任务执行完毕
    * */
    public void waitFinish() throws InterruptedException {
        //wait过程我们都清楚，需要等待所有的线程执行结束后
        //  才完成，但是如果我们一开始选择错误的文件夹，此时文件夹比较大
        // 我们就需要中断现在运行的线程，中段的线程也就是线程池中的线程。
        /*try {
            synchronized (this){
                this.wait();//开闭原则
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //等待
        try {

            LATCH.await();
        }finally {

            //捕获到线程我们中断线程池
            POOL.shutdown();//调用每个线程的interrupt中断
            // POOL.shutdownNow();//调用每个线程stop（）关闭。
        }




        /*try {
            BARRIER.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }*/
    }


    /*
    * 在这里使用递归的方法，因为我们获取到子文件，还有会是文件夹的情况，写一个递归的方法
    * */
    public   void list(File dir) {
        if (!Thread.interrupted()) {
            try {
                //将整个文件添加到接口中
                fileScanCallback.execute(dir);


                //System.out.println(dir.getPath());
                if (dir.isDirectory()) {
                    //将子文件放入到一个数组中
                    // children是子文件，子文件夹
                    File[] children = dir.listFiles();
                    //如果根文件夹中有数据进入
                    if (children != null && children.length > 0) {
                        //遍历，使用递归，获取所有的内容
                        for (File child : children) {
                            //这里我们已经进入到文件内，所以现在我们看的是文件夹就就进行递归，不是的话直接打印
                            if (child.isDirectory()) {
                                //递归我们每次只要看到里面有子文件夹我们就启动一个线程执行
                                //这样就不可避免产生大量的线程，所以我们创建一个线程池
                            /*synchronized (this) {
                                COUNT++;
                            }*/
                                COUNT.incrementAndGet();
                                POOL.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        list(child);
                                    }
                                });
                            } else {
                                fileScanCallback.execute(dir);
                                //System.out.println(child.getPath());
                            }
                        }
                    }
                }
            } finally {
                if (COUNT.decrementAndGet() == 0) {
                    //通知
                    LATCH.countDown();


                    /*try {
                        BARRIER.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        }

    }
}