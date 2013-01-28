package net.chrisrichardson.asyncpojos.futures;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Function;

public interface EnhancedFuture<T> extends Future<T> {

  void addCompletionCallback(CompletionCallback<T> completionCallback);
  void addSuccessCallback(SuccessCallback<T> successCallback);
  void addFailureCallback(FailureCallback failureCallback);
  EnhancedFuture<T> withTimeout(TimeUnit timeUnit, int amount);
  <ResultType> Future<ResultType> map(Function<T, ResultType> function);

}