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

### Memory

|     | Entries    | Redis      | Redis 32-bit | ChronicleMap |
|-----|------------|------------|--------------|--------------|
| 1M  | 1,000,000  | 130.25 MB  | 118.38 MB    | 40.0 MB      |
| 25M | 25,000,000 | 3256.25 MB | 2992 MB      | 1076.0 MB    |


### Redis

#### Serial
- Loaded Redis with 1000000 records. Elapsed time: 450624 ms. 
- Average write time 450624 ns. 
- Read 200000 records. Elapsed time: 23597 ms 
- Average entry read time: 117986 ns 
- Used Memory: 130.25M
- For 25_000_000 this will result in a footprint of: 3256.25 MB.

#### Serial 32-bit
- Loaded Redis with 1000000 records. Elapsed time: 331177 ms.
- Average write time 331177 ns.
- Read 200000 records. Elapsed time: 14381 ms
- Average entry read time: 71905 ns
- Used Memory: 118.38M
- For 25_000_000 this will result in a footprint of: 2959.5 MB.

#### Serial write
| Benchmark                        | Elapsed time (ms) | Average write time (ns) |
|----------------------------------|-------------------|-------------------------|
| Redis Serial (1M)                | 450624            | 450624                  |
| Redis Serial (32-bit 1M)         | 331177            | 331177                  |
| Chronicle Map (Serial, Int) 25M  | 52749             | 2109                    |
| Chronicle Map (Serial, Byte) 25M | 34022             | 1360                    |


#### Serial Read
| Benchmark                        | Elapsed time (ms) | Average entry read time (ns) |
|----------------------------------|-------------------|------------------------------|
| Redis Serial (1M)                | 23597             | 117986                       |
| Redis Serial (32-bit 1M)         | 14381             | 71905                        |
| Chronicle Map (Serial, Int) 25M  | 3974              | 794                          |
| Chronicle Map (Serial, Byte) 25M | 1603              | 320                          |

#### Combi in ms

| Benchmark                         | Elapsed time (ms) (Write) | Average write time (ms) | Elapsed time (ms) (Read) | Average entry read time (ms) |
|-----------------------------------|---------------------------|-------------------------|--------------------------|------------------------------|
| Redis Serial (1M)                 | 450.624                   | 0.450624                | 23.597                   | 0.117986                     |
| Redis Serial (32-bit 1M)          | 331.177                   | 0.331177                | 14.381                   | 0.071905                     |
| Chronicle Map (Serial, Int, 25M)  | 52.749                    | 0.002109                | 3.974                    | 0.000794                     |
| Chronicle Map (Serial, Byte, 25M) | 34.022                    | 0.001360                | 1.603                    | 0.000320                     |




#### Redis Coroutines with batching

#### Performance Metrics for Redis Database (1,000,000 Records)

| Batch Size | Total Load Time (ms) | Average Load Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|----------------------|------------------------|----------------------|------------------------------|
| 50         | 95845                | 95845                  | 143027               | 143027                       |
| 100        | 92883                | 92883                  | 91908                | 91908                        |
| 500        | 137798               | 137798                 | 90316                | 90316                        |
| 1000       | 96541                | 96541                  | 125214               | 125214                       |
| 5000       | 92372                | 92372                  | 88692                | 88692                        |

#### Performance Metrics for Redis Database (10,000 Records)

| Batch Size | Total Load Time (ms) | Average Load Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|----------------------|------------------------|----------------------|------------------------------|
| 50         | 2370                 | 237056                 | 1291                 | 129194                       |
| 100        | 1165                 | 116552                 | 1050                 | 105086                       |
| 500        | 1163                 | 116345                 | 1090                 | 109044                       |
| 1000       | 1127                 | 112775                 | 1406                 | 140694                       |
| 5000       | 1231                 | 123142                 | 1211                 | 121186                       |

#### Performance Metrics for Redis 32BIT Database (1M Records)

| Batch Size | Total Load Time (ms) | Average Load Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|----------------------|------------------------|----------------------|------------------------------|
| 50         | 93996                | 93996                  | 89552                | 89552                        |
| 100        | 91197                | 91197                  | 102461               | 102461                       |
| 500        | 94433                | 94433                  | 87090                | 87090                        |
| 1000       | 124044               | 124044                 | 84025                | 84025                        |
| 5000       | 93603                | 93603                  | 118898               | 118898                       |

#### Redis 25 M , 1000 batch size run coroutine 32bit
- Loaded Redis with 25000000 records. Elapsed time: 3204607 ms.
- Average write time 128184 ns.
- Read 25000000 records. Elapsed time: 1841305 ms
- Average entry read time: 73652 ns

### Chronicle Map

#### Serial <IntValue, ByteArray>
- Loaded Chronicle Map with 25,000,000 entries
- Elapsed time: 52,749 ms
- Memory footprint: 1,151 MB
- Average write time: 2,109 ns
- Reading 5,000,000 records...
- Read 5,000,000 records
- Elapsed time: 3,974 ms
- Average entry read time: 794

#### Serial <ByteValue, ByteArray>
- Loaded Chronicle Map with 25000000 entries.
- Elapsed time: 34022 ms
- Memory footprint: 1076 MB
- Average write time 1360 ns
- Reading 5000000 records...
  Read 5000000 records. Elapsed time: 1603 ms
- Average entry read time: 320 ns

#### With coroutines:
- Memory footprint is the same with 1076 MB for 25_000_000 entries.

#### Chronicle Map Performance Metrics for 1,000,000 Records 

| Batch Size | Total Write Time (ns) | Average Write Time (ns) |
|------------|-----------------------|-------------------------|
| 1          | 2310743158            | 2310.743158             |
| 5          | 1740793335            | 1740.793335             |
| 20         | 1054909881            | 1054.909881             |
| 50         | 805283261             | 805.283261              |
| 100        | 696266866             | 696.266866              |
| 500        | 672799112             | 672.799112              |
| 1000       | 526959575             | 526.959575              |
| 5000       | 695293044             | 695.293044              |
| 10000      | 812736331             | 812.736331              |
| 100000     | 653749348             | 653.749348              |


#### Performance Metrics for Chronicle Map (25,000,000 Entries)

| Batch Size | Total Load Time (ms) | Average Load Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|----------------------|------------------------|----------------------|------------------------------|
| 50         | 16862                | 674.494                | 5895                 | 235                          |
| 100        | 16401                | 656.043                | 5817                 | 232                          |
| 500        | 13275                | 531.032                | 5136                 | 205                          |
| 1000       | 13841                | 553.679                | 4283                 | 171                          |
| 5000       | 13921                | 556.875                | 4302                 | 172                          |
