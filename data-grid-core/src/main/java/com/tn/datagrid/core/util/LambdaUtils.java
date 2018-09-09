package com.tn.datagrid.core.util;

import java.util.function.Function;

public class LambdaUtils
{
  public static <T, R, E extends Throwable> Function<T, R> wrap(FunctionWithThrow<T, R, E> f)
  {
    return (t) ->
    {
      try
      {
        return f.apply(t);
      }
      catch (Throwable e)
      {
        if (e instanceof RuntimeException)
        {
          throw (RuntimeException)e;
        }
        else
        {
          throw new WrappedException(e);
        }
      }
    };
  }

  public static class WrappedException extends RuntimeException
  {
    public WrappedException(Throwable cause)
    {
      super(cause);
    }
  }

  @FunctionalInterface
  public static interface FunctionWithThrow<T, R, E extends Throwable>
  {
    public R apply(T t) throws E;
  }
}
