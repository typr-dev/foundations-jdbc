package dev.typr.foundations;

import java.util.*;

public final class OptionallyResolver {

  private OptionallyResolver() {}

  @SuppressWarnings("unchecked")
  public static Fragment resolve(Fragment fragment, Iterator<Object> values) {
    return switch (fragment) {
      case Fragment.Param<?> p -> new Fragment.Value<>(values.next(), (DbType<Object>) p.type());
      case Fragment.Optionally o -> {
        int innerParams = o.innerParamCount();
        if (innerParams == 0) {
          Boolean flag = (Boolean) values.next();
          yield flag ? o.inner() : Fragment.EMPTY;
        } else if (innerParams == 1) {
          Optional<?> opt = (Optional<?>) values.next();
          if (opt.isPresent()) {
            yield resolve(o.inner(), List.of(opt.get()).iterator());
          } else {
            yield Fragment.EMPTY;
          }
        } else {
          Optional<?> opt = (Optional<?>) values.next();
          if (opt.isPresent()) {
            Tuple tuple = (Tuple) opt.get();
            Object[] arr = tuple.asArray();
            List<Object> flat = new ArrayList<>(arr.length);
            flat.addAll(Arrays.asList(arr));
            yield resolve(o.inner(), flat.iterator());
          } else {
            yield Fragment.EMPTY;
          }
        }
      }
      case Fragment.Branch b -> b; // already resolved at construction time
      case Fragment.Append a -> new Fragment.Append(resolve(a.a(), values), resolve(a.b(), values));
      case Fragment.Concat c ->
          new Fragment.Concat(c.frags().stream().map(f -> resolve(f, values)).toList());
      default -> fragment;
    };
  }

  /**
   * Expand all branch points ({@link Fragment.Optionally} and {@link Fragment.Branch}) into
   * concrete variants for query analysis. Each branch point with N alternatives multiplies
   * the variant count by N.
   */
  public static List<Fragment> analysisVariants(Fragment fragment) {
    List<BranchPoint> points = new ArrayList<>();
    collectBranchPoints(fragment, points);

    if (points.isEmpty()) {
      return List.of(fragment);
    }

    // Calculate total combinations: product of all alternative counts
    int combinations = 1;
    for (BranchPoint bp : points) {
      combinations *= bp.alternativeCount();
    }

    List<Fragment> variants = new ArrayList<>(combinations);
    for (int combo = 0; combo < combinations; combo++) {
      int[] idx = {0};
      variants.add(expandVariant(fragment, points, combo, idx));
    }

    return variants;
  }

  /** A branch point in the fragment tree — either Optionally (2 variants) or Branch (N variants). */
  private sealed interface BranchPoint {
    int alternativeCount();
    Fragment alternative(int index);
  }

  private record OptionallyPoint(Fragment.Optionally node) implements BranchPoint {
    @Override public int alternativeCount() { return 2; }
    @Override public Fragment alternative(int index) {
      return index == 0 ? node.inner() : Fragment.EMPTY;
    }
  }

  private record BranchPointNode(Fragment.Branch node) implements BranchPoint {
    @Override public int alternativeCount() { return node.variants().size(); }
    @Override public Fragment alternative(int index) { return node.variants().get(index); }
  }

  private static void collectBranchPoints(Fragment fragment, List<BranchPoint> points) {
    switch (fragment) {
      case Fragment.Optionally o -> points.add(new OptionallyPoint(o));
      case Fragment.Branch b -> points.add(new BranchPointNode(b));
      case Fragment.Append a -> {
        collectBranchPoints(a.a(), points);
        collectBranchPoints(a.b(), points);
      }
      case Fragment.Concat c -> {
        for (Fragment f : c.frags()) collectBranchPoints(f, points);
      }
      default -> {}
    }
  }

  private static Fragment expandVariant(
      Fragment fragment, List<BranchPoint> points, int combo, int[] idx) {
    return switch (fragment) {
      case Fragment.Optionally o -> {
        BranchPoint bp = points.get(idx[0]++);
        int choice = choiceForPoint(combo, idx[0] - 1, points);
        yield bp.alternative(choice);
      }
      case Fragment.Branch b -> {
        BranchPoint bp = points.get(idx[0]++);
        int choice = choiceForPoint(combo, idx[0] - 1, points);
        yield bp.alternative(choice);
      }
      case Fragment.Append a ->
          new Fragment.Append(
              expandVariant(a.a(), points, combo, idx),
              expandVariant(a.b(), points, combo, idx));
      case Fragment.Concat c ->
          new Fragment.Concat(
              c.frags().stream().map(f -> expandVariant(f, points, combo, idx)).toList());
      default -> fragment;
    };
  }

  /**
   * Given a combination index and a point index, compute which alternative to use.
   * Uses mixed-radix decomposition: each point can have a different number of alternatives.
   */
  private static int choiceForPoint(int combo, int pointIndex, List<BranchPoint> points) {
    int divisor = 1;
    for (int i = pointIndex + 1; i < points.size(); i++) {
      divisor *= points.get(i).alternativeCount();
    }
    return (combo / divisor) % points.get(pointIndex).alternativeCount();
  }
}
