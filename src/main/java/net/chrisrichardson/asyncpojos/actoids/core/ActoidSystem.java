package net.chrisrichardson.asyncpojos.actoids.core;


import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.task.AsyncTaskExecutor;


public class ActoidSystem implements ApplicationContextAware {

  private AsyncTaskExecutor executorService;
  private ActoidContextThreadLocalHolder actoidContextThreadLocalHolder;
  private ApplicationContext ctx;

  
  public void setExecutorService(AsyncTaskExecutor executorService) {
    this.executorService = executorService;
  }


  public void setActoidContextThreadLocalHolder(ActoidContextThreadLocalHolder actoidContextThreadLocalHolder) {
    this.actoidContextThreadLocalHolder = actoidContextThreadLocalHolder;
  }
  
  public AsyncTaskExecutor getExecutorService() {
    return executorService;
  }
  
  public ActoidContextThreadLocalHolder getActoidContextThreadLocalHolder() {
    return actoidContextThreadLocalHolder;
  }
  
  public ApplicationContext getApplicationContext() {
    return ctx;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.ctx = applicationContext;
  }

  public <T> T makeActoid(Object actoidImplementation, Class<T> actoidInterface) {
      return makeActoid(new Class[]{actoidInterface}, actoidImplementation);
  }

  public <T> T makeChildActoid(Class<T> type) {
    return ctx.getBean(type);
  }

  public <T> T makeChildActoid(Object actoidImplementation, Class<T> actoidInterface) {
    ((AbstractApplicationContext) ctx).getBeanFactory().autowireBeanProperties(actoidImplementation, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
    return makeActoid(new Class[]{actoidInterface}, actoidImplementation);
  }
  
  public <T> T findActoid(String name, Class<T> type) {
    return ctx.getBean(name, type);
  }  

  public <T> T makeActoid(Object ai) {
    ProxyFactory pf = new ProxyFactory(ai);
    pf.addInterface(ActoidRef.class);
    return makeActoidProxy(ai, pf);
  }


  private <T> T makeActoidProxy(Object actoidImplementation, ProxyFactory pf) {
    ActoidInterceptor handler = new ActoidInterceptor(actoidImplementation, this);
    pf.addAdvice(handler);
    Advised proxy = (Advised) pf.getProxy();
    handler.setSelf(proxy);
    @SuppressWarnings("unchecked")T r = (T)proxy;
    return r;
  }

  public <T> T makeActoid(Class<?>[] targetInterfaces, Object actoidImplementation) {
    ProxyFactory pf = new ProxyFactory(targetInterfaces);
    return makeActoidProxy(actoidImplementation, pf);
  }
  
}
