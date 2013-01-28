package net.chrisrichardson.asyncpojos.actoids.core;

import java.lang.reflect.Method;

import net.chrisrichardson.asyncpojos.futures.EnhancedFutureImpl;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ActoidInterceptor implements MethodInterceptor {

  private final Actoid actoid;

  public ActoidInterceptor(Actoid actoid) {
    this.actoid = actoid;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (method.getName().equals("toString"))
      return "SomeProxy";
    EnhancedFutureImpl<?> future = new EnhancedFutureImpl<Object>();
    ActoidRequest request = new ActoidRequest(method.getName(), invocation.getArguments(), future);
    actoid.handle(request);
    return future;
  }

  public Actoid getActoid() {
    return actoid;
  }
}
