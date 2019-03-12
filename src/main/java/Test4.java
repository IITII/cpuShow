import java.util.Timer;
import java.util.TimerTask;

public class Test4 {

    //这个任务两秒执行一次  
    static class Task2000 extends TimerTask {
        public void run() {
            System.out.println("两秒炸一次");
            //new Timer().schedule(new Task4000(), 2000);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("1");
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("1");
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    System.out.println("1");
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            System.out.println("1");
                                            new Timer().schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    System.out.println("1");
                                                }
                                            },1000);
                                        }
                                    },1000);
                                }
                            },1000);
                        }

                    },1000);


                }
            },1000);
        }
    }

    //这个任务四秒执行一次  
    /*static class Task4000 extends TimerTask {
        public void run() {
            System.out.println("四秒炸一次");
            new Timer().schedule(new Task2000(), 4000);
        }
    }*/


    public static void main(String[] args) {
        new Timer().schedule(new Task2000(),0);

    }
}  