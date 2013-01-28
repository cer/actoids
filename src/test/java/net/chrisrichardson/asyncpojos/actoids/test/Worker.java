package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.Future;

import net.chrisrichardson.asyncpojos.futures.EnhancedFuture;

public interface Worker {

    EnhancedFuture<Integer> doSomeWork();

    Future<Integer> return99();

    Future<Integer> getInstanceCount();
    
    Future<Integer> getInstanceId();

    Future<Integer> doubleMe(Integer i);
}
