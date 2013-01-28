package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/appctx/actoids.xml")
public class ActoidTest {

  @Autowired
  private SampleActoid sampleActoid;

  @Autowired
  @Qualifier("pooledWorker")
  private Worker worker;

  @Autowired
  private Master master;

  @Test
  public void actoidShouldSayHello() throws Exception {
    Future<String> f1 = sampleActoid.sayHello();
    Future<String> f2 = sampleActoid.sayHello();

    long start = System.currentTimeMillis();
    String result1 = f1.get();
    String result2 = f2.get();
    long end = System.currentTimeMillis();
    System.out.println("waited for millis: " + (end - start));

    Assert.assertEquals("Hello 0", result1);
    Assert.assertEquals("Hello 1", result2);

    sampleActoid.goodbye();
  }

  @Test
  public void workerActorShouldWork() throws Exception {
    Assert.assertEquals(new Integer(0), worker.doSomeWork().get());
    Assert.assertEquals(new Integer(99), worker.return99().get());
  }

  @Test
  public void workerActorShouldHave10Instances() throws Exception {
    Assert.assertTrue(worker.getInstanceCount().get() >= 10);
  }

  @Test
  public void masterShouldDelegate() throws Exception {
    // FIXME - interesting: this can throw an exception but its void so the
    // exception is ignored!
    master.prepareToWork();
    Future<Integer> result = master.computeSomething();
    Integer i1 = result.get(2000, TimeUnit.MILLISECONDS);
    Assert.assertNotNull(i1);
  }

  @Test
  public void masterShouldForward() throws Exception {
    master.prepareToWork();
    Future<Integer> result = master.computeSomethingElse();
    Integer i1 = result.get(20000, TimeUnit.MILLISECONDS);
    Assert.assertNotNull(i1);
  }

  @Autowired
  private FactorialCalculator factorialActor;

  @Test
  public void factorialActorShouldCallSelf() throws Exception {
    Future<Integer> result = factorialActor.factorial(10);
    Assert.assertEquals(new Integer(3628800), result.get());
  }

  @Test
  public void workersShouldBeLoadBalanced() throws Exception {
    Set<Integer> instanceIds = new HashSet<Integer>();
    for (int i = 0; i < 10; i++)
      instanceIds.add(worker.getInstanceId().get());
    System.out.println("workerIds=" + instanceIds);
    Assert.assertEquals(10, instanceIds.size());
  }

  @Test
  public void workerShouldDoubleMe() throws Exception {
    Assert.assertEquals(new Integer(180), worker.doubleMe(90).get());
  }

}