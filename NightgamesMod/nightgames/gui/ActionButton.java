package nightgames.gui;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Global;

public class ActionButton extends RunnableButton {
    /**
     * 
     */
    private static final long serialVersionUID = 2822534455509003521L;

    public ActionButton(Action action, Character user) {
        super(action.toString(), () -> {
            Global.gui().clearText();
            action.execute(user);
            if (!action.freeAction()) {
                Global.getMatch().resume();
            }
        });
    }
}
