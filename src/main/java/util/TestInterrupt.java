package util;

//test  shutDown  interrupt是否为   通过异常这是改变状态位，具体的是否停止线程自己决定
public class TestInterrupt {
    public static void main(String[] args) throws InterruptedException {
     Thread t=new Thread(new Runnable() {
         @Override
         public void run() {
             System.out.println("ok");
         }
     });
     t.start();
     Thread.sleep(2);
        System.out.println(1);
     t.interrupt();
    }
}
