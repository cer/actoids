package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.Future;

public interface SampleActoid {
    Future<String> sayHello();
    void goodbye();
}
