package net.chrisrichardson.asyncpojos.actoids.core;

import java.lang.reflect.Method;

import net.chrisrichardson.asyncpojos.utils.MethodUtils;

import org.springframework.util.Assert;

public class ActoidRequestExecutor {
  private ActoidContextThreadLocalHolder actoidContextThreadLocalHolder;
  private ActoidSystem actoidSystem;
  private Object self;
  private Object actoidImplementation;

  public void setSelf(Object self) {
    Assert.notNull(self);
    this.self = self;
  }
  
  public ActoidRequestExecutor(Object actoidImplementation, ActoidSystem actoidSystem) {
    this.actoidImplementation = actoidImplementation;
    this.actoidContextThreadLocalHolder = actoidSystem.getActoidContextThreadLocalHolder();
    this.actoidSystem = actoidSystem;
  }


  void execute(ActoidRequest ar) {
    Object[] params = ar.params;
    Method targetMethod = MethodUtils.findMethodMaybe(actoidImplementation, ar.methodName, params);
    if (targetMethod == null) {
      if (actoidImplementation instanceof ActoidRequestHandler) {
        params = new Object[]{ar};
        targetMethod = MethodUtils.findMethod(actoidImplementation, ActoidRequestHandler.class.getMethods()[0].getName(), params);
      } else if ("invoke".equals(ar.methodName) && ar.hasSingleParameterOfType(ActoidRequest.class)) {
        ActoidRequest ar2 = (ActoidRequest)ar.params[0];
        params = ar2.params;
        targetMethod = MethodUtils.findMethod(actoidImplementation, ar2.methodName, params);
      } else
        throw new RuntimeException("Method not found exception: " + ar.methodName);
    }
    try {
      ActoidContext actoidContext = new ActoidContextImpl(self, actoidSystem);
      actoidContextThreadLocalHolder.push(actoidContext);
      Assert.notNull(ar.promise);
      ar.promise.supplyResult(targetMethod.invoke(actoidImplementation, params), actoidSystem.getExecutorService());
    } catch (Throwable e) {
      e.printStackTrace();
      ar.promise.supplyThrowable(e, actoidSystem.getExecutorService());
    } finally {
      actoidContextThreadLocalHolder.pop();
    }
  }
  
}