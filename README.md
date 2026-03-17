# TypeResolver

A lightweight static analysis component that infers the type of a PHP variable
from its `@var` PHPDoc tag.

## Problem

Given a `PhpVariable` with an optional doc block, determine its declared type.
Handles plain types, union types, named tags, and multi-tag doc blocks.

## Behavior

| Doc block                               | Variable | Result                        |
|-----------------------------------------|----------|-------------------------------|
| `/** @var User */`                      | `$user`  | `User`                        |
| `/** @var string\|int */`               | `$id`    | `UnionType(string, int)`      |
| `/** @var string \| int */`             | `$id`    | `UnionType(string, int)`      |
| `/** @var string\|int\|null */`         | `$val`   | `UnionType(string, int, null)`|
| `/** @var Logger $log */`               | `$log`   | `Logger`                      |
| `/** @var Admin $adm */`               | `$guest` | `mixed` (name mismatch)       |
| `/** @var int $id @var string $name */` | `$name`  | `string`                      |
| *(no doc block)*                        | any      | `mixed`                       |
| *(doc block with no `@var` tags)*       | any      | `mixed`                       |

## Tag resolution order

1. **Named match** — tag of the form `@var Type $varName` where `$varName`
   (always the **last** whitespace-delimited token) equals the variable's name.
   Supports spaced union types: `@var string | int $id`.
2. **Unnamed fallback** — tag of the form `@var Type` with no `$`-prefixed
   token at the end. Used when no named match exists.
3. **No match** → `mixed`.

## Usage

```java
TypeResolver resolver = new TypeResolver();
PhpType type = resolver.inferTypeFromDoc(variable);
```

## Run tests

```bash
mvn test
```

## Structure

```
src/
  main/java/typeinference/
    PhpVariable.java     # API interface
    PhpDocBlock.java     # API interface
    DocTag.java          # API interface
    PhpType.java         # type value object
    UnionType.java       # extends PhpType for multi-type values
    TypeFactory.java     # creates PhpType / UnionType
    TypeResolver.java    # core inference logic
  test/java/typeinference/
    Fakes.java           # hand-rolled test doubles
    TypeResolverTest.java
```

## Requirements

- Java 17+
- Maven 3.8+
