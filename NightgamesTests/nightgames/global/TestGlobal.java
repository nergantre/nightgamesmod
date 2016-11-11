package nightgames.global;

import nightgames.gui.GUI;
import nightgames.gui.TestGUI;

/**
 * Creates a version of Global that has no visible GUI.
 */
public class TestGlobal extends Global {
    public TestGlobal() {
        super(false);
    }

    @Override protected GUI makeGUI(boolean headless) {
        return new TestGUI();
    }
}
