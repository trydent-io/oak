package oak.query.many;

import oak.func.$2.IntCons;
import oak.func.$2.IntPred;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.One;
import oak.query.many.internal.Quantify;

import static oak.func.$2.IntPred.tautology;
import static oak.type.Nullability.nonNullable;

// TODO: replace One<Boolean> with OneBoolean (internal primitive boolean and not boxed-boolean)
public interface Quantifiable<T> extends Queryable<T> {
  default <C> One<Boolean> allTypeOf(final Class<C> type) {
    return all((index, value) -> type.isInstance(value));
  }

  default One<Boolean> all(final Pred<? super T> where) {
    nonNullable(where, "where");
    return all((index, value) -> where.test(value));
  }

  default One<Boolean> all(final IntPred<? super T> where) {
    return new Quantify<>(this, IntCons.nothing(), false, nonNullable(where, "where"))::iterator;
  }

  default One<Boolean> any() { return this.any(tautology()); }

  default One<Boolean> any(final IntPred<? super T> where) {
    return new Quantify<>(this, IntCons.nothing(), true, nonNullable(where, "where"))::iterator;
  }
}
