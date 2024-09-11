package tetrominos.controller;

import tetrominos.view.GamePlay;
import tetrominos.view.GameSetUp;
import tetrominos.models.Board;

public class GameController {
    private Board board;
    private GameSetUp setUpScreen;
    private GamePlay gamePlayScreen;

    public GameSetUp getSetUpScreen() {
        return setUpScreen;
    }

    public GamePlay getGamePlayScreen() {
        return gamePlayScreen;
    }

    public Board getBoard() {
        return board;
    }

    public void start() {
        setUpScreen = new GameSetUp();
        setUpScreen.display();
        setUpScreen.addGameStartButtonListener(e -> continueToGamePlaying());
    }

    private void continueToGamePlaying() {
        this.board = new Board(setUpScreen.getBoardWidth(), setUpScreen.getBoardHeight());
        setUpScreen.dispose();
        gamePlayScreen = new GamePlay(this);
        gamePlayScreen.display();
        gamePlayScreen.listen();
    }

}
