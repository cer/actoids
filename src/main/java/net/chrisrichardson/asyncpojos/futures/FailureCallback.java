package net.chrisrichardson.asyncpojos.futures;

public interface FailureCallback {
  void onFailure(Throwable t);
}
