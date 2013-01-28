package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/appctx/actoids.xml")
public class ConcurrencyActoidTest {

  private final class ConcurrencyActoidInvoker implements Runnable {
    private final int numberOfThreads;
    private final CountDownLatch startLatch;
    private final AtomicLong invocationCounter;
    private final CountDownLatch endLatch;
    private final BlockingQueue<Throwable> exceptions;
    private final int numberOfIterations;

    private ConcurrencyActoidInvoker(int numberOfThreads, CountDownLatch startLatch, AtomicLong invocationCounter,
        CountDownLatch endLatch, BlockingQueue<Throwable> exceptions, int numberOfIterations) {
      this.numberOfThreads = numberOfThreads;
      this.startLatch = startLatch;
      this.invocationCounter = invocationCounter;
      this.endLatch = endLatch;
      this.exceptions = exceptions;
      this.numberOfIterations = numberOfIterations;
    }

    @Override
    public void run() {
      try {
        try {
          startLatch.await();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        try {
          for (int i = 0; i < numberOfIterations; i++)
            invocationCounter.addAndGet(concurrencyActoid.serialAccess().get(numberOfThreads * 2, TimeUnit.MILLISECONDS));
        } catch (Throwable t) {
          exceptions.add(t);
        }
      } finally {
        endLatch.countDown();
      }
    }
  }

  @Autowired
  private ConcurrencyActoid concurrencyActoid;

  @Test
  public void requestsAreExecutedSerially() throws Exception {
    final int numberOfThreads = 10;
    final int numberOfIterations = 100;
    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    final CountDownLatch startLatch = new CountDownLatch(1);
    final CountDownLatch endLatch = new CountDownLatch(numberOfThreads);
    final BlockingQueue<Throwable> exceptions = new LinkedBlockingQueue<Throwable>();
    final AtomicLong invocationCounter = new AtomicLong();
    
    for (int i = 0; i < numberOfThreads; i++) {
      executorService.execute(new ConcurrencyActoidInvoker(numberOfThreads, startLatch, invocationCounter, endLatch, exceptions, numberOfIterations));
    }
    startLatch.countDown();
    endLatch.await();
    if (!exceptions.isEmpty())
      Assert.fail("Should be empty: " + exceptions);
    Assert.assertEquals(expectedCount(numberOfIterations, numberOfThreads), invocationCounter.get());
  }

  private long expectedCount(long numberOfIterations, long numberOfThreads) {
    long n = numberOfIterations * numberOfThreads;
    return n * (n + 1) / 2;
  }
}
