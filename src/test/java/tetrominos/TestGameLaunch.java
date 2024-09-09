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

public class TestGameLaunch {
    private FrameFixture window;
    @BeforeEach
    public void setUp() {
        GameLaunch frame = GuiActionRunner.execute(() -> new GameLaunch());
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
        JFrame newFrame = window.robot().finder().findByType(JFrame.class, true);
        assertThat(newFrame.getSize(), equalTo(new Dimension(336,1063)));
    }
}
