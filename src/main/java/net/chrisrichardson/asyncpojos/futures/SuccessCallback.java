package net.chrisrichardson.asyncpojos.futures;

public interface SuccessCallback<T> {

  public void onSuccess(T value);
}
