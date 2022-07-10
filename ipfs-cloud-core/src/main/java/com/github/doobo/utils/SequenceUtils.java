package com.github.doobo.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 唯一ID生成器
 **/
@Slf4j
public abstract class SequenceUtils {

    /** 初始偏移时间戳 */
    private static final long OFFSET = 1655863933L;

    /** 机器id (0~15 保留 16~31作为备份机器) */
    private static final long WORKER_ID;
    /** 机器id所占位数 (5bit, 支持最大机器数 2^5 = 32)*/
    private static final long WORKER_ID_BITS = 5L;
    /** 自增序列所占位数 (16bit, 支持最大每秒生成 2^16 = ‭65536‬) */
    private static final long SEQUENCE_ID_BITS = 16L;
    /** 机器id偏移位数 */
    private static final long WORKER_SHIFT_BITS = SEQUENCE_ID_BITS;
    /** 自增序列偏移位数 */
    private static final long OFFSET_SHIFT_BITS = SEQUENCE_ID_BITS + WORKER_ID_BITS;
    /** 机器标识最大值 (2^5 / 2 - 1 = 15) */
    private static final long WORKER_ID_MAX = ((1 << WORKER_ID_BITS) - 1) >> 1;
    /** 备份机器ID开始位置 (2^5 / 2 = 16) */
    private static final long BACK_WORKER_ID_BEGIN = (1 << WORKER_ID_BITS) >> 1;
    /** 自增序列最大值 (2^16 - 1 = ‭65535) */
    private static final long SEQUENCE_MAX = (1 << SEQUENCE_ID_BITS) - 1;
    /** 发生时间回拨时容忍的最大回拨时间 (秒) */
    private static final long BACK_TIME_MAX = 1L;

    /** 上次生成ID的时间戳 (秒) */
    private static long lastTimestamp = 0L;
    /** 当前秒内序列 (2^16)*/
    private static long sequence = 0L;
    /** 备份机器上次生成ID的时间戳 (秒) */
    private static long lastTimestampBak = 0L;
    /** 备份机器当前秒内序列 (2^16)*/
    private static long sequenceBak = 0L;

    public static final int[] EMPTY_INT_ARRAY = new int[0];

    static {
        // 初始化机器ID
        // 伪代码: 由你的配置文件获取节点ID
        long workerId = getWorkId();
        if (workerId < 0 || workerId > WORKER_ID_MAX) {
            throw new IllegalArgumentException(String.format("cmallshop.workerId范围: 0 ~ %d 目前: %d", WORKER_ID_MAX, workerId));
        }
        WORKER_ID = workerId;
    }

    private static Long getWorkId(){
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = toCodePoints(hostAddress);
            int sums = 0;
            for(int b : ints){
                sums += b;
            }
            return (long)(sums % 16);
        } catch (UnknownHostException e) {
            // 如果获取失败，则使用随机数备用
            return nextLong(0,15);
        }
    }

    /** 私有构造函数禁止外部访问 */
    private SequenceUtils() {}

    /**
     * 获取自增序列
     * @return long
     */
    public static long nextId() {
        return nextId(System.currentTimeMillis() / 1000);
    }

    /**
     * 主机器自增序列
     * @param timestamp 当前Unix时间戳
     * @return long
     */
    private static synchronized long nextId(long timestamp) {
        // 时钟回拨检查
        if (timestamp < lastTimestamp) {
            // 发生时钟回拨
            log.warn("时钟回拨, 启用备份机器ID: now: [{}] last: [{}]", timestamp, lastTimestamp);
            return nextIdBackup(timestamp);
        }

        // 开始下一秒
        if (timestamp != lastTimestamp) {
            lastTimestamp = timestamp;
            sequence = 0L;
        }
        if (0L == (++sequence & SEQUENCE_MAX)) {
            // 秒内序列用尽
            sequence--;
            return nextIdBackup(timestamp);
        }

        return ((timestamp - OFFSET) << OFFSET_SHIFT_BITS) | (WORKER_ID << WORKER_SHIFT_BITS) | sequence;
    }

    /**
     * 备份机器自增序列
     * @param timestamp timestamp 当前Unix时间戳
     * @return long
     */
    private static long nextIdBackup(long timestamp) {
        if (timestamp < lastTimestampBak) {
            if (lastTimestampBak - System.currentTimeMillis() / 1000 <= BACK_TIME_MAX) {
                timestamp = lastTimestampBak;
            } else {
                throw new RuntimeException(String.format("时钟回拨: now: [%d] last: [%d]", timestamp, lastTimestampBak));
            }
        }
        if (timestamp != lastTimestampBak) {
            lastTimestampBak = timestamp;
            sequenceBak = 0L;
        }
        if (0L == (++sequenceBak & SEQUENCE_MAX)) {
            // 秒内序列用尽
            return nextIdBackup(timestamp + 1);
        }
        return ((timestamp - OFFSET) << OFFSET_SHIFT_BITS) | ((WORKER_ID ^ BACK_WORKER_ID_BEGIN) << WORKER_SHIFT_BITS) | sequenceBak;
    }

    public static int[] toCodePoints(CharSequence str) {
        if (str == null) {
            return null;
        } else if (str.length() == 0) {
            return EMPTY_INT_ARRAY;
        } else {
            String s = str.toString();
            int[] result = new int[s.codePointCount(0, s.length())];
            int index = 0;

            for(int i = 0; i < result.length; ++i) {
                result[i] = s.codePointAt(index);
                index += Character.charCount(result[i]);
            }

            return result;
        }
    }

    public static long nextLong(long startInclusive, long endExclusive) {
        isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        isTrue(startInclusive >= 0L, "Both range values must be non-negative.");
        return startInclusive == endExclusive ? startInclusive : (long)nextDouble((double)startInclusive, (double)endExclusive);
    }

    public static double nextDouble(double startInclusive, double endExclusive) {
        isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.");
        isTrue(startInclusive >= 0.0D, "Both range values must be non-negative.");
        return startInclusive == endExclusive ? startInclusive : startInclusive + (endExclusive - startInclusive) * ThreadLocalRandom.current().nextDouble();
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

}
