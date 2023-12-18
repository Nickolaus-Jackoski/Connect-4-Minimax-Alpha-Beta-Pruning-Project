# Connect Four AI using Minimax and Alpha-Beta Pruning in Java

## Overview
This project involves the creation of an AI for playing Connect Four, with the implementation of advanced algorithms such as minimax with alpha-beta pruning, and a added heuristic. The goal is to demonstrate proficiency in algorithm design, game theory, and Java programming.

## Key Components

### Board Representation (`Board.java`)
A class that models the Connect Four game board, uses a 2D byte array for efficient memory usage and includes methods for board manipulation and game state evaluation.

### Game Mechanics (`Player.java`, `GameState.java`)
Enums to define player roles (MAX and MIN) and game states (in progress, win, loss, tie), central to the game's logic.

### Algorithm Implementation (`MiniMaxSearch.java`)
The core of the project, where the minimax algorithm and its variations (with alpha-beta pruning and heuristics) are implemented. It includes:
- A `Map<Board, MiniMaxInfo>` as a transposition table to store board states and their evaluations.
- The ability to switch between different parts of the project (Part A, B, C) using the `PartType` enum.
- A method `Search` that encapsulates the logic for minimax and alpha-beta pruning.

### Application Entry Point (`Main.java`)
Manages user interaction, where users can choose between different parts of the project and input game parameters. It ties together the game board representation and the AI algorithms.

### Utility and Demo Classes (`BoardDemo.java`, `MiniMaxInfo.java`)
`BoardDemo` for demonstrating board functionality and `MiniMaxInfo` as a record to store minimax values and actions.

## Highlights of Implementation

### Efficiency in Board Representation
The use of bytes instead of integers for representing the board showcases efficient memory management.

### Modular Design
The project is structured in a way that allows for easy switching between different algorithmic strategies (Parts A, B, and C), demonstrating my understanding of modular programming. Part A being the regular minimax algorthrim. Part B being minimax with alpha-beta pruning. Part C being minimax with alpha-beta pruning and a added heuristic.

### Advanced Algorithmic Techniques
Implementation of minimax with enhancements like alpha-beta pruning and heuristics illustrates the application of complex algorithmic concepts in a real-world problem.

## Added Heuristic
This Connect Four AI project includes an advanced heuristic implemented in the `MiniMaxSearch` class. The heuristic is a pivotal component of the AI's decision-making process, especially in depth-limited searches where it evaluates potential future game states.

### Heuristic Implementation

#### Heuristic Function
Located in the `MiniMaxSearch` class, the `Eval` function is used to assess the board states. It calculates scores based on the alignment of tokens (X's and O's) and adjacent empty spaces.

#### Scoring Mechanism
- **Consecutive Tokens**:
  - If there are two consecutive tokens (X's or O's), the score is adjusted by +3 for MAX (computer) or -3 for MIN (opponent).
  - For three consecutive tokens, the score is adjusted by +10 for MAX or -10 for MIN.
- **Adjacent Empty Spaces**: The number of adjacent blank spaces to these tokens is added to the score. This approach assigns higher value to states with more room for maneuvering and potential for future wins.

#### Directional Checking
- The heuristic evaluates consecutive tokens in rows, columns, and both diagonal directions (upward and downward).
- It utilizes helper methods like `countConsecutiveRowsIdx`, `countConsecutiveColsIdx`, `countConsecutiveDiagonalUpIdx`, and `countConsecutiveDiagonalDownIdx` for identifying these patterns.
- Additional methods like `findEmptySpacesInSameRow`, `findEmptySpacesInSameDiagUp`, and `findEmptySpacesInSameDiagDown` are used to count adjacent blank spaces.

## Strategic Depth
This heuristic adds a layer of strategic depth to the AI, enabling it to prioritize not only immediate wins or blocks but also positions that offer strategic advantages for future moves.

### Depth-Limited Search
In Part C, where the AI uses a fixed-depth lookahead, this heuristic effectively evaluates non-terminal states, guiding the AI in making more informed decisions.

## How to Run The Program
1. Ensure that all of the .java and .txt files are in the same file of the compiler or IDE of your choice that runs java.
2. Run the program from the Main.java class
3. In the user prompt it will ask you for Part A,B,C. Part A is the dumbest algorthrim out of them all as it is just Minimax. 
Part B is slightly smarter, implementing the minimax with alpha-beta search. Part C is the smartest algorthrim as it is minimax with alpha-beta search and the added heuristic I described above. 
4. The program will prompt the user for debugging info. All this shows is the different states that the search algorthrim ran through to train itself. 
5. The program will ask you to choose the size of your game giving the number of rows, columns, and how many in a row to win. This program takes any number of these and it doesn't have to be connect 4, you can play connect 3 if you want!
6. The program will prompt the user for the number of moves to look ahead the more moves the algorthrim looks ahead the smarter it is. 
7. The program will prompt the user for who plays first the computer or the user. Now play the game!

## Conclusion
This project is a testament to the ability to apply advanced computer science concepts in a practical setting. It demonstrates not only proficiency in Java programming and algorithm design but also an understanding of efficient data structures, user interaction, and modular software architecture. This project was part of my COMP 372 A.I. class at Rhodes College. 
