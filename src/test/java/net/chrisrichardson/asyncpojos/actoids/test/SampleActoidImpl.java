package net.chrisrichardson.asyncpojos.actoids.test;

import static net.chrisrichardson.asyncpojos.futures.FutureUtils.complete;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.chrisrichardson.asyncpojos.actoids.stereotypes.Actoid;

import org.springframework.context.annotation.Scope;

@Actoid
@Scope("prototype")
public class SampleActoidImpl implements SampleActoid {

  private int counter;
  
  @Override
  public Future<String> sayHello() {
    System.out.println("In sayHello");
    try {
      TimeUnit.MILLISECONDS.sleep(20);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return complete("Hello " + counter++);
  }

  @Override
  public void goodbye() {
    System.out.println("Bye " + counter++);
  }

}
