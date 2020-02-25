package oak.func.$2;

import oak.func.Functional;

@FunctionalInterface
public interface IntPre<T> extends Pre<Integer, T>, Func<Integer, T, Boolean>, Functional.Pre {
  boolean verify(final int index, final T param);

  @Override
  default Boolean apply(Integer index, T param) {
    return verify(index, param);
  }

  @Override
  default boolean test(Integer index, T param) {
    return verify(index, param);
  }
}