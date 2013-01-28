package net.chrisrichardson.asyncpojos.futures;

import com.google.common.base.Function;

public class MappedEnhancedFuture<T, ArgumentType> extends EnhancedFutureImpl<T> {

  private final Function<ArgumentType, T> function;

  public MappedEnhancedFuture(Function<ArgumentType, T> function) {
    this.function = function;
  }

  @Override
  protected Object transformResult(Object result) {
    @SuppressWarnings("unchecked")
    ArgumentType arg = (ArgumentType) result;
    return function.apply(arg);
  };

}
