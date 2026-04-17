package dev.typr.foundations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class DuckDbTypenameParserTest {

  @Test
  public void baseScalar() {
    var t = DuckDbTypenameParser.parse("VARCHAR");
    assertEquals(new DuckDbTypename.Base<>("VARCHAR"), t);
  }

  @Test
  public void baseScalarPrecision() {
    var t = DuckDbTypenameParser.parse("VARCHAR(100)");
    assertEquals(
        new DuckDbTypename.Base<>("VARCHAR", Optional.of(100), Optional.empty()), t);
  }

  @Test
  public void baseScalarPrecisionScale() {
    var t = DuckDbTypenameParser.parse("DECIMAL(18, 3)");
    assertEquals(
        new DuckDbTypename.Base<>("DECIMAL", Optional.of(18), Optional.of(3)), t);
  }

  @Test
  public void list() {
    var t = DuckDbTypenameParser.parse("INTEGER[]");
    assertEquals(new DuckDbTypename.ListOf<>(new DuckDbTypename.Base<>("INTEGER")), t);
  }

  @Test
  public void array() {
    var t = DuckDbTypenameParser.parse("FLOAT[1536]");
    assertEquals(new DuckDbTypename.ArrayOf<>(new DuckDbTypename.Base<>("FLOAT"), 1536), t);
  }

  @Test
  public void nestedListOfArray() {
    var t = DuckDbTypenameParser.parse("FLOAT[3][]");
    assertEquals(
        new DuckDbTypename.ListOf<>(
            new DuckDbTypename.ArrayOf<>(new DuckDbTypename.Base<>("FLOAT"), 3)),
        t);
  }

  @Test
  public void simpleStruct() {
    var t = DuckDbTypenameParser.parse("STRUCT(name VARCHAR, age INTEGER)");
    var expected =
        new DuckDbTypename.StructOf<>(
            "",
            List.of(
                new DuckDbTypename.StructOf.StructField(
                    "name", new DuckDbTypename.Base<>("VARCHAR")),
                new DuckDbTypename.StructOf.StructField(
                    "age", new DuckDbTypename.Base<>("INTEGER"))));
    assertEquals(expected, t);
  }

  @Test
  public void structWithQuotedIdentifier() {
    var t = DuckDbTypenameParser.parse("STRUCT(\"name\" VARCHAR, age INTEGER)");
    var expected =
        new DuckDbTypename.StructOf<>(
            "",
            List.of(
                new DuckDbTypename.StructOf.StructField(
                    "name", new DuckDbTypename.Base<>("VARCHAR")),
                new DuckDbTypename.StructOf.StructField(
                    "age", new DuckDbTypename.Base<>("INTEGER"))));
    assertEquals(expected, t);
  }

  @Test
  public void structArray() {
    DuckDbTypename<?> t = DuckDbTypenameParser.parse("STRUCT(name VARCHAR, age INTEGER)[]");
    assertTrue(t instanceof DuckDbTypename.ListOf<?> list
        && list.elementType() instanceof DuckDbTypename.StructOf<?> struct
        && struct.fields().size() == 2);
  }

  @Test
  public void nestedStruct() {
    DuckDbTypename<?> t =
        DuckDbTypenameParser.parse("STRUCT(outer VARCHAR, nested STRUCT(a INTEGER, b BOOLEAN))");
    assertTrue(t instanceof DuckDbTypename.StructOf<?> s
        && s.fields().size() == 2
        && s.fields().get(1).name().equals("nested")
        && s.fields().get(1).type() instanceof DuckDbTypename.StructOf<?>);
  }

  @Test
  public void structWithListField() {
    DuckDbTypename<?> t =
        DuckDbTypenameParser.parse("STRUCT(name VARCHAR, hobbies VARCHAR[])");
    assertTrue(t instanceof DuckDbTypename.StructOf<?> s
        && s.fields().size() == 2
        && s.fields().get(1).type() instanceof DuckDbTypename.ListOf<?>);
  }

  @Test
  public void mapType() {
    DuckDbTypename<?> t = DuckDbTypenameParser.parse("MAP(VARCHAR, INTEGER)");
    assertTrue(
        t instanceof DuckDbTypename.MapOf<?, ?> m
            && m.keyType().equals(new DuckDbTypename.Base<>("VARCHAR"))
            && m.valueType().equals(new DuckDbTypename.Base<>("INTEGER")));
  }

  @Test
  public void mapWithStructValue() {
    DuckDbTypename<?> t =
        DuckDbTypenameParser.parse("MAP(VARCHAR, STRUCT(id INTEGER, name VARCHAR))");
    assertTrue(
        t instanceof DuckDbTypename.MapOf<?, ?> m
            && m.valueType() instanceof DuckDbTypename.StructOf<?>);
  }

  @Test
  public void union() {
    DuckDbTypename<?> t = DuckDbTypenameParser.parse("UNION(num INTEGER, str VARCHAR)");
    assertTrue(
        t instanceof DuckDbTypename.UnionOf<?> u
            && u.members().size() == 2
            && u.members().get(0).tag().equals("num")
            && u.members().get(1).tag().equals("str"));
  }

  @Test
  public void structuralMatchIgnoresStructName() {
    // Declared via our DuckDbTypes.compositeOf carries the user's CREATE TYPE name; the
    // vendor-reported typename never has that name. Structural match must ignore it.
    var declared =
        new DuckDbTypename.StructOf<>(
            "person_t",
            List.of(
                new DuckDbTypename.StructOf.StructField(
                    "name", new DuckDbTypename.Base<>("VARCHAR")),
                new DuckDbTypename.StructOf.StructField(
                    "age", new DuckDbTypename.Base<>("INTEGER"))));
    var parsed =
        DuckDbTypenameParser.parse("STRUCT(\"name\" VARCHAR, age INTEGER)");
    assertTrue(QueryAnalysis.duckDbTypenamesMatch(declared, parsed));
  }

  @Test
  public void structuralMismatchOnFieldName() {
    var declared =
        new DuckDbTypename.StructOf<>(
            "person_t",
            List.of(
                new DuckDbTypename.StructOf.StructField(
                    "name", new DuckDbTypename.Base<>("VARCHAR")),
                new DuckDbTypename.StructOf.StructField(
                    "age", new DuckDbTypename.Base<>("INTEGER"))));
    var parsed =
        DuckDbTypenameParser.parse("STRUCT(name VARCHAR, years INTEGER)");
    assertTrue(!QueryAnalysis.duckDbTypenamesMatch(declared, parsed));
  }

  @Test
  public void structuralMismatchOnFieldType() {
    var declared =
        new DuckDbTypename.StructOf<>(
            "person_t",
            List.of(
                new DuckDbTypename.StructOf.StructField(
                    "name", new DuckDbTypename.Base<>("VARCHAR")),
                new DuckDbTypename.StructOf.StructField(
                    "age", new DuckDbTypename.Base<>("INTEGER"))));
    var parsed = DuckDbTypenameParser.parse("STRUCT(name VARCHAR, age BIGINT)");
    assertTrue(!QueryAnalysis.duckDbTypenamesMatch(declared, parsed));
  }

  @Test
  public void rejectsTrailingGarbage() {
    try {
      DuckDbTypenameParser.parse("VARCHAR junk");
      fail("expected parse failure");
    } catch (IllegalArgumentException expected) {
      // ok
    }
  }
}
