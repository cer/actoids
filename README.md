Actoids
=======

Actoids implements a Actor-like concurrency model on top of the Spring framework. The design is inspired by Spring's @Async annotation, which indicates that a component's methods should be called asynchronously. Actoids provides the @Actoid annotation, which indicates that a component should be invoked asynchronously like @Async but with one major difference: requests are queued and executed one at time. This solves the concurrency problem (multiple threads simultaneously accessing mutable state) by ensuring that only a single thread as access to an actor's mutable state. Let's look at an example.

Simple example
--------------

Here is a example actoid. It consists of an interface that defines the public API and implementation class. Note that the implementation has the @Actoid annotation.

	public interface SampleActoid {
	    Future<String> sayHello();
	    void goodbye();
	}

	@Actoid
	@Scope("prototype")
	public class SampleActoidImpl implements SampleActoid {

	  private int counter;

	  public Future<String> sayHello() {
	    System.out.println("In sayHello");
	    try {
	      TimeUnit.MILLISECONDS.sleep(20);
	    } catch (InterruptedException e) {
	      throw new RuntimeException(e);
	    }
	    return FutureUtils.complete("Hello " + counter++);
	  }

	  public void goodbye() {
	    System.out.println("Bye " + counter++);
	  }
	}

Actor-like semantics ensures single-threaded execution within each instance of this class. No need to use synchronize, locks or volatile to ensure thread safe access to the 'counter' variable. The sayHello() method sleeps for 20 msec and then returns a future containing the current count.

Here is an example test that invokes the SampleActoid:

	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration("classpath:/appctx/actoids.xml")
	public class ActoidTest {

	  @Autowired
	  private SampleActoid sampleActor;
	
	  @Test
	  public void actoidShouldSayHello() throws Exception {
	    Future<String> f1 = sampleActor.sayHello();
	    Future<String> f2 = sampleActor.sayHello();
	    ...
	  }

To make this work you need a simple application context XML file:

    <context:component-scan base-package="net.chrisrichardson.asyncpojos.actoids.test"/>

    <actoid:config default-executor="executor"/>

    <task:executor id="executor" pool-size="5"/>

The @Actoid annotation is annotated with @Component so it is picked up by <context:component-scan/>. Actoids provides an <actoid:*/> namespace. You must configure the Actor system using <actoid:config/>. It's main purpose is to specify which <task:executor/> to use. It also configures a ActoidBeanPostProcessor that detects beans with @Actoid annotations and wraps them with a proxy that executes them asynchronously.

Pooled actoids
--------------

Actoids can be pooled with requests load balanced across them. You do this using the PooledActoidFactory. Here is an example that creates a pool of 10 workers.

	public interface Worker { ... }

	@Actoid
	@Scope("prototype")
	public class WorkerImpl implements Worker {
 		...
 	}

	@Configuration
	public class MyActoidConfiguration {

	    @Bean
	    public Worker pooledWorker(ActoidSystem actoidSystem) {
	      PooledActoidFactory<Worker> paf = new PooledActoidFactory<Worker>(Worker.class, actoidSystem, "workerImpl", 10);
	      return paf.make();
	    }
	}

Note (1) It's essential that WorkerImpl has prototype scope (2) The pooled Actor and the workers both implement Worker.class so you must use @Qualifier to inject the pool:

	@Autowired
	@Qualifier("pooledWorker")
	private Worker worker;

Enhanced Futures
----------------

Actoids provides an enhanced Future implementation that's inspired by Scala Futures. This implementation provides completion callbacks.

	public interface EnhancedFuture<T> extends Future<T> {

	  void addCompletionCallback(CompletionCallback<T> completionCallback);
	  void addSuccessCallback(SuccessCallback<T> successCallback);
	  void addFailureCallback(FailureCallback failureCallback);

	}
