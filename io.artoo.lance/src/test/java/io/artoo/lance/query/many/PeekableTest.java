package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static io.artoo.lance.query.TestData.Pet;
import static java.lang.System.out;
import static java.lang.System.setOut;
import static org.assertj.core.api.Assertions.assertThat;

class PeekableTest {
  static class Touched {
    int value = 0;
  }

  @Test
  @DisplayName("should peek every element in many")
  void shouldPeekElements() {
    final var touched = new Touched();

    final var summed = Many.from(1, 2, 3, 4)
      .peek(element -> touched.value += element)
      .sum()
      .peek(out::println)
      .yield();

    assertThat(summed).isEqualTo(10);
    assertThat(touched.value).isEqualTo(10);
  }

  @Test
  @DisplayName("should exceptionally peek throwable")
  void shouldExceptionallyPeekThrowable() {
    final var selected = Many.from(1, 2, 3, 4)
      .select(it -> {
        if (it == 3) throw new IllegalStateException("I don't like 3");
        return it * 2;
      })
      .exceptionally(throwable -> assertThat(throwable.getMessage()).isEqualTo("I don't like 3"));

    assertThat(selected).containsExactly(2, 4, 8);
  }

  @Test
  void shouldPeekOrdered() {
    Many.from(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .peek(it -> out.println("I'm " + it))
      .distinct()
      .peek(it -> out.println("Years " + it))
      .select(it -> new Pet("No name", it))
      .peek(pet -> out.println("Operating on Pet " + pet.age()))
      .eventually(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years"));

    out.println("\n========\n");

    Stream.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .peek(it -> out.println("I'm " + it))
      .distinct()
      .peek(it -> out.println("Years " + it))
      .map(it -> new Pet("No name", it))
      .peek(pet -> out.println("Operating on Pet " + pet.age()))
      .forEach(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years"));
  }
}
