package net.chrisrichardson.asyncpojos.utils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodUtils {

  public static Method findMethod(Object targetObject, String methodName, Object[] args) {
    Method m = findMethodMaybe(targetObject, methodName, args);
    if (m == null)
      throw new RuntimeException("method not found: " + methodName);
    return m;
  }

  public static Method findMethodMaybe(Object targetObject, String methodName, Object[] args) {
    try {
      Class<?> searchType = targetObject.getClass();
      do {
        Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
        for (Method method : methods) {
          if (!methodName.equals(method.getName()))
            continue;
          if (args == null && method.getParameterTypes().length == 0)
            return method;
          if (method.getParameterTypes().length != args.length)
            continue;
          boolean match = true;
          for (int i = 0; i < args.length && match; i++) {
              match = isInstanceOfType((Class<?>) method.getParameterTypes()[i], args[i]);
          }
          if (match)
            return method;
        }
        searchType = searchType.getSuperclass();
      } while (searchType != Object.class && searchType != null);
      return null;
    } catch (Throwable e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private static Class<?>[] primitiveTypeMapping = new Class<?>[]{
    Boolean.TYPE, Boolean.class, 
    Character.TYPE, Character.class,
    Byte.TYPE, Byte.class,
    Short.TYPE, Short.class,
    Integer.TYPE, Integer.class,
    Long.TYPE, Long.class, 
    Float.TYPE, Float.class,
    Double.TYPE, Double.class,
    Void.TYPE, Void.class};
  
  private static Map<Class<?>, Class<?>> primitiveTypeMap = new ConcurrentHashMap<Class<?>, Class<?>>();
  
  static {
    for (int i = 0 ; i < primitiveTypeMapping.length ; i = i + 2) {
      primitiveTypeMap.put(primitiveTypeMapping[i], primitiveTypeMapping[i+1]);
    }
  }
  
  private static boolean isInstanceOfType(Class<?> paramType, Object arg) {
    return paramType.isPrimitive() ? primitiveTypeMap.get(paramType).isInstance(arg) : paramType.isInstance(arg);
  }
}
