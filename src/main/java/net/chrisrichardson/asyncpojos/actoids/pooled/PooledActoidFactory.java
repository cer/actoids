package net.chrisrichardson.asyncpojos.actoids.pooled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import net.chrisrichardson.asyncpojos.actoids.core.ActoidInterceptor;
import net.chrisrichardson.asyncpojos.actoids.core.ActoidRef;
import net.chrisrichardson.asyncpojos.actoids.core.ActoidRequest;
import net.chrisrichardson.asyncpojos.actoids.core.ActoidSystem;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.Advised;

public class PooledActoidFactory<T> {

  private final String actoidName;
  private final int poolSize;
  private final Class<T> targetInterface;
  private final ActoidSystem actoidSystem;

  public PooledActoidFactory(Class<T> targetInterface, ActoidSystem actoidSystem, String actoidName, int poolSize) {
    this.targetInterface = targetInterface;
    this.actoidSystem = actoidSystem;
    this.actoidName = actoidName;
    this.poolSize = poolSize;
  }

  public T make() {
    List<ActoidRef> pool = makePool();
    LoadBalancingActoidImpl ai = new LoadBalancingActoidImpl(pool);
    return actoidSystem.makeActoid(new Class[] { targetInterface }, ai);
  }

  private List<ActoidRef> makePool() {
    List<ActoidRef> pool = makeActoids();
    configuredSharedMailboxQueue(pool);
    return pool;
  }

  private List<ActoidRef> makeActoids() {
    List<ActoidRef> pool = new ArrayList<ActoidRef>(poolSize);
    for (int i = 0; i < poolSize; i++) {
      ActoidRef actoid = actoidSystem.getApplicationContext().getBean(actoidName, ActoidRef.class);
      pool.add(actoid);
    }
    return pool;
  }

  private void configuredSharedMailboxQueue(List<ActoidRef> pool) {
    BlockingQueue<ActoidRequest> sharedQueue = null;
    for (int i = 0; i < poolSize; i++) {
      ActoidRef actoid = pool.get(i);
      ActoidInterceptor interceptor = findInterceptor(actoid);
      if (i == 0)
        sharedQueue = interceptor.getMailboxQueue();
      else
        interceptor.setMailboxQueue(sharedQueue);
    }
  }

  private ActoidInterceptor findInterceptor(ActoidRef actoid) {
    Advised advised = (Advised) actoid;
    for (Advisor advisor : advised.getAdvisors()) {
      if (advisor.getAdvice() instanceof ActoidInterceptor)
        return (ActoidInterceptor) advisor.getAdvice();
    }
    throw new RuntimeException("ActoidInterceptor not found");
  }
}
