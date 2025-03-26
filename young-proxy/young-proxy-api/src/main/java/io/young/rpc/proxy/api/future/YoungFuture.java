package io.young.rpc.proxy.api.future;

import io.young.rpc.common.exception.ExceptionFactory;
import io.young.rpc.common.exception.YoungException;
import io.young.rpc.common.threadpool.ClientThreadPool;
import io.young.rpc.common.util.RespUtils;
import io.young.rpc.protocol.YoungProtocol;
import io.young.rpc.protocol.request.YoungRequest;
import io.young.rpc.protocol.response.YoungResponse;
import io.young.rpc.proxy.api.callback.AsyncRpcCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author YoungCR
 * @date 2025/2/6 15:48
 * @descritpion YoungFuture 异步调用的 Future
 */
public class YoungFuture extends CompletableFuture<Object> {

    private static final Logger logger = LoggerFactory.getLogger(YoungFuture.class);

    private final Sync sync;
    private final YoungProtocol<YoungRequest> requestYoungProtocol;
    private YoungProtocol<YoungResponse> responseYoungProtocol;
    private final long startTime;
    private final List<AsyncRpcCallback> pendingCallbacks = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final long responseTimeThreshold = 5000;

    public YoungFuture(YoungProtocol<YoungRequest> requestYoungProtocol) {
        this.sync = new Sync();
        this.requestYoungProtocol = requestYoungProtocol;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() {
        sync.acquire(-1);
        if (this.responseYoungProtocol != null) {
            return this.responseYoungProtocol.getBody().getResult();
        } else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.responseYoungProtocol != null) {
                return this.responseYoungProtocol.getBody().getResult();
            } else {
                return null;
            }
        } else {
            throw ExceptionFactory.createException(YoungException.class,
                    RespUtils.getContent("Timeout.0", this.requestYoungProtocol.getHeader().getRequestId(), this.requestYoungProtocol.getBody().getClassName(), this.requestYoungProtocol.getBody().getMethodName()));
        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void done(YoungProtocol<YoungResponse> responseYoungProtocol) {
        this.responseYoungProtocol = responseYoungProtocol;
        sync.release(1);
        invokeCallbacks();
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            logger.warn(RespUtils.getContent("Timeout.1", responseYoungProtocol.getHeader().getRequestId(), responseTime));
        }
    }


    private void runCallback(final AsyncRpcCallback callback) {
        YoungResponse res = responseYoungProtocol.getBody();
        ClientThreadPool.submit(() -> {
            if (!res.isError()) {
                callback.onSuccess(res.getResult());
            } else {
                callback.onException(ExceptionFactory.createException(YoungException.class, RespUtils.getContent("Response.0", res.getError())));
            }
        });
    }

    public YoungFuture addCallback(AsyncRpcCallback callback) {
        lock.lock();
        try {
            if (isDone()) {
                runCallback(callback);
            } else {
                this.pendingCallbacks.add(callback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    private void invokeCallbacks() {
        lock.lock();

        try {
            for (AsyncRpcCallback callback : pendingCallbacks) {
                runCallback(callback);
            }
        } finally {
            lock.unlock();
        }
    }

    static class Sync extends AbstractQueuedSynchronizer implements Serializable {

        @Serial
        private static final long serialVersionUID = -580658180060863191L;

        private final int done = 1;
        private final int pending = 0;

        @Override
        protected boolean tryAcquire(int acquire) {
            return getState() == done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == pending) {
                return compareAndSetState(pending, done);
            }
            return false;
        }

        public boolean isDone() {
            return getState() == done;
        }
    }
}
