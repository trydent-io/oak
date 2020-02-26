package oak.query.many;

import oak.cursor.Cursor;
import oak.func.Pred;
import oak.query.Queryable;
import oak.query.one.One;
import oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static oak.func.Pred.tautology;
import static oak.type.Nullability.nonNullable;

public interface Uniquable<T> extends Queryable<T> {
  default One<T> at(final int index) {
    return new At<>(this, index);
  }

  default One<T> first() {
    return new First<>(this, tautology());
  }

  default One<T> first(final Pred<? super T> filter) {
    return new First<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default One<T> last() {
    return new Last<>(this, tautology());
  }

  default One<T> last(final Pred<? super T> filter) {
    return new Last<>(this, Nullability.nonNullable(filter, "filter"));
  }

  default One<T> single() {
    return new Single<>(this, tautology());
  }

  default One<T> single(final Pred<? super T> filter) {
    return new Single<>(this, Nullability.nonNullable(filter, "filter"));
  }
}

final class At<T> implements One<T> {
  private final Queryable<T> queryable;
  private final int index;

  @Contract(pure = true)
  At(final Queryable<T> queryable, final int index) {
    this.queryable = queryable;
    this.index = index;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    var count = 0;
    T returned = null;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && count <= index; count++) {
      returned = iterator.next();
    }
    if (count < index)
      returned = null;
    return Cursor.ofNullable(returned);
  }
}

final class First<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Pred<? super T> filter;

  @Contract(pure = true)
  First(final Queryable<T> queryable, final Pred<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    T result = null;
    final var cursor = queryable.iterator();
    if (cursor.hasNext()) result = cursor.next();
    if (cursor.hasNext()) result = null;
    return Cursor.ofNullable(result);
  }
}

final class Last<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Pred<? super T> filter;

  @Contract(pure = true)
  Last(final Queryable<T> queryable, final Pred<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    T last = null;
    for (final var value : queryable) {
      if (filter.test(value))
        last = value;
    }
    return Cursor.ofNullable(last);
  }
}

final class Single<T> implements One<T> {
  private final Queryable<T> queryable;
  private final Pred<? super T> filter;

  @Contract(pure = true)
  Single(final Queryable<T> queryable, final Pred<? super T> filter) {
    this.queryable = queryable;
    this.filter = filter;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    if (filter.equals(tautology())) {
      final var cursor = queryable.iterator();
      final var returned = cursor.next();
      if (cursor.hasNext())
        throw new IllegalStateException("Queryable must contain one element.");
      return Cursor.once(returned);
    } else {
      final var array = new ArrayList<T>();
      for (final var cursor = queryable.iterator(); cursor.hasNext() && array.size() < 2; ) {
        final var next = cursor.next();
        if (filter.test(next)) {
          array.add(next);
        }
      }
      if (array.size() > 1)
        throw new IllegalStateException("Queryable must satisfy the condition once.");
      return Cursor.once(array.get(0));
    }
  }
}
