package oak.func.con;

import oak.func.Functional;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface Consumer4<T1, T2, T3, T4> extends Functional.Con {
  void accept(T1 t1, T2 t2, T3 t3, T4 t4);

  default Consumer4<T1, T2, T3, T4> then(Consumer4<? super T1, ? super T2, ? super T3, ? super T4> after) {
    requireNonNull(after, "After-consumer can't ben null.");
    return (t1, t2, t3, t4) -> { accept(t1, t2, t3, t4); after.accept(t1, t2, t3, t4); };
  }
}