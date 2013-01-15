package net.chrisrichardson.asyncpojos.actoids.core;


public interface ActoidContext {
  <T> T self(Class<T> type);

  <T> T makeActoid(Class<T> type);

  <T> T actoidFor(Object actoidImplementation, Class<T> actoidInterface);

  <T> T findActoid(String name, Class<T> type);
}
