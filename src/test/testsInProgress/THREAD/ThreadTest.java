package test.testsInProgress.THREAD;

import thread.CouponExpirationDailyJob;

public class ThreadTest {

    public static void main(String[] args) {

        CouponExpirationDailyJob r = new CouponExpirationDailyJob(); // runnable
        Thread t = new Thread(r); // thread covers runnable
        t.start();

        try {
            System.out.println("I sleep");
            Thread.sleep(1000 * 12); // main sleep
            System.out.println("I finish sleep");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("I awake!!");
        t.interrupt(); // interrupt (stop) the thread
//r.stop();
        System.out.println("t interrupted");


    }
}
