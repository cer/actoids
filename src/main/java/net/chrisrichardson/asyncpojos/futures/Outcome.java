package net.chrisrichardson.asyncpojos.futures;

public class Outcome<T> {
  public Throwable failure;
  public T success;

  public boolean isSuccessful() {
    return failure == null;
  }

  static Outcome<?> makeFailedOutcome(Throwable e) {
    Outcome<?> o = new Outcome<Object>();
    o.failure = e;
    return o;
  }

  static <T> Outcome<T> makeSuccessfullOutcome(T result) {
    Outcome<T> o = new Outcome<T>();
    o.success = result;
    return o;
  }
}