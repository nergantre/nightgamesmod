package nightgames.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class SkillButton extends JPanel {

    private static final long serialVersionUID = -1253735466299929203L;
    protected Skill action;
    protected Combat combat;
    private JButton button;

    public SkillButton(final Skill action, Combat c) {
        super();
        setButton(new JButton(action.getLabel(c)));
        getButton().setBorderPainted(false);
        getButton().setOpaque(true);
        getButton().setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
        this.action = action;
        String text = "<html>" + action.describe(c);
        if (action.type(c) == Tactics.damage) {
            getButton().setBackground(new Color(150, 0, 0));
        } else if (action.type(c) == Tactics.pleasure) {
            getButton().setBackground(Color.PINK);
        } else if (action.type(c) == Tactics.fucking) {
            getButton().setBackground(new Color(255, 100, 200));
        } else if (action.type(c) == Tactics.positioning) {
            getButton().setBackground(new Color(0, 100, 0));
        } else if (action.type(c) == Tactics.stripping) {
            getButton().setBackground(new Color(0, 100, 0));
        } else if (action.type(c) == Tactics.debuff) {
            getButton().setBackground(Color.CYAN);
        } else if (action.type(c) == Tactics.recovery || action.type(c) == Tactics.calming) {
            getButton().setBackground(Color.WHITE);
        } else if (action.type(c) == Tactics.summoning) {
            getButton().setBackground(Color.YELLOW);
        } else {
            getButton().setBackground(new Color(200, 200, 200));
        }
        button.setForeground(foregroundColor(action.type(c)));
        
        if (action.getMojoCost(c) > 0) {
            setBorder(new LineBorder(Color.RED, 3));
            text += "<br>Mojo cost: " + action.getMojoCost(c);
        } else if (action.getMojoBuilt(c) > 0) {
            setBorder(new LineBorder(new Color(53, 201, 255), 3));
            text += "<br>Mojo generated: " + action.getMojoBuilt(c) + "%";
        } else {
            setBorder(new LineBorder(getButton().getBackground(), 3));
        }
        if (!action.user()
                   .cooldownAvailable(action)) {
            getButton().setEnabled(false);
            text += String.format("<br>Remaining Cooldown: %d turns", action.user()
                                                                            .getCooldown(action));
            getButton().setForeground(Color.WHITE);
            getButton().setBackground(getBackground().darker());
        }

        text += "</html>";
        getButton().setToolTipText(text);
        combat = c;
        getButton().addActionListener(arg0 -> {
            if (action.subChoices()
                      .size() == 0) {
                combat.act(SkillButton.this.action.user(), SkillButton.this.action, "");
            } else {
                Global.gui().commandPanel.removeAll();
                for (String choice : action.subChoices()) {
                    Global.gui().commandPanel.add(new SubSkillButton(action, choice, combat));
                }
                Global.gui().commandPanel.repaint();
                Global.gui().commandPanel.revalidate();
            }
        });
        setLayout(new BorderLayout());
        add(getButton());
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public void addIndex(int idx) {
        button.setText(button.getText() + " [" + idx + "]");
    }

    private Color foregroundColor(Tactics tact) {
        switch (tact) {
            case damage:
            case positioning:
            case stripping:
                return Color.WHITE;
            default:
                return Color.BLACK;
        }
    }
}
