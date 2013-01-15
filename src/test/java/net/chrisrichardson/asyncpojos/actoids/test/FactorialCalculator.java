package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.Future;

public interface FactorialCalculator {

    Future<Integer> factorial(int n);

    Future<Integer> factorial(int n, int result);
}
