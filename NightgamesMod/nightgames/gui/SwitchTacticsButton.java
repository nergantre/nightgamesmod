package nightgames.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.LineBorder;

import nightgames.global.Global;
import nightgames.skills.TacticGroup;
import nightgames.skills.Tactics;

public class SwitchTacticsButton extends KeyableButton {
    private static final long serialVersionUID = -3949203523669294068L;
    private String label;
    public SwitchTacticsButton(TacticGroup group) {
        super(Global.capitalizeFirstLetter(group.name()));
        label = Global.capitalizeFirstLetter(group.name());
        getButton().setBorderPainted(false);
        getButton().setOpaque(true);
        getButton().setFont(new Font("Baskerville Old Face", Font.PLAIN, 14));
        Color bgColor = Tactics.misc.getColor();
        for (Tactics tactic : Tactics.values()) {
            if (tactic.getGroup() == group) {
                bgColor = tactic.getColor();
                break;
            }
        }

        getButton().setBackground(bgColor);
        getButton().setMinimumSize(new Dimension(0, 20));
        getButton().setForeground(foregroundColor(bgColor));
        setBorder(new LineBorder(getButton().getBackground(), 3));
        int nSkills = Global.gui().nSkillsForGroup(group);
        getButton().setText(Global.capitalizeFirstLetter(group.name()) + " [" + nSkills + "]");
        if (nSkills == 0 && group != TacticGroup.all) {
            getButton().setEnabled(false);
            getButton().setForeground(Color.WHITE);
            getButton().setBackground(getBackground().darker());
        }

        getButton().addActionListener(arg0 -> {
            Global.gui().switchTactics(group);
        });
        setLayout(new BorderLayout());
        add(getButton());
    }

    private static Color foregroundColor(Color bgColor) {
        float hsb[] = new float[3];
        Color.RGBtoHSB(bgColor.getRed(), bgColor.getGreen(), bgColor.getRed(), hsb);
        if (hsb[2] < .5) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    @Override
    public String getText() {
        return label;
    }
}
