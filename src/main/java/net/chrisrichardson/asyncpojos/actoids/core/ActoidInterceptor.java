package net.chrisrichardson.asyncpojos.actoids.core;

import java.lang.reflect.Method;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;

import net.chrisrichardson.asyncpojos.futures.EnhancedFutureImpl;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.task.AsyncTaskExecutor;

public class ActoidInterceptor implements MethodInterceptor {

  private final AsyncTaskExecutor executorService;
  private final Mailbox mailbox;
  private final ActoidRequestExecutor requestExecutor;
  
  public ActoidInterceptor(Object actoidImplementation, ActoidSystem actoidSystem) {
    this.executorService = actoidSystem.getExecutorService();
    this.requestExecutor = new ActoidRequestExecutor(actoidImplementation, actoidSystem);
    this.mailbox = new Mailbox(requestExecutor);
  }


  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (method.getName().equals("toString"))
      return "SomeProxy";
    EnhancedFutureImpl<?> future = new EnhancedFutureImpl<Object>();
    ActoidRequest request = new ActoidRequest(method.getName(), invocation.getArguments(), future);
    queueRequestAndSubmitIfNecessary(request);
    return future;
  }

  
  private void queueRequestAndSubmitIfNecessary(ActoidRequest request) {

    if (mailbox.queue(request)) {
      try {
        executorService.submit(mailbox);
      } catch (RejectedExecutionException e) {
        mailbox.setIdle();
        throw e;
      }
    } else {

    }
  }

  public void setSelf(Object self) {
    this.requestExecutor.setSelf(self);
  }


  public BlockingQueue<ActoidRequest> getMailboxQueue() {
    return mailbox.getQueue();
  }


  public void setMailboxQueue(BlockingQueue<ActoidRequest> sharedQueue) {
    mailbox.setQueue(sharedQueue);
  }
}
