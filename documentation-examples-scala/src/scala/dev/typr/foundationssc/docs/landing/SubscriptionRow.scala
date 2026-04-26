package dev.typr.foundationssc.docs.landing

import dev.typr.foundationssc.data.*
import java.time.Instant
import java.util.UUID

@SuppressWarnings(Array("unused"))
object SubscriptionRow:
  enum Plan:
    case free, pro, team

  // start
  case class Subscription(
      id: UUID,
      email: String,
      plan: Plan,
      activeRange: Range[Instant],
      metadata: Option[Jsonb],
      cancelledAt: Option[Instant]
  )
  // stop
