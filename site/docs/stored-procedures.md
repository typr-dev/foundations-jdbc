---
title: Stored Procedures & Functions
---

import Snippet from '@site/src/components/Snippet';

# Stored Procedures & Functions

Define stored procedures and functions once with full type safety on both inputs and outputs. The builder tracks types statically — wrong argument types or missing parameters are compile errors, not runtime surprises.

## Procedures

### Void Procedures

Procedures with only IN parameters return `Void` (or `Unit` in Scala/Kotlin):

<Snippet file="routines/VoidProcedure" />

### OUT Parameters

Add `.out()` to declare output parameters. The builder tracks their types:

<Snippet file="routines/OutProcedure" />

The type signature `Def1_2<Integer, String, String>` means: 1 input (`Integer`), 2 outputs (`String`, `String`). Multiple outputs are returned as a `Tuple`.

### INOUT Parameters

INOUT parameters count as both input and output. The value goes in and comes back modified:

<Snippet file="routines/InoutProcedure" />

## Functions

Functions return a single value via `SELECT` instead of `CALL`:

<Snippet file="routines/FunctionExample" />

## Any Database

The procedure and function builders work with any database's types. Just use the right `*Types` class:

<Snippet file="routines/OracleExample" />

## How It Works

| Feature | Procedures (`DbProcedure`) | Functions (`DbFunction`) |
|---------|---------------------------|-------------------------|
| SQL | `{call proc_name(?, ...)}` | `SELECT func_name(?, ...)` |
| Statement | `CallableStatement` | `PreparedStatement` |
| Inputs | `.in()` | `.in()` |
| Outputs | `.out()`, `.inout()` | Return type (single value) |
| Max arity | 10 inputs, 10 outputs | 10 inputs |
| Result | `Void`, single value, or `Tuple` | Single value |

## Verifying Against the Database

Use [Query Analysis](/docs/query-analysis#routine-analysis) to verify that your procedure and function definitions match what's actually in the database — parameter count, types, modes, and return types are all checked.

## Database Support

OUT parameter support requires a `DbOutParam` implementation for each type. All standard types are supported for all six databases:

| Database | Procedures | Functions | OUT/INOUT |
|----------|-----------|-----------|-----------|
| PostgreSQL | Yes | Yes | Yes |
| MariaDB/MySQL | Yes | Yes | Yes |
| DuckDB | N/A | N/A | No (no stored procedure support) |
| Oracle | Yes | Yes | Yes |
| SQL Server | Yes | Yes | Yes |
| DB2 | Yes | Yes | Yes |
