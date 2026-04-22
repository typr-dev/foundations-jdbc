package dev.typr.foundations;

import java.time.Duration;
import java.util.Optional;

public record TransactionEvent(Duration elapsed, Optional<Throwable> error) {}
