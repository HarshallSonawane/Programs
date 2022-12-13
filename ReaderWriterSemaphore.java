import java.util.concurrent.Semaphore;

class ReaderWriterSemaphore {
    static Semaphore readLock = new Semaphore(1);
    static Semaphore writeLock = new Semaphore(1);

    static class Writer implements Runnable {
        public void run() {
            try {
                writeLock.acquire();
                System.out.println(Thread.currentThread().getName() + " is writing");
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName() + " has finished writing");
                writeLock.release();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    static class Reader implements Runnable {
        int rc = 0;

        public void run() {
            try {
                readLock.acquire();
                rc++;
                if (rc == 1) {
                    writeLock.acquire();
                }
                readLock.release();
                System.out.println(Thread.currentThread().getName() + " is reading");
                Thread.sleep(2500);
                System.out.println(Thread.currentThread().getName() + " has finished reading");
                readLock.acquire();
                rc--;
                if (rc == 0) {
                    writeLock.release();
                }
                readLock.release();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        Reader read = new Reader();
        Writer write = new Writer();
        Thread t1 = new Thread(read);
        t1.setName("reader-1");
        Thread t2 = new Thread(read);
        t2.setName("reader-2");
        Thread t3 = new Thread(read);
        t3.setName("reader-3");
        Thread t4 = new Thread(write);
        t4.setName("writer-1");
        Thread t5 = new Thread(write);
        t5.setName("writer-2");
        t1.start();
        t4.start();
        t2.start();
        t3.start();
        t5.start();
    }
}
