package dev.typr.foundationssc

trait Analyzable:
  def analyzable: dev.typr.foundations.Analyzable

  /**
   * Human-readable description. Non-verbose returns just the name for named analyzables;
   * verbose appends the detailed rendering. Unnamed analyzables always return the detailed
   * rendering.
   */
  def description: String = analyzable.description(false)
  def description(verbose: Boolean): String = analyzable.description(verbose)
