# Ataxx Game Engine

A modular board game application inspired by the strategic game Ataxx, featuring advanced AI-driven gameplay, multiplayer support, and a seamless user experience through both graphical (GUI) and command-line interfaces (CLI). Built with a focus on robust design, efficient algorithms, and extensibility.

## Features

* Interactive Gameplay: Play against an AI or a human opponent via GUI or CLI(command line interface).
* AI-Powered Opponent: Implements the Minimax algorithm with Alpha-Beta pruning for intelligent decision-making under computational constraints.
* Customizable Board: Includes options for setting up blocks, configuring player types (AI or manual), and restarting games.
* Command-Based Interactions: Supports command-line inputs for moves, undo, and game management.
* Robust Architecture: Modular and maintainable design following object-oriented programming (OOP) principles and utilizing design patterns such as the Command Pattern.
* Comprehensive Error Handling: Ensures smooth user experience with clear feedback for invalid moves or inputs.

## Technologies Used

* Programming Language: Java
* Testing Framework: JUnit for unit and integration testing
* Build Tool: Makefile for automated compilation
* Data Structures:
  * 2D Arrays for board representation
  * Hash Maps for command mapping and state management
* Algorithms:
  * Minimax Algorithm with Alpha-Beta Pruning for AI decision-making
  * Recursive Algorithms for move validation and board control
 
## How to Play

### GUI Mode
Launch the program with the GUI enabled:
java ataxx.Main --display
Use the mouse to click on a piece, then on the destination square to make a move.
To set blocks before starting a game, go to the Game -> Blocks menu.
Start or restart the game from the Game -> New menu option.
### CLI Mode
Launch the program in CLI mode:
java ataxx.Main
Enter moves using the format C1R1-C2R2 (e.g., a1-b2) to move a piece.
Use commands like:
new: Start a new game
auto <color>: Set a player (red or blue) to AI
manual <color>: Set a player to manual
block <position>: Place a block at a specific position
quit: Exit the game
For detailed command instructions, refer to the HELP.md file.
