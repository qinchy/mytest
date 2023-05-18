package com.qinchy.jcedemo.random;

import sun.misc.BASE64Encoder;

import java.util.concurrent.ThreadLocalRandom;

/**
 * java里有伪随机型和安全型两种随机数生成器，伪随机生成器根据特定公式将seed转换成新的伪随机数据的一部分，安全随机生成器在底层依赖到操作系统提供的随机事件来生成数据。
 * 而伪随机生成器，只依赖于“seed”的初始值，如果给生成算法提供相同的seed，可以得到一样的伪随机序列。
 *
 * @author qinchy
 */
public class ThreadLocalRandomDemo {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Player().start();
        }
    }

    private static class Player extends Thread {
        @Override
        public void run() {
            byte[] bytes = new byte[16];
            ThreadLocalRandom.current().nextBytes(bytes);
            System.out.println(new BASE64Encoder().encode(bytes));
        }
    }
}
