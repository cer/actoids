package net.chrisrichardson.asyncpojos.futures;

import java.util.concurrent.Future;

public interface EnhancedFuture<T> extends Future<T> {

  void addCompletionCallback(CompletionCallback<T> completionCallback);
  void addSuccessCallback(SuccessCallback<T> successCallback);
  void addFailureCallback(FailureCallback failureCallback);

}