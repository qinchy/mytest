package com.qinchy.deadlock;

public class EasyJstackResource {
    /**
     * 管理的两个资源，如果有多个线程并发，那么就会死锁
     */
    private final Resource resourceA = new Resource();
    private final Resource resourceB = new Resource();

    public EasyJstackResource() {
        this.resourceA.setValue(0);
        this.resourceB.setValue(0);
    }

    public int read() {
        synchronized (this.resourceA) {
            System.out.println(Thread.currentThread().getName() + "线程拿到了资源 resourceA的对象锁");
            synchronized (resourceB) {
                System.out.println(Thread.currentThread().getName() + "线程拿到了资源 resourceB的对象锁");
                return this.resourceA.getValue() + this.resourceB.getValue();
            }
        }
    }

    public void write(int a, int b) {
        synchronized (this.resourceB) {
            System.out.println(Thread.currentThread().getName() + "线程拿到了资源 resourceB的对象锁");
            synchronized (this.resourceA) {
                System.out.println(Thread.currentThread().getName() + "线程拿到了资源 resourceA的对象锁");
                this.resourceA.setValue(a);
                this.resourceB.setValue(b);
            }
        }
    }
}