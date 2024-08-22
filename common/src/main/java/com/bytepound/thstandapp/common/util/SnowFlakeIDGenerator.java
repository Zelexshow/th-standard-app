package com.bytepound.thstandapp.common.util;

public class SnowFlakeIDGenerator implements IDGenerator {
    // 开始时间戳 (2023-01-01)
    private final long twepoch = 1672531200000L;

    // 机器 ID 所占的位数
    private final long workerIdBits = 5L;

    // 数据中心 ID 所占的位数
    private final long datacenterIdBits = 5L;

    // 支持的最大机器 ID，结果是 31 (这个移位算法可以很快地计算出几位二进制数所能表示的最大十进制数)
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    // 支持的最大数据中心 ID，结果是 31
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    // 序列在 ID 中占的位数
    private final long sequenceBits = 12L;

    // 机器 ID 左移 12 位
    private final long workerIdShift = sequenceBits;

    // 数据中心 ID 左移 17 位 (12+5)
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    // 时间戳左移 22 位 (5+5+12)
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    // 生成序列的掩码，这里为 4095 (0b111111111111=0xfff=4095)
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    // 工作机器 ID (0~31)
    private long workerId;

    // 数据中心 ID (0~31)
    private long datacenterId;

    // 毫秒内序列 (0~4095)
    private long sequence = 0L;

    // 上次生成 ID 的时间戳
    private long lastTimestamp = -1L;

    // 构造函数
    public SnowFlakeIDGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    // 生成唯一 ID
    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    // 阻塞到下一个毫秒，直到获得新的时间戳
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    // 返回以毫秒为单位的当前时间
    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowFlakeIDGenerator idGenerator = new SnowFlakeIDGenerator(1, 1);
        for (int i = 0; i < 10; i++) {
            System.out.println(idGenerator.nextId());
        }
    }
}
