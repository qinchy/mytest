package com.qinchy.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * reference:https://www.jianshu.com/p/0da2988b9846
 */
// 基准测试前对代码预热总计5秒（迭代5次，每次1秒）
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
// 表示循环运行5次，总计5秒时间。
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
// 表示fork多少个线程运行基准测试，如果@Fork(1)，那么就是一个线程，这时候就是同步模式。
@Fork(1)
// 表示每次操作需要的平均时间
@BenchmarkMode(Mode.AverageTime)
// OutputTimeUnit申明为纳秒，所以基准测试单位是ns/op，即每次操作的纳秒单位平均时间。
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class RandomBenchmark {

    @Benchmark
    public List<Integer> testMethod() {
        int cardCount = 54;
        List<Integer> cardList = new ArrayList<Integer>();
        for (int i = 0; i < cardCount; i++) {
            cardList.add(i);
        }
        // 洗牌算法
        Random random = new Random();
        for (int i = 0; i < cardCount; i++) {
            int rand = random.nextInt(cardCount);
            Collections.swap(cardList, i, rand);
        }
        return cardList;
    }

}