package com.mycompany.app7;

public class TestMessageRunnable {
    public static void main(String[] args) {
        // Option 1: Direct (no lambda)
        new Thread(new MessageRunnable("Hello Direct")).start();

        // Option 2: Lambda calling run()
        MessageRunnable task = new MessageRunnable("Hello Lambda");
        new Thread(() -> task.run()).start();

        // Option 3: Method reference
        MessageRunnable task2 = new MessageRunnable("Hello Method Reference");
        new Thread(task2::run).start();

        // Option 4: Inline with lambda
        new Thread(() -> new MessageRunnable("Hello Inline").run()).start();
    }
}