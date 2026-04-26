package dev.typr.foundations.docs.landing;

import dev.typr.foundations.data.Jsonb;
import dev.typr.foundations.data.Range;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class SubscriptionRow {
  enum Plan { free, pro, team }

  // start
  record Subscription(
      UUID id,
      String email,
      Plan plan,
      Range<Instant> activeRange,
      Optional<Jsonb> metadata,
      Optional<Instant> cancelledAt
  ) {}
  // stop
}
