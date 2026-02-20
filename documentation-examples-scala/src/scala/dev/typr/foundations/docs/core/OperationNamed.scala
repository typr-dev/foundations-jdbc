package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.time.Duration

@SuppressWarnings(Array("unused"))
object OperationNamed:
  //start
  val users: Operation[List[String]] =
    Fragment.of("SELECT name FROM users")
      .queryAll(PgTypes.text)
      .named("load-users")
      .timeout(Duration.ofSeconds(5))
  //stop
