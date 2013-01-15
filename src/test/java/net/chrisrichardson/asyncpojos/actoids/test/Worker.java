package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.Future;

public interface Worker {

    Future<Integer> doSomeWork();

    Future<Integer> return99();

    Future<Integer> getInstanceCount();
    
    Future<Integer> getInstanceId();

    Future<Integer> doubleMe(Integer i);
}
