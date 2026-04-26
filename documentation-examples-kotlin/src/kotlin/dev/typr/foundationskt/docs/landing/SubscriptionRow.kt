package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.data.*
import java.time.Instant
import java.util.UUID

@Suppress("unused")
class SubscriptionRow {
    enum class Plan { free, pro, team }

    //start
    data class Subscription(
        val id: UUID,
        val email: String,
        val plan: Plan,
        val activeRange: Range<Instant>,
        val metadata: Jsonb?,
        val cancelledAt: Instant?
    )
    //stop
}
