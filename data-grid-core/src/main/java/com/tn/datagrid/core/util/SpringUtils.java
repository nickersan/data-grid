package com.tn.datagrid.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class SpringUtils
{
  public static <T, R> Function<T, R> asFunction(Object object, String methodName, Class<? extends T> argumentType) throws NoSuchMethodException
  {
    Method method = object.getClass().getDeclaredMethod(methodName, argumentType);
    return (argument) -> invoke(object, method, argument);
  }

  private static <T, R> R invoke(Object object, Method method, T argument)
  {
    try
    {
      //noinspection unchecked
      return (R)method.invoke(object, argument);
    }
    catch (IllegalAccessException | InvocationTargetException e)
    {
      throw new IllegalStateException("An error occurred invoking method", e);
    }
  }
}
