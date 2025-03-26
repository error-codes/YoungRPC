package io.young.rpc.common.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author YoungCR
 * @date 2025/3/24 15:22
 * @descritpion ThreadFactoryBuilder 线程工厂构建器
 */
public class ThreadFactoryBuilder {

    private boolean daemon;
    private int priority;
    private String poolName;
    private ThreadGroup threadGroup;
    private String namePrefix;

    public ThreadFactoryBuilder() {
        daemon = false;
        priority = Thread.NORM_PRIORITY;
        poolName = "default_pool";
        threadGroup = Thread.currentThread().getThreadGroup();
    }

    public boolean isDaemon() {
        return daemon;
    }

    public ThreadFactoryBuilder setDaemon(boolean daemon) {
        this.daemon = daemon;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public ThreadFactoryBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public String getPoolName() {
        return poolName;
    }

    public ThreadFactoryBuilder setPoolName(String poolName) {
        this.poolName = poolName;
        return this;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public ThreadFactoryBuilder setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
        return this;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public ThreadFactoryBuilder setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
        return this;
    }

    public ThreadFactory build() {
        namePrefix = poolName + "_";
        AtomicLong nextId = new AtomicLong(1);
        return r -> {
            Thread thread = new Thread(threadGroup, r, namePrefix + nextId.getAndIncrement());
            thread.setDaemon(daemon);
            thread.setPriority(priority);
            return thread;
        };
    }
}
