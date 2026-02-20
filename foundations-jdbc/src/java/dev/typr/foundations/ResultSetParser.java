package dev.typr.foundations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface ResultSetParser<Out> {
  Out apply(ResultSet resultSet) throws SQLException;

  default <Out2> ResultSetParser<Out2> map(Function<Out, Out2> f) {
    return new Mapped<>(this, f);
  }

  record Mapped<In, Out>(ResultSetParser<In> inner, Function<In, Out> f)
      implements ResultSetParser<Out> {
    @Override
    public Out apply(ResultSet resultSet) throws SQLException {
      return f.apply(inner.apply(resultSet));
    }
  }

  record All<Out>(RowCodec<Out> rowCodec) implements ResultSetParser<List<Out>> {
    @Override
    public List<Out> apply(ResultSet resultSet) throws SQLException {
      var rowNum = 0;
      ArrayList<Out> rows = new ArrayList<>();
      while (resultSet.next()) {
        rows.add(rowCodec.readRow(resultSet, rowNum));
        rowNum += 1;
      }
      return rows;
    }
  }

  record Foreach<Out>(RowCodec<Out> rowCodec, Consumer<Out> consumer)
      implements ResultSetParser<Void> {
    @Override
    public Void apply(ResultSet resultSet) throws SQLException {
      var rowNum = 0;
      while (resultSet.next()) {
        consumer.accept(rowCodec.readRow(resultSet, rowNum));
        rowNum += 1;
      }
      return null;
    }
  }

  record First<Out>(RowCodec<Out> rowCodec) implements ResultSetParser<Optional<Out>> {
    @Override
    public Optional<Out> apply(ResultSet resultSet) throws SQLException {
      if (resultSet.next()) {
        return Optional.of(rowCodec.readRow(resultSet, 0));
      } else {
        return Optional.empty();
      }
    }
  }

  record MaxOne<Out>(RowCodec<Out> rowCodec) implements ResultSetParser<Optional<Out>> {
    @Override
    public Optional<Out> apply(ResultSet resultSet) throws SQLException {
      if (resultSet.next()) {
        Out result = rowCodec.readRow(resultSet, 0);
        if (resultSet.next()) {
          throw new SQLException("Expected single row, but found more");
        }
        return Optional.of(result);
      } else {
        return Optional.empty();
      }
    }
  }

  record ExactlyOne<Out>(RowCodec<Out> rowCodec) implements ResultSetParser<Out> {
    @Override
    public Out apply(ResultSet resultSet) throws SQLException {
      if (resultSet.next()) {
        Out result = rowCodec.readRow(resultSet, 0);
        if (resultSet.next()) {
          throw new SQLException("Expected single row, but found more");
        }
        return result;
      } else {
        throw new SQLException("No rows when expecting a single one");
      }
    }
  }
}
