package io.artoo.lance.type;

import io.artoo.lance.func.Func;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static io.artoo.lance.type.TupleType.has;
import static io.artoo.lance.type.TupleType.tryComponentOf;

@FunctionalInterface
public interface Tuple<R extends Record> {
  Class<R> type();

  @FunctionalInterface
  interface Single<R extends Record, A> extends Tuple<R> {
    default A first() { return tryComponentOf(type(), 0); }

    default <T extends Record> T to(final @NotNull Func.Uni<? super A, ? extends T> to) {
      return to.apply(first());
    }

    default <T> T as(final @NotNull Func.Uni<? super A, ? extends T> as) {
      return as.apply(first());
    }

    default boolean is(final A value) {
      return has(first(), value);
    }
  }

  @FunctionalInterface
  interface Pair<R extends Record, A, B> extends Single<R, A> {
    default B second() { return tryComponentOf(type(), 1); }

    default <T extends Record> T to(final @NotNull Func.Bi<? super A, ? super B, ? extends T> to) {
      return to.apply(first(), second());
    }

    default <T> T as(final @NotNull Func.Bi<? super A, ? super B, ? extends T> as) {
      return as.apply(first(), second());
    }

    default boolean is(final A value1, final B value2) {
      return is(value1) && has(second(), value2);
    }
  }

  @FunctionalInterface
  interface Triple<R extends Record, A, B, C> extends Pair<R, A, B> {
    default C third() { return tryComponentOf(type(), 2); }

    default <T extends Record> T as(final @NotNull Func.Tri<? super A, ? super B, ? super C, ? extends T> as) {
      return as.apply(first(), second(), third());
    }

    default boolean is(final A value1, final B value2, final C value3) {
      return is(value1, value2) && has(third(), value3);
    }
  }

  @FunctionalInterface
  interface Quadruple<R extends Record, A, B, C, D> extends Triple<R, A, B, C> {
    default D forth() { return tryComponentOf(type(), 3); }

    default <T extends Record> T as(final @NotNull Func.Quad<? super A, ? super B, ? super C, ? super D, ? extends T> as) {
      return as.apply(first(), second(), third(), forth());
    }

    default boolean is(final A value1, final B value2, final C value3, final D value4) {
      return is(value1, value2, value3) && has(forth(), value4);
    }
  }
}

@SuppressWarnings("unchecked")
enum TupleType {;
  static <R extends Record, T> @NotNull Optional<T> componentOf(@NotNull final Class<R> type, final int index) {
    try {
      return Optional.ofNullable(
        index >= 0 && type.getRecordComponents().length > index
          ? (T) type.getRecordComponents()[index]
          : null
      );
    } catch (ClassCastException cce) {
      return Optional.empty();
    }
  }

  static <R extends Record, T> @NotNull T tryComponentOf(@NotNull final Class<R> type, final int index) {
    return TupleType.<R, T>componentOf(type, index).orElseThrow(IllegalStateException::new);
  }

  static <T> boolean has(final T property, final T value) {
    if (property == null && value == null) {
      return true;
    }
    assert property != null;
    return property.equals(value);
  }
}