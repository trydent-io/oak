package io.artoo.type;

@FunctionalInterface
public interface AsArray<T> {
  T[] eval();
}