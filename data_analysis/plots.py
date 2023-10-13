import matplotlib.pyplot as plt
import numpy as np
from tabulate import tabulate

from data_analysis.data import redis_serial_1M, redis_serial_32bit_1M, chronicle_map_serial_int, \
    chronicle_map_serial_byte, chronicle_map_metrics_25M_entries, \
    redis_coroutines_metrics_1M, redis_coroutines_32bit_metrics_1M, memory_footprint, lettuce_coroutines_32bit_metrics_1M, lettuce_coroutines_32bit_metrics_1M_dispatcher_default


def plot_serial():
    import matplotlib.pyplot as plt

    # Extract data for Redis and Chronicle Map benchmarks and convert to milliseconds
    redis_serial_1M_write_time_ms = redis_serial_1M["Loaded Redis with 1000000 records"][
                                        "Average write time (ns)"] / 1e6
    redis_serial_1M_read_time_ms = redis_serial_1M["Loaded Redis with 1000000 records"]["Read 200000 records"][
                                       "Average entry read time (ns)"] / 1e6

    redis_serial_32bit_1M_write_time_ms = redis_serial_32bit_1M["Loaded Redis with 1000000 records"][
                                              "Average write time (ns)"] / 1e6
    redis_serial_32bit_1M_read_time_ms = \
        redis_serial_32bit_1M["Loaded Redis with 1000000 records"]["Read 200000 records"][
            "Average entry read time (ns)"] / 1e6

    chronicle_map_int_write_time_ms = chronicle_map_serial_int["Loaded Chronicle Map with 25,000,000 entries"][
                                          "Average write time (ns)"] / 1e6
    chronicle_map_int_read_time_ms = \
        chronicle_map_serial_int["Loaded Chronicle Map with 25,000,000 entries"]["Reading 5,000,000 records..."][
            "Read 5,000,000 records"]["Average entry read time (ns)"] / 1e6

    chronicle_map_byte_write_time_ms = chronicle_map_serial_byte["Loaded Chronicle Map with 25,000,000 entries"][
                                           "Average write time (ns)"] / 1e6
    chronicle_map_byte_read_time_ms = \
        chronicle_map_serial_byte["Loaded Chronicle Map with 25,000,000 entries"]["Reading 5,000,000 records..."][
            "Read 5,000,000 records"]["Average entry read time (ns)"] / 1e6

    # Create a bar plot with a logarithmic y scale
    labels = ['Redis Serial (1M) Write', 'Redis Serial (1M) Read', 'Redis 32-bit Serial (1M) Write',
              'Redis 32-bit Serial (1M) Read',
              'Chronicle Map Serial (25M IntValue) Write', 'Chronicle Map Serial (25M IntValue) Read',
              'Chronicle Map Serial (25M ByteValue) Write', 'Chronicle Map Serial (25M ByteValue) Read']

    times = [redis_serial_1M_write_time_ms, redis_serial_1M_read_time_ms,
             redis_serial_32bit_1M_write_time_ms, redis_serial_32bit_1M_read_time_ms,
             chronicle_map_int_write_time_ms, chronicle_map_int_read_time_ms,
             chronicle_map_byte_write_time_ms, chronicle_map_byte_read_time_ms]

    plt.bar(labels, times)
    plt.yscale('log')  # Use a logarithmic y scale
    plt.ylabel('Time (ms) [Logarithmic Scale]')
    plt.title('Benchmark Results: Average Read and Write Times (ms)')
    plt.xticks(rotation=45, ha="right")

    plt.tight_layout()  # Adjust layout for better spacing

    # Save the plot to an image file (e.g., PNG)
    plt.savefig('serial_benchmark_results.png')

    # Extracted data for Redis and Chronicle Map benchmarks in milliseconds
    data = [
        ["Redis Serial (1M) Write", redis_serial_1M_write_time_ms],
        ["Redis Serial (1M) Read", redis_serial_1M_read_time_ms],
        ["Redis 32-bit Serial (1M) Write", redis_serial_32bit_1M_write_time_ms],
        ["Redis 32-bit Serial (1M) Read", redis_serial_32bit_1M_read_time_ms],
        ["Chronicle Map Serial (25M IntValue) Write", chronicle_map_int_write_time_ms],
        ["Chronicle Map Serial (25M IntValue) Read", chronicle_map_int_read_time_ms],
        ["Chronicle Map Serial (25M ByteValue) Write", chronicle_map_byte_write_time_ms],
        ["Chronicle Map Serial (25M ByteValue) Read", chronicle_map_byte_read_time_ms]
    ]

    # Create a table
    table = tabulate(data, headers=["Benchmark", "Average Time (ms)"], tablefmt="grid")

    # Display the table
    print(table)


