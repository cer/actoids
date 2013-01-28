package net.chrisrichardson.asyncpojos.actoids.test;

import java.util.concurrent.Future;

public interface ConcurrencyActoid {
  Future<Integer> serialAccess();

}
