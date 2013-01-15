package net.chrisrichardson.asyncpojos.futures;

import org.springframework.core.task.TaskExecutor;

public interface Promise {
  void supplyResult(Object result, TaskExecutor executorService);
  void supplyThrowable(Throwable e, TaskExecutor executionService);
}