def plot_coroutines_write():
    datasets = {
        "Redis Coroutines 1M Records": redis_coroutines_metrics_1M,
        "Redis 32-bit Coroutines 1M Records": redis_coroutines_32bit_metrics_1M,
        "Redis lettuce coroutines 32bit 1M Dis.Def.": lettuce_coroutines_32bit_metrics_1M,
        "Redis lettuce coroutines 32bit 1M Dis.IO.": lettuce_coroutines_32bit_metrics_1M_dispatcher_default,
        "Chronicle Map 25M Records Dis.Def.": chronicle_map_metrics_25M_entries,
    }

    # Create a single plot with different colors and a legend
    plt.figure(figsize=(10, 6))

    for dataset_name, dataset in datasets.items():
        batch_sizes = dataset["Batch Size"]
        average_write_times_ms = [time / 1e6 for time in dataset["Average Write Time (ns)"]]
        plt.plot(batch_sizes, average_write_times_ms, marker='o', label=dataset_name)

    plt.xlabel('Batch Size')
    plt.ylabel('Average Time (ms)')
    plt.yscale('log')  # Set y-axis to logarithmic scale
    plt.title('Average Write Time')
    plt.grid(True)
    plt.legend()

    plt.tight_layout()

    # Save the plot to an image file (e.g., PNG)
    plt.savefig('coroutines_benchmark_write_results.png')


def plot_coroutines_read():
    datasets = {
        "Redis Coroutines 1M Records": redis_coroutines_metrics_1M,
        "Redis 32-bit Coroutines 1M Records": redis_coroutines_32bit_metrics_1M,
        "Redis lettuce coroutines 32bit 1M Dis.Def.": lettuce_coroutines_32bit_metrics_1M,
        "Redis lettuce coroutines 32bit 1M Dis.IO.": lettuce_coroutines_32bit_metrics_1M_dispatcher_default,
        "Chronicle Map 25M Records Dis.Def.": chronicle_map_metrics_25M_entries,
    }

    # Create a single plot with different colors and a legend
    plt.figure(figsize=(10, 6))

    for dataset_name, dataset in datasets.items():
        batch_sizes = dataset["Batch Size"]
        average_read_times_ms = [time / 1e6 for time in dataset["Average Entry Read Time (ns)"]]
        plt.plot(batch_sizes, average_read_times_ms, marker='o', label=dataset_name)

    plt.xlabel('Batch Size')
    plt.ylabel('Average Time (ms)')
    plt.yscale('log')  # Set y-axis to logarithmic scale
    plt.title('Average Read Time')
    plt.grid(True)
    plt.legend()

    plt.tight_layout()

    # Save the plot to an image file (e.g., PNG)
    plt.savefig('coroutines_benchmark_read_results.png')


def plot_memory_footprint():
    entries_data = memory_footprint["entries"]

    plt.plot(entries_data, marker='o', linestyle='-')
    plt.xlabel('Entries')
    plt.ylabel('Memory Footprint (MB)')
    plt.title('Memory Footprint vs. Entries')
    plt.grid(True)
    plt.tight_layout()
    plt.savefig("memory_footprint.png")

plot_serial()
plot_coroutines_write()
plot_coroutines_read()
plot_memory_footprint()
