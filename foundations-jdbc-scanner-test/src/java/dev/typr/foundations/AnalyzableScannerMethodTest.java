package dev.typr.foundations;

import static org.junit.jupiter.api.Assertions.*;

import dev.typr.foundations.fixtures.directives.DirectiveFixtures;
import dev.typr.foundations.fixtures.instance.InstanceFixtures;
import dev.typr.foundations.fixtures.strict.StrictFixtures;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class AnalyzableScannerMethodTest {

  // --- Method scanning basics ---

  @Test
  void findsFieldsAndMethods() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.methods");
    var names = fieldNames(results);
    assertTrue(names.contains("fieldQuery"), "should find field");
    assertTrue(names.contains("noArgMethod"), "should find no-arg method");
  }

  @Test
  void findsMethodsWithArgs() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.methods");
    assertTrue(
        fieldNames(results).contains("withArgs"), "should find method with constructible args");
  }

  @Test
  void findsTemplateMethods() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.methods");
    assertTrue(
        fieldNames(results).contains("templateMethod"), "should find Template-returning method");
  }

  @Test
  void findsPrivateMethods() {
    // The scanner is a test-scope tool — private / package-private methods are fair game and
    // get unlocked via setAccessible(true). Keeping them hidden was a footgun for callers whose
    // repo classes weren't deliberately public.
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.methods");
    var names = fieldNames(results);
    assertTrue(names.contains("privateMethod"), "should find private method via reflection");
  }

  @Test
  void skipsStaticNonAnalyzable() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.methods");
    var names = fieldNames(results);
    assertFalse(names.contains("staticMethod"), "should not find instance-scan static method");
    assertFalse(names.contains("notAnalyzable"), "should not find non-analyzable method");
  }

  // --- Dummy arg construction (unit tests) ---

  @Test
  void constructDummyPrimitives() {
    assertEquals(false, constructDummy(boolean.class));
    assertEquals((byte) 0, constructDummy(byte.class));
    assertEquals((short) 0, constructDummy(short.class));
    assertEquals(0, constructDummy(int.class));
    assertEquals(0L, constructDummy(long.class));
    assertEquals(0.0f, constructDummy(float.class));
    assertEquals(0.0, constructDummy(double.class));
    assertEquals('\0', constructDummy(char.class));
    assertEquals("", constructDummy(String.class));
  }

  @Test
  void constructDummyEnum() {
    enum TestEnum {
      A,
      B,
      C
    }
    assertEquals(TestEnum.A, constructDummy(TestEnum.class));
  }

  @Test
  void constructDummyRecord() {
    record TestRecord(String name, int count) {}
    var dummy = constructDummy(TestRecord.class);
    assertInstanceOf(TestRecord.class, dummy);
    var r = (TestRecord) dummy;
    assertEquals("", r.name());
    assertEquals(0, r.count());
  }

  @Test
  void constructDummyTimeTypes() {
    assertEquals(LocalDate.of(2000, 1, 1), constructDummy(LocalDate.class));
    assertEquals(Instant.EPOCH, constructDummy(Instant.class));
    assertEquals(
        OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
        constructDummy(OffsetDateTime.class));
  }

  @Test
  void constructDummyCollections() {
    assertEquals(Optional.empty(), constructDummy(Optional.class));
    assertEquals(List.of(), constructDummy(List.class));
    assertEquals(Map.of(), constructDummy(Map.class));
  }

  @Test
  void constructDummyArray() {
    var dummy = constructDummy(int[].class);
    assertInstanceOf(int[].class, dummy);
    assertEquals(0, ((int[]) dummy).length);
  }

  @Test
  void constructDummyCustomClass() {
    var dummy =
        constructDummy(dev.typr.foundations.fixtures.dummyargs.DummyArgFixtures.Config.class);
    assertNotNull(dummy);
  }

  @Test
  void constructDummyThrowsForInterface() {
    assertThrows(IllegalArgumentException.class, () -> constructDummy(Runnable.class));
  }

  @Test
  void constructDummyKotlinDataClass() throws ClassNotFoundException {
    var klazz = Class.forName("dev.typr.foundationskt.docs.duckdb.DomainType$ProductId");
    var dummy = constructDummy(klazz);
    assertNotNull(dummy);
  }

  // --- Integration: dummy args → scan ---

  @Test
  void dummyArgMethodsAllDiscovered() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.dummyargs");
    var names = fieldNames(results);
    for (var expected :
        List.of(
            "withEnum",
            "withRecord",
            "withLocalDate",
            "withInstant",
            "withOffsetDateTime",
            "withUUID",
            "withList",
            "withOptional",
            "withMap",
            "withBigDecimal",
            "withPrimitives",
            "withArray",
            "withCustomClass")) {
      assertTrue(names.contains(expected), "should find method: " + expected);
    }
  }

  // --- Getter dedup ---

  @Test
  void scalaStyleGetterDeduped() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.dedup");
    // field "query" + method "query(int)" = 2; the 0-arg query() getter is deduped
    long queryCount = results.stream().filter(r -> r.fieldName().equals("query")).count();
    assertEquals(2, queryCount, "query field + query(int) method, no 0-arg getter duplicate");
  }

  @Test
  void kotlinStyleGetterDeduped() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.dedup");
    assertTrue(
        results.stream().noneMatch(r -> r.fieldName().equals("getUserQuery")),
        "getUserQuery() should be deduped as getter for userQuery field");
    assertTrue(
        results.stream().anyMatch(r -> r.fieldName().equals("userQuery")),
        "userQuery field should be present");
  }

  @Test
  void methodWithArgsSameNameNotDeduped() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.dedup");
    // query(int) has args → not a getter → should appear alongside the field
    long queryCount = results.stream().filter(r -> r.fieldName().equals("query")).count();
    assertTrue(queryCount >= 2, "query(int) method should appear alongside field");
  }

  @Test
  void kotlinDocSnippetsNoDuplicates() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundationskt.docs.core");
    assertNoDuplicateFieldNames(results);
  }

  @Test
  void scalaDocSnippetsNoDuplicates() {
    var results = AnalyzableScanner.scanDetailed("dev.typr.foundationssc.docs.core");
    assertNoDuplicateFieldNames(results);
  }

  // --- Directives: skip ---

  @Test
  void skipDirective() {
    var fixtures = new DirectiveFixtures();
    var results =
        AnalyzableScanner.scanDetailed(
            "dev.typr.foundations.fixtures.directives",
            ScanDirective.skip(fixtures::toBeSkipped),
            ScanDirective.skip(fixtures::needsManual));
    var names = fieldNames(results);
    assertFalse(names.contains("toBeSkipped"), "skipped method should be absent");
    assertTrue(names.contains("normalMethod"), "normal method should be present");
  }

  // --- Directives: manual ---

  @Test
  void manualDirective() {
    var fixtures = new DirectiveFixtures();
    var results =
        AnalyzableScanner.scanDetailed(
            "dev.typr.foundations.fixtures.directives",
            ScanDirective.manual(fixtures::needsManual, "v1", (Runnable) () -> {}));
    var names = fieldNames(results);
    assertTrue(names.contains("needsManual[v1]"), "manual variant should be present");
  }

  @Test
  void multipleManualVariants() {
    var fixtures = new DirectiveFixtures();
    var results =
        AnalyzableScanner.scanDetailed(
            "dev.typr.foundations.fixtures.directives",
            ScanDirective.manual(fixtures::needsManual, "v1", (Runnable) () -> {}),
            ScanDirective.manual(fixtures::needsManual, "v2", (Runnable) () -> {}));
    var names = fieldNames(results);
    assertTrue(names.contains("needsManual[v1]"), "first manual variant should be present");
    assertTrue(names.contains("needsManual[v2]"), "second manual variant should be present");
  }

  // --- Directives: instance ---

  @Test
  void instanceAddsExternalObject() {
    var dirFixtures = new DirectiveFixtures();
    var instanceFixtures = new InstanceFixtures();
    var results =
        AnalyzableScanner.scanDetailed(
            "dev.typr.foundations.fixtures.directives",
            ScanDirective.skip(dirFixtures::needsManual),
            ScanDirective.instance(
                instanceFixtures, (inst, cfg) -> cfg.skip(inst::uninvokableMethod)));
    var names = fieldNames(results);
    assertTrue(names.contains("fieldOp"), "instance field should be present");
    assertTrue(names.contains("instanceMethod"), "instance method should be present");
  }

  @Test
  void instanceWithSkipOverride() {
    var dirFixtures = new DirectiveFixtures();
    var instanceFixtures = new InstanceFixtures();
    var results =
        AnalyzableScanner.scanDetailed(
            "dev.typr.foundations.fixtures.directives",
            ScanDirective.skip(dirFixtures::needsManual),
            ScanDirective.instance(
                instanceFixtures,
                (inst, cfg) -> {
                  cfg.skip(inst::instanceMethod);
                  cfg.skip(inst::uninvokableMethod);
                }));
    assertTrue(
        results.stream()
            .anyMatch(
                r -> r.className().equals("InstanceFixtures") && r.fieldName().equals("fieldOp")),
        "instance field should be present");
    assertTrue(
        results.stream()
            .noneMatch(
                r ->
                    r.className().equals("InstanceFixtures")
                        && r.fieldName().equals("instanceMethod")),
        "skipped instance method should be absent");
  }

  @Test
  void instanceWithManualOverride() {
    var dirFixtures = new DirectiveFixtures();
    var instanceFixtures = new InstanceFixtures();
    var results =
        AnalyzableScanner.scanDetailed(
            "dev.typr.foundations.fixtures.directives",
            ScanDirective.skip(dirFixtures::needsManual),
            ScanDirective.instance(
                instanceFixtures,
                (inst, cfg) -> cfg.manual(inst::uninvokableMethod, "manual", (Runnable) () -> {})));
    assertTrue(
        results.stream()
            .anyMatch(
                r ->
                    r.className().equals("InstanceFixtures")
                        && r.fieldName().equals("uninvokableMethod[manual]")),
        "manual override for uninvokable method should be present");
  }

  // --- Always-strict error handling ---

  @Test
  void throwsOnUninvokable() {
    var ex =
        assertThrows(
            IllegalStateException.class,
            () -> AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.strict"));
    assertTrue(ex.getMessage().contains("uninvokable"), "error should mention the method name");
    assertTrue(ex.getMessage().contains("ScanDirective.skip"), "error should suggest skip()");
  }

  @Test
  void errorMessageIncludesCopyPasteableDirectives() {
    var ex =
        assertThrows(
            IllegalStateException.class,
            () -> AnalyzableScanner.scanDetailed("dev.typr.foundations.fixtures.strict"));
    var msg = ex.getMessage();
    assertTrue(msg.contains("StrictFixtures.class"), "should include class name for copy-paste");
    assertTrue(msg.contains("\"uninvokable\""), "should include method name in quotes");
  }

  @Test
  void skipByClassAndName() {
    var results =
        AnalyzableScanner.scanDetailed(
            "dev.typr.foundations.fixtures.strict",
            ScanDirective.skip(StrictFixtures.class, "uninvokable"));
    var names = fieldNames(results);
    assertTrue(names.contains("workingMethod"));
    assertFalse(names.contains("uninvokable"));
  }

  @Test
  void queryCheckerCreate() {
    var t =
        new Transactor(
            () -> {
              throw new UnsupportedOperationException();
            },
            Transactor.defaultStrategy());
    var checker = QueryChecker.create(t);
    assertNotNull(checker);
    assertEquals(t, checker.transactor());
  }

  // --- SerializedLambda extraction ---

  @Test
  void skipExtractsMethodIdentity() {
    var fixtures = new DirectiveFixtures();
    var directive = ScanDirective.skip(fixtures::toBeSkipped);
    assertInstanceOf(ScanDirective.SkipMethod.class, directive);
    var skip = (ScanDirective.SkipMethod) directive;
    assertEquals(
        "dev.typr.foundations.fixtures.directives.DirectiveFixtures", skip.declaringClass());
    assertEquals("toBeSkipped", skip.methodName());
  }

  @Test
  void manualExtractsMethodIdentity() {
    var fixtures = new DirectiveFixtures();
    var directive = ScanDirective.manual(fixtures::needsManual, "v1", (Runnable) () -> {});
    assertInstanceOf(ScanDirective.ManualMethod.class, directive);
    var manual = (ScanDirective.ManualMethod) directive;
    assertEquals(
        "dev.typr.foundations.fixtures.directives.DirectiveFixtures", manual.declaringClass());
    assertEquals("needsManual", manual.methodName());
    assertEquals("v1", manual.variantName());
  }

  // --- Helpers ---

  private static Object constructDummy(Class<?> type) {
    return AnalyzableScanner.constructDummy(type, new HashSet<>());
  }

  private static Set<String> fieldNames(List<AnalyzableScanner.Result> results) {
    return results.stream().map(AnalyzableScanner.Result::fieldName).collect(Collectors.toSet());
  }

  private static void assertNoDuplicateFieldNames(List<AnalyzableScanner.Result> results) {
    var byClass =
        results.stream().collect(Collectors.groupingBy(AnalyzableScanner.Result::className));
    for (var entry : byClass.entrySet()) {
      var names = entry.getValue().stream().map(AnalyzableScanner.Result::fieldName).toList();
      var unique = new HashSet<>(names);
      assertEquals(
          unique.size(), names.size(), "Duplicate fieldNames in " + entry.getKey() + ": " + names);
    }
  }
}
