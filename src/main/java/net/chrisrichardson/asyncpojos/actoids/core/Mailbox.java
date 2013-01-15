package net.chrisrichardson.asyncpojos.actoids.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Mailbox implements Runnable {

  private static final int IDLE = 0;
  private static final int RUNNING = 1;

  private AtomicInteger state = new AtomicInteger(IDLE);
  private BlockingQueue<ActoidRequest> queue = new LinkedBlockingQueue<ActoidRequest>();
  private ActoidRequestExecutor requestExecutor;

  public Mailbox(ActoidRequestExecutor requestExecutor) {
    this.requestExecutor = requestExecutor;
  }

  public  boolean queue(ActoidRequest request) {
    synchronized (queue) {
      queue.add(request);
      return shouldSubmit();
    }
  }

  private  ActoidRequest getNextRequest() {
    synchronized (queue) {
      ActoidRequest ar = queue.poll();
      if (ar == null)
        setIdle();
      return ar;
    }
  }

  @Override
  public void run() {
    boolean finishedNormally = false;
    try {
      while (true) {
        ActoidRequest ar = getNextRequest();
        if (ar == null) {
          finishedNormally = true;
          break;
        }
        requestExecutor.execute(ar);
      }
    } finally {
      if (!finishedNormally)
        setIdle();
    }
  }

  public void setIdle() {
    if (!state.compareAndSet(RUNNING, IDLE))
      throw new RuntimeException("wrong state: " + state.get());
  }

  public boolean shouldSubmit() {
    return state.compareAndSet(IDLE, RUNNING);
  }

  public BlockingQueue<ActoidRequest> getQueue() {
    return queue;
  }

  public void setQueue(BlockingQueue<ActoidRequest> sharedQueue) {
    this.queue = sharedQueue;
  }

}
