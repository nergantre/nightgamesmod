package nightgames.gui;

import java.awt.Font;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Global;

public class ActionButton extends KeyableButton {
    /**
     * 
     */
    private static final long serialVersionUID = 2822534455509003521L;
    protected Action action;
    protected Character user;

    public ActionButton(Action action, Character user) {
        super(action.toString());
        getButton().setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
        this.action = action;
        this.user = user;
        getButton().addActionListener(arg0 -> {
            Global.gui().clearText();
            ActionButton.this.action.execute(ActionButton.this.user);
            if (!ActionButton.this.action.freeAction()) {
                Global.getMatch().resume();
            }
        });
    }

    @Override
    public String getText() {
        return action.toString();
    }
}
