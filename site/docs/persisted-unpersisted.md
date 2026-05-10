---
title: Persisted vs Unpersisted Types
---

import Snippet from '@site/src/components/Snippet';

# Persisted vs unpersisted types

Most domain entities exist in two forms: one *before* the database has seen them (no id, no generated timestamps), and one *after* (with an id assigned by the database). Using the same type for both forces you to invent placeholder values for fields that don't exist yet:

```java
// The id here is meaningless — the database assigns it
var venue = new Venue(0, "Madison Square Garden", 20789);
```

Placeholder values are a data modelling smell. The type says "I have an id" when the value doesn't. A better model separates the two forms: `Venue` is what you *create*, and `PersistedVenue` is what the database *gives back*.

## Defining the types

<Snippet file="core/PersistedTypes" />

`Venue` holds the data you provide. `PersistedVenue` wraps a `Venue` together with its database-assigned id. The nesting is intentional: `PersistedVenue` doesn't re-declare `name` and `capacity`, and you can always extract the original `Venue` from a persisted one.

## Composing the codecs

The write codec (`venueCodec`) maps `Venue` to the columns the INSERT targets. The read codec (`persistedVenueCodec`) maps the full row, including the `id` column, back to `PersistedVenue`.

Rather than re-declaring all the venue fields in the read codec, compose it from parts:

<Snippet file="core/PersistedCodecs" />

Three methods make this work:

- **`RowCodec.ofNamed(name, type)`** — creates a single-column named codec
- **`.join(other)`** — concatenates two named codecs, producing a tuple (preserving all column names)
- **`.to(forward, backward)`** — maps the tuple to your domain type via a bijection

The result is a `RowCodecNamed<PersistedVenue>` that knows about all three columns (`id`, `name`, `capacity`), reusing the `venueCodec` definition for the last two.

## Building a repository

With both codecs defined, the repository is straightforward. The two-codec `insertIntoReturning` takes the write codec for the INSERT columns and the read codec for the RETURNING clause:

<Snippet file="core/PersistedRepo" />

The types tell the full story: `insert` takes a `Venue` and returns a `PersistedVenue`. No placeholder ids, no ambiguity about what goes in and what comes out.

## When to use this

This pattern is most useful when the persisted form differs from the input, typically when the database generates an id, a timestamp, or a version number.

For simple cases where you're happy to provide all fields up front (including the id), a single codec with `insertIntoReturning(table, codec)` works fine. Use whichever model fits your domain.
