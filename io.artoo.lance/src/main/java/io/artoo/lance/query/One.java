package io.artoo.lance.query;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.query.one.Filterable;
import io.artoo.lance.query.one.Otherwise;
import io.artoo.lance.query.one.Peekable;
import io.artoo.lance.query.one.Projectable;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Otherwise<T> {
  static <T> One<T> of(final T element) {
    return element != null ? One.lone(element) : One.none();
  }

  static <L> One<L> lone(final L element) {
    return new Lone<>(element);
  }

  static <L> One<L> done(final Cursor<L> cursor) {
    return new Done<>(cursor);
  }

  static <L> One<L> none() {
    return new None<>();
  }

  default T yield() {
    return iterator().next();
  }
}

final class Lone<T> implements One<T> {
  private final T element;

  Lone(final T element) {
    assert element != null;
    this.element = element;
  }

  @Override
  public final Cursor<T> cursor() {
    return Cursor.just(element);
  }
}

final class None<T> implements One<T> {
  @Override
  public final Cursor<T> cursor() {
    return Cursor.nothing();
  }
}

final class Done<T> implements One<T> {
  private final Cursor<T> origin;

  Done(final Cursor<T> origin) {
    assert origin != null;
    this.origin = origin;
  }

  @Override
  public final Cursor<T> cursor() {
    try {
      class Last { T value; }

      final var last = new Last();
      if (origin.hasNext()) {

        do last.value = origin.fetch();
        while (origin.hasNext());

      }
      return Cursor.maybe(last.value);
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return Cursor.every();
    }
  }
}



