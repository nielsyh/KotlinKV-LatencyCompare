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

#### Coroutines

#### Performance Metrics for Redis Database (1,000,000 Records)

| Batch Size | Total Load Time (ms) | Average Load Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|----------------------|------------------------|----------------------|------------------------------|
| 50         | 95845                | 95845                  | 143027               | 143027                       |
| 100        | 92883                | 92883                  | 91908                | 91908                        |
| 500        | 137798               | 137798                 | 90316                | 90316                        |
| 1000       | 96541                | 96541                  | 125214               | 125214                       |
| 5000       | 92372                | 92372                  | 88692                | 88692                        |

#### Same thing but with 32bit redis

| Batch Size | Total Load Time (ms) | Average Load Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|----------------------|------------------------|----------------------|------------------------------|
| 50         | 93996                | 93996                  | 89552                | 89552                        |
| 100        | 91197                | 91197                  | 102461               | 102461                       |
| 500        | 94433                | 94433                  | 87090                | 87090                        |
| 1000       | 124044               | 124044                 | 84025                | 84025                        |
| 5000       | 93603                | 93603                  | 118898               | 118898                       |


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
#### Performance Metrics for Redis Database (10,000 Records)

| Batch Size | Total Load Time (ms) | Average Load Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|----------------------|------------------------|----------------------|------------------------------|
| 50         | 2370                 | 237056                 | 1291                 | 129194                       |
| 100        | 1165                 | 116552                 | 1050                 | 105086                       |
| 500        | 1163                 | 116345                 | 1090                 | 109044                       |
| 1000       | 1127                 | 112775                 | 1406                 | 140694                       |
| 5000       | 1231                 | 123142                 | 1211                 | 121186                       |


#### Performance Metrics for 1,000,000 Records

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

#### Performance Metrics for Redis Database (1,000,000 Records)

| Batch Size | Total Load Time (ms) | Average Load Time (ms) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|----------------------|------------------------|----------------------|------------------------------|
| 50         | 72545937671          | 72545                  | 71987159572          | 71987                        |
| 100        | 72740324489          | 72740                  | 71669914517          | 71669                        |
| 500        | 72033659979          | 72033                  | 69934617394          | 69934                        |
| 1000       | 94574314717          | 94574                  | 70758909919          | 70758                        |
| 5000       | 73871336801          | 73871                  | 69684356113          | 69684                        |


#### Performance Metrics for 25,000,000 Records

| Batch Size | Total Write Time (ms) | Average Write Time (ns) |
|------------|-----------------------|-------------------------|
| 100        | 17.141                | 685.65                  |
| 500        | 13.456                | 538.25                  |
| 1000       | 13.219                | 528.80                  |
| 5000       | 14.324                | 573.00                  |


#### Performance Metrics for 25,000,000 Records

| Batch Size | Total Write Time (ms) | Average Write Time (ns) | Total Read Time (ms) | Average Entry Read Time (ns) |
|------------|-----------------------|-------------------------|----------------------|------------------------------|
| 50         | 14.194                | 567.79                  | 4.663                | 186                          |
| 100        | 15.144                | 605.78                  | 4.865                | 194                          |
| 500        | 13.635                | 545.40                  | 4.837                | 193                          |
| 1000       | 19.921                | 796.85                  | 4.943                | 197                          |
| 5000       | 14.328                | 573.15                  | 3.607                | 144                          |


