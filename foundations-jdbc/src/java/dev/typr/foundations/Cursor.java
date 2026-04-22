package dev.typr.foundations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;

public final class Cursor<Row> implements Iterator<Row>, Iterable<Row>, AutoCloseable {
  private final PreparedStatement stmt;
  private final ResultSet rs;
  private final RowCodec<Row> codec;
  private int rowNum;
  private Boolean hasNextCached;
  private boolean closed;

  private final Optional<Iterator<Row>> backing;
  private final Optional<Runnable> closeAction;

  public Cursor(PreparedStatement stmt, ResultSet rs, RowCodec<Row> codec) {
    this.stmt = stmt;
    this.rs = rs;
    this.codec = codec;
    this.rowNum = 0;
    this.hasNextCached = null;
    this.closed = false;
    this.backing = Optional.empty();
    this.closeAction = Optional.empty();
  }

  private Cursor(Iterator<Row> backing, Runnable closeAction) {
    this.stmt = null;
    this.rs = null;
    this.codec = null;
    this.rowNum = 0;
    this.hasNextCached = null;
    this.closed = false;
    this.backing = Optional.of(backing);
    this.closeAction = Optional.of(closeAction);
  }

  public static <Row> Cursor<Row> fromIterator(Iterator<Row> rows, Runnable onClose) {
    return new Cursor<>(rows, onClose);
  }

  @Override
  public boolean hasNext() {
    if (closed) return false;
    if (backing.isPresent()) {
      boolean has = backing.get().hasNext();
      if (!has) close();
      return has;
    }
    if (hasNextCached != null) return hasNextCached;
    try {
      hasNextCached = rs.next();
      if (!hasNextCached) {
        close();
      }
      return hasNextCached;
    } catch (SQLException e) {
      throw new DatabaseException.Jdbc(e);
    }
  }

  @Override
  public Row next() {
    if (!hasNext()) throw new NoSuchElementException();
    if (backing.isPresent()) return backing.get().next();
    hasNextCached = null;
    rowNum++;
    try {
      return codec.readRow(rs, rowNum);
    } catch (SqlResultParseException e) {
      throw new DatabaseException.Jdbc(e);
    }
  }

  @Override
  public void forEach(Consumer<? super Row> action) {
    while (hasNext()) {
      action.accept(next());
    }
  }

  @Override
  public Iterator<Row> iterator() {
    return this;
  }

  public List<Row> toList() {
    ArrayList<Row> list = new ArrayList<>();
    forEach(list::add);
    return list;
  }

  @Override
  public void close() {
    if (closed) return;
    closed = true;
    if (closeAction.isPresent()) {
      closeAction.get().run();
      return;
    }
    try {
      rs.close();
    } catch (SQLException ignored) {
    }
    try {
      stmt.close();
    } catch (SQLException ignored) {
    }
  }
}
