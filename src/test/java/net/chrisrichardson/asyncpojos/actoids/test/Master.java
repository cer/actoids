package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public interface Master {

    void prepareToWork();
    Future<Integer> computeSomething() throws InterruptedException, ExecutionException, TimeoutException;
    Future<Integer> computeSomethingElse();
    
}
