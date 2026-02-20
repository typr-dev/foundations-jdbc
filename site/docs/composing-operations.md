---
title: Composing Operations
---

import Snippet from '@site/src/components/Snippet';

# Composing Operations

Operations can be composed as values — combined, sequenced, and chained — so that multiple database actions run in a single transaction without manual connection handling.

## Combining Independent Operations

`.with()` combines two operations that don't depend on each other. Both run in the same transaction, and a function combines their results:

<Snippet file="core/ComposingWith" />

## Running Multiple Writes

When you have several write operations and only care about completion (not individual results), use `Operation.allOf()`:

<Snippet file="core/ComposingAllOf" />

## Sequencing a List

When you have a dynamic list of operations, `Operation.sequence()` runs them all and collects the results:

<Snippet file="core/ComposingSequence" />

## Data Flow Between Operations

Use `.then()` to feed one operation's result into the next operation's [SQL template](./sql-templates). The first operation runs, and its result becomes the input to the template:

<Snippet file="core/SqlTemplateThen" />

When the first operation returns a record and the template uses `.from()`, use `.then()` with the `SqlTemplate.From` directly:

<Snippet file="core/SqlTemplateThenFrom" />

## Conditional Execution

`Operation.ifEmpty()` implements the find-or-create pattern: run the first operation, and if it returns empty (empty Optional, null, or None), run the fallback instead:

<Snippet file="core/ComposingIfEmpty" />

## Analyzing Composed Operations

`QueryAnalyzer` can walk the entire operation tree and analyze every SQL statement in one call. See [Query Analysis](./query-analysis#analyzing-composed-operations) for details.

<Snippet file="analysis/QueryAnalysisAll" />
