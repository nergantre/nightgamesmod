package nightgames.gui;

import java.awt.Font;

import javax.swing.JButton;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Global;

public class ActionButton extends JButton {
    /**
     * 
     */
    private static final long serialVersionUID = 2822534455509003521L;
    protected Action action;
    protected Character user;

    public ActionButton(Action action, Character user) {
        super(action.toString());
        setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
        this.action = action;
        this.user = user;
        addActionListener(arg0 -> {
            Global.gui().clearText();
            ActionButton.this.action.execute(ActionButton.this.user);
            if (!ActionButton.this.action.freeAction()) {
                Global.getMatch().resume();
            }
        });
    }
}
