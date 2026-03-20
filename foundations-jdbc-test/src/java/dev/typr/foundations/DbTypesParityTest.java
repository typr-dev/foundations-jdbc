package dev.typr.foundations;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import org.junit.Test;

/**
 * Ensures that Scala and Kotlin type facades expose the same fields as the Java interfaces. This
 * prevents forgetting to forward a type when adding new types to the Java interface.
 */
public class DbTypesParityTest {

  // Fields that are intentionally excluded from parity checks
  private static final Set<String> EXCLUDED_FIELDS =
      Set.of(
          // Scala/Kotlin internal fields
          "MODULE$",
          "$VALUES",
          "Companion",
          // Static method helpers that don't need forwarding
          "mapJson");

  @Test
  public void pgTypesFieldsParity() {
    assertFieldsParity(
        PgTypes.class,
        dev.typr.foundationssc.PgTypes$.class,
        dev.typr.foundationskt.PgTypes.Companion.getClass());
  }

  @Test
  public void mariaTypesFieldsParity() {
    assertFieldsParity(
        MariaTypes.class,
        dev.typr.foundationssc.MariaTypes$.class,
        dev.typr.foundationskt.MariaTypes.Companion.getClass());
  }

  @Test
  public void duckDbTypesFieldsParity() {
    assertFieldsParity(
        DuckDbTypes.class,
        dev.typr.foundationssc.DuckDbTypes$.class,
        dev.typr.foundationskt.DuckDbTypes.Companion.getClass());
  }

  @Test
  public void oracleTypesFieldsParity() {
    assertFieldsParity(
        OracleTypes.class,
        dev.typr.foundationssc.OracleTypes$.class,
        dev.typr.foundationskt.OracleTypes.Companion.getClass());
  }

  @Test
  public void sqlServerTypesFieldsParity() {
    assertFieldsParity(
        SqlServerTypes.class,
        dev.typr.foundationssc.SqlServerTypes$.class,
        dev.typr.foundationskt.SqlServerTypes.Companion.getClass());
  }

  @Test
  public void db2TypesFieldsParity() {
    assertFieldsParity(
        Db2Types.class,
        dev.typr.foundationssc.Db2Types$.class,
        dev.typr.foundationskt.Db2Types.Companion.getClass());
  }

  private void assertFieldsParity(
      Class<?> javaInterface, Class<?> scalaObject, Class<?> kotlinCompanion) {
    Set<String> javaFields = getJavaInterfaceFields(javaInterface);
    Set<String> scalaFields = getScalaObjectFields(scalaObject);
    Set<String> kotlinFields = getKotlinCompanionFields(kotlinCompanion);

    // Check Scala has all Java fields
    Set<String> missingInScala = new TreeSet<>(javaFields);
    missingInScala.removeAll(scalaFields);
    if (!missingInScala.isEmpty()) {
      fail(
          "Scala "
              + scalaObject.getSimpleName()
              + " is missing fields from Java "
              + javaInterface.getSimpleName()
              + ": "
              + missingInScala);
    }

    // Check Kotlin has all Java fields
    Set<String> missingInKotlin = new TreeSet<>(javaFields);
    missingInKotlin.removeAll(kotlinFields);
    if (!missingInKotlin.isEmpty()) {
      fail(
          "Kotlin "
              + kotlinCompanion.getSimpleName()
              + " is missing fields from Java "
              + javaInterface.getSimpleName()
              + ": "
              + missingInKotlin);
    }
  }

  /** Get field names from Java interface (static fields) */
  private Set<String> getJavaInterfaceFields(Class<?> clazz) {
    Set<String> fields = new HashSet<>();
    for (Field field : clazz.getDeclaredFields()) {
      if (Modifier.isPublic(field.getModifiers())
          && Modifier.isStatic(field.getModifiers())
          && !EXCLUDED_FIELDS.contains(field.getName())
          && isDbType(field.getType())) {
        fields.add(field.getName());
      }
    }
    return fields;
  }

  /** Get field names from Scala object (via getter methods) */
  private Set<String> getScalaObjectFields(Class<?> clazz) {
    Set<String> fields = new HashSet<>();
    for (Method method : clazz.getMethods()) {
      if (Modifier.isPublic(method.getModifiers())
          && method.getParameterCount() == 0
          && !method.getName().contains("$")
          && !EXCLUDED_FIELDS.contains(method.getName())
          && isDbType(method.getReturnType())) {
        fields.add(method.getName());
      }
    }
    return fields;
  }

  /** Get field names from Kotlin companion (via getter methods) */
  private Set<String> getKotlinCompanionFields(Class<?> clazz) {
    Set<String> fields = new HashSet<>();
    for (Method method : clazz.getMethods()) {
      String name = method.getName();
      // Kotlin generates getXxx() methods for properties
      if (Modifier.isPublic(method.getModifiers())
          && method.getParameterCount() == 0
          && name.startsWith("get")
          && name.length() > 3
          && !EXCLUDED_FIELDS.contains(name)
          && isDbType(method.getReturnType())) {
        // Convert getXxx to xxx
        String fieldName = Character.toLowerCase(name.charAt(3)) + name.substring(4);
        fields.add(fieldName);
      }
    }
    return fields;
  }

  /** Check if the type is a DbType or related type */
  private boolean isDbType(Class<?> type) {
    String name = type.getSimpleName();
    return name.equals("PgType")
        || name.equals("MariaType")
        || name.equals("DuckDbType")
        || name.equals("OracleType")
        || name.equals("SqlServerType")
        || name.equals("Db2Type");
  }
}
