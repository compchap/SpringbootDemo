package org.example.ws.util;

import java.util.concurrent.*;

/**
 * Created by ng88763 on 2/15/2016.
 */
public class AsyncResponse<V> implements Future<V>{

    private V value;
    private Exception executionException;
    private boolean isCompleteExceptionally = false;
    private boolean isCancelled = false;
    private boolean isDone = false;
    private long checkCompletedInterval = 100;

    public AsyncResponse() {
    }

    public AsyncResponse(V val){
        this.value = val;
        this.isDone = true;
    }

    public AsyncResponse(Throwable ex){
        this.executionException = new ExecutionException(ex);
        this.isCompleteExceptionally = true;
        this.isDone = true;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        this.isCancelled = true;
        this.isDone = true;

        return false;
    }

    @Override
    public boolean isCancelled(){
        return this.isCancelled;
    }

    @Override
    public boolean isDone() {
        return this.isDone;
    }

    public boolean isCompleteExceptionally(){
        return this.isCompleteExceptionally;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {

        block(0);

        if(isCancelled){
            throw new CancellationException();
        }
        if(isCompleteExceptionally){
            throw new ExecutionException(this.executionException);
        }
        if(isDone){
            return this.value;
        }

        throw new InterruptedException();
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {

        long timeoutInMillis = unit.toMillis(timeout);
        block(timeoutInMillis);

        if(isCancelled){
            throw new CancellationException();
        }
        if(isCompleteExceptionally){
            throw new ExecutionException(this.executionException);
        }
        if(isDone){
            return this.value;
        }

        throw new InterruptedException();
    }

    public boolean complete(V val){
        this.value = val;
        this.isDone = true;

        return true;
    }

    public boolean completeExceptionally(Throwable ex){
        this.value = null;
        this.executionException = new ExecutionException(ex);
        this.isCompleteExceptionally = true;
        this.isDone = true;

        return true;
    }

    public void setCheckCompletedInterval(long millis){
        this.checkCompletedInterval =millis;
    }

    private void block(long timeout) throws InterruptedException {
        long start = System.currentTimeMillis();

//        block until done, cancelled, or the timeout is Exceeded
        while(!isDone() && !isCancelled()){
            if(timeout > 0){
                long now = System.currentTimeMillis();
                if(now > start + timeout){
                    break;
                }
            }
            Thread.sleep(checkCompletedInterval);
        }
    }
}