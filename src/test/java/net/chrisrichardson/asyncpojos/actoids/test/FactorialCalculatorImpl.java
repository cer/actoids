package net.chrisrichardson.asyncpojos.actoids.test;

import static net.chrisrichardson.asyncpojos.futures.FutureUtils.complete;

import java.util.concurrent.Future;

import net.chrisrichardson.asyncpojos.actoids.core.ActoidContext;
import net.chrisrichardson.asyncpojos.actoids.stereotypes.Actoid;

import org.springframework.beans.factory.annotation.Autowired;

@Actoid
public class FactorialCalculatorImpl implements FactorialCalculator {

  @Autowired
  private ActoidContext actoidContext;
  
  @Override
  public Future<Integer> factorial(int n) {
    return actoidContext.self(FactorialCalculator.class).factorial(n, 1);
  }
  
  @Override
  public Future<Integer> factorial(int n, int result) {
    if (n < 1)
      return complete(result);
    else
      return actoidContext.self(FactorialCalculator.class).factorial(n - 1, result * n);
  }

}
