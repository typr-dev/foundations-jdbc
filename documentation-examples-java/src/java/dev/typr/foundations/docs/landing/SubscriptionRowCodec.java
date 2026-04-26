package dev.typr.foundations.docs.landing;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.RowCodecNamed;
import dev.typr.foundations.data.Jsonb;
import dev.typr.foundations.data.Range;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class SubscriptionRowCodec {
  enum Plan { free, pro, team }

  record Subscription(
      UUID id, String email, Plan plan,
      Range<Instant> activeRange,
      Optional<Jsonb> metadata,
      Optional<Instant> cancelledAt) {}

  // start
  static final PgType<Plan> plan = PgTypes.ofEnum("plan_tier", Plan.values());

  static final RowCodecNamed<Subscription> subscriptionCodec =
      RowCodec.<Subscription>namedBuilder()
          .field("id",           PgTypes.uuid,              Subscription::id)
          .field("email",        PgTypes.text,              Subscription::email)
          .field("plan",         plan,                      Subscription::plan)
          .field("active_range", PgTypes.tstzrange,         Subscription::activeRange)
          .field("metadata",     PgTypes.jsonb.opt(),       Subscription::metadata)
          .field("cancelled_at", PgTypes.timestamptz.opt(), Subscription::cancelledAt)
          .build(Subscription::new);
  // stop
}
