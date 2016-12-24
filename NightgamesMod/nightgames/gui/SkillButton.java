package nightgames.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.border.LineBorder;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Stage;

public class SkillButton extends KeyableButton {
    private static final long serialVersionUID = -1253735466299929203L;
    protected Skill action;
    protected Combat combat;

    public SkillButton(Combat c, final Skill action, Character target) {
        super(action.getLabel(c));
        getButton().setBorderPainted(false);
        getButton().setOpaque(true);
        getButton().setFont(fontForStage(action.getStage()));
        this.action = action;
        int actualAccuracy = target.getChanceToHit(action.getSelf(), c, action.accuracy(c, target));
        int clampedAccuracy = Math.min(100, Math.max(0, actualAccuracy));
        String text = "<html>" + action.describe(c) + " <br/><br/>Accuracy: " + (actualAccuracy >=150 ? "---" : clampedAccuracy + "%") + "</p>";
        Color bgColor = action.type(c).getColor();
        getButton().setBackground(bgColor);
        getButton().setForeground(foregroundColor(bgColor));

        if (action.getMojoCost(c) > 0) {
            setBorder(new LineBorder(Color.RED, 3));
            text += "<br/>Mojo cost: " + action.getMojoCost(c);
        } else if (action.getMojoBuilt(c) > 0) {
            setBorder(new LineBorder(new Color(53, 201, 255), 3));
            text += "<br/>Mojo generated: " + action.getMojoBuilt(c) + "%";
        } else {
            setBorder(new LineBorder(getButton().getBackground(), 3));
        }
        if (!action.user()
                   .cooldownAvailable(action)) {
            getButton().setEnabled(false);
            text += String.format("<br/>Remaining Cooldown: %d turns", action.user()
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
                Global.gui().commandPanel.reset();
                for (String choice : action.subChoices()) {
                    Global.gui().commandPanel.add(new SubSkillButton(action, choice, combat));
                }
                Global.gui().commandPanel.refresh();
            }
        });
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(500, 20));
        add(getButton());
    }

    private static Color foregroundColor(Color bgColor) {
        float hsb[] = new float[3];
        Color.RGBtoHSB(bgColor.getRed(), bgColor.getGreen(), bgColor.getRed(), hsb);
        if (hsb[2] < .6) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    private static Font fontForStage(Stage stage) {
        switch (stage) {
            case FINISHER:
                return new Font("Baskerville Old Face", Font.BOLD, 18);
            case FOREPLAY:
                return new Font("Baskerville Old Face", Font.ITALIC, 18);
            default:
                return new Font("Baskerville Old Face", Font.PLAIN, 18);
            
        }
    }

    @Override
    public String getText() {
        return action.getLabel(combat);
    }
}
