package dev.typr.foundations;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

/**
 * Strategy for executing Combine nodes. JDBC runs sequentially, PgPipe fans out to separate
 * connections in parallel.
 */
public interface CombineStrategy {

  <A, B> Tuple.Tuple2<A, B> combine(Supplier<A> first, Supplier<B> second);

  CombineStrategy SEQUENTIAL =
      new CombineStrategy() {
        @Override
        public <A, B> Tuple.Tuple2<A, B> combine(Supplier<A> first, Supplier<B> second) {
          return Tuple.of(first.get(), second.get());
        }
      };

  CombineStrategy PARALLEL =
      new CombineStrategy() {
        @Override
        public <A, B> Tuple.Tuple2<A, B> combine(Supplier<A> first, Supplier<B> second) {
          CompletableFuture<A> fa = CompletableFuture.supplyAsync(first);
          CompletableFuture<B> fb = CompletableFuture.supplyAsync(second);
          try {
            return Tuple.of(fa.join(), fb.join());
          } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException re) throw re;
            throw new RuntimeException(cause);
          }
        }
      };
}
