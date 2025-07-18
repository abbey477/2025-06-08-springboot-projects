# Java Built-in Functional Interfaces Reference

## Core Functional Interfaces

| Interface | Signature | Method | Description | Common Use Cases |
|-----------|-----------|---------|-------------|------------------|
| **Supplier\<T>** | `() -> T` | `T get()` | No input, produces a value | Factory methods, lazy evaluation, default values |
| **Consumer\<T>** | `T -> void` | `void accept(T t)` | Takes input, no return value | Side effects, printing, logging, forEach operations |
| **Predicate\<T>** | `T -> boolean` | `boolean test(T t)` | Takes input, returns boolean | Filtering, validation, stream filter operations |
| **Function\<T,R>** | `T -> R` | `R apply(T t)` | Takes input of type T, returns type R | Transformations, mapping, stream map operations |

## Bi-Parameter Interfaces

| Interface | Signature | Method | Description | Common Use Cases |
|-----------|-----------|---------|-------------|------------------|
| **BiConsumer\<T,U>** | `(T,U) -> void` | `void accept(T t, U u)` | Takes two inputs, no return | Map iteration, printing pairs, side effects with two values |
| **BiPredicate\<T,U>** | `(T,U) -> boolean` | `boolean test(T t, U u)` | Takes two inputs, returns boolean | Comparing two values, validation with context |
| **BiFunction\<T,U,R>** | `(T,U) -> R` | `R apply(T t, U u)` | Takes two inputs, returns result | Combining values, calculations, reduce operations |

## Specialized Operator Interfaces

| Interface | Signature | Method | Description | Common Use Cases |
|-----------|-----------|---------|-------------|------------------|
| **UnaryOperator\<T>** | `T -> T` | `T apply(T t)` | Takes T, returns same type T | In-place transformations, updates, replacements |
| **BinaryOperator\<T>** | `(T,T) -> T` | `T apply(T t1, T t2)` | Takes two T's, returns T | Combining same types, reduce operations, Math operations |

## Primitive Specializations

### Integer Specializations
| Interface | Signature | Method | Description | Common Use Cases |
|-----------|-----------|---------|-------------|------------------|
| **IntSupplier** | `() -> int` | `int getAsInt()` | No input, returns int | Random numbers, counters, primitive factories |
| **IntConsumer** | `int -> void` | `void accept(int value)` | Takes int, no return | Primitive side effects, avoiding boxing |
| **IntPredicate** | `int -> boolean` | `boolean test(int value)` | Takes int, returns boolean | Primitive filtering, number validation |
| **IntFunction\<R>** | `int -> R` | `R apply(int value)` | Takes int, returns object | Converting primitives to objects |
| **ToIntFunction\<T>** | `T -> int` | `int applyAsInt(T value)` | Takes object, returns int | Object to primitive conversion |
| **IntUnaryOperator** | `int -> int` | `int applyAsInt(int operand)` | Takes int, returns int | Primitive math operations |
| **IntBinaryOperator** | `(int,int) -> int` | `int applyAsInt(int a, int b)` | Takes two ints, returns int | Primitive calculations, sum, max, min |

### Double Specializations
| Interface | Signature | Method | Description | Common Use Cases |
|-----------|-----------|---------|-------------|------------------|
| **DoubleSupplier** | `() -> double` | `double getAsDouble()` | No input, returns double | Random doubles, measurements, calculations |
| **DoubleConsumer** | `double -> void` | `void accept(double value)` | Takes double, no return | Primitive side effects, avoiding boxing |
| **DoublePredicate** | `double -> boolean` | `boolean test(double value)` | Takes double, returns boolean | Floating point validation, range checks |
| **DoubleFunction\<R>** | `double -> R` | `R apply(double value)` | Takes double, returns object | Converting primitives to objects |
| **ToDoubleFunction\<T>** | `T -> double` | `double applyAsDouble(T value)` | Takes object, returns double | Object to primitive conversion |
| **DoubleUnaryOperator** | `double -> double` | `double applyAsDouble(double operand)` | Takes double, returns double | Math operations, transformations |
| **DoubleBinaryOperator** | `(double,double) -> double` | `double applyAsDouble(double a, double b)` | Takes two doubles, returns double | Mathematical calculations |

### Long Specializations
| Interface | Signature | Method | Description | Common Use Cases |
|-----------|-----------|---------|-------------|------------------|
| **LongSupplier** | `() -> long` | `long getAsLong()` | No input, returns long | Timestamps, IDs, large number generation |
| **LongConsumer** | `long -> void` | `void accept(long value)` | Takes long, no return | Processing large numbers, timestamps |
| **LongPredicate** | `long -> boolean` | `boolean test(long value)` | Takes long, returns boolean | Large number validation, ID checks |
| **LongFunction\<R>** | `long -> R` | `R apply(long value)` | Takes long, returns object | Converting large numbers to objects |
| **ToLongFunction\<T>** | `T -> long` | `long applyAsLong(T value)` | Takes object, returns long | Object to long conversion |
| **LongUnaryOperator** | `long -> long` | `long applyAsLong(long operand)` | Takes long, returns long | Large number operations |
| **LongBinaryOperator** | `(long,long) -> long` | `long applyAsLong(long a, long b)` | Takes two longs, returns long | Large number calculations |

## Boolean Specializations
| Interface | Signature | Method | Description | Common Use Cases |
|-----------|-----------|---------|-------------|------------------|
| **BooleanSupplier** | `() -> boolean` | `boolean getAsBoolean()` | No input, returns boolean | Configuration flags, conditional logic |

## Common Default Methods

### Predicate Methods
- `and(Predicate<T> other)` - Logical AND
- `or(Predicate<T> other)` - Logical OR  
- `negate()` - Logical NOT
- `isEqual(Object targetRef)` - Static method for equality check

### Function Methods
- `andThen(Function<R,V> after)` - Compose functions (this first, then after)
- `compose(Function<V,T> before)` - Compose functions (before first, then this)
- `identity()` - Static method returning identity function

### Consumer Methods
- `andThen(Consumer<T> after)` - Chain consumers

## Usage Examples by Category

### **Stream Operations**
```java
// Supplier for stream generation
Stream.generate(() -> Math.random())

// Consumer for forEach
stream.forEach(System.out::println)

// Predicate for filtering  
stream.filter(x -> x > 0)

// Function for mapping
stream.map(String::toUpperCase)
```

### **Optional Operations**
```java
// Supplier for orElseGet
optional.orElseGet(() -> "default")

// Consumer for ifPresent
optional.ifPresent(System.out::println)

// Function for map
optional.map(String::length)
```

### **Primitive Performance**
Use primitive specializations to avoid boxing/unboxing overhead:
```java
// Instead of Function<Integer, Integer>
IntUnaryOperator square = x -> x * x;

// Instead of Supplier<Double>
DoubleSupplier random = Math::random;
```

---

**Note**: All primitive specializations exist to avoid the performance cost of boxing/unboxing when working with primitive types. Choose the appropriate specialization based on your data types for optimal performance.