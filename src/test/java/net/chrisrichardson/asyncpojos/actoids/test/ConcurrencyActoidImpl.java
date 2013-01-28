package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import net.chrisrichardson.asyncpojos.actoids.stereotypes.Actoid;
import net.chrisrichardson.asyncpojos.futures.FutureUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

@Actoid
@Scope("prototype")
public class ConcurrencyActoidImpl implements ConcurrencyActoid {

  private AtomicBoolean inside = new AtomicBoolean();
  private int counter = 1;
  
  @Override
  public Future<Integer> serialAccess() {
    Assert.isTrue(inside.compareAndSet(false, true));
    try {
      TimeUnit.MILLISECONDS.sleep(1);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    Assert.isTrue(inside.compareAndSet(true, false));
    return FutureUtils.complete(counter++);
  }
}
