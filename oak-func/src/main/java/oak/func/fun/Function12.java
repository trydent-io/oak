package oak.func.fun;

import oak.func.Functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> extends Functional.Fun {
  R apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12);

  default <V> Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, V> then(Function1<? super R, ? extends V> after) {
    requireNonNull(after);
    return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> after.apply(apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
  }
}