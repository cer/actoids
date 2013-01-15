package net.chrisrichardson.asyncpojos.actoids.core;

import net.chrisrichardson.asyncpojos.futures.Promise;

import org.springframework.util.Assert;

/**

 */
public class ActoidRequest {
    public final String  methodName;
    public final Object[] params;
    public final Promise promise;

    public ActoidRequest(String methodName, Object[] params, Promise promise) {
        this.methodName = methodName;
        this.params = params;
        this.promise = promise;
        Assert.notNull(methodName);
        Assert.notNull(promise);

    }

    boolean hasSingleParameter() {
        return params != null && params.length == 1;
    }

    public boolean hasSingleParameterOfType(Class<?> type) {
      return hasSingleParameter() && type.isInstance(params[0]);
    }
}
