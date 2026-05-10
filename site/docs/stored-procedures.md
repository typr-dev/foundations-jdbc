---
title: Stored Procedures & Functions
---

import Snippet from '@site/src/components/Snippet';

# Stored procedures and functions

Define stored procedures and functions once with full type safety on inputs and outputs. The builder tracks types statically, so wrong argument types or missing parameters are compile errors.

## Procedures

### Void procedures

Procedures with only IN parameters return `Void` (or `Unit` in Scala/Kotlin):

<Snippet file="routines/VoidProcedure" />

### OUT parameters

Add `.out()` to declare output parameters. The builder tracks their types:

<Snippet file="routines/OutProcedure" />

The builder returns a typed definition that captures the input and output parameter types. The numbers in the type name encode the arity: `Def1_2` means 1 input and 2 outputs. For functions, `Def2` means 2 inputs (functions always have exactly one output, the return value). You never need to write these types explicitly. Use `var` in Java, or let type inference work in Kotlin and Scala.

### INOUT parameters

INOUT parameters count as both input and output. The value goes in and comes back modified:

<Snippet file="routines/InoutProcedure" />

## Functions

Functions return a single value via `SELECT` instead of `CALL`:

<Snippet file="routines/FunctionExample" />

## Any database

The procedure and function builders work with any database's types. Use the matching `*Types` class:

<Snippet file="routines/OracleExample" />

## How it works

| Feature | Procedures (`DbProcedure`) | Functions (`DbFunction`) |
|---------|---------------------------|-------------------------|
| SQL | `{call proc_name(?, ...)}` | `SELECT func_name(?, ...)` |
| Statement | `CallableStatement` | `PreparedStatement` |
| Inputs | `.input()` | `.input()` |
| Outputs | `.out()`, `.inout()` | Return type (single value) |
| Max arity | 10 inputs, 10 outputs | 10 inputs |
| Result | `Void`, single value, or `Tuple` | Single value |

## Verifying against the database

Use [Query Analysis](/docs/query-analysis#routine-analysis) to verify that your procedure and function definitions match the database. Parameter count, types, modes, and return types are all checked.

## Database support

OUT parameter support requires a `DbOutParam` implementation for each type. All standard types are supported on every database that has procedures and functions; DuckDB and SQLite have neither and surface that fact through `DbType.outParam()` returning `Optional.empty()`:

| Database | Procedures | Functions | OUT/INOUT |
|----------|-----------|-----------|-----------|
| PostgreSQL | Yes | Yes | Yes |
| MariaDB/MySQL | Yes | Yes | Yes |
| DuckDB | N/A | N/A | No (no stored procedure support) |
| SQLite | N/A | N/A | No (no stored procedure support) |
| Oracle | Yes | Yes | Yes |
| SQL Server | Yes | Yes | Yes |
| DB2 | Yes | Yes | Yes |
