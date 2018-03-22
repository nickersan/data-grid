package com.tn.datagrid.core.util;

import static java.util.Arrays.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class NumberUtils
{
  private static final List<Class<? extends Number>> NUMBER_TYPES = asList(
    Byte.class,
    Short.class,
    Integer.class,
    Long.class,
    Float.class,
    Double.class,
    BigInteger.class,
    BigDecimal.class
  );

  public static Number add(Number left, Number right)
  {
    Class<? extends Number> targetType = toTargetType(left, right);

    if (Byte.class.equals(targetType))
    {
      return left.byteValue() + right.byteValue();
    }
    else if (Short.class.equals(targetType))
    {
      return left.shortValue() + right.shortValue();
    }
    else if (Integer.class.equals(targetType))
    {
      return left.intValue() + right.intValue();
    }
    else if (Long.class.equals(targetType))
    {
      return left.longValue() + right.longValue();
    }
    else if (Float.class.equals(targetType))
    {
      return left.floatValue() + right.floatValue();
    }
    else if (Double.class.equals(targetType))
    {
      return left.doubleValue() + right.doubleValue();
    }
    else if (BigInteger.class.equals(targetType))
    {
      return toBigInteger(left).add(toBigInteger(right));
    }
    else if (BigDecimal.class.equals(targetType))
    {
      return toBigDecimal(left).add(toBigDecimal(right));
    }
    else
    {
      throw new IllegalArgumentException("Unsupported type: " + targetType);
    }
  }

  public static Number divide(Number left, Number right)
  {
    Class<? extends Number> targetType = toTargetType(left, right);

    if (Byte.class.equals(targetType))
    {
      return left.byteValue() / right.byteValue();
    }
    else if (Short.class.equals(targetType))
    {
      return left.shortValue() / right.shortValue();
    }
    else if (Integer.class.equals(targetType))
    {
      return left.intValue() / right.intValue();
    }
    else if (Long.class.equals(targetType))
    {
      return left.longValue() / right.longValue();
    }
    else if (Float.class.equals(targetType))
    {
      return left.floatValue() / right.floatValue();
    }
    else if (Double.class.equals(targetType))
    {
      return left.doubleValue() / right.doubleValue();
    }
    else if (BigInteger.class.equals(targetType))
    {
      return toBigInteger(left).divide(toBigInteger(right));
    }
    else if (BigDecimal.class.equals(targetType))
    {
      return toBigDecimal(left).divide(toBigDecimal(right));
    }
    else
    {
      throw new IllegalArgumentException("Unsupported type: " + targetType);
    }
  }

  public static Number multiple(Number left, Number right)
  {
    Class<? extends Number> targetType = toTargetType(left, right);

    if (Byte.class.equals(targetType))
    {
      return left.byteValue() * right.byteValue();
    }
    else if (Short.class.equals(targetType))
    {
      return left.shortValue() * right.shortValue();
    }
    else if (Integer.class.equals(targetType))
    {
      return left.intValue() * right.intValue();
    }
    else if (Long.class.equals(targetType))
    {
      return left.longValue() * right.longValue();
    }
    else if (Float.class.equals(targetType))
    {
      return left.floatValue() * right.floatValue();
    }
    else if (Double.class.equals(targetType))
    {
      return left.doubleValue() * right.doubleValue();
    }
    else if (BigInteger.class.equals(targetType))
    {
      return toBigInteger(left).multiply(toBigInteger(right));
    }
    else if (BigDecimal.class.equals(targetType))
    {
      return toBigDecimal(left).multiply(toBigDecimal(right));
    }
    else
    {
      throw new IllegalArgumentException("Unsupported type: " + targetType);
    }
  }

  public static Number subtract(Number left, Number right)
  {
    Class<? extends Number> targetType = toTargetType(left, right);

    if (Byte.class.equals(targetType))
    {
      return left.byteValue() - right.byteValue();
    }
    else if (Short.class.equals(targetType))
    {
      return left.shortValue() - right.shortValue();
    }
    else if (Integer.class.equals(targetType))
    {
      return left.intValue() - right.intValue();
    }
    else if (Long.class.equals(targetType))
    {
      return left.longValue() - right.longValue();
    }
    else if (Float.class.equals(targetType))
    {
      return left.floatValue() - right.floatValue();
    }
    else if (Double.class.equals(targetType))
    {
      return left.doubleValue() - right.doubleValue();
    }
    else if (BigInteger.class.equals(targetType))
    {
      return toBigInteger(left).subtract(toBigInteger(right));
    }
    else if (BigDecimal.class.equals(targetType))
    {
      return toBigDecimal(left).subtract(toBigDecimal(right));
    }
    else
    {
      throw new IllegalArgumentException("Unsupported type: " + targetType);
    }
  }

  private static int toIndex(Class<? extends Number> targetClass)
  {
    int index = NUMBER_TYPES.indexOf(targetClass);

    if (index < 0)
    {
      throw new IllegalArgumentException("Unsupported type: " + targetClass.getCanonicalName());
    }

    return index;
  }
  private static BigInteger toBigInteger(Number number)
  {
    return number instanceof BigInteger ? (BigInteger)number : BigInteger.valueOf(number.longValue());
  }
  //

  private static BigDecimal toBigDecimal(Number number)
  {
    return number instanceof BigDecimal ? (BigDecimal)number : BigDecimal.valueOf(number.doubleValue());
  }

  private static Class<? extends Number> toTargetType(Number left, Number right)
  {
    int leftIndex = toIndex(left.getClass());
    int rightIndex = toIndex(right.getClass());

    return leftIndex > rightIndex ? NUMBER_TYPES.get(leftIndex) : NUMBER_TYPES.get(rightIndex);
  }
}
