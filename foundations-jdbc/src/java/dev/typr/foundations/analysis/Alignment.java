package dev.typr.foundations.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the alignment of two values at the same position.
 * Similar to Haskell/Scala's Ior (inclusive or) type.
 *
 * <ul>
 *   <li>{@link Both} - both values present (the happy path)</li>
 *   <li>{@link LeftOnly} - only the left value present (extra in code)</li>
 *   <li>{@link RightOnly} - only the right value present (missing in code)</li>
 * </ul>
 */
public sealed interface Alignment<L, R> {

  /**
   * Both values are present at this position.
   */
  record Both<L, R>(L left, R right) implements Alignment<L, R> {}

  /**
   * Only the left value is present (the right side is missing).
   */
  record LeftOnly<L, R>(L left) implements Alignment<L, R> {}

  /**
   * Only the right value is present (the left side is missing).
   */
  record RightOnly<L, R>(R right) implements Alignment<L, R> {}

  /**
   * Align two lists by position. Produces:
   * <ul>
   *   <li>{@link Both} for positions where both lists have elements</li>
   *   <li>{@link LeftOnly} for positions where only the first list has elements</li>
   *   <li>{@link RightOnly} for positions where only the second list has elements</li>
   * </ul>
   *
   * @param left the first list (typically our types)
   * @param right the second list (typically JDBC metadata)
   * @return aligned pairs
   */
  static <L, R> List<Alignment<L, R>> align(List<L> left, List<R> right) {
    int maxLen = Math.max(left.size(), right.size());
    List<Alignment<L, R>> result = new ArrayList<>(maxLen);

    for (int i = 0; i < maxLen; i++) {
      boolean hasLeft = i < left.size();
      boolean hasRight = i < right.size();

      if (hasLeft && hasRight) {
        result.add(new Both<>(left.get(i), right.get(i)));
      } else if (hasLeft) {
        result.add(new LeftOnly<>(left.get(i)));
      } else {
        result.add(new RightOnly<>(right.get(i)));
      }
    }

    return result;
  }
}
