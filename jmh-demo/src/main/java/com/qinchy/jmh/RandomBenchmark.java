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

    private static final int CARD_COUNT = 54;

    private static final List<Integer> CARD_LIST = new ArrayList<Integer>();

    private static final Random GLOBAL_RANDOM = new Random();

    static {
        for (int i = 0; i < CARD_COUNT; i++) {
            CARD_LIST.add(i);
        }
    }

    /**
     * 所有循环使用一个random
     *
     * @return
     */
    @Benchmark
    public List<Integer> testOuterRandom1() {
        // 洗牌算法
        Random random = new Random();
        for (int i = 0; i < CARD_COUNT; i++) {
            int rand = random.nextInt(CARD_COUNT);
            Collections.swap(CARD_LIST, i, rand);
        }
        return CARD_LIST;
    }

    /**
     * 所有循环使用一个random
     *
     * @return
     */
    @Benchmark
    public List<Integer> testOuterRandom2() {
        // 洗牌算法
        Random random = new Random();
        Collections.shuffle(CARD_LIST, random);
        return CARD_LIST;
    }


    /**
     * 每个循环使用新的random
     *
     * @return
     */
    @Benchmark
    public List<Integer> testInnerRandom1() {
        // 洗牌算法
        for (int i = 0; i < CARD_COUNT; i++) {
            Random random = new Random();
            int rand = random.nextInt(CARD_COUNT);
            Collections.swap(CARD_LIST, i, rand);
        }
        return CARD_LIST;
    }

    /**
     * 每个循环使用新的random
     *
     * @return
     */
    @Benchmark
    public List<Integer> testInnerRandom2() {
        // 洗牌算法
        for (int i = 0; i < CARD_COUNT; i++) {
            Random random = new Random();
            Collections.shuffle(CARD_LIST, random);
        }
        return CARD_LIST;
    }

    /**
     * 所有线程使用全局random
     *
     * @return
     */
    @Benchmark
    public List<Integer> testGlobalRandom1() {
        // 洗牌算法
        for (int i = 0; i < CARD_COUNT; i++) {
            int rand = GLOBAL_RANDOM.nextInt(CARD_COUNT);
            Collections.swap(CARD_LIST, i, rand);
        }
        return CARD_LIST;
    }

    /**
     * 所有线程使用全局random
     *
     * @return
     */
    @Benchmark
    public List<Integer> testGlobalRandom2() {
        // 洗牌算法
        for (int i = 0; i < CARD_COUNT; i++) {
            Collections.shuffle(CARD_LIST, GLOBAL_RANDOM);
        }
        return CARD_LIST;
    }
}