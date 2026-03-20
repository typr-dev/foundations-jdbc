package dev.typr.foundationssc.docs.test

import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

object ExportTest:
  // Test if types are accessible
  val t: PgType[Int] = PgTypes.int4

  // Test if Fragment is accessible as a type
  def useFragment(f: Fragment): String = f.render()
