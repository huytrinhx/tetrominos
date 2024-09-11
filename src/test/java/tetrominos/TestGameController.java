package tetrominos;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import javax.swing.JFrame;
import java.awt.Dimension;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tetrominos.controller.GameController;
import tetrominos.view.*;

public class TestGameController {
    private FrameFixture window;
    private GameSetUp frame;
    private GameController game;
    @BeforeEach
    public void setUp() {
        game = GuiActionRunner.execute(() -> new GameController());
        game.start();
        frame = game.getSetUpScreen();
        window = new FrameFixture(frame);
        window.show();
    }
    
    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }

    @Test
    public void testGetUsersInputForGame() {
        window.textBox("boardRows").enterText("32");
        window.textBox("boardCols").enterText("10");
        window.button("startButton").click();
        frame = (GameSetUp) window.target();
        assertThat(frame.getBoardHeight(), equalTo(32));
        assertThat(frame.getBoardWidth(), equalTo(10));
    }
    
    @Test
    public void whenGameStartsBoardShouldMatchGameSetupInputs() {
        window.textBox("boardRows").enterText("32");
        window.textBox("boardCols").enterText("10");
        window.button("startButton").click();
        var nextframe = game.getGamePlayScreen();
        assertThat(nextframe.getBoard().getHeight(), equalTo(32));
        assertThat(nextframe.getBoard().getWidth(), equalTo(10));
    }
}
