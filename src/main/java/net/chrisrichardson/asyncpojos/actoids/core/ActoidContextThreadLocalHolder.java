package net.chrisrichardson.asyncpojos.actoids.core;

public class ActoidContextThreadLocalHolder implements ActoidContext {

    private ThreadLocal<ActoidContext> holder = new ThreadLocal<ActoidContext>();

    public void push(ActoidContext actoidContext) {
        holder.set(actoidContext);
    }

    public void pop() {
        holder.set(null);
    }

    @Override
    public <T> T self(Class<T> type) {
      return holder.get().self(type);
    }

    @Override
    public <T> T makeActoid(Class<T> type) {
      return holder.get().makeActoid(type);
    }

    @Override
    public <T> T actoidFor(Object actoidImplementation, Class<T> actoidInterface) {
      return holder.get().actoidFor(actoidImplementation, actoidInterface);
    }

    @Override
    public <T> T findActoid(String name, Class<T> type) {
      return holder.get().findActoid(name, type);
    }
}
