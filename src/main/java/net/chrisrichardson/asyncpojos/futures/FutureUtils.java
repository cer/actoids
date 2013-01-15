package net.chrisrichardson.asyncpojos.futures;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.core.task.TaskExecutor;

public class FutureUtils {

  public static <T> EnhancedFuture<T> complete(T value) {
    return complete(value, null);
  }
  
  public static <T> EnhancedFuture<T> complete(T value, TaskExecutor executorService) {
    EnhancedFutureImpl<T> f = new EnhancedFutureImpl<T>();
    f.supplyResult(value, executorService);
    return f;
  }

  public static <T> EnhancedFuture<T> fail(Throwable e) {
    EnhancedFutureImpl<T> f = new EnhancedFutureImpl<T>();
    f.supplyThrowable(e, null);
    return f;
  }

  public static void await(TimeUnit unit, int timeout, Future<?>... futures) {
    DateTime deadline = DateTime.now().plusMillis((int) unit.toMillis(timeout));
    for (Future<?> future : futures) {
      try {
        // Need to adjust the timeout as we iterate
        Interval i = new Interval(DateTime.now(), deadline);
        long m = Math.max(0, i.toDurationMillis());
        future.get(m, TimeUnit.MILLISECONDS);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static <T> T get(Future<T> future) {
    try {
      return future.get();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  

}
