package nightgames.gui;

import java.awt.Color;
import java.awt.Font;

import nightgames.combat.Combat;
import nightgames.skills.Skill;

public class SubSkillButton extends KeyableButton {
    private static final long serialVersionUID = -3177604366435328960L;
    protected Skill action;
    private String choice;

    public SubSkillButton(final Skill action, final String choice, Combat c) {
        super(choice);
        this.choice = choice;        
        getButton().setOpaque(true);
        getButton().setBorderPainted(false);
        getButton().setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
        this.action = action;
        getButton().setBackground(new Color(200, 200, 200));
        getButton().addActionListener(arg0 -> {
            c.act(action.user(), action, choice);
            c.resume();
        });
    }

    @Override
    public String getText() {
        return choice;
    }
}
