package dev.typr.foundations;

/**
 * Definition of a stored procedure parameter, used by {@link Procedure} builders. Tracks the
 * {@link DbType}, the parameter {@link Mode} (IN, OUT, or INOUT), and the {@link DbOutParam}
 * for output parameters. Values for IN and INOUT parameters are provided at call time via
 * {@link Procedure#call(Object...)}.
 */
public record ParamDef(DbType<?> type, Mode mode, DbOutParam<?> outParam) {

  public enum Mode {
    IN,
    OUT,
    INOUT
  }

  /** Create a ParamDef for an OUT or INOUT parameter with an explicit outParam. */
  public static ParamDef of(DbType<?> type, Mode mode) {
    DbOutParam<?> cr = type.outParam().orElseThrow(() ->
        new UnsupportedOperationException(
            "Type " + type.typename().sqlType()
                + " does not support callable statement reading (no outParam)"));
    return new ParamDef(type, mode, cr);
  }

  /** Create a ParamDef for an IN parameter (no outParam needed). */
  public static ParamDef input(DbType<?> type) {
    return new ParamDef(type, Mode.IN, null);
  }

  /** Returns true if this parameter receives an input value (IN or INOUT). */
  public boolean isInput() {
    return mode == Mode.IN || mode == Mode.INOUT;
  }

  /** Returns true if this parameter produces an output value (OUT or INOUT). */
  public boolean isOutput() {
    return mode == Mode.OUT || mode == Mode.INOUT;
  }
}
