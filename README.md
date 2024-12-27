# Tomasulo's Algorithm simulator
[Tomasulo's algorithm](https://en.wikipedia.org/wiki/Tomasulo_algorithm) is a computer architecture hardware algorithm developed by Robert Tomasulo at IBM in 1967. It is designed for the dynamic scheduling of instructions, allowing for out-of-order execution and facilitating more efficient utilization of multiple execution units within a processor. This groundbreaking algorithm was first implemented in the floating-point unit of the IBM System/360 Model 91.

Tomasulo's algorithm has played a pivotal role in the world of microprocessors. Its contribution to dynamic instruction scheduling has become a fundamental aspect of modern processor design. Many microprocessor architectures around the world incorporate variants or adaptations of Tomasulo's algorithm to enhance the execution efficiency of instructions, making it a widely influential and adopted technique in the field of computer architecture.

## The idea
I wanted to make an easy way to track the algorithm on different programs without paper and pen, I guess this will be much more efficient.
## How to run 
fork the repo then add your instructions to the file called instructions open the **Tomasulo.jar** file and it will open a sequence of different input fields where you have to specify the Multiplication, addition, load, and store execution clock cycles count + the capacity of the reservation stations for each one of them then you will encounter a basic GUI where there's a Next button to proceed for the next clock cycle.
## Supported Instructions

### Arithmetic Instructions (Integer and Floating Point)

#### Integer Operations
- **ADDI R1, R2, imm**  
  Add immediate: Adds the value in `R2` to the immediate `imm` and stores the result in `R1`.
- **SUBI R1, R2, imm**  
  Subtract immediate: Subtracts the immediate `imm` from the value in `R2` and stores the result in `R1`.

#### Floating-Point Operations
- **ADD.D F1, F2, F3**  
  Floating-point addition: Adds the values in `F2` and `F3` and stores the result in `F1`.
- **SUB.D F1, F2, F3**  
  Floating-point subtraction: Subtracts the value in `F3` from `F2` and stores the result in `F1`.
- **MUL.D F1, F2, F3**  
  Floating-point multiplication: Multiplies the values in `F2` and `F3` and stores the result in `F1`.
- **DIV.D F1, F2, F3**  
  Floating-point division: Divides the value in `F2` by `F3` and stores the result in `F1`.

---

### Load and Store Instructions

#### Load Operations
- **L.D F1, offset(R1)**  
  Load double: Loads a double-precision floating-point value from the memory address `offset + R1` into the register `F1`.

#### Store Operations
- **S.D F1, offset(R1)**  
  Store double: Stores the double-precision floating-point value in `F1` into the memory address `offset + R1`.

---

### Branch Instructions

#### Branch Operations
- **BNEZ R1, label**  
  Branch if not equal to zero: If the value in `R1` is not zero, branch to the instruction at the specified `label`.

---
