# Tomasulo's Algorithm simulator
[Tomasulo's algorithm](https://en.wikipedia.org/wiki/Tomasulo_algorithm) is a computer architecture hardware algorithm developed by Robert Tomasulo at IBM in 1967. It is designed for the dynamic scheduling of instructions, allowing for out-of-order execution and facilitating more efficient utilization of multiple execution units within a processor. This groundbreaking algorithm was first implemented in the floating-point unit of the IBM System/360 Model 91.

Tomasulo's algorithm has played a pivotal role in the world of microprocessors. Its contribution to dynamic instruction scheduling has become a fundamental aspect of modern processor design. Many microprocessor architectures around the world incorporate variants or adaptations of Tomasulo's algorithm to enhance the execution efficiency of instructions, making it a widely influential and adopted technique in the field of computer architecture.

## The idea
I wanted to make an easy way to track the algorithm on different programs without paper and pen, I guess this will be much more efficient.
## How to run 
fork the repo then add your instructions to the file called instructions open the **Tomasulo.jar** file and it will open a sequence of different input fields where you have to specify the Multiplication, addition, load, and store execution clock cycles count + the capacity of the reservation stations for each one of them then you will encounter a basic GUI where there's a Next button to proceed for the next clock cycle.
