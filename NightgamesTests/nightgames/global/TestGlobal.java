package nightgames.global;

import nightgames.gui.GUI;
import nightgames.gui.TestGUI;

/**
 * TODO: Write class-level documentation.
 */
public class TestGlobal extends Global {
    @Override protected GUI makeGUI() {
        return new TestGUI();
    }
}
