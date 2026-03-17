package dev.typr.foundations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
            for (Object v : arr) flat.add(v);
            yield resolve(o.inner(), flat.iterator());
          } else {
            yield Fragment.EMPTY;
          }
        }
      }
      case Fragment.Append a -> new Fragment.Append(resolve(a.a(), values), resolve(a.b(), values));
      case Fragment.Concat c ->
          new Fragment.Concat(c.frags().stream().map(f -> resolve(f, values)).toList());
      default -> fragment;
    };
  }

  public static List<Fragment> analysisVariants(Fragment fragment) {
    List<Fragment.Optionally> optionals = new ArrayList<>();
    collectOptionally(fragment, optionals);

    if (optionals.isEmpty()) {
      return List.of(fragment);
    }

    int n = optionals.size();
    int combinations = 1 << n;
    List<Fragment> variants = new ArrayList<>(combinations);

    for (int mask = 0; mask < combinations; mask++) {
      int[] idx = {0};
      variants.add(replaceOptionally(fragment, optionals, mask, idx));
    }

    return variants;
  }

  private static void collectOptionally(Fragment fragment, List<Fragment.Optionally> list) {
    switch (fragment) {
      case Fragment.Optionally o -> list.add(o);
      case Fragment.Append a -> {
        collectOptionally(a.a(), list);
        collectOptionally(a.b(), list);
      }
      case Fragment.Concat c -> {
        for (Fragment f : c.frags()) collectOptionally(f, list);
      }
      default -> {}
    }
  }

  private static Fragment replaceOptionally(
      Fragment fragment, List<Fragment.Optionally> optionals, int mask, int[] idx) {
    return switch (fragment) {
      case Fragment.Optionally o -> {
        int i = idx[0]++;
        boolean include = (mask & (1 << i)) != 0;
        yield include ? o.inner() : Fragment.EMPTY;
      }
      case Fragment.Append a ->
          new Fragment.Append(
              replaceOptionally(a.a(), optionals, mask, idx),
              replaceOptionally(a.b(), optionals, mask, idx));
      case Fragment.Concat c ->
          new Fragment.Concat(
              c.frags().stream().map(f -> replaceOptionally(f, optionals, mask, idx)).toList());
      default -> fragment;
    };
  }
}
