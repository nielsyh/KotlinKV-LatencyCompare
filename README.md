# KotlinKV-LatencyCompare

Kotlin-based project for comparing low-latency key-value storage solutions.

## Overview

This project is a benchmarking and comparison tool for evaluating the performance of low-latency key-value storage solutions. It specifically focuses on comparing two storage solutions: Redis and Chronicle Map.

## JVM Arguments

Before running the benchmark, make sure to use the following JVM arguments:

```bash
--add-exports=java.base/jdk.internal.ref=ALL-UNNAMED --add-exports=java.base/sun.nio.ch=ALL-UNNAMED --add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED
```

## Benchmark Results

### Redis


Loading Redis database with 1000000 records
Loaded Redis with 1000000 records. Elapsed time: 231602 ms.
Average write time 231602 ns.
Reading 200000 records...
Read 200000 records. Elapsed time: 6916 ms
Average entry read time: 34582 ns
Used Memory: 715.61M

### Chronicle Map

#### IntValue, ByteArray
- Loaded Chronicle Map with 25,000,000 entries
- Elapsed time: 52,749 ms
- Memory footprint: 1,151 MB
- Average write time: 2,109 ns
- Reading 5,000,000 records...
- Read 5,000,000 records
- Elapsed time: 3,974 ms
- Average entry read time: 794

#### With coroutines:
| BatchSize | Total Write Time (ms) | Average Write Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|-----------|-----------------------|-------------------------|----------------------|------------------------------|
| 1         | 210,988               | 8,439                   | 41,811               | 8,362                        |
| 5         | 113,448               | 4,537                   | 17,876               | 3,575                        |
| 20        | 102,116               | 4,084                   | 2,654                | 530                          |
| 50        | 96,674                | 3,866                   | 2,093                | 418                          |
| 100       | 95,610                | 3,824                   | 1,230                | 246                          |
| 500       | 101,500               | 4,060                   | 1,027                | 205                          |
| 1,000     | 103,578               | 4,143                   | 571                  | 114                          |
| 5,000     | 84,831                | 3,393                   | 705                  | 141                          |
| 10,000    | 87,686                | 3,507                   | 708                  | 141                          |



#### ByteValue, ByteArray
- Loaded Chronicle Map with 25000000 entries. 
- Elapsed time: 34022 ms
- Memory footprint: 1076 MB
- Average write time 1360 ns
- Reading 5000000 records...
 Read 5000000 records. Elapsed time: 1603 ms
- Average entry read time: 320 ns
