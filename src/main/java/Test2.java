import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
public class Test2 extends TimerTask
{
    private Timer timer;
    public static void main(String[] args)
    {
        Test2 timerTest= new Test2();
        timerTest.timer = new Timer();
        //立刻开始执行timerTest任务，只执行一次  
        timerTest.timer.schedule(timerTest,new Date());
        //立刻开始执行timerTest任务，执行完本次任务后，隔2秒再执行一次  
        //timerTest.timer.schedule(timerTest,new Date(),2000);            
        //一秒钟后开始执行timerTest任务，只执行一次  
        //timerTest.timer.schedule(timerTest,1000);            
        //一秒钟后开始执行timerTest任务，执行完本次任务后，隔2秒再执行一次  
        //timerTest.timer.schedule(timerTest,1000,2000);            
        //立刻开始执行timerTest任务，每隔2秒执行一次  
        //timerTest.timer.scheduleAtFixedRate(timerTest,new Date(),2000);           
        //一秒钟后开始执行timerTest任务，每隔2秒执行一次  
        //timerTest.timer.scheduleAtFixedRate(timerTest,1000,2000);  

        try
        {
            Thread.sleep(10000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        //结束任务执行，程序终止  
        timerTest.timer.cancel();
        //结束任务执行，程序并不终止,因为线程是JVM级别的  
        //timerTest.cancel();  
    }
    @Override
    public void run()
    {
        System.out.println("Task is running!");
    }
} 