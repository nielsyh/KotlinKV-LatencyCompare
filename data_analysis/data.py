# Benchmark Results for Redis (Serial)
redis_serial_1M = {
    "Loaded Redis with 1000000 records": {
        "Elapsed time (ms)": 450624,
        "Average write time (ns)": 450624,
        "Read 200000 records": {
            "Elapsed time (ms)": 23597,
            "Average entry read time (ns)": 117986
        },
        "Used Memory": "130.25M",
        "Footprint for 25_000_000": "3256.25 MB"
    },
}

redis_serial_32bit_1M = {
    "Loaded Redis with 1000000 records": {
        "Elapsed time (ms)": 331177,
        "Average write time (ns)": 331177,
        "Read 200000 records": {
            "Elapsed time (ms)": 14381,
            "Average entry read time (ns)": 71905
        },
        "Used Memory": "118.38M",
        "Footprint for 25_000_000": "2959.5 MB"
    },
}

# Chronicle Map Benchmark Results
chronicle_map_serial_int = {
    "Loaded Chronicle Map with 25,000,000 entries": {
        "Elapsed time (ms)": 52749,
        "Memory footprint": "1,151 MB",
        "Average write time (ns)": 2109,
        "Reading 5,000,000 records...": {
            "Read 5,000,000 records": {
                "Elapsed time (ms)": 3974,
                "Average entry read time (ns)": 794
            }
        }
    }
}

chronicle_map_serial_byte = {
    "Loaded Chronicle Map with 25,000,000 entries": {
        "Elapsed time (ms)": 34022,
        "Memory footprint": "1,076 MB",
        "Average write time (ns)": 1360,
        "Reading 5,000,000 records...": {
            "Read 5,000,000 records": {
                "Elapsed time (ms)": 1603,
                "Average entry read time (ns)": 320
            }
        }
    }
}

# Benchmark Results for Redis Coroutines with Batching
redis_coroutines_metrics_1M = {
    "Batch Size": [50, 100, 500, 1000, 5000],
    "Total Load Time (ms)": [95845, 92883, 137798, 96541, 92372],
    "Average Write Time (ns)": [95845, 92883, 137798, 96541, 92372],
    "Total Read Time (ms)": [143027, 91908, 90316, 125214, 88692],
    "Average Entry Read Time (ns)": [143027, 91908, 90316, 125214, 88692]
}

# Redis 32-bit Benchmark Results
redis_coroutines_32bit_metrics_1M = {
    "Batch Size": [50, 100, 500, 1000, 5000],
    "Total Load Time (ms)": [93996, 91197, 94433, 124044, 93603],
    "Average Write Time (ns)": [93996, 91197, 94433, 124044, 93603],
    "Total Read Time (ms)": [89552, 102461, 87090, 84025, 118898],
    "Average Entry Read Time (ns)": [89552, 102461, 87090, 84025, 118898]
}

lettuce_coroutines_32bit_metrics_1M = {
    "Batch Size": [50, 100, 500, 1000, 5000],
    "Total Load Time (ms)": [29035, 32155, 31871, 29836, 31624],
    "Average Entry Read Time (ns)": [29035, 32155, 31871, 29836, 31624],
    "Total Read Time (ms)": [30989, 30028, 30190, 31560, 47525],
    "Average Write Time (ns)": [30989, 30028, 30190, 31560, 47525]
}

lettuce_coroutines_32bit_metrics_1M_dispatcher_default = {
    "Batch Size": [50, 100, 500, 1000, 5000],
    "Total Load Time (ms)": [54524, 56183, 55298, 55105, 60346],
    "Average Entry Read Time (ns)": [54524, 56183, 55298, 55105, 60346],
    "Total Read Time (ms)": [54754, 53877, 55669, 66286, 58759],
    "Average Write Time (ns)": [54754, 53877, 55669, 66286, 58759]
}


# Chronicle Map Performance Metrics for 1,000,000 Records
chronicle_map_metrics_1M = {
    "Batch Size": [1, 5, 20, 50, 100, 500, 1000, 5000, 10000, 100000],
    "Total Write Time (ns)": [2310743158, 1740793335, 1054909881, 805283261, 696266866, 672799112, 526959575, 695293044, 812736331, 653749348],
    "Average Write Time (ns)": [2310.743158, 1740.793335, 1054.909881, 805.283261, 696.266866, 672.799112, 526.959575, 695.293044, 812.736331, 653.749348]
}

# Chronicle Map Performance Metrics for 25,000,000 Records
chronicle_map_metrics_25M_entries = {
    "Batch Size": [50, 100, 500, 1000, 5000],
    "Total Load Time (ms)": [60, 206, 11, 3, 6],
    "Average Write Time (ns)": [60, 206, 11, 3, 6],
    "Total Read Time (ms)": [596, 137, 250, 47, 19],
    "Average Entry Read Time (ns)": [596, 137, 250, 47, 19]
}

memory_footprint = {
    "entries": [1_000_000, 25_000_000],
    "redis": [130.25, 3256.25],
    "redis_32bit": [118.38, 2959.5],
    "chronicleMap": [40, 1076],
}