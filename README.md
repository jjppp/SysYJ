# About this repo

A simple SysY compiler written in Java

Coming soon.

# TODO

- [X] ANTLR parser
- [ ] Type check
- [X] Convert to my AST
- [X] Resolve symbols
- [X] Linear IR design
    - [X] IR Interpreter
- [X] CFG construction
    - [ ] Dominator tree construction
    - [ ] SSA transformation
- [ ] Local optimization/analysis
    - [X] Local Value Numbering(LVN)
    - [X] Local Dead Code Elimination(DCE)
    - [ ] Loop invariant
    - [ ] Code motion
- [ ] Global optimization/analysis
    - [X] Dataflow analysis framework
        - [X] Constant propagation
        - [ ] Reaching definition
        - [ ] Live variables
- [ ] Inter procedural optimization (optional)
- [ ] Code generation
    - [ ] X86-64
    - [ ] risc-v 32
    - [ ] ARM V7
- [ ] Peephole optimization
    - [ ] Pattern matching

# SysY Spec (Partially)

Grammar and Semantics defined here may differ slightly to the original version from BUAA.

## Array

**All** arrays are of constant length.

When used as function parameters, the first dimension of the array can be omitted.

```c
int x = 20005;
int global_arr_err[x][114]; // not allowed!

const int N = 20005;
int global_arr[N][114]; // correct

int foo(int arr3[][114][514], int arr2[][514], int arr0) {
}

int main() {
    int arr[255][114][514];
    foo(arr, arr[1], arr[1][2][3]);
}
```

Arrays can be initialized using bracket expressions. Positions not specified in global arrays are initialized to 0s.

```c
int arr[114][514] = {
    { 1, 1, 4, 5, 1, 4},
    { 1, 9, 1, 9, 8, 1, 0},
    6, 6, 6
};
```

## const

const expressions can be evaluated at compile time.

```c
const int N = 114;
const int constA[N * 2 + 1] = { N, 514 };
const int constB[constA[0]][2] = { { 1919, 810 } };

int main() {
  const int local[4] = { 1, 2, 3, 4 };
  return constB[1][0] - local[N % 3]; // 1918
}
 
```

## Function

Functions return `Void` `Int` `Flt` only.

## Library Functions

## `putint(int n)`

## `putfloat(float f)`

## `putch(int ch)`

## `int getarray(int a[])`

## `int getfarray(float f[])`

## `int getch()`

# IR Spec

Global Variables and Arrays are automatically allocated in global space.

Local Arrays are allocated when the function it belongs to executes.

Local Variables can be treated as virtual registers.

## Constructs

A program in the form of IR consists of several functions and allocations.

i.e. A program is a `List<Fun | Alloc>`, where each `Fun` contains a `List<Instr>`that makes up the function body.

## Basics

### Operands

* Val. Literals including `Int` `Flt` `Ptr` `Void`
* Var. All local variables can be used directly without declaration.

### Definition

A definition is a raw assignment in the form of `var = rhs`, where `var` must be `Var` and `rhs` can be either `Val`
or `Var`.

### Binary

Binary expressions are in the form of `var = OP lhs rhs`.

* `ADD`, `FADD`, `PADD`. Addition OPs, prefix stands for `Int` `Flt` and `Ptr` ADD.
* `SUB`, `FSUB`.
* `MUL`, `FMUL`.
* `DIV`, `FDIV`.
* `MOD`,
* `LT`, `LE`, `GT`, `GE`, `NE`, `EQ`.
* `AND`, `OR`

### Unary

`var = OP sub`

* `NEG`, `POS`
* `NOT`
* `TOF`, `TOI`. Conversion between `Flt` and `Int`.

### Function Call

`var = CALL funSig [argList]`

#### Function Signature

Function Signature is a 4-tuple `(name, retType, [argTypes], [params])` that identifies this function.

### Load & Store

`var = LOAD loc`, where `loc` is a `Var` of type `Loc`.

`loc = STORE rhs`, where `loc` is a `Var` of type `Loc`.

## Control flow

### Jump

Jump. `JMP label`

### Branch

`BR cond lTru lFls` reads `cond` and determine

* if the value of `cond` is zero, then goto `lFls`.
* otherwise, goto `lTru`.

### Label

Labels mark positions that `JMP` and `BR` can transfer their control flow to.

### Return

`RET var`.

A `RET var` ends current function, take `var` as the return value and assign it to `lhs` of the call site.

## Memory

TODO

## Types

### Void

Trivial type for trivial values.

### Int

An `Int` typed variable holds a 32-bit 2's-complement encoded signed integer.

### Flt

A `Flt` typed variable holds a 32-bit IEEE-754 standard floating point number.

### Loc\<T\>

`Loc` is a type constructor that takes a BaseType `T` and returns a new Type `Loc<T>`.

A `Loc<T>` typed variable points to a location that holds values of type `T`.
