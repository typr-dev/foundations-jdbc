package dev.typr.foundationssc.docs.landing

import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*
import java.time.Instant
import java.util.UUID

@SuppressWarnings(Array("unused"))
object SubscriptionRowCodec:
  enum Plan:
    case free, pro, team

  case class Subscription(
      id: UUID,
      email: String,
      plan: Plan,
      activeRange: Range[Instant],
      metadata: Option[Jsonb],
      cancelledAt: Option[Instant]
  )

  // start
  val plan: PgType[Plan] = PgTypes.ofEnum("plan_tier", Plan.values)

  val subscriptionCodec: RowCodecNamed[Subscription] = RowCodec
    .namedBuilder[Subscription]()
    .field("id",           PgTypes.uuid)(_.id)
    .field("email",        PgTypes.text)(_.email)
    .field("plan",         plan)(_.plan)
    .field("active_range", PgTypes.tstzrange)(_.activeRange)
    .field("metadata",     PgTypes.jsonb.opt)(_.metadata)
    .field("cancelled_at", PgTypes.timestamptz.opt)(_.cancelledAt)
    .build(Subscription.apply)
  // stop
