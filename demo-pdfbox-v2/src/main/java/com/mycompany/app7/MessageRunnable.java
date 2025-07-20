package com.mycompany.app7;

class MessageRunnable implements Runnable {
    private final String message;

    public MessageRunnable(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        System.out.println("MessageRunnable: " + message +
                " on thread: " + Thread.currentThread().getName());
    }
}
