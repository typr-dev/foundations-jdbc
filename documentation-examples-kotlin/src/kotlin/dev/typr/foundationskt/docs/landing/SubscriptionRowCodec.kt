package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.Instant
import java.util.UUID

@Suppress("unused")
class SubscriptionRowCodec {
    enum class Plan { free, pro, team }

    data class Subscription(
        val id: UUID, val email: String, val plan: Plan,
        val activeRange: Range<Instant>,
        val metadata: Jsonb?,
        val cancelledAt: Instant?
    )

    //start
    val plan: PgType<Plan> = PgTypes.ofEnum<Plan>("plan_tier")

    val subscriptionCodec: RowCodecNamed<Subscription> =
        RowCodec.namedBuilder<Subscription>()
            .field("id",           PgTypes.uuid,              Subscription::id)
            .field("email",        PgTypes.text,              Subscription::email)
            .field("plan",         plan,                      Subscription::plan)
            .field("active_range", PgTypes.tstzrange,         Subscription::activeRange)
            .field("metadata",     PgTypes.jsonb.opt(),       Subscription::metadata)
            .field("cancelled_at", PgTypes.timestamptz.opt(), Subscription::cancelledAt)
            .build(::Subscription)
    //stop
}
