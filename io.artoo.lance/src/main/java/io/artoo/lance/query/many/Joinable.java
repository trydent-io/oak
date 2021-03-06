package io.artoo.lance.query.many;

import io.artoo.lance.func.Pred;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.cursor.routine.join.Join;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.tuple.Pair;

public interface Joinable<T> extends Queryable<T> {
  default <R, Q extends Queryable<R>> Joining<T, R> join(Q queryable) {
    return new Default<>(this, queryable);
  }

  default <R> Joining<T, R> join(R... items) {
    return join(Many.from(items));
  }

  interface Joining<A, B> extends Many.OfTwo<A, B> {
    Many.OfTwo<A, B> on(Pred.Bi<? super A, ? super B> on);
  }

  final class Default<A, B, Q extends Queryable<B>> implements Joining<A, B> {
    private final Queryable<A> first;

    private final Q second;

    Default(final Queryable<A> first, final Q second) {
      this.first = first;
      this.second = second;
    }

    @Override
    public Cursor<Pair<A, B>> cursor() {
      return first.cursor().to(Join.natural(second.cursor()));
    }
    @Override
    public Many.OfTwo<A, B> on(final Pred.Bi<? super A, ? super B> on) {
      return () -> first.cursor().to(Join.inner(second.cursor(), on));
    }

  }
}
