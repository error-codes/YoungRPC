package io.young.rpc.common.threadpool;

import io.young.rpc.common.util.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author YoungCR
 * @date 2024/12/24 17:34
 * @descritpion ServerThreadPool 服务端线程池
 */
public class ServerThreadPool {

    private static final ThreadPoolExecutor SERVER_THREAD_POOL;
    private static final int CORE_POOL_SIZE = 16;
    private static final int MAX_POOL_SIZE = 16;
    private static final int KEEP_ALIVE_TIME = 600;
    private static final int BLOCKING_QUEUE_CAPACITY = 65536;

    static {
        ThreadFactory serverThreadFactory = new ThreadFactoryBuilder().setNamePrefix("young-server-").build();
        SERVER_THREAD_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY), serverThreadFactory);
    }

    public static void submit(Runnable task) {
        SERVER_THREAD_POOL.submit(task);
    }

    public static void shutdown() {
        SERVER_THREAD_POOL.shutdown();
    }
}