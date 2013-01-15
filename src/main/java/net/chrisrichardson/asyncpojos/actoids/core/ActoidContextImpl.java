package net.chrisrichardson.asyncpojos.actoids.core;


import org.springframework.util.Assert;

public class ActoidContextImpl implements ActoidContext {
  private Object self;
  private ActoidSystem actoidSystem;

  public ActoidContextImpl(Object self, ActoidSystem actoidSystem) {
    this.actoidSystem = actoidSystem;
    Assert.notNull(self);
    this.self = self;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T self(Class<T> type) {
    return (T) self;
  }

  @Override
  public <T> T makeActoid(Class<T> type) {
    return actoidSystem.makeChildActoid(type);
  }

  @Override
  public <T> T actoidFor(Object actoidImplementation, Class<T> actoidInterface) {
    return actoidSystem.makeChildActoid(actoidImplementation, actoidInterface);
  }

  @Override
  public <T> T findActoid(String name, Class<T> type) {
    return actoidSystem.findActoid(name, type);
  }
}
