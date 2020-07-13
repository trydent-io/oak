package io.artoo.lance.query.cursor;

final class Just<R> implements Cursor<R> {
  private final R element;
  private final NotFetched notFetched;

  Just(final R element) {
    this.element = element;
    this.notFetched = new NotFetched();
  }

  @Override
  public R fetch() {
    notFetched.value = false;
    return element;
  }

  @Override
  public boolean hasNext() {
    return notFetched.value;
  }

  @Override
  public R next() {
    return fetch();
  }

  private final class NotFetched {
    private boolean value = true;
  }
}