package nightgames.gui;

import nightgames.global.Global;

public class SceneButton extends RunnableButton {
    private static final long serialVersionUID = -4333729595458261030L;
    public SceneButton(String label) {
        super(label, () -> Global.current.respond(label));
    }
}
