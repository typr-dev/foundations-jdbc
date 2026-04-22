package dev.typr.foundations;

import static org.junit.Assert.*;

import org.junit.Test;

public class RedactedTypeTest {

  @Test
  public void redactedTypeHidesValueInInterpolation() {
    var password = DuckDbTypes.text.redacted();

    var frag =
        Fragment.of("INSERT INTO users (name, pass) VALUES (")
            .value(DuckDbTypes.text, "alice")
            .append(", ")
            .value(password, "s3cret")
            .append(")");

    String interpolated = frag.renderInterpolated();
    assertTrue(interpolated.contains("'alice'"));
    assertTrue(interpolated.contains("<redacted>"));
    assertFalse(interpolated.contains("s3cret"));
  }

  @Test
  public void redactedTypePreservesEncoding() {
    var password = DuckDbTypes.text.redacted();

    var frag = Fragment.of("SELECT ").value(password, "s3cret");

    String rendered = frag.render();
    assertTrue(rendered.contains("?"));
    assertFalse(rendered.contains("s3cret"));
    assertFalse(rendered.contains("<redacted>"));
  }

  @Test
  public void isRedactedReturnsTrueForRedacted() {
    assertFalse(DuckDbTypes.text.isRedacted());
    assertTrue(DuckDbTypes.text.redacted().isRedacted());
  }

  @Test
  public void redactedOfRedactedIsIdempotent() {
    var r1 = DuckDbTypes.text.redacted();
    var r2 = r1.redacted();
    assertSame(r1, r2);
  }

  @Test
  public void redactedOptPreservesRedaction() {
    var redactedOpt = DuckDbTypes.text.redacted().opt();
    assertTrue(redactedOpt.isRedacted());

    var frag = Fragment.of("SELECT ").value(redactedOpt, java.util.Optional.of("secret"));
    String interpolated = frag.renderInterpolated();
    assertTrue(interpolated.contains("<redacted>"));
    assertFalse(interpolated.contains("secret"));
  }

  @Test
  public void redactedToPreservesRedaction() {
    var redactedUpper =
        DuckDbTypes.text.redacted().to(Bijection.of(String::toUpperCase, String::toLowerCase));
    assertTrue(redactedUpper.isRedacted());

    var frag = Fragment.of("SELECT ").value(redactedUpper, "SECRET");
    String interpolated = frag.renderInterpolated();
    assertTrue(interpolated.contains("<redacted>"));
    assertFalse(interpolated.contains("SECRET"));
  }

  @Test
  public void nonRedactedShowsValue() {
    var frag = Fragment.of("SELECT ").value(DuckDbTypes.text, "visible");
    String interpolated = frag.renderInterpolated();
    assertTrue(interpolated.contains("'visible'"));
    assertFalse(interpolated.contains("<redacted>"));
  }
}
