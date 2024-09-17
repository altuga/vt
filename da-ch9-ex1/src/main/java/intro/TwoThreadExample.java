package intro;

class TwoThreadExample implements Runnable {
    private String message;

    public TwoThreadExample(String message) {
        this.message = message;
    }

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(message);

    }

    public static void main(String[] args) {
        System.out.println(" " + Thread.currentThread().getName());
        Thread threadA = new Thread(new TwoThreadExample("A"));
        Thread threadB = new Thread(new TwoThreadExample("B"));
        threadA.setDaemon(false);
        threadB.setDaemon(false);

        threadA.start();
        threadB.start();
        System.out.println(" finised ...");
    }
}
