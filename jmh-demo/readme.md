```shell
λ java -jar D:\Source_Code\java\mytest\jmh-demo\target\benchmarks.jar
# JMH version: 1.23
# VM version: JDK 1.8.0_181, Java HotSpot(TM) 64-Bit Server VM, 25.181-b13
# VM invoker: C:\Progra~1\Java\jdk1.8.0_181\jre\bin\java.exe
# VM options: <none>
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 8 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.qinchy.jmh.MapperBenchmark.globalTest

# Run progress: 0.00% complete, ETA 00:00:40
# Fork: 1 of 1
# Warmup Iteration   1: 7360690.457 ops/s
# Warmup Iteration   2: 9118344.934 ops/s
# Warmup Iteration   3: 11379090.544 ops/s
# Warmup Iteration   4: 11400235.048 ops/s
# Warmup Iteration   5: 11179867.887 ops/s
Iteration   1: 11022872.770 ops/s
Iteration   2: 11093583.964 ops/s
Iteration   3: 10811646.590 ops/s
Iteration   4: 11107821.253 ops/s
Iteration   5: 8370258.821 ops/s


Result "com.qinchy.jmh.MapperBenchmark.globalTest":
  10481236.679 ±(99.9%) 4566847.996 ops/s [Average]
  (min, avg, max) = (8370258.821, 10481236.679, 11107821.253), stdev = 1185995.922
  CI (99.9%): [5914388.683, 15048084.676] (assumes normal distribution)


# JMH version: 1.23
# VM version: JDK 1.8.0_181, Java HotSpot(TM) 64-Bit Server VM, 25.181-b13
# VM invoker: C:\Progra~1\Java\jdk1.8.0_181\jre\bin\java.exe
# VM options: <none>
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 8 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.qinchy.jmh.MapperBenchmark.globalTestThreadLocal

# Run progress: 25.00% complete, ETA 00:00:33
# Fork: 1 of 1
# Warmup Iteration   1: 5194361.910 ops/s
# Warmup Iteration   2: 6650782.807 ops/s
# Warmup Iteration   3: 7378176.061 ops/s
# Warmup Iteration   4: 7598544.411 ops/s
# Warmup Iteration   5: 7464538.389 ops/s
Iteration   1: 7584613.358 ops/s
Iteration   2: 7686580.563 ops/s
Iteration   3: 7643946.174 ops/s
Iteration   4: 7586205.699 ops/s
Iteration   5: 7871024.493 ops/s


Result "com.qinchy.jmh.MapperBenchmark.globalTestThreadLocal":
  7674474.057 ±(99.9%) 453889.666 ops/s [Average]
  (min, avg, max) = (7584613.358, 7674474.057, 7871024.493), stdev = 117873.705
  CI (99.9%): [7220584.391, 8128363.723] (assumes normal distribution)


# JMH version: 1.23
# VM version: JDK 1.8.0_181, Java HotSpot(TM) 64-Bit Server VM, 25.181-b13
# VM invoker: C:\Progra~1\Java\jdk1.8.0_181\jre\bin\java.exe
# VM options: <none>
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 8 threads, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.qinchy.jmh.MapperBenchmark.localTest

# Run progress: 50.00% complete, ETA 00:00:22
# Fork: 1 of 1
# Warmup Iteration   1: 46653.978 ops/s
# Warmup Iteration   2: 85422.792 ops/s
# Warmup Iteration   3: 309876.454 ops/s
# Warmup Iteration   4: 328555.970 ops/s
# Warmup Iteration   5: 329232.613 ops/s
Iteration   1: 335297.533 ops/s
Iteration   2: 334220.702 ops/s
Iteration   3: 316714.204 ops/s
Iteration   4: 336393.975 ops/s
Iteration   5: 339492.130 ops/s


Result "com.qinchy.jmh.MapperBenchmark.localTest":
  332423.709 ±(99.9%) 34655.913 ops/s [Average]
  (min, avg, max) = (316714.204, 332423.709, 339492.130), stdev = 9000.031
  CI (99.9%): [297767.796, 367079.622] (assumes normal distribution)


# JMH version: 1.23
# VM version: JDK 1.8.0_181, Java HotSpot(TM) 64-Bit Server VM, 25.181-b13
# VM invoker: C:\Progra~1\Java\jdk1.8.0_181\jre\bin\java.exe
# VM options: <none>
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: com.qinchy.jmh.RandomBenchmark.testMethod

# Run progress: 75.00% complete, ETA 00:00:11
# Fork: 1 of 1
# Warmup Iteration   1: 1316.313 ns/op
# Warmup Iteration   2: 1043.191 ns/op
# Warmup Iteration   3: 1040.598 ns/op
# Warmup Iteration   4: 1055.954 ns/op
# Warmup Iteration   5: 1041.952 ns/op
Iteration   1: 1046.003 ns/op
Iteration   2: 1079.393 ns/op
Iteration   3: 1040.887 ns/op
Iteration   4: 1039.441 ns/op
Iteration   5: 1048.056 ns/op


Result "com.qinchy.jmh.RandomBenchmark.testMethod":
  1050.756 ±(99.9%) 63.138 ns/op [Average]
  (min, avg, max) = (1039.441, 1050.756, 1079.393), stdev = 16.397
  CI (99.9%): [987.619, 1113.894] (assumes normal distribution)


# Run complete. Total time: 00:00:43

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

Benchmark                               Mode  Cnt         Score         Error  Units
MapperBenchmark.globalTest             thrpt    5  10481236.679 ± 4566847.996  ops/s
MapperBenchmark.globalTestThreadLocal  thrpt    5   7674474.057 ±  453889.666  ops/s
MapperBenchmark.localTest              thrpt    5    332423.709 ±   34655.913  ops/s
RandomBenchmark.testMethod              avgt    5      1050.756 ±      63.138  ns/op
```