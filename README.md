Current code coverage:
[![codecov](https://codecov.io/github/huytrinhx/tetrominos/graph/badge.svg?token=VZF443AHJ1)](https://codecov.io/github/huytrinhx/tetrominos). [See detailed report](https://app.codecov.io/github/huytrinhx/tetrominos)


# Table Of Contents
+ [Game Requirements](#game-requirements)
+ [Low-level Object Design](#low-level-object-design)
    + Objects and Relationships
    + Class Diagram
+ [Test-Driven Development: Basic Tests](#test-driven-development-basic-tests)
+ [MVC Pattern](#mvc-pattern)
+ [Detailed Implementations](#detailed-implementations)
    + Piece Generation
    + Piece Rotation
    + Rows Clearing
+ [Continuous Testing and Code Coverage](#continuous-testing-and-code-coverage)
+ [Future Developments](#future-developments)
+ [Resources](#resources)

# Game Requirements

![Tetris Board](https://github.com/user-attachments/assets/bc37c104-2892-4455-bb9d-08d9bb187dcf)

The basic rules of the game are the same as for the game [Tetris™](https://en.wikipedia.org/wiki/Tetris); you can easily find several browser-based implementations of Tetris with a web search. Following are summaries of major behaviors of our game:
- The game starts with a falling piece, called a tetromino, until it reaches the bottom of the game board or collide with other fallen pieces.
- Each tetromino is a shape composed of four blocks, each block orthogonal to each other, and should have same color.
- Tetromino can be moved side to side under keyboard control (LeftArrow or RightArrow), or rotated to the left or right (A or D key).
- Whenever a row is full, it should get cleared and all other fallen blocks move downward. The goal of the game is to clear as many rows as possible before the board becomes so cluttered that a new piece cannot start falling from the top (game over).

# Low-level Object Design

Now we'll create the class diagram for the game. We will first design the classes and then identify the relationship between classes.

We'll follow the bottom-up approach to designing the classes:
- __Position__: a data structure to indicate where an object (block, piece) is in the game board. Since the pieces are falling downward from the top, we'll let the point of origin (0,0) to be on the top left corner, a typical convention in computer graphics. This data structure is defined by horizontal x  (column) and vertical y position (row). (Note: this convention is not the same as 2D matrix notation in most programming languages, where in a point, referred as P[x][y], x indicates the row and y indicates the column coordinates).

![origin-convention](https://github.com/user-attachments/assets/a08a8912-c9e1-4e81-8800-9397786041ba)


- __Block__: an object that is defined by a Position and a Color.
- __Piece__: an object that contains 4 blocks. It can be slid left or right or down. Also, it can be rotated left (counter-clockwise) or right (clockwise).
- __Board__: an object that is defined by Width, number of blocks that can be lined up horizontally, and Height, vertically.
- __GameController__: represents the business logics of the game and control the game. It moves the active piece down in each time step, detect collision and clear rows.
- __GameView__: represents the display of the game components to the screen.
- Enumerations and Data Types: 
	- __Colors__: this enumerations keeps track of the colors that we allow the piece to have
	- __BoardStates__: a 2D integer matrix that has value of 0 or 1 in each cell to indicate whether it is currently occupied or not.


## Relationships and activities between the classes:
- The Piece class is composed of Block
- The Board class is composed Piece (active piece) and Block (settled blocks)
- The Game Controller is composed of Board
- The Game Controller has two-way association with GameView. Users enter keystrokes effects the piece and states of the game. The game reflects the new states visually on the view. 
## Class Diagram

![class-diagram](https://github.com/user-attachments/assets/e277e6ff-0c76-4a93-bac7-2cb714bf203f)

# Test-Driven Development: Basic Tests

* When a new piece is created, it should have 4 blocks, each block should have similar color and orthogonal to each other.
* When a falling piece is moved left or right and rotated left or right, it should not be moved outside of the board left or right boundary
* When a falling piece reaches the bottom or collides with other fallen blocks, the board should detect collision.
* When a falling piece makes a row full, the row should be cleared and all other fallen pieces should shift downward.
* When the GameSetUp view is completed, the active frame should be GamePlay view and the board dimension should match user's inputs.

# MVC Pattern

Model View Controller (MVC) pattern is pretty much the standard pattern when it comes to software development. It essentially splits user interface interaction into three distinct roles.

The model is an object that represents information about the game, containing all the data and behavior rather than the visual aspects. In this game, the blocks, the pieces and the board are the models. The view represents the display of the model in the UI, so it's the frame that captures the user interactions and display the changes. The last member of the trinity: the controller, takes user input, manipulates the model, and causes the view to update appropriately.

We can see this playout in when GameController starts, it first displays the GameSetUp frame. Once the board dimensions are input correctly, the controller calls up GamePlay frame and disposes the GameSetup.

Any keyboards registered on GameSetup frame now correspond to a method or sequence of methods that change the data on the board, which is stored as a member of GameController. To be true to the MVC definition, some of the methods like nextTurn(), updateBoardStatesOnCollision(), rotateLeft(), rotateRight() should have been under GameController rather than under Board class. But so as not to risk unnecessary complexity, I decide to keep them all under Board.

# Detailed Implementations

1. Piece Generation

![piece-generation](https://github.com/user-attachments/assets/1e2a1879-76f3-408c-8fc7-eaf812fc240a)

Based on my research, there are 2 approaches to piece generation. The first approach is basically listing all possible basic shapes (4) of tetrominos: a T, a square, a Z and an L. Then we can add on different orientations (4) to the shape ( a 90-degree turn, 180-degree turn, 270-degree turn and a mirror). With 7 colors, we have in total more than 110 different combinations.

I favor a more algorithmic approach. Based on the definition of a tetrominos, I represented a piece within a 4 by 4 matrix. To place the first block, I simply placed a block randomly on this matrix. All subsequent placements must be randomly chosen among 4 potential directions (north, south, west, east) to the last one and must be legal, not already chosen and not fall outside of our 4 by 4 matrix.


2. Piece Rotation

Rotation of a piece is one of the most challenging feature in the game to get it right. It is primarily because standard matrix rotation involve transposition: swapping x coordinate with y coordinate. This would make a piece land outside of the board dimension when the rotated piece is currently close the bottom of the board (having a small x and large y). 

To compensate this, before applying rotation, I move the piece so that its edges touch the y and x axis. To do this, I must compute minX and minY and shift the current piece by minX and minY. Post rotation, I undo the offset by minX and minY to restore its original position.

We are not done here, note that as we rotate left, because of the rotation occurred at the point of origin (0,0), our piece could fall out of the board if it currently has small x coordinates. To compensate for this, I add a method alignLeft() which makes sure a piece's minX could never be smaller than 0. Similarly, alignTop() and alignRight() should be applied when a piece's position could fall out of board after rotation.

Now comes the rotation itself, I followed the matrix rotation formula as detailed in this [Wikipedia page](https://en.wikipedia.org/wiki/Rotation_matrix).

![piece-rotation](https://github.com/user-attachments/assets/e8b4329f-92f7-4597-a7a2-7e6547b549dc)

3. Rows Clearing

As I mentioned briefly in the object design, I use BoardStates, a 2D matrix to track which blocks in our board are occupied or not. It also makes it easy to check whether a row is full by doing a row sum.

Also note that, we also keep track of all settled blocks in a separate data structure (an array of blocks). This settled blocks will be primarily used to display in the game GUI.

Therefore, after getting all the index of rows that are full, our clearing process is as follows:
- Traverse the index in reverse order, since we would want to clear the lowest row first.
- Remove all blocks where y coordinates equals row index
- Shift all the upper blocks down 1 block.
Once the settled blocks are updated, we update the BoardStates by filling 1 in cells where the blocks are present.

![row-clearance](https://github.com/user-attachments/assets/bd156d3b-fbbd-415e-9e21-ee1975123e17)

# Continuous Testing and Code Coverage
* Writing tests
* Setup GitHub Actions
* Adding Code Coverage

# Future Developments
* Design and implement a frontend and backend system for displaying gamer's scores and progresses
* Design and implement a system to allow users to resume game upon launching the app
* Design and implement features to allow users customize game difficulty regarding speed

# Resources
