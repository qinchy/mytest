package com.qinchy.jmockitdemo;

/**
 * 一个包含初始代码块的普通类
 */
public class AnOrdinaryClassWithBlock {
    public static int j;

    // 静态初始代码块
    static {
        j = 2;
    }

    private int i;

    // 初始代码块
    {
        i = 1;
    }

    // 构造函数
    public AnOrdinaryClassWithBlock(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

}
