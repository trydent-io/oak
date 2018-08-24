package oak.collect.query.element;

import org.junit.jupiter.api.Test;

import static oak.collect.query.Queryable.asQueryable;
import static org.assertj.core.api.Assertions.assertThat;

class FirstTest {
  @Test
  void shouldRetrieveFirstElement() {
    assertThat(asQueryable(1, 2, 3).first()).contains(1);
  }

  @Test
  void shouldRetrieveNone() {
    assertThat(asQueryable().first()).isEmpty();
  }
}
