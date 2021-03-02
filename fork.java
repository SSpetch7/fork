//package BasicThreads;
import java.util.Random;
public class SimpleThreads {
    // Display a message, preceded by the name of the current thread
    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }
    private static class MessageLoop implements Runnable {
        public void run() {
            String importantInfo[] = {
                "First man eats an orange",
                "Second woman eats an orange",
                "Third boy eats an orange",
                "Forth girl will eat an orange too."
            };
            try {
                for (int i = 0; i < importantInfo.length; i++) {
                    Thread.sleep(3000);  // sleep 3s
                    threadMessage(importantInfo[i]); // Print a message
                }
            } catch (InterruptedException e) {
                threadMessage("I wasn't done!");
            }
        }
    }
    public static void main(String args[])
        throws InterruptedException {
        // Delay, in milliseconds before we interrupt MessageLoop thread.
        long patience = 1000 * 20; // 20s
        //patience = 5000; // แล้วลองเปลี่ยนค่า patience เป็น 5000 ดูว่าจะเป้นอย่างไร
        // If command line argument present, gives patience in seconds.
        if (args.length > 0) {
            try {
                patience = Long.parseLong(args[0]) * 1000;
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer.");
                System.exit(1);
            }
        }
        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread t = new Thread(new MessageLoop());
        t.start();
        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop thread exits
        int count = 1;
        while (t.isAlive()) {
            threadMessage("Still waiting... " + count++);
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) { }
            t.join(1000);
            if ( (System.currentTimeMillis() - startTime > patience)
                  && t.isAlive() ) {
                threadMessage("Tired of waiting!");
                t.interrupt();
                // Shouldn't be long now
                // -- wait indefinitely
                t.join();
            }
        }
        threadMessage("Finally!");
    }
}