package nightgames.global;

import nightgames.characters.Player;
import nightgames.gui.GUI;
import nightgames.gui.TestGUI;

/**
 * Creates a version of Global that has no visible GUI.
 */
public class TestGlobal extends Global {
    @Override protected GUI makeGUI() {
        return new TestGUI();
    }
}
