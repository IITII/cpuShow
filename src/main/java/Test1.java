import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Test1 {

    public static void main(String[] args) {

        // 启动定时器线程，并在10000毫秒后执行TimerTask实例的run方法
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("bombing!");
                System.err.println("err");

            }
        },0);
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("bombing!");
                System.err.println("err");

            }
        },2000);

        while(true) {
            System.out.println("时钟时间：" + new Date().getSeconds());
            try {
                Thread.sleep(1000);// 主线程每隔1秒钟，打印当前时钟时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}