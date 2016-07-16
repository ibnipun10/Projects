# TicTacToe
### This is a Tic Tac Toe Program build in visual studio.

### Game Structure

**I have implemented only Human - Human fight**

**This game can be easily extended to Computer - Human or Computer- Computer fight**

### C++ Classes

**CPlayer : Base class**

CHuman and CComputer are 2 derieved classes from base class CPlayer
CFactoryPlayer will give type of class object using factory pattern

**Game Controller**

**CController** : This is the brain of the game
It controls the game and is a singleton class.

**CBoard Class** : This class merely print the board on the screen.
