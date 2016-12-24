package nightgames.gui;

import java.awt.Font;

import javax.swing.JButton;

import nightgames.combat.Combat;
import nightgames.combat.CombatSceneChoice;
import nightgames.global.Global;

public class CombatSceneButton extends JButton {
    /**
     * 
     */
    private static final long serialVersionUID = -4333729595458261030L;

    public CombatSceneButton(String label, Combat c, nightgames.characters.Character npc, CombatSceneChoice choice) {
        super(label);
        setFont(new Font("Baskerville Old Face", 0, 18));
        addActionListener(arg0 -> {
            c.write("<br/>");
            choice.choose(c, npc);
            c.updateMessage();
            Global.gui().next(c);
        });
    }
}
