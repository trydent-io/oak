package oak.quill.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static oak.quill.Q.from;
import static oak.quill.query.Queryable.query;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UniquableTest {
  @Test
  @DisplayName("should get element at index 4 or default if out of bound")
  void shouldGetElementAt4() {
    final String[] names = {
      "Hartono, Tommy", "Adams, Terry",
      "Andersen, Henriette Thaulow",
      "Hedlund, Magnus", "Ito, Shu"
    };

    for (final var value : query(names).elementAt(4)) assertThat(value).isEqualTo("Ito, Shu");
    for (final var value : query(names).elementAt(-1).or("none")) assertThat(value).isEqualTo("none");
  }

  @Test
  @DisplayName("should get first element or default if none")
  void shouldGetFirstOrDefaultIfNone() {
    final Integer[] numbers = { 9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19 };

    for (final var value : query(numbers).first()) assertThat(value).isEqualTo(9);
    for (final var value : query((Integer) null).first().or(-1)) assertThat(value).isEqualTo(-1);
  }

  @Test
  @DisplayName("should get first even number or default if none")
  void shouldGetFirstEvenNumberOrDefaultIfNone() {
    final Integer[] numbers = { 9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19 };
    final Integer[] odds = { 9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19 };

    for (final var value : query(numbers).first(number -> number % 2 == 0)) assertThat(value).isEqualTo(34);
    for (final var value : query(odds).first(number -> number % 2 == 0).or(-1)) assertThat(value).isEqualTo(-1);
  }

  @Test
  @DisplayName("should get last element or default if none")
  void shouldGetLastOrDefaultIfNone() {
    final Integer[] numbers = { 9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19 };

    for (final var value : query(numbers).last()) assertThat(value).isEqualTo(19);
    for (final var value : query((Integer) null).last().or(-1)) assertThat(value).isEqualTo(-1);
  }

  @Test
  @DisplayName("should get last even number or default if none")
  void shouldGetLastEvenNumberOrDefaultIfNone() {
    final Integer[] numbers = { 9, 34, 65, 92, 87, 435, 3, 54, 83, 23, 87, 435, 67, 12, 19 };
    final Integer[] odds = { 9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19 };

    for (final var value : query(numbers).last(number -> number % 2 == 0)) assertThat(value).isEqualTo(12);
    for (final var value : query(odds).last(number -> number % 2 == 0).or(-1)) assertThat(value).isEqualTo(-1);
  }

  @Test
  @DisplayName("should get single element")
  void shouldGetSingle() {
    final Integer[] numbers = { 9 };
    final Integer[] evens = { 9, 34, 65, 87, 435, 3, 83, 23 };

    for (final var value : query(numbers).single()) assertThat(value).isEqualTo(9);
    for (final var value : query(evens).single(number -> number % 2 == 0)) assertThat(value).isEqualTo(34);
  }

  @Test
  @DisplayName("should fail if there's more than single element")
  void shouldFailIfThereIsMoreThanSingleElement() {
    final Integer[] numbers = { 9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19 };

    assertThrows(IllegalStateException.class, query(numbers).single()::get, "Queryable must have one element only.");
    assertThrows(IllegalStateException.class, query(numbers).single(number -> number < 20)::get, "Queryable must have one element only.");
  }

  @Test
  @DisplayName("should get negative number since it fails")
  void shouldGetNegativeNumberSinceItFails() {
    final Integer[] numbers = { 9, 65, 87, 435, 3, 83, 23, 87, 435, 67, 19 };

    for (final var value : query(numbers).single().or(-1)) assertThat(value).isEqualTo(-1);
    for (final var value : query(numbers).single(number -> number < 20).or(-1)) assertThat(value).isEqualTo(-1);
  }
}
