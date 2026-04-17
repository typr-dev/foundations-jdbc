package dev.typr.foundations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Parses the text DuckDB reports through {@code ResultSetMetaData.getColumnTypeName} — which for
 * composites is the fully-expanded shape (e.g. {@code STRUCT("name" VARCHAR, age INTEGER)[]} or
 * {@code MAP(VARCHAR, INTEGER)}) — back into the {@link DuckDbTypename} tree. Lets {@link
 * QueryAnalysis} compare struct field names and types structurally rather than by strict string
 * equality.
 *
 * <p>Grammar handled:
 *
 * <ul>
 *   <li>Base scalars: {@code VARCHAR}, {@code INTEGER}, {@code DECIMAL(18, 3)}, {@code BIT(8)}, …
 *   <li>{@code STRUCT(name type, "quoted name" type, …)}
 *   <li>{@code UNION(tag type, tag type, …)}
 *   <li>{@code MAP(keyType, valueType)}
 *   <li>Trailing {@code []} for LIST, {@code [n]} for fixed ARRAY, in any combination.
 * </ul>
 */
public final class DuckDbTypenameParser {
  private DuckDbTypenameParser() {}

  public static DuckDbTypename<Object> parse(String input) {
    Cursor c = new Cursor(input);
    DuckDbTypename<Object> result = parseType(c);
    c.skipWhitespace();
    if (!c.atEnd()) {
      throw new IllegalArgumentException(
          "Unexpected trailing content in DuckDB typename '" + input + "' at position " + c.pos);
    }
    return result;
  }

  private static DuckDbTypename<Object> parseType(Cursor c) {
    DuckDbTypename<Object> t = parseBase(c);
    while (true) {
      c.skipWhitespace();
      if (c.peek() != '[') break;
      int save = c.pos;
      c.advance();
      c.skipWhitespace();
      if (c.peek() == ']') {
        c.advance();
        t = t.list().as();
        continue;
      }
      int size = 0;
      boolean any = false;
      while (Character.isDigit(c.peek())) {
        size = size * 10 + (c.advance() - '0');
        any = true;
      }
      c.skipWhitespace();
      if (!any || c.peek() != ']') {
        c.pos = save;
        break;
      }
      c.advance();
      t = t.array(size).as();
    }
    return t;
  }

  private static DuckDbTypename<Object> parseBase(Cursor c) {
    c.skipWhitespace();
    String ident = readIdentifier(c);
    String kind = ident.toUpperCase();
    c.skipWhitespace();
    if (c.peek() == '(') {
      c.advance();
      switch (kind) {
        case "STRUCT" -> {
          return parseStructBody(c);
        }
        case "UNION" -> {
          return parseUnionBody(c);
        }
        case "MAP" -> {
          return parseMapBody(c);
        }
        default -> {
          return parseBaseWithPrecision(ident, c);
        }
      }
    }
    return new DuckDbTypename.Base<>(ident);
  }

  private static DuckDbTypename<Object> parseBaseWithPrecision(String ident, Cursor c) {
    c.skipWhitespace();
    int first = readInt(c);
    c.skipWhitespace();
    Optional<Integer> scale = Optional.empty();
    if (c.peek() == ',') {
      c.advance();
      c.skipWhitespace();
      scale = Optional.of(readInt(c));
      c.skipWhitespace();
    }
    expect(c, ')');
    return new DuckDbTypename.Base<>(ident, Optional.of(first), scale);
  }

  private static DuckDbTypename<Object> parseStructBody(Cursor c) {
    List<DuckDbTypename.StructOf.StructField> fields = new ArrayList<>();
    c.skipWhitespace();
    if (c.peek() != ')') {
      while (true) {
        c.skipWhitespace();
        String name = readIdentifier(c);
        c.skipWhitespace();
        DuckDbTypename<Object> fieldType = parseType(c);
        fields.add(new DuckDbTypename.StructOf.StructField(name, fieldType));
        c.skipWhitespace();
        if (c.peek() == ',') {
          c.advance();
          continue;
        }
        break;
      }
    }
    expect(c, ')');
    return new DuckDbTypename.StructOf<>("", fields);
  }

  private static DuckDbTypename<Object> parseUnionBody(Cursor c) {
    List<DuckDbTypename.UnionOf.UnionMember> members = new ArrayList<>();
    c.skipWhitespace();
    if (c.peek() != ')') {
      while (true) {
        c.skipWhitespace();
        String tag = readIdentifier(c);
        c.skipWhitespace();
        DuckDbTypename<Object> memberType = parseType(c);
        members.add(new DuckDbTypename.UnionOf.UnionMember(tag, memberType));
        c.skipWhitespace();
        if (c.peek() == ',') {
          c.advance();
          continue;
        }
        break;
      }
    }
    expect(c, ')');
    return new DuckDbTypename.UnionOf<>("", members);
  }

  private static DuckDbTypename<Object> parseMapBody(Cursor c) {
    DuckDbTypename<Object> key = parseType(c);
    c.skipWhitespace();
    expect(c, ',');
    DuckDbTypename<Object> value = parseType(c);
    c.skipWhitespace();
    expect(c, ')');
    return new DuckDbTypename.MapOf<>(key, value).as();
  }

  private static String readIdentifier(Cursor c) {
    c.skipWhitespace();
    if (c.peek() == '"') {
      c.advance();
      StringBuilder sb = new StringBuilder();
      while (!c.atEnd()) {
        char ch = c.advance();
        if (ch == '"') {
          if (c.peek() == '"') {
            sb.append('"');
            c.advance();
            continue;
          }
          return sb.toString();
        }
        sb.append(ch);
      }
      throw new IllegalArgumentException("Unterminated quoted identifier at " + c.pos);
    }
    int start = c.pos;
    while (!c.atEnd()) {
      char ch = c.peek();
      if (Character.isLetterOrDigit(ch) || ch == '_') {
        c.advance();
      } else {
        break;
      }
    }
    if (start == c.pos) {
      throw new IllegalArgumentException(
          "Expected identifier at position " + c.pos + " in '" + c.input + "'");
    }
    return c.input.substring(start, c.pos);
  }

  private static int readInt(Cursor c) {
    int start = c.pos;
    while (Character.isDigit(c.peek())) c.advance();
    if (start == c.pos) {
      throw new IllegalArgumentException(
          "Expected integer at position " + c.pos + " in '" + c.input + "'");
    }
    return Integer.parseInt(c.input.substring(start, c.pos));
  }

  private static void expect(Cursor c, char ch) {
    c.skipWhitespace();
    if (c.peek() != ch) {
      throw new IllegalArgumentException(
          "Expected '" + ch + "' at position " + c.pos + " in '" + c.input + "'");
    }
    c.advance();
  }

  private static final class Cursor {
    final String input;
    int pos;

    Cursor(String input) {
      this.input = input;
      this.pos = 0;
    }

    char peek() {
      return pos < input.length() ? input.charAt(pos) : '\0';
    }

    char advance() {
      return input.charAt(pos++);
    }

    boolean atEnd() {
      return pos >= input.length();
    }

    void skipWhitespace() {
      while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) pos++;
    }
  }
}
