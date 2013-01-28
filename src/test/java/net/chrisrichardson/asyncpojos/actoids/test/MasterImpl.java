package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.chrisrichardson.asyncpojos.actoids.core.ActoidContext;
import net.chrisrichardson.asyncpojos.actoids.stereotypes.Actoid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.common.base.Function;

@Actoid
public class MasterImpl implements Master {

  @Autowired
  private ActoidContext actoidContext;

  private Worker worker;

  public void prepareToWork() {
    System.out.println("Preparing: " + this);
    worker = actoidContext.findActoid("pooledWorker", Worker.class);
  }

  public Future<Integer> computeSomething() throws InterruptedException, ExecutionException, TimeoutException {
    Assert.notNull(worker);
    return worker.doSomeWork().withTimeout(TimeUnit.MILLISECONDS, 10).map(new Function<Integer, Integer>() {

      @Override
      public Integer apply(Integer input) {
        return 2 + input;
      }});
  }

  public Future<Integer> computeSomethingElse() {
    Assert.notNull(worker);
    return worker.doSomeWork();
  }

}
