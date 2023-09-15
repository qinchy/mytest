package com.qinchy.jmh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(8)
public class MapperBenchmark {
    String json = "{\"color\":\"Black\",\"type\":\"BMW\"}";

    /**
     * 每次基准测试都用全局的ObjectMapper将字符串反序列化成map
     *
     * @param state
     * @return
     * @throws Exception
     */
    @Benchmark
    public Map globalTest(BenchmarkState state) throws Exception {
        Map map = state.GLOBAL_MAP.readValue(json, Map.class);
        return map;
    }

    /**
     * 每个线程都用已经缓存的ObjectMapper将字符串反序列化成map
     *
     * @param state
     * @return
     * @throws Exception
     */
    @Benchmark
    public Map globalTestThreadLocal(BenchmarkState state) throws Exception {
        if (null == state.GLOBAL_MAP_THREAD.get()) {
            state.GLOBAL_MAP_THREAD.set(new ObjectMapper());
        }
        Map map = state.GLOBAL_MAP_THREAD.get().readValue(json, Map.class);
        return map;
    }

    /**
     * 每次基准测试都用本地的ObjectMapper将字符串反序列化成map
     *
     * @return
     * @throws Exception
     */
    @Benchmark
    public Map localTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        return map;
    }

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        ObjectMapper GLOBAL_MAP = new ObjectMapper();

        ThreadLocal<ObjectMapper> GLOBAL_MAP_THREAD = new ThreadLocal<>();

    }
}