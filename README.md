# About this repo

A simple SysY compiler written in Java

Coming soon.

# TODO

- [X] ANTLR parser
- [ ] Type check
- [X] Convert to my AST
- [X] Resolve symbols
- [X] Linear IR design
    - [ ] IR Interpreter
- [X] CFG construction
    - [ ] SSA transformation
- [ ] Local optimization
    - [X] Local Value Numbering(LVN)
- [ ] Global optimization
    - [ ] Constant Propagation
- [ ] Inter procedural optimization (optional)
- [ ] Code generation (x86-64)

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
};
```

## const

const expressions can be evaluated at compile time.

## Function

Function can return `void` `int` `float` only.

## Library Functions

## `putint(int n)`

## `putfloat(float f)`

## `putch(int ch)`

## `getarray(int a[], int n)`

## `getfarray(float f[], int n)`

## `getch()`

# IR Spec

Global Variables and Arrays are automatically allocated in global space.

Local Arrays are allocated when the function it belongs to executes.

Local Variables can be treated as virtual registers.

## Basics

### Operands

* Val. Including `Int` `Flt` `Addr`
* Var. All local variables can be used directly without declaration.

### Expressions

* Binary Exp.
* Unary Exp.
* FunCall Exp.
* Load Exp.

Raw assignments are treated as Unary Exp with op=`NONE`.

## Control flow

* Jump.
* Branch.
* Label.
* Ret.

## Memory

* Global Alloc.
* Local Alloc.

## Types

* Int
* Flt
* Ptr of (Int or Flt)

爭取不咕咕咕
