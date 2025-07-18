# Java Built-in Functional Interfaces - Simple Reference

## Core Functional Interfaces

| Interface | Signature | Method |
|-----------|-----------|---------|
| **Supplier\<T>** | `() -> T` | `T get()` |
| **Consumer\<T>** | `T -> void` | `void accept(T t)` |
| **Predicate\<T>** | `T -> boolean` | `boolean test(T t)` |
| **Function\<T,R>** | `T -> R` | `R apply(T t)` |

## Bi-Parameter Interfaces

| Interface | Signature | Method |
|-----------|-----------|---------|
| **BiConsumer\<T,U>** | `(T,U) -> void` | `void accept(T t, U u)` |
| **BiPredicate\<T,U>** | `(T,U) -> boolean` | `boolean test(T t, U u)` |
| **BiFunction\<T,U,R>** | `(T,U) -> R` | `R apply(T t, U u)` |

## Specialized Operator Interfaces

| Interface | Signature | Method |
|-----------|-----------|---------|
| **UnaryOperator\<T>** | `T -> T` | `T apply(T t)` |
| **BinaryOperator\<T>** | `(T,T) -> T` | `T apply(T t1, T t2)` |

## Integer Specializations

| Interface | Signature | Method |
|-----------|-----------|---------|
| **IntSupplier** | `() -> int` | `int getAsInt()` |
| **IntConsumer** | `int -> void` | `void accept(int value)` |
| **IntPredicate** | `int -> boolean` | `boolean test(int value)` |
| **IntFunction\<R>** | `int -> R` | `R apply(int value)` |
| **ToIntFunction\<T>** | `T -> int` | `int applyAsInt(T value)` |
| **IntUnaryOperator** | `int -> int` | `int applyAsInt(int operand)` |
| **IntBinaryOperator** | `(int,int) -> int` | `int applyAsInt(int left, int right)` |

## Double Specializations

| Interface | Signature | Method |
|-----------|-----------|---------|
| **DoubleSupplier** | `() -> double` | `double getAsDouble()` |
| **DoubleConsumer** | `double -> void` | `void accept(double value)` |
| **DoublePredicate** | `double -> boolean` | `boolean test(double value)` |
| **DoubleFunction\<R>** | `double -> R` | `R apply(double value)` |
| **ToDoubleFunction\<T>** | `T -> double` | `double applyAsDouble(T value)` |
| **DoubleUnaryOperator** | `double -> double` | `double applyAsDouble(double operand)` |
| **DoubleBinaryOperator** | `(double,double) -> double` | `double applyAsDouble(double left, double right)` |

## Long Specializations

| Interface | Signature | Method |
|-----------|-----------|---------|
| **LongSupplier** | `() -> long` | `long getAsLong()` |
| **LongConsumer** | `long -> void` | `void accept(long value)` |
| **LongPredicate** | `long -> boolean` | `boolean test(long value)` |
| **LongFunction\<R>** | `long -> R` | `R apply(long value)` |
| **ToLongFunction\<T>** | `T -> long` | `long applyAsLong(T value)` |
| **LongUnaryOperator** | `long -> long` | `long applyAsLong(long operand)` |
| **LongBinaryOperator** | `(long,long) -> long` | `long applyAsLong(long left, long right)` |

## Boolean Specializations

| Interface | Signature | Method |
|-----------|-----------|---------|
| **BooleanSupplier** | `() -> boolean` | `boolean getAsBoolean()` |

---

**Quick Reference Notes:**
- **Core 4**: Supplier, Consumer, Predicate, Function
- **Bi-versions**: Two parameters instead of one
- **Operators**: Same type input/output (UnaryOperator) or combining same types (BinaryOperator)
- **Primitive versions**: Avoid boxing/unboxing for performance
- **To[Type]Function**: Convert from object to primitive
- **[Type]Function**: Convert from primitive to object