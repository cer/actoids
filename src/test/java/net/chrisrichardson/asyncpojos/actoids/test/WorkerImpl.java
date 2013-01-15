package net.chrisrichardson.asyncpojos.actoids.test;

import static net.chrisrichardson.asyncpojos.futures.FutureUtils.complete;

import java.util.concurrent.Future;

import net.chrisrichardson.asyncpojos.actoids.stereotypes.Actoid;

import org.springframework.context.annotation.Scope;

@Actoid
@Scope("prototype")
public class WorkerImpl implements Worker {

    private int counter;
    private static int instanceCount;
    private int instanceId;
    
    public WorkerImpl() {
      this.instanceId = instanceCount++;
    }
    
    @Override
    public Future<Integer> getInstanceCount() {
      return complete(instanceCount);
    }
    
    @Override
    public Future<Integer> doSomeWork() {
        System.out.println("Worker - " + instanceId + " is working: " + counter);
        return complete(counter++);
    }
    public Future<Integer> return99() {
        System.out.println("Worker - " + instanceId + " is working: " + counter);
        return complete(99);
    }

    @Override
    public Future<Integer> getInstanceId() {
      return complete(instanceId);
    }

    @Override
    public Future<Integer> doubleMe(Integer i) {
      return complete(i * 2);
    }
}
