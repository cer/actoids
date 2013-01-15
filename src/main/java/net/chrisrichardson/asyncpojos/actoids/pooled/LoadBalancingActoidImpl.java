package net.chrisrichardson.asyncpojos.actoids.pooled;

import java.util.List;

import net.chrisrichardson.asyncpojos.actoids.core.ActoidRef;
import net.chrisrichardson.asyncpojos.actoids.core.ActoidRequest;

public class LoadBalancingActoidImpl implements LoadBalancingActoid {

  private final List<ActoidRef> pool;
  private int nextTarget;
  
  public LoadBalancingActoidImpl(List<ActoidRef> pool) {
    this.pool = pool;
  }

  @Override
  public Object handleActoidRequest(ActoidRequest ar) {
    nextTarget = (nextTarget + 1) % pool.size();
    ActoidRef target = pool.get(nextTarget);
    return target.invoke(ar);
  }

}
