package dev.typr.foundations.docs.test

import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


object ExportTest:
  // Test if types are accessible
  val t: PgType[Int] = PgTypes.int4

  // Test if Fragment is accessible as a type
  def useFragment(f: Fragment): String = f.render()
