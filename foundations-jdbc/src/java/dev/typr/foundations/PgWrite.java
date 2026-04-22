package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.postgresql.util.PGobject;

public sealed interface PgWrite<A> extends DbWrite<A> permits PgWrite.Instance {
  void set(PreparedStatement ps, int idx, A a) throws SQLException;

  // combinators
  PgWrite<Optional<A>> opt(PgTypename<A> typename);

  /**
   * Build a writer for {@code List<A>} bound as a PG array (SQL {@code T[]}). Each element is
   * converted via this writer's pre-bind transformation. Use {@code createArrayOf(typename, ...)}
   * against the JDBC connection; nested arrays recurse by calling {@code .list()} twice.
   */
  PgWrite<List<A>> list(PgTypename<A> typename);

  <B> PgWrite<B> contramap(Function<B, A> f);

  @FunctionalInterface
  interface RawWriter<A> {
    void set(PreparedStatement ps, int index, A a) throws SQLException;
  }

  record Instance<A, U>(RawWriter<U> rawWriter, Function<A, U> f) implements PgWrite<A> {
    @Override
    public void set(PreparedStatement ps, int index, A a) throws SQLException {
      rawWriter.set(ps, index, f.apply(a));
    }

    @Override
    public PgWrite<Optional<A>> opt(PgTypename<A> typename) {
      return new Instance<>(
          (ps, index, u) -> {
            if (u == null) ps.setNull(index, 0, typename.sqlTypeNoPrecision());
            else set(ps, index, u);
          },
          a -> a.orElse(null));
    }

    @Override
    public PgWrite<List<A>> list(PgTypename<A> typename) {
      return new Instance<List<A>, Object[]>(
          (ps, index, us) ->
              ps.setArray(
                  index, ps.getConnection().createArrayOf(typename.sqlTypeNoPrecision(), us)),
          list -> {
            if (list == null) return null;
            Object[] result = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
              A elem = list.get(i);
              result[i] = elem == null ? null : f.apply(elem);
            }
            return result;
          });
    }

    @Override
    public <B> PgWrite<B> contramap(Function<B, A> f) {
      return new Instance<>(rawWriter, f.andThen(this.f));
    }
  }

  static <A> PgWrite<A> primitive(RawWriter<A> rawWriter) {
    return new Instance<>(rawWriter, Function.identity());
  }

  static <A> PgWrite<A> passObjectToJdbc() {
    return primitive(PreparedStatement::setObject);
  }

  static PgWrite<String> pgObject(String sqlType) {
    return PgWrite.<PGobject>passObjectToJdbc()
        .contramap(
            str -> {
              var obj = new PGobject();
              obj.setType(sqlType);
              try {
                obj.setValue(str);
              } catch (SQLException e) {
                throw new DatabaseException.Jdbc(e);
              }
              return obj;
            });
  }

  PgWrite<byte[]> writeByteArray = primitive(PreparedStatement::setObject);

  PgWrite<Boolean> writeBoolean = primitive(PreparedStatement::setBoolean);
  PgWrite<BigDecimal> writeBigDecimal = primitive(PreparedStatement::setBigDecimal);
  PgWrite<Double> writeDouble = primitive(PreparedStatement::setDouble);
  PgWrite<Float> writeFloat = primitive(PreparedStatement::setFloat);
  PgWrite<Integer> writeInteger = primitive(PreparedStatement::setInt);
  PgWrite<Long> writeLong = primitive(PreparedStatement::setLong);
  PgWrite<Short> writeShort = primitive(PreparedStatement::setShort);
  PgWrite<String> writeString = primitive(PreparedStatement::setString);
  PgWrite<UUID> writeUUID = primitive(PreparedStatement::setObject);
}
